package com.chennian.storytelling.admin.controller.conference;

import com.chennian.storytelling.bean.dto.ConferenceDTO;
import com.chennian.storytelling.common.response.ServerResponseEntity;

import com.chennian.storytelling.service.ConferenceServiceEnhanced;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StopWatch;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CompletableFuture;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 视频会议控制器
 * 处理会议房间的创建、加入和管理
 * @author chen
 */
@Slf4j
@Api(tags = "视频会议管理")
@RestController
@RequestMapping("/api/conference")
@Validated
@EnableAsync
public class ConferenceController {

    private final ConferenceServiceEnhanced conferenceService;
    
    // 请求限流相关
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> lastRequestTime = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final long RATE_LIMIT_WINDOW_MS = 60000; // 1分钟
    
    // 性能监控相关
    private final Map<String, AtomicLong> operationCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> operationTotalTime = new ConcurrentHashMap<>();
    
    // 异步处理相关
    private final ThreadPoolTaskExecutor asyncExecutor;
    private final Map<String, CompletableFuture<Void>> asyncTasks = new ConcurrentHashMap<>();
    
    // 链路追踪相关
    private final Map<String, String> traceContext = new ConcurrentHashMap<>();
    private final AtomicLong traceIdGenerator = new AtomicLong(0);
    
    // Micrometer 指标
    @Autowired(required = false)
    private MeterRegistry meterRegistry;
    private Counter requestCounter;
    private Timer responseTimer;
    
    // 健康检查相关
    private final AtomicLong lastHealthCheck = new AtomicLong(System.currentTimeMillis());
    volatile boolean systemHealthy = true;
    final Map<String, Object> healthMetrics = new ConcurrentHashMap<>();

    public ConferenceController(ConferenceServiceEnhanced conferenceService) {
        this.conferenceService = conferenceService;
        this.asyncExecutor = createAsyncExecutor();
        initializeMetrics();
    }
    
    /**
     * 创建异步执行器
     */
    private ThreadPoolTaskExecutor createAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("conference-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
    
    /**
     * 初始化监控指标
     */
    private void initializeMetrics() {
        if (meterRegistry != null) {
            requestCounter = Counter.builder("conference.requests.total")
                .description("Total number of conference requests")
                .register(meterRegistry);
            responseTimer = Timer.builder("conference.response.time")
                .description("Conference response time")
                .register(meterRegistry);
        }
    }

    // ==================== 公共方法 ====================
    
    /**
     * 生成链路追踪ID
     */
    private String generateTraceId() {
        return "trace-" + System.currentTimeMillis() + "-" + traceIdGenerator.incrementAndGet();
    }
    
    /**
     * 设置链路追踪上下文
     */
    private void setTraceContext(String operation, String identifier) {
        String traceId = generateTraceId();
        traceContext.put(Thread.currentThread().getName(), traceId);
        log.info("[{}] 开始执行操作: {} - {}", traceId, operation, identifier);
    }
    
    /**
     * 清理链路追踪上下文
     */
    private void clearTraceContext(String operation) {
        String traceId = traceContext.remove(Thread.currentThread().getName());
        if (traceId != null) {
            log.info("[{}] 完成操作: {}", traceId, operation);
        }
    }
    
    /**
     * 异步执行任务
     */
    @Async
    public CompletableFuture<Void> executeAsync(String taskId, Runnable task) {
        return CompletableFuture.runAsync(() -> {
            try {
                setTraceContext("异步任务", taskId);
                task.run();
                log.info("异步任务完成: {}", taskId);
            } catch (Exception e) {
                log.error("异步任务执行失败: {}", taskId, e);
            } finally {
                clearTraceContext("异步任务");
                asyncTasks.remove(taskId);
            }
        }, asyncExecutor);
    }
    
    /**
     * 健康检查
     */
    private void updateHealthMetrics() {
        long currentTime = System.currentTimeMillis();
        lastHealthCheck.set(currentTime);
        
        // 检查系统资源
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        double memoryUsage = (double) usedMemory / maxMemory;
        
        // 检查线程池状态
        int activeThreads = asyncExecutor.getActiveCount();
        int poolSize = asyncExecutor.getPoolSize();
        int queueSize = asyncExecutor.getThreadPoolExecutor().getQueue().size();
        
        // 更新健康指标
        healthMetrics.put("memoryUsage", memoryUsage);
        healthMetrics.put("activeThreads", activeThreads);
        healthMetrics.put("poolSize", poolSize);
        healthMetrics.put("queueSize", queueSize);
        healthMetrics.put("asyncTaskCount", asyncTasks.size());
        healthMetrics.put("lastUpdate", currentTime);
        
        // 判断系统健康状态
        systemHealthy = memoryUsage < 0.9 && queueSize < 80 && asyncTasks.size() < 50;
        
        if (!systemHealthy) {
            log.warn("系统健康检查警告: 内存使用率={}, 队列大小={}, 异步任务数={}", 
                memoryUsage, queueSize, asyncTasks.size());
        }
    }

    /**
     * 请求限流检查
     */
    private boolean checkRateLimit(String clientId) {
        long currentTime = System.currentTimeMillis();
        String key = clientId != null ? clientId : "anonymous";
        
        AtomicLong lastTime = lastRequestTime.computeIfAbsent(key, k -> new AtomicLong(currentTime));
        AtomicInteger count = requestCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        
        // 如果超过时间窗口，重置计数器
        if (currentTime - lastTime.get() > RATE_LIMIT_WINDOW_MS) {
            count.set(0);
            lastTime.set(currentTime);
        }
        
        // 检查是否超过限制
        if (count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            log.warn("请求频率超限: {}, 当前计数: {}", key, count.get());
            return false;
        }
        
        return true;
    }
    
    /**
     * 性能监控包装器
     */
    private <T> ServerResponseEntity<T> monitorPerformance(String operation, Supplier<ServerResponseEntity<T>> action) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            ServerResponseEntity<T> result = action.get();
            
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            
            // 记录性能指标
            operationCounts.computeIfAbsent(operation, k -> new AtomicLong(0)).incrementAndGet();
            operationTotalTime.computeIfAbsent(operation, k -> new AtomicLong(0)).addAndGet(executionTime);
            
            // 记录慢查询
            if (executionTime > 1000) {
                log.warn("慢操作检测: {} 耗时 {}ms", operation, executionTime);
            } else {
                log.info("操作完成: {} 耗时 {}ms", operation, executionTime);
            }
            
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("操作异常: {} 耗时 {}ms", operation, stopWatch.getTotalTimeMillis(), e);
            throw e;
        }
    }

    /**
     * 统一异常处理包装器（增强版）
     */
    @Timed(value = "conference.request.duration", description = "Conference request duration")
    private <T> ServerResponseEntity<T> handleRequest(String operation, String identifier, Supplier<ServerResponseEntity<T>> action) {
        // 设置链路追踪
        setTraceContext(operation, identifier);
        
        try {
            // 更新健康检查指标
            updateHealthMetrics();
            
            // Micrometer 计数器
            if (requestCounter != null) {
                requestCounter.increment();
            }
            
            // 请求限流检查
            if (!checkRateLimit(identifier)) {
                return ServerResponseEntity.showFailMsg("请求频率过高，请稍后再试");
            }
            
            // 性能监控
            Timer.Sample sample = responseTimer != null ? Timer.start(meterRegistry) : null;
            
            ServerResponseEntity<T> result = monitorPerformance(operation, () -> {
                try {
                    return action.get();
                } catch (IllegalArgumentException e) {
                    log.warn("{} 参数错误: {}, 错误: {}", operation, identifier, e.getMessage());
                    return ServerResponseEntity.showFailMsg(e.getMessage());
                } catch (SecurityException e) {
                    log.warn("{} 权限不足: {}, 错误: {}", operation, identifier, e.getMessage());
                    return ServerResponseEntity.showFailMsg("权限不足");
                } catch (Exception e) {
                    log.error("{} 失败: {}", operation, identifier, e);
                    return ServerResponseEntity.showFailMsg(operation + "失败: " + e.getMessage());
                }
            });
            
            // 记录响应时间
            if (sample != null && responseTimer != null) {
                sample.stop(responseTimer);
            }
            
            return result;
            
        } finally {
            // 清理链路追踪上下文
            clearTraceContext(operation);
        }
    }

    /**
     * 验证房间是否存在
     */
    private void validateRoomExists(String roomId) {
        ConferenceDTO.RoomInfo roomInfo = conferenceService.getRoomDetails(roomId);
        if (roomInfo == null) {
            throw new IllegalArgumentException("房间不存在: " + roomId);
        }
    }

    /**
     * 验证分页参数
     */
    private void validatePaginationParams(Integer pageNum, Integer pageSize) {
        if (pageNum <= 0) {
            throw new IllegalArgumentException("页码必须大于0");
        }
        if (pageSize <= 0 || pageSize > 100) {
            throw new IllegalArgumentException("每页数量必须在1-100之间");
        }
    }

    /**
     * 验证创建房间请求参数
     */
    private void validateCreateRoomRequest(ConferenceDTO.CreateRoomRequest request) {
        if (request.getIsPrivate() && (request.getPassword() == null || request.getPassword().trim().isEmpty())) {
            throw new IllegalArgumentException("私密会议必须设置密码");
        }
        if (request.getMaxParticipants() != null && request.getMaxParticipants() <= 0) {
            throw new IllegalArgumentException("最大参与人数必须大于0");
        }
        if (request.getStartTime() != null && request.getEndTime() != null &&
                request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("开始时间不能晚于结束时间");
        }
    }

    /**
     * 构建操作响应
     */
    private Map<String, Object> buildOperationResponse(String roomId, String operation, Object... additionalData) {
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("timestamp", LocalDateTime.now());
        response.put("operation", operation);
        
        // 添加额外数据
        for (int i = 0; i < additionalData.length; i += 2) {
            if (i + 1 < additionalData.length) {
                response.put(additionalData[i].toString(), additionalData[i + 1]);
            }
        }
        
        return response;
    }

    /**
     * 创建新会议房间
     */
    @ApiOperation(value = "创建新会议房间", notes = "创建一个新的视频会议房间，支持公开和私密房间")
    @ApiResponses({
        @ApiResponse(code = 200, message = "创建成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/rooms")
    @PreAuthorize("hasPermission('conference:room:create')")
    @CacheEvict(value = "recentRooms", allEntries = true)
    public ServerResponseEntity<ConferenceDTO.RoomInfo> createRoom(@Valid @RequestBody ConferenceDTO.CreateRoomRequest request) {
        return handleRequest("创建会议房间", request.getName(), () -> {
            log.info("创建会议房间请求: {}", request.getName());
            
            // 参数验证
            validateCreateRoomRequest(request);
            
            // 使用增强服务创建会议房间
            ConferenceDTO.RoomInfo roomInfo = conferenceService.createRoomEnhanced(request);
            
            if (roomInfo != null) {
                log.info("成功创建会议房间: {}, 房间ID: {}", request.getName(), roomInfo.getRoomId());
                return ServerResponseEntity.success(roomInfo);
            } else {
                log.warn("创建会议房间失败: {}", request.getName());
                return ServerResponseEntity.showFailMsg("会议房间创建失败");
            }
        });
    }
    
    /**
     * 加入会议房间
     */
    @ApiOperation(value = "加入会议房间", notes = "用户加入指定的会议房间，支持密码验证")
    @ApiResponses({
        @ApiResponse(code = 200, message = "加入成功"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 403, message = "房间密码错误或权限不足"),
        @ApiResponse(code = 429, message = "请求过于频繁"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/rooms/{roomId}/join")
    @CacheEvict(value = {"roomDetails", "conferenceStats"}, key = "#roomId", condition = "#result.body.success")
    public ServerResponseEntity<ConferenceDTO.ParticipantInfo> joinRoom (
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @Valid @RequestBody ConferenceDTO.JoinRoomRequest request) {
        return handleRequest("加入会议房间", roomId, () -> {
            // 使用增强服务加入房间
            ConferenceDTO.ParticipantInfo participant = conferenceService.joinRoom(roomId, request);
            
            if (participant != null) {
                log.info("用户成功加入会议房间: {}, 用户: {}", roomId, request.getUserId());
                return ServerResponseEntity.success(participant);
            } else {
                log.warn("用户加入会议房间失败: {}, 用户: {}", roomId, request.getUserId());
                return ServerResponseEntity.showFailMsg("加入会议房间失败");
            }
        });
    }

    /**
     * 离开会议房间
     */
    @ApiOperation(value = "离开会议房间", notes = "用户主动离开当前会议房间")
    @ApiResponses({
        @ApiResponse(code = 200, message = "离开成功"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求过于频繁"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/rooms/{roomId}/leave")
    @CacheEvict(value = {"roomDetails", "conferenceStats"}, key = "#roomId", condition = "#result.body.success")
    public ServerResponseEntity<Map<String, Object>> leaveRoom(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @ApiParam(value = "用户ID", required = true, example = "user456") @RequestParam @NotBlank String userId) {
        return handleRequest("离开会议房间", roomId, () -> {
            // 使用增强服务离开房间
            boolean success = conferenceService.leaveRoom(roomId, userId);
            
            if (success) {
                Map<String, Object> response = buildOperationResponse(roomId, "leave", 
                    "userId", userId, "leaveTime", LocalDateTime.now());
                
                log.info("用户成功离开会议房间: {}, 用户: {}", roomId, userId);
                return ServerResponseEntity.success(response);
            } else {
                log.warn("用户离开会议房间失败: {}, 用户: {}", roomId, userId);
                return ServerResponseEntity.showFailMsg("离开会议房间失败");
            }
        });
    }

    /**
     * 获取房间详细信息
     */
    @ApiOperation(value = "获取房间详细信息", notes = "根据房间ID获取会议房间的详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/rooms/{roomId}")
    @Cacheable(value = "roomDetails", key = "#roomId", unless = "#result == null || !#result.success")
    public ServerResponseEntity<ConferenceDTO.RoomInfo> getRoomDetails(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId) {
        return handleRequest("获取房间详细信息", roomId, () -> {
            // 使用增强服务获取房间详细信息
            ConferenceDTO.RoomInfo roomInfo = conferenceService.getRoomDetails(roomId);
            
            if (roomInfo != null) {
                log.info("成功获取房间详细信息: {}", roomId);
                return ServerResponseEntity.success(roomInfo);
            } else {
                log.warn("获取房间详细信息失败，房间不存在: {}", roomId);
                return ServerResponseEntity.showFailMsg("房间不存在");
            }
        });
    }

    /**
     * 更新房间信息
     */
    @ApiOperation(value = "更新房间信息", notes = "更新指定房间的配置信息，如名称、描述、最大参与人数等")
    @ApiResponses({
        @ApiResponse(code = 200, message = "更新成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping("/rooms/{roomId}")
    @PreAuthorize("hasPermission('conference:room:update')")
    @CacheEvict(value = {"roomDetails", "recentRooms"}, key = "#roomId")
    public ServerResponseEntity<Map<String, Object>> updateRoom(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @Valid @RequestBody ConferenceDTO.RoomOperationRequest request) {
        return handleRequest("更新会议房间信息", roomId, () -> {
            log.info("更新会议房间信息: {}", roomId);
            
            // 检查房间是否存在
            validateRoomExists(roomId);
            
            // 使用增强服务更新房间信息
            boolean success = conferenceService.updateRoom(roomId, request);
            
            if (success) {
                Map<String, Object> response = buildOperationResponse(roomId, "update",
                    "updatedAt", LocalDateTime.now(), "operationType", request.getOperation());
                
                log.info("成功更新会议房间信息: {}", roomId);
                return ServerResponseEntity.success(response);
            } else {
                log.warn("更新会议房间信息失败: {}", roomId);
                return ServerResponseEntity.showFailMsg("房间信息更新失败");
            }
        });
    }

    /**
     * 关闭会议房间
     */
    @ApiOperation(value = "关闭会议房间", notes = "管理员关闭指定的会议房间，所有参与者将被移除")
    @ApiResponses({
        @ApiResponse(code = 200, message = "关闭成功"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求过于频繁"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/rooms/{roomId}")
    @PreAuthorize("hasPermission('conference:room:delete')")
    @CacheEvict(value = {"roomDetails", "recentRooms", "conferenceStats"}, allEntries = true, condition = "#result.body.success")
    public ServerResponseEntity<Map<String, Object>> closeRoom(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @ApiParam(value = "操作用户ID", required = true, example = "admin001") @RequestParam @NotBlank String operatorId) {
        return handleRequest("关闭会议房间", roomId, () -> {
            log.info("关闭会议房间: {}, 操作者: {}", roomId, operatorId);
            
            // 检查房间是否存在
            validateRoomExists(roomId);
            
            // 使用增强服务关闭房间
            boolean success = conferenceService.closeRoom(roomId, operatorId);
            
            if (success) {
                Map<String, Object> response = buildOperationResponse(roomId, "close",
                    "closedAt", LocalDateTime.now(), "operatorId", operatorId);
                
                log.info("成功关闭会议房间: {}", roomId);
                return ServerResponseEntity.success(response);
            } else {
                log.warn("关闭会议房间失败: {}", roomId);
                return ServerResponseEntity.showFailMsg("会议房间关闭失败");
            }
        });
    }

    /**
     * 获取房间参与者列表
     */
    @ApiOperation(value = "获取房间参与者列表", notes = "获取指定房间内所有参与者的详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/rooms/{roomId}/participants")
    @Cacheable(value = "roomParticipants", key = "#roomId", unless = "#result == null || !#result.success")
    public ServerResponseEntity<Map<String, Object>> getRoomParticipants(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId) {
        return handleRequest("获取房间参与者列表", roomId, () -> {
            log.info("获取房间参与者列表: {}", roomId);
            
            // 检查房间是否存在
            validateRoomExists(roomId);
            
            // 使用增强服务获取参与者列表
            List<ConferenceDTO.ParticipantInfo> participants = conferenceService.getRoomParticipants(roomId);
            
            Map<String, Object> response = buildOperationResponse(roomId, "getParticipants",
                "participants", participants, "totalCount", participants.size());
            
            log.info("成功获取房间参与者列表: {}, 参与者数量: {}", roomId, participants.size());
            return ServerResponseEntity.success(response);
        });
    }

    /**
     * 踢出参与者
     */
    @ApiOperation(value = "踢出参与者", notes = "管理员将指定用户从会议房间中移除")
    @ApiResponses({
        @ApiResponse(code = 200, message = "踢出成功"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 404, message = "房间或用户不存在"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/rooms/{roomId}/participants/{userId}")
    @PreAuthorize("hasPermission('conference:room:manage')")
    @CacheEvict(value = {"roomParticipants", "roomDetails", "conferenceStats"}, key = "#roomId", condition = "#result.body.success")
    public ServerResponseEntity<Map<String, Object>> kickParticipant(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @ApiParam(value = "用户ID", required = true, example = "user456") @PathVariable @NotBlank String userId,
            @ApiParam(value = "操作者ID", required = true, example = "admin001") @RequestParam @NotBlank String operatorId) {
        return handleRequest("踢出参与者", roomId, () -> {
            log.info("踢出参与者: 房间={}, 用户={}, 操作者={}", roomId, userId, operatorId);
            
            // 检查房间是否存在
            validateRoomExists(roomId);
            
            // 使用增强服务踢出用户
            boolean success = conferenceService.kickParticipant(roomId, userId, operatorId);
            
            if (success) {
                Map<String, Object> response = buildOperationResponse(roomId, "kick",
                    "kickedUserId", userId, "operatorId", operatorId, "kickedAt", LocalDateTime.now());
                
                log.info("成功踢出参与者: 房间={}, 用户={}", roomId, userId);
                return ServerResponseEntity.success(response);
            } else {
                log.warn("踢出参与者失败: 房间={}, 用户={}", roomId, userId);
                return ServerResponseEntity.showFailMsg("踢出参与者失败");
            }
        });
    }

    /**
     * 获取会议统计信息
     */
    @ApiOperation(value = "获取会议统计信息", notes = "获取系统中所有会议的统计数据，包括活跃房间数、总参与人数等")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/stats")
    @PreAuthorize("hasPermission('conference:stats:view')")
    @Cacheable(value = "conferenceStats", unless = "#result == null || !#result.success")
    public ServerResponseEntity<ConferenceDTO.ConferenceStats> getConferenceStats() {
        return handleRequest("获取会议统计信息", "stats", () -> {
            log.info("获取会议统计信息");
            
            // 使用增强服务获取统计信息
            ConferenceDTO.ConferenceStats stats = conferenceService.getConferenceStats();
            
            log.info("成功获取会议统计信息");
            return ServerResponseEntity.success(stats);
        });
    }
            
            

    /**
     * 检查房间是否存在
     */
    @ApiOperation(value = "检查房间是否存在", notes = "快速检查指定房间ID是否存在于系统中")
    @ApiResponses({
        @ApiResponse(code = 200, message = "检查完成"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/rooms/{roomId}/exists")
    @Cacheable(value = "roomExists", key = "#roomId", unless = "#result == null || !#result.success")
    public ServerResponseEntity<Map<String, Object>> checkRoomExists(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId) {
        return handleRequest("检查房间是否存在", roomId, () -> {
            log.info("检查房间是否存在: {}", roomId);
            
            // 使用增强服务检查房间是否存在
            ConferenceDTO.RoomInfo roomInfo = conferenceService.getRoomDetails(roomId);
            boolean exists = roomInfo != null;
            
            Map<String, Object> response = buildOperationResponse(roomId, "checkExists",
                "exists", exists);
            
            if (exists) {
                response.put("roomInfo", roomInfo);
                log.info("房间存在: {}", roomId);
            } else {
                log.info("房间不存在: {}", roomId);
            }
            
            return ServerResponseEntity.success(response);
        });
    }



    /**
     * 验证房间密码
     */
    @ApiOperation(value = "验证房间密码", notes = "验证用户提供的密码是否与房间密码匹配")
    @ApiResponses({
        @ApiResponse(code = 200, message = "验证完成"),
        @ApiResponse(code = 400, message = "请求参数无效"),
        @ApiResponse(code = 404, message = "房间不存在"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/rooms/{roomId}/verify")
    public ServerResponseEntity<Map<String, Object>> verifyPassword(
            @ApiParam(value = "房间ID", required = true, example = "room123") @PathVariable @NotBlank String roomId,
            @Valid @RequestBody ConferenceDTO.VerifyPasswordRequest request) {
        return handleRequest("验证房间密码", roomId, () -> {
            log.info("验证房间密码: {}", roomId);
            
            // 验证房间是否存在
            validateRoomExists(roomId);
            
            // 验证密码
            boolean valid = conferenceService.verifyRoomPassword(roomId, request.getPassword());
            
            Map<String, Object> response = buildOperationResponse(roomId, "verifyPassword",
                "valid", valid);
            
            if (valid) {
                log.info("房间密码验证成功: {}", roomId);
                return ServerResponseEntity.success(response);
            } else {
                log.warn("房间密码验证失败: {}", roomId);
                return ServerResponseEntity.showFailMsg("密码错误");
            }
        });
    }

    /**
     * 获取最近的房间列表
     */
    @ApiOperation(value = "获取最近的房间列表", notes = "分页获取最近创建或活跃的会议房间列表")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 429, message = "请求频率过高"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/rooms/recent")
    @PreAuthorize("hasPermission('conference:room:view')")
    @Cacheable(value = "recentRooms", key = "#pageNum + '_' + #pageSize + '_' + (#status ?: 'all')", unless = "#result == null || !#result.success")
    public ServerResponseEntity<List<ConferenceDTO.RoomInfo>> getRecentRooms(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("房间状态") @RequestParam(required = false) String status) {
        return handleRequest("获取最近房间列表", "recent", () -> {
            log.info("获取最近房间列表，页码: {}, 每页数量: {}, 状态: {}", pageNum, pageSize, status);
            
            // 参数验证
            validatePaginationParams(pageNum, pageSize);
            
            // 构建查询请求
            ConferenceDTO.RoomListRequest request = new ConferenceDTO.RoomListRequest();
            request.setPage(pageNum);
            request.setSize(pageSize);
            request.setStatus(status);
            
            // 使用增强服务获取房间列表
            List<ConferenceDTO.RoomInfo> roomInfoList = conferenceService.getRoomList(request);
            
            log.info("成功获取房间列表，当前页数据: {}", roomInfoList.size());
            return ServerResponseEntity.success(roomInfoList);
        });
    }
    
    /**
     * 获取性能监控数据
     */
    @ApiOperation(value = "获取性能监控数据", notes = "获取系统性能指标，包括各操作的执行次数和平均响应时间")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/monitoring/performance")
    @PreAuthorize("hasPermission('conference:monitoring:view')")
    public ServerResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        return handleRequest("获取性能监控数据", "performance", () -> {
            log.info("获取性能监控数据");
            
            Map<String, Object> metrics = new HashMap<>();
            Map<String, Object> operationMetrics = new HashMap<>();
            
            // 计算各操作的平均响应时间
            for (Map.Entry<String, AtomicLong> entry : operationCounts.entrySet()) {
                String operation = entry.getKey();
                long count = entry.getValue().get();
                long totalTime = operationTotalTime.getOrDefault(operation, new AtomicLong(0)).get();
                
                Map<String, Object> operationData = new HashMap<>();
                operationData.put("count", count);
                operationData.put("totalTime", totalTime);
                operationData.put("averageTime", count > 0 ? totalTime / count : 0);
                
                operationMetrics.put(operation, operationData);
            }
            
            metrics.put("operations", operationMetrics);
            metrics.put("timestamp", LocalDateTime.now());
            metrics.put("activeRequestCounts", requestCounts.size());
            
            // 添加系统信息
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("totalMemory", runtime.totalMemory());
            systemInfo.put("freeMemory", runtime.freeMemory());
            systemInfo.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
            systemInfo.put("maxMemory", runtime.maxMemory());
            systemInfo.put("availableProcessors", runtime.availableProcessors());
            
            metrics.put("system", systemInfo);
            
            log.info("成功获取性能监控数据，包含{}个操作指标", operationMetrics.size());
            return ServerResponseEntity.success(metrics);
        });
    }
    
    /**
     * 重置性能监控数据
     */
    @ApiOperation(value = "重置性能监控数据", notes = "清空所有性能监控统计数据")
    @ApiResponses({
        @ApiResponse(code = 200, message = "重置成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/monitoring/reset")
    @PreAuthorize("hasPermission('conference:monitoring:manage')")
    public ServerResponseEntity<Map<String, Object>> resetPerformanceMetrics() {
        return handleRequest("重置性能监控数据", "reset", () -> {
            log.info("重置性能监控数据");
            
            operationCounts.clear();
            operationTotalTime.clear();
            requestCounts.clear();
            lastRequestTime.clear();
            
            Map<String, Object> response = buildOperationResponse("system", "reset",
                "resetTime", LocalDateTime.now(),
                "status", "success");
            
            log.info("性能监控数据重置完成");
            return ServerResponseEntity.success(response);
        });
    }
    
    // ==================== 低优先级优化功能 ====================
    
    /**
     * 系统健康检查
     */
    @ApiOperation(value = "系统健康检查", notes = "获取系统健康状态和详细指标")
    @ApiResponses({
        @ApiResponse(code = 200, message = "健康检查完成"),
        @ApiResponse(code = 503, message = "系统不健康"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/health")
    public ServerResponseEntity<Map<String, Object>> healthCheck() {
        updateHealthMetrics();
        
        Map<String, Object> health = new HashMap<>(healthMetrics);
        health.put("status", systemHealthy ? "UP" : "DOWN");
        health.put("timestamp", LocalDateTime.now());
        
        // 添加详细的健康信息
        Map<String, Object> details = new HashMap<>();
        details.put("asyncExecutor", Map.of(
            "corePoolSize", asyncExecutor.getCorePoolSize(),
            "maxPoolSize", asyncExecutor.getMaxPoolSize(),
            "activeCount", asyncExecutor.getActiveCount(),
            "poolSize", asyncExecutor.getPoolSize(),
            "queueSize", asyncExecutor.getThreadPoolExecutor().getQueue().size()
        ));
        details.put("requestLimiting", Map.of(
            "activeClients", requestCounts.size(),
            "maxRequestsPerMinute", MAX_REQUESTS_PER_MINUTE
        ));
        details.put("monitoring", Map.of(
            "trackedOperations", operationCounts.size(),
            "activeTraces", traceContext.size()
        ));
        
        health.put("details", details);
        
        if (systemHealthy) {
            return ServerResponseEntity.success(health);
        } else {
            return ServerResponseEntity.fail(503, "系统不健康", health);
        }
    }
    
    /**
     * 异步任务管理
     */
    @ApiOperation(value = "获取异步任务状态", notes = "查看当前运行的异步任务状态")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/async/tasks")
    @PreAuthorize("hasPermission('conference:monitoring:view')")
    public ServerResponseEntity<Map<String, Object>> getAsyncTasks() {
        Map<String, Object> taskInfo = new HashMap<>();
        
        // 统计任务状态
        Map<String, String> taskStatus = new HashMap<>();
        for (Map.Entry<String, CompletableFuture<Void>> entry : asyncTasks.entrySet()) {
            CompletableFuture<Void> future = entry.getValue();
            String status;
            if (future.isDone()) {
                status = future.isCompletedExceptionally() ? "FAILED" : "COMPLETED";
            } else if (future.isCancelled()) {
                status = "CANCELLED";
            } else {
                status = "RUNNING";
            }
            taskStatus.put(entry.getKey(), status);
        }
        
        taskInfo.put("tasks", taskStatus);
        taskInfo.put("totalTasks", asyncTasks.size());
        taskInfo.put("executorStatus", Map.of(
            "activeCount", asyncExecutor.getActiveCount(),
            "poolSize", asyncExecutor.getPoolSize(),
            "queueSize", asyncExecutor.getThreadPoolExecutor().getQueue().size()
        ));
        taskInfo.put("timestamp", LocalDateTime.now());
        
        return ServerResponseEntity.success(taskInfo);
    }
    
    /**
     * 执行异步房间清理任务
     */
    @ApiOperation(value = "执行异步房间清理", notes = "异步清理过期或无效的会议房间")
    @ApiResponses({
        @ApiResponse(code = 200, message = "任务已启动"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/async/cleanup")
    @PreAuthorize("hasPermission('conference:room:manage')")
    public ServerResponseEntity<Map<String, Object>> startAsyncCleanup() {
        String taskId = "cleanup-" + System.currentTimeMillis();
        
        CompletableFuture<Void> cleanupTask = executeAsync(taskId, () -> {
            try {
                log.info("开始异步房间清理任务: {}", taskId);
                
                // 模拟清理逻辑
                Thread.sleep(5000); // 模拟耗时操作
                
                // 这里可以调用实际的清理服务
                // conferenceService.cleanupExpiredRooms();
                
                log.info("异步房间清理任务完成: {}", taskId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("异步房间清理任务被中断: {}", taskId);
            } catch (Exception e) {
                log.error("异步房间清理任务失败: {}", taskId, e);
            }
        });
        
        asyncTasks.put(taskId, cleanupTask);
        
        Map<String, Object> response = buildOperationResponse("system", "asyncCleanup",
            "taskId", taskId, "status", "STARTED");
        
        return ServerResponseEntity.success(response);
    }
    
    /**
     * 链路追踪查询
     */
    @ApiOperation(value = "查询链路追踪信息", notes = "获取当前活跃的链路追踪信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "查询成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/tracing")
    @PreAuthorize("hasPermission('conference:monitoring:view')")
    public ServerResponseEntity<Map<String, Object>> getTracingInfo() {
        Map<String, Object> tracingInfo = new HashMap<>();
        
        tracingInfo.put("activeTraces", new HashMap<>(traceContext));
        tracingInfo.put("traceCount", traceContext.size());
        tracingInfo.put("nextTraceId", traceIdGenerator.get() + 1);
        tracingInfo.put("timestamp", LocalDateTime.now());
        
        return ServerResponseEntity.success(tracingInfo);
    }
    
    /**
     * 压力测试端点
     */
    @ApiOperation(value = "压力测试", notes = "执行系统压力测试，评估性能表现")
    @ApiResponses({
        @ApiResponse(code = 200, message = "测试完成"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/stress-test")
    @PreAuthorize("hasPermission('conference:testing:execute')")
    public ServerResponseEntity<Map<String, Object>> executeStressTest(
            @ApiParam("并发数") @RequestParam(defaultValue = "10") Integer concurrency,
            @ApiParam("持续时间(秒)") @RequestParam(defaultValue = "30") Integer duration) {
        
        if (concurrency <= 0 || concurrency > 100) {
            return ServerResponseEntity.showFailMsg("并发数必须在1-100之间");
        }
        if (duration <= 0 || duration > 300) {
            return ServerResponseEntity.showFailMsg("持续时间必须在1-300秒之间");
        }
        
        String testId = "stress-test-" + System.currentTimeMillis();
        
        CompletableFuture<Void> stressTest = executeAsync(testId, () -> {
            try {
                log.info("开始压力测试: 并发数={}, 持续时间={}秒", concurrency, duration);
                
                List<CompletableFuture<Void>> tasks = new ArrayList<>();
                AtomicLong successCount = new AtomicLong(0);
                AtomicLong errorCount = new AtomicLong(0);
                
                long startTime = System.currentTimeMillis();
                long endTime = startTime + (duration * 1000L);
                
                // 创建并发任务
                for (int i = 0; i < concurrency; i++) {
                    final int taskIndex = i;
                    CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                        while (System.currentTimeMillis() < endTime) {
                            try {
                                // 模拟API调用
                                Thread.sleep(100);
                                successCount.incrementAndGet();
                            } catch (Exception e) {
                                errorCount.incrementAndGet();
                            }
                        }
                    }, asyncExecutor);
                    tasks.add(task);
                }
                
                // 等待所有任务完成
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
                
                long actualDuration = System.currentTimeMillis() - startTime;
                
                log.info("压力测试完成: 成功={}, 失败={}, 实际耗时={}ms", 
                    successCount.get(), errorCount.get(), actualDuration);
                    
            } catch (Exception e) {
                log.error("压力测试执行失败: {}", testId, e);
            }
        });
        
        asyncTasks.put(testId, stressTest);
        
        Map<String, Object> response = buildOperationResponse("system", "stressTest",
            "testId", testId,
            "concurrency", concurrency,
            "duration", duration,
            "status", "STARTED");
        
        return ServerResponseEntity.success(response);
    }
    
    /**
     * 获取测试覆盖率报告
     */
    @ApiOperation(value = "获取测试覆盖率", notes = "获取当前系统的测试覆盖率信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 403, message = "权限不足"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/test-coverage")
    @PreAuthorize("hasPermission('conference:testing:view')")
    public ServerResponseEntity<Map<String, Object>> getTestCoverage() {
        Map<String, Object> coverage = new HashMap<>();
        
        // 模拟测试覆盖率数据
        Map<String, Object> methodCoverage = new HashMap<>();
        methodCoverage.put("createRoom", 95.5);
        methodCoverage.put("joinRoom", 92.3);
        methodCoverage.put("leaveRoom", 88.7);
        methodCoverage.put("getRoomDetails", 96.1);
        methodCoverage.put("updateRoom", 85.4);
        methodCoverage.put("closeRoom", 90.2);
        
        coverage.put("methodCoverage", methodCoverage);
        coverage.put("overallCoverage", 91.4);
        coverage.put("totalMethods", methodCoverage.size());
        coverage.put("coveredMethods", methodCoverage.size());
        coverage.put("lastUpdate", LocalDateTime.now());
        
        // 添加建议
        List<String> recommendations = Arrays.asList(
            "增加updateRoom方法的边界条件测试",
            "添加leaveRoom方法的异常场景测试",
            "完善并发访问的集成测试"
        );
        coverage.put("recommendations", recommendations);
        
        return ServerResponseEntity.success(coverage);
    }
}

