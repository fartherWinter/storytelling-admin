package com.chennian.storytelling.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chennian.storytelling.bean.dto.ConferenceDTO;
import com.chennian.storytelling.bean.enums.ConferenceEnum;
import com.chennian.storytelling.service.ConferenceServiceEnhanced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 增强的视频会议服务实现类
 * 提供完整的会议管理功能
 * @author chen
 */
@Service
public class ConferenceServiceEnhancedImpl extends ConferenceServiceImpl implements ConferenceServiceEnhanced {

    private static final Logger logger = LoggerFactory.getLogger(ConferenceServiceEnhancedImpl.class);
    
    // 房间质量设置
    private final Map<String, ConferenceEnum.QualityLevel> roomQualitySettings = new ConcurrentHashMap<>();
    
    // 房间录制状态
    private final Map<String, Boolean> roomRecordingStatus = new ConcurrentHashMap<>();
    
    // 房间消息历史
    private final Map<String, List<Map<String, Object>>> roomMessageHistory = new ConcurrentHashMap<>();
    
    // 用户参与的房间
    private final Map<String, Set<String>> userRooms = new ConcurrentHashMap<>();
    
    // 房间信息映射
    private final Map<String, Map<String, Object>> roomInfoMap = new ConcurrentHashMap<>();

    /**
     * 获取房间信息映射
     * @return 房间信息映射
     */
    private Map<String, Map<String, Object>> getRoomInfoMap() {
        return roomInfoMap;
    }
    
    /**
     * 获取会话到房间的映射
     * 通过遍历父类的rooms映射来构建WebSocketSession到roomId的映射
     * @return WebSocketSession到roomId的映射
     */
    private Map<WebSocketSession, String> getSessionRoomMap() {
        Map<WebSocketSession, String> sessionRoomMap = new ConcurrentHashMap<>();
        
        try {
            // 使用反射获取父类的rooms字段
            java.lang.reflect.Field roomsField = ConferenceServiceImpl.class.getDeclaredField("rooms");
            roomsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Map<String, WebSocketSession>> rooms = 
                (Map<String, Map<String, WebSocketSession>>) roomsField.get(this);
            
            // 遍历所有房间，构建会话到房间的映射
            for (Map.Entry<String, Map<String, WebSocketSession>> roomEntry : rooms.entrySet()) {
                String roomId = roomEntry.getKey();
                Map<String, WebSocketSession> sessions = roomEntry.getValue();
                
                for (WebSocketSession session : sessions.values()) {
                    if (session != null && session.isOpen()) {
                        sessionRoomMap.put(session, roomId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取会话房间映射失败", e);
        }
        
        return sessionRoomMap;
    }

    @Override
    public ConferenceDTO.RoomInfo createRoomEnhanced(ConferenceDTO.CreateRoomRequest request) {
        if (request == null) {
            logger.error("创建房间失败，请求参数为空");
            return null;
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            logger.error("创建房间失败，房间名称不能为空");
            return null;
        }
        
        logger.info("创建增强版会议房间: {}", request.getName());
        
        // 调用原有方法创建房间
        String roomId = createRoom(request.getName(), request.getIsPrivate(), request.getPassword());
        if (roomId == null) {
            logger.error("创建房间失败，无法生成房间ID");
            return null;
        }
        
        // 创建增强的房间信息
        ConferenceDTO.RoomInfo roomInfo = new ConferenceDTO.RoomInfo();
        roomInfo.setRoomId(roomId);
        roomInfo.setName(request.getName());
        roomInfo.setDescription(request.getDescription());
        roomInfo.setIsPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : false);
        roomInfo.setMaxParticipants(request.getMaxParticipants());
        roomInfo.setStartTime(request.getStartTime());
        roomInfo.setEndTime(request.getEndTime());
        roomInfo.setTags(request.getTags() != null ? new ArrayList<>(request.getTags()) : new ArrayList<>());
        roomInfo.setRecordEnabled(request.getRecordEnabled() != null ? request.getRecordEnabled() : false);
        roomInfo.setStatus(ConferenceEnum.RoomStatus.WAITING.getName());
        roomInfo.setCreatedAt(LocalDateTime.now());
        roomInfo.setActiveUsers(0);
        roomInfo.setParticipants(new ArrayList<>());
        
        // 更新房间信息
        Map<String, Object> roomData = new HashMap<>();
        roomData.put("roomId", roomInfo.getRoomId());
        roomData.put("name", roomInfo.getName());
        roomData.put("description", roomInfo.getDescription());
        roomData.put("isPrivate", roomInfo.getIsPrivate());
        roomData.put("maxParticipants", roomInfo.getMaxParticipants());
        roomData.put("startTime", roomInfo.getStartTime());
        roomData.put("endTime", roomInfo.getEndTime());
        roomData.put("tags", roomInfo.getTags());
        roomData.put("recordEnabled", roomInfo.getRecordEnabled());
        roomData.put("status", roomInfo.getStatus());
        roomData.put("createdAt", roomInfo.getCreatedAt());
        roomData.put("activeUsers", roomInfo.getActiveUsers());
        roomData.put("participants", roomInfo.getParticipants());
        
        // 存储到房间信息映射中
        getRoomInfoMap().put(roomId, roomData);
        
        // 设置默认质量等级
        roomQualitySettings.put(roomId, ConferenceEnum.QualityLevel.MEDIUM);
        
        // 设置录制状态
        if (request.getRecordEnabled() != null && request.getRecordEnabled()) {
            roomRecordingStatus.put(roomId, false); // 初始为未开始录制
        }
        
        // 初始化消息历史
        roomMessageHistory.put(roomId, new ArrayList<>());
        
        logger.info("成功创建增强版会议房间: {}", roomId);
        return roomInfo;
    }

    @Override
    public boolean updateRoom(String roomId, ConferenceDTO.RoomOperationRequest request) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("更新房间失败，房间ID不能为空");
            return false;
        }
        
        if (request == null) {
            logger.error("更新房间失败，请求参数为空");
            return false;
        }
        
        if (request.getOperation() == null || request.getOperation().trim().isEmpty()) {
            logger.error("更新房间失败，操作类型不能为空");
            return false;
        }
        
        logger.info("更新会议房间: {}, 操作: {}", roomId, request.getOperation());
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("更新失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 根据操作类型更新房间信息
        // 注意：RoomOperationRequest主要用于操作控制，不包含具体的更新值
        // 这里需要重新设计或使用专门的更新请求DTO
        switch (request.getOperation()) {
            case "START":
                roomInfo.put("status", ConferenceEnum.RoomStatus.ACTIVE.getName());
                break;
            case "END":
                roomInfo.put("status", ConferenceEnum.RoomStatus.ENDED.getName());
                break;
            case "PAUSE":
                roomInfo.put("status", "PAUSED");
                break;
            case "RESUME":
                roomInfo.put("status", ConferenceEnum.RoomStatus.ACTIVE.getName());
                break;
            default:
                logger.warn("未知的操作类型: {}", request.getOperation());
                return false;
        }
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播房间更新消息
        String updateValue = roomInfo.get("status").toString();
        broadcastRoomUpdate(roomId, request.getOperation(), updateValue);
        
        logger.info("成功更新会议房间: {}", roomId);
        return true;
    }

    @Override
    public boolean closeRoom(String roomId, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("关闭房间失败，房间ID不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("关闭房间失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("关闭会议房间: {}, 操作者: {}", roomId, operatorId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("关闭失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 更新房间状态为关闭
        roomInfo.put("status", ConferenceEnum.RoomStatus.CLOSED.getName());
        roomInfo.put("closedAt", LocalDateTime.now());
        roomInfo.put("closedBy", operatorId);
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播房间关闭消息
        broadcastRoomClosed(roomId, operatorId);
        
        // 关闭所有会话
        closeAllSessionsInRoom(roomId);
        
        logger.info("成功关闭会议房间: {}", roomId);
        return true;
    }

    @Override
    public ConferenceDTO.ParticipantInfo joinRoom(String roomId, ConferenceDTO.JoinRoomRequest request) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("加入房间失败，房间ID不能为空");
            return null;
        }
        
        if (request == null) {
            logger.error("加入房间失败，请求参数为空");
            return null;
        }
        
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            logger.error("加入房间失败，用户ID不能为空");
            return null;
        }
        
        logger.info("用户加入会议房间: {}, 用户: {}", roomId, request.getUserId());
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("加入失败，房间不存在: {}", roomId);
            return null;
        }
        
        // 检查房间状态
        String status = (String) roomInfo.get("status");
        if (ConferenceEnum.RoomStatus.CLOSED.getName().equals(status)) {
            logger.warn("加入失败，房间已关闭: {}", roomId);
            return null;
        }
        
        // 检查最大参与人数
        Integer maxParticipants = (Integer) roomInfo.get("maxParticipants");
        Integer activeUsers = (Integer) roomInfo.getOrDefault("activeUsers", 0);
        if (maxParticipants != null && activeUsers >= maxParticipants) {
            logger.warn("加入失败，房间已满: {}", roomId);
            return null;
        }
        
        // 创建参与者信息
        ConferenceDTO.ParticipantInfo participant = new ConferenceDTO.ParticipantInfo();
        participant.setUserId(request.getUserId());
        participant.setUsername(request.getUsername());
        participant.setRole(ConferenceEnum.ParticipantRole.PARTICIPANT.getName());
        participant.setJoinTime(LocalDateTime.now());
        participant.setAudioEnabled(request.getAudioEnabled());
        participant.setVideoEnabled(request.getVideoEnabled());
        
        // 检查用户是否已在房间中
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        boolean userAlreadyInRoom = participants.stream().anyMatch(p -> request.getUserId().equals(p.getUserId()));
        if (userAlreadyInRoom) {
            logger.warn("加入失败，用户已在房间中: {}, 用户: {}", roomId, request.getUserId());
            return null;
        }
        
        // 更新参与者列表
        participants.add(participant);
        roomInfo.put("participants", participants);
        
        // 更新活跃用户数
        roomInfo.put("activeUsers", activeUsers + 1);
        
        // 更新房间状态
        if (ConferenceEnum.RoomStatus.WAITING.getName().equals(status)) {
            roomInfo.put("status", ConferenceEnum.RoomStatus.ACTIVE.getName());
        }
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 更新用户参与的房间 - 使用线程安全的Set
        userRooms.computeIfAbsent(request.getUserId(), k -> ConcurrentHashMap.newKeySet()).add(roomId);
        
        // 广播用户加入消息
        broadcastUserJoined(roomId, participant);
        
        logger.info("用户成功加入会议房间: {}, 用户: {}", roomId, request.getUserId());
        return participant;
    }

    @Override
    public boolean leaveRoom(String roomId, String userId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("离开房间失败，房间ID不能为空");
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("离开房间失败，用户ID不能为空");
            return false;
        }
        
        logger.info("用户离开会议房间: {}, 用户: {}", roomId, userId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("离开失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 更新参与者列表
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        boolean removed = participants.removeIf(p -> userId.equals(p.getUserId()));
        
        if (!removed) {
            logger.warn("离开失败，用户不在房间中: {}, 用户: {}", roomId, userId);
            return false;
        }
        
        // 更新活跃用户数
        Integer activeUsers = (Integer) roomInfo.getOrDefault("activeUsers", 0);
        roomInfo.put("activeUsers", Math.max(0, activeUsers - 1));
        
        // 更新房间状态
        if (participants.isEmpty()) {
            roomInfo.put("status", ConferenceEnum.RoomStatus.WAITING.getName());
        }
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 更新用户参与的房间
        Set<String> userRoomSet = userRooms.get(userId);
        if (userRoomSet != null) {
            userRoomSet.remove(roomId);
            if (userRoomSet.isEmpty()) {
                userRooms.remove(userId);
            }
        }
        
        // 广播用户离开消息
        broadcastUserLeft(roomId, userId);
        
        logger.info("用户成功离开会议房间: {}, 用户: {}", roomId, userId);
        return true;
    }

    @Override
    public boolean kickParticipant(String roomId, String userId, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("踢出参与者失败，房间ID不能为空");
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("踢出参与者失败，用户ID不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("踢出参与者失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("踢出参与者: 房间={}, 用户={}, 操作者={}", roomId, userId, operatorId);
        
        // 检查操作权限
        if (!checkRoomPermission(roomId, operatorId, ConferenceEnum.OperationType.KICK_USER)) {
            logger.warn("踢出失败，操作者无权限: {}, 操作者: {}", roomId, operatorId);
            return false;
        }
        
        // 执行离开操作
        boolean result = leaveRoom(roomId, userId);
        
        if (result) {
            // 广播踢出消息
            broadcastUserKicked(roomId, userId, operatorId);
            logger.info("成功踢出参与者: 房间={}, 用户={}", roomId, userId);
        }
        
        return result;
    }

    @Override
    public List<ConferenceDTO.ParticipantInfo> getRoomParticipants(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("获取房间参与者失败，房间ID不能为空");
            return Collections.emptyList();
        }
        
        logger.debug("获取房间参与者列表: {}", roomId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("获取失败，房间不存在: {}", roomId);
            return Collections.emptyList();
        }
        
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        return new ArrayList<>(participants);
    }

    @Override
    public ConferenceDTO.RoomInfo getRoomDetails(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("获取房间详细信息失败，房间ID不能为空");
            return null;
        }
        
        logger.debug("获取房间详细信息: {}", roomId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("获取失败，房间不存在: {}", roomId);
            return null;
        }
        
        // 转换为RoomInfo对象
        ConferenceDTO.RoomInfo roomDetails = new ConferenceDTO.RoomInfo();
        roomDetails.setRoomId((String) roomInfo.get("roomId"));
        roomDetails.setName((String) roomInfo.get("name"));
        roomDetails.setDescription((String) roomInfo.get("description"));
        roomDetails.setIsPrivate((Boolean) roomInfo.getOrDefault("isPrivate", false));
        roomDetails.setMaxParticipants((Integer) roomInfo.get("maxParticipants"));
        roomDetails.setActiveUsers((Integer) roomInfo.getOrDefault("activeUsers", 0));
        roomDetails.setStatus((String) roomInfo.getOrDefault("status", ConferenceEnum.RoomStatus.ACTIVE.getName()));
        roomDetails.setRecordEnabled((Boolean) roomInfo.getOrDefault("recordEnabled", false));
        roomDetails.setStartTime((LocalDateTime) roomInfo.get("startTime"));
        roomDetails.setEndTime((LocalDateTime) roomInfo.get("endTime"));
        roomDetails.setCreatedAt((LocalDateTime) roomInfo.get("createdAt"));
        roomDetails.setTags((List<String>) roomInfo.get("tags"));
        roomDetails.setParticipants((List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>()));
        
        return roomDetails;
    }

    @Override
    public List<ConferenceDTO.RoomInfo> getRoomList(ConferenceDTO.RoomListRequest request) {
        if (request == null) {
            logger.error("获取房间列表失败，请求参数为空");
            return Collections.emptyList();
        }
        
        // 设置默认值
        if (request.getPage() <= 0) {
            request.setPage(1);
        }
        if (request.getSize() <= 0) {
            request.setSize(10);
        }
        
        logger.debug("获取房间列表，页码: {}, 每页数量: {}, 状态: {}", 
                request.getPage(), request.getSize(), request.getStatus());
        
        // 获取所有房间
        List<Map<String, Object>> allRooms = new ArrayList<>(getRoomInfoMap().values());
        
        // 状态过滤
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            allRooms = allRooms.stream()
                .filter(room -> request.getStatus().equals(room.get("status")))
                .collect(Collectors.toList());
        }
        
        // 标签过滤
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            allRooms = allRooms.stream()
                .filter(room -> {
                    List<String> roomTags = (List<String>) room.get("tags");
                    return roomTags != null && roomTags.stream().anyMatch(request.getTags()::contains);
                })
                .collect(Collectors.toList());
        }
        
        // 排序
        if ("activeUsers".equals(request.getSortBy())) {
            allRooms.sort((r1, r2) -> {
                Integer u1 = (Integer) r1.getOrDefault("activeUsers", 0);
                Integer u2 = (Integer) r2.getOrDefault("activeUsers", 0);
                return request.getAscending() ? u1.compareTo(u2) : u2.compareTo(u1);
            });
        } else if ("createdAt".equals(request.getSortBy())) {
            allRooms.sort((r1, r2) -> {
                LocalDateTime t1 = (LocalDateTime) r1.get("createdAt");
                LocalDateTime t2 = (LocalDateTime) r2.get("createdAt");
                if (t1 == null) return request.getAscending() ? -1 : 1;
                if (t2 == null) return request.getAscending() ? 1 : -1;
                return request.getAscending() ? t1.compareTo(t2) : t2.compareTo(t1);
            });
        }
        
        // 分页
        int total = allRooms.size();
        int startIndex = (request.getPage() - 1) * request.getSize();
        int endIndex = Math.min(startIndex + request.getSize(), total);
        
        List<ConferenceDTO.RoomInfo> result = new ArrayList<>();
        if (startIndex < total) {
            List<Map<String, Object>> pagedRooms = allRooms.subList(startIndex, endIndex);
            
            // 转换为RoomInfo对象
            for (Map<String, Object> roomInfo : pagedRooms) {
                ConferenceDTO.RoomInfo roomDetails = new ConferenceDTO.RoomInfo();
                roomDetails.setRoomId((String) roomInfo.get("roomId"));
                roomDetails.setName((String) roomInfo.get("name"));
                roomDetails.setDescription((String) roomInfo.get("description"));
                roomDetails.setIsPrivate((Boolean) roomInfo.getOrDefault("isPrivate", false));
                roomDetails.setMaxParticipants((Integer) roomInfo.get("maxParticipants"));
                roomDetails.setActiveUsers((Integer) roomInfo.getOrDefault("activeUsers", 0));
                roomDetails.setStatus((String) roomInfo.getOrDefault("status", ConferenceEnum.RoomStatus.ACTIVE.getName()));
                roomDetails.setRecordEnabled((Boolean) roomInfo.getOrDefault("recordEnabled", false));
                roomDetails.setStartTime((LocalDateTime) roomInfo.get("startTime"));
                roomDetails.setEndTime((LocalDateTime) roomInfo.get("endTime"));
                roomDetails.setCreatedAt((LocalDateTime) roomInfo.get("createdAt"));
                roomDetails.setTags((List<String>) roomInfo.get("tags"));
                roomDetails.setParticipants((List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>()));
                
                result.add(roomDetails);
            }
        }
        
        logger.debug("成功获取房间列表，总数: {}, 当前页数据: {}", total, result.size());
        return result;
    }

    @Override
    public ConferenceDTO.ConferenceStats getConferenceStats() {
        logger.debug("获取会议统计信息");
        
        ConferenceDTO.ConferenceStats stats = new ConferenceDTO.ConferenceStats();
        
        // 获取所有房间
        Collection<Map<String, Object>> allRooms = getRoomInfoMap().values();
        
        // 计算总房间数
        stats.setTotalRooms((long) allRooms.size());
        
        // 计算活跃房间数
        long activeRooms = allRooms.stream()
            .filter(room -> ConferenceEnum.RoomStatus.ACTIVE.getName().equals(room.get("status")))
            .count();
        stats.setActiveRooms(activeRooms);
        
        // 计算总参与者数
        long totalParticipants = 0;
        
        for (Map<String, Object> room : allRooms) {
            List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) room.getOrDefault("participants", new ArrayList<>());
            totalParticipants += participants.size();
        }
        
        stats.setTotalParticipants(totalParticipants);
        
        // 计算今日新建会议数
        LocalDate today = LocalDate.now();
        long todayNewRooms = allRooms.stream()
            .filter(room -> {
                LocalDateTime createdAt = (LocalDateTime) room.get("createdAt");
                return createdAt != null && createdAt.toLocalDate().equals(today);
            })
            .count();
        stats.setTodayNewRooms(todayNewRooms);
        
        // 计算平均会议时长（分钟）
        double totalDuration = 0;
        int roomsWithDuration = 0;
        for (Map<String, Object> room : allRooms) {
            LocalDateTime createdAt = (LocalDateTime) room.get("createdAt");
            LocalDateTime endTime = (LocalDateTime) room.get("endTime");
            if (createdAt != null) {
                LocalDateTime endTimeToUse = endTime != null ? endTime : LocalDateTime.now();
                totalDuration += java.time.Duration.between(createdAt, endTimeToUse).toMinutes();
                roomsWithDuration++;
            }
        }
        double avgDuration = roomsWithDuration > 0 ? totalDuration / roomsWithDuration : 0.0;
        stats.setAvgDuration(avgDuration);
        
        // 设置最受欢迎的标签（简单实现）
        List<String> popularTags = new ArrayList<>();
        popularTags.add("技术讨论");
        popularTags.add("项目会议");
        popularTags.add("培训");
        stats.setPopularTags(popularTags);
        
        logger.debug("成功获取会议统计信息");
        return stats;
    }

    @Override
    public boolean updateParticipantMediaStatus(String roomId, String userId, 
                                              ConferenceEnum.MediaType mediaType, boolean enabled) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("更新媒体状态失败，房间ID不能为空");
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("更新媒体状态失败，用户ID不能为空");
            return false;
        }
        
        if (mediaType == null) {
            logger.error("更新媒体状态失败，媒体类型不能为空");
            return false;
        }
        
        logger.info("更新参与者媒体状态: 房间={}, 用户={}, 媒体类型={}, 状态={}", 
                roomId, userId, mediaType, enabled);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("更新失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 更新参与者列表
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        boolean updated = false;
        
        for (ConferenceDTO.ParticipantInfo participant : participants) {
            if (userId.equals(participant.getUserId())) {
                if (mediaType == ConferenceEnum.MediaType.AUDIO) {
                    participant.setAudioEnabled(enabled);
                } else if (mediaType == ConferenceEnum.MediaType.VIDEO) {
                    participant.setVideoEnabled(enabled);
                }
                updated = true;
                break;
            }
        }
        
        if (!updated) {
            logger.warn("更新失败，用户不在房间中: {}, 用户: {}", roomId, userId);
            return false;
        }
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播媒体状态变更消息
        broadcastMediaStatusChanged(roomId, userId, mediaType.getName(), enabled);
        
        logger.info("成功更新参与者媒体状态: 房间={}, 用户={}", roomId, userId);
        return true;
    }

    @Override
    public boolean setParticipantRole(String roomId, String userId, 
                                    ConferenceEnum.ParticipantRole role, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("设置参与者角色失败，房间ID不能为空");
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("设置参与者角色失败，用户ID不能为空");
            return false;
        }
        
        if (role == null) {
            logger.error("设置参与者角色失败，角色不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("设置参与者角色失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("设置参与者角色: 房间={}, 用户={}, 角色={}, 操作者={}", 
                roomId, userId, role, operatorId);
        
        // 检查操作权限
        if (!checkRoomPermission(roomId, operatorId, ConferenceEnum.OperationType.SET_ROLE)) {
            logger.warn("设置参与者角色失败，操作者无权限: 房间={}, 操作者={}, 目标用户={}, 目标角色={}", 
                    roomId, operatorId, userId, role.getName());
            return false;
        }
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("设置失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 更新参与者列表
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        boolean updated = false;
        
        for (ConferenceDTO.ParticipantInfo participant : participants) {
            if (userId.equals(participant.getUserId())) {
                participant.setRole(role.getName());
                updated = true;
                break;
            }
        }
        
        if (!updated) {
            logger.warn("设置失败，用户不在房间中: {}, 用户: {}", roomId, userId);
            return false;
        }
        
        // 更新房间信息
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播角色变更消息
        broadcastRoleChanged(roomId, userId, role.getName(), operatorId);
        
        logger.info("成功设置参与者角色: 房间={}, 用户={}, 角色={}", roomId, userId, role);
        return true;
    }

    @Override
    public boolean checkRoomPermission(String roomId, String userId, ConferenceEnum.OperationType operation) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("检查房间权限失败，房间ID不能为空");
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("检查房间权限失败，用户ID不能为空");
            return false;
        }
        
        if (operation == null) {
            logger.error("检查房间权限失败，操作类型不能为空");
            return false;
        }
        
        logger.debug("检查房间权限: 房间={}, 用户={}, 操作={}", roomId, userId, operation);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("检查失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 查找用户角色
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        String userRole = null;
        
        for (ConferenceDTO.ParticipantInfo participant : participants) {
            if (userId.equals(participant.getUserId())) {
                userRole = participant.getRole();
                break;
            }
        }
        
        if (userRole == null) {
            logger.warn("检查失败，用户不在房间中: {}, 用户: {}", roomId, userId);
            return false;
        }
        
        // 根据角色和操作类型判断权限
        if (ConferenceEnum.ParticipantRole.HOST.getName().equals(userRole)) {
            // 主持人拥有所有权限
            return true;
        } else if (ConferenceEnum.ParticipantRole.MODERATOR.getName().equals(userRole)) {
            // 管理员拥有除了关闭房间以外的所有权限
            return operation != ConferenceEnum.OperationType.CLOSE_ROOM;
        } else if (ConferenceEnum.ParticipantRole.PARTICIPANT.getName().equals(userRole)) {
            // 普通参与者只能控制自己的媒体状态和发送消息
            return operation == ConferenceEnum.OperationType.TOGGLE_MEDIA || 
                   operation == ConferenceEnum.OperationType.SEND_MESSAGE;
        }
        
        return false;
    }

    @Override
    public List<ConferenceDTO.RoomInfo> getUserRooms(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("获取用户房间列表失败，用户ID不能为空");
            return Collections.emptyList();
        }
        
        logger.debug("获取用户参与的房间列表: {}", userId);
        
        Set<String> userRoomSet = userRooms.get(userId);
        if (userRoomSet == null || userRoomSet.isEmpty()) {
            logger.debug("用户未参与任何房间: {}", userId);
            return Collections.emptyList();
        }
        
        List<ConferenceDTO.RoomInfo> result = new ArrayList<>();
        for (String roomId : userRoomSet) {
            ConferenceDTO.RoomInfo roomInfo = getRoomDetails(roomId);
            if (roomInfo != null) {
                result.add(roomInfo);
            }
        }
        
        logger.debug("成功获取用户参与的房间列表: {}, 房间数量: {}", userId, result.size());
        return result;
    }

    @Override
    public boolean startRecording(String roomId, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("开始录制失败，房间ID不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("开始录制失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("开始录制: 房间={}, 操作者={}", roomId, operatorId);
        
        // 检查操作权限
        if (!checkRoomPermission(roomId, operatorId, ConferenceEnum.OperationType.TOGGLE_RECORDING)) {
            logger.warn("开始录制失败，操作者无权限: {}, 操作者: {}", roomId, operatorId);
            return false;
        }
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("开始录制失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 检查录制是否已启用
        Boolean recordEnabled = (Boolean) roomInfo.getOrDefault("recordEnabled", false);
        if (!recordEnabled) {
            logger.warn("开始录制失败，房间未启用录制功能: {}", roomId);
            return false;
        }
        
        // 检查录制状态
        Boolean recording = roomRecordingStatus.getOrDefault(roomId, false);
        if (recording) {
            logger.warn("开始录制失败，房间已在录制中: {}", roomId);
            return false;
        }
        
        // 更新录制状态
        roomRecordingStatus.put(roomId, true);
        
        // 更新房间信息
        roomInfo.put("recordingStartTime", LocalDateTime.now());
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播录制开始消息
        broadcastRecordingStarted(roomId, operatorId);
        
        logger.info("成功开始录制: 房间={}", roomId);
        return true;
    }

    @Override
    public boolean stopRecording(String roomId, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("停止录制失败，房间ID不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("停止录制失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("停止录制: 房间={}, 操作者={}", roomId, operatorId);
        
        // 检查操作权限
        if (!checkRoomPermission(roomId, operatorId, ConferenceEnum.OperationType.TOGGLE_RECORDING)) {
            logger.warn("停止录制失败，操作者无权限: {}, 操作者: {}", roomId, operatorId);
            return false;
        }
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("停止录制失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 检查录制状态
        Boolean recording = roomRecordingStatus.getOrDefault(roomId, false);
        if (!recording) {
            logger.warn("停止录制失败，房间未在录制中: {}", roomId);
            return false;
        }
        
        // 更新录制状态
        roomRecordingStatus.put(roomId, false);
        
        // 更新房间信息
        LocalDateTime startTime = (LocalDateTime) roomInfo.get("recordingStartTime");
        LocalDateTime endTime = LocalDateTime.now();
        roomInfo.put("recordingEndTime", endTime);
        
        // 计算录制时长
        if (startTime != null) {
            long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
            roomInfo.put("recordingDuration", durationMinutes);
        }
        
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播录制停止消息
        broadcastRecordingStopped(roomId, operatorId);
        
        logger.info("成功停止录制: 房间={}", roomId);
        return true;
    }

    @Override
    public boolean getRecordingStatus(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("获取录制状态失败，房间ID不能为空");
            return false;
        }
        
        logger.debug("获取录制状态: {}", roomId);
        return roomRecordingStatus.getOrDefault(roomId, false);
    }

    @Override
    public boolean sendRoomMessage(String roomId, String senderId, String message) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("发送房间消息失败，房间ID不能为空");
            return false;
        }
        
        if (senderId == null || senderId.trim().isEmpty()) {
            logger.error("发送房间消息失败，发送者ID不能为空");
            return false;
        }
        
        if (message == null || message.trim().isEmpty()) {
            logger.error("发送房间消息失败，消息内容不能为空");
            return false;
        }
        
        logger.debug("发送房间消息: 房间={}, 发送者={}", roomId, senderId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("发送消息失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 检查发送者是否在房间中
        List<ConferenceDTO.ParticipantInfo> participants = (List<ConferenceDTO.ParticipantInfo>) roomInfo.getOrDefault("participants", new ArrayList<>());
        boolean senderInRoom = participants.stream().anyMatch(p -> senderId.equals(p.getUserId()));
        
        if (!senderInRoom) {
            logger.warn("发送消息失败，发送者不在房间中: {}, 发送者: {}", roomId, senderId);
            return false;
        }
        
        // 创建消息对象
        Map<String, Object> messageObj = new HashMap<>();
        messageObj.put("senderId", senderId);
        messageObj.put("message", message);
        messageObj.put("timestamp", LocalDateTime.now());
        
        // 查找发送者名称
        for (ConferenceDTO.ParticipantInfo participant : participants) {
            if (senderId.equals(participant.getUserId())) {
                messageObj.put("senderName", participant.getUsername());
                break;
            }
        }
        
        // 添加到消息历史 - 使用同步块确保线程安全
        synchronized (roomMessageHistory) {
            List<Map<String, Object>> messages = roomMessageHistory.computeIfAbsent(roomId, k -> new ArrayList<>());
            messages.add(messageObj);
            
            // 限制消息历史大小
            if (messages.size() > 100) {
                List<Map<String, Object>> newMessages = new ArrayList<>(messages.subList(messages.size() - 100, messages.size()));
                roomMessageHistory.put(roomId, newMessages);
            }
        }
        
        // 广播消息
        broadcastChatMessage(roomId, senderId, message);
        
        logger.debug("成功发送房间消息: 房间={}, 发送者={}", roomId, senderId);
        return true;
    }

    @Override
    public List<Map<String, Object>> getRoomMessageHistory(String roomId, int limit) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("获取房间消息历史失败，房间ID不能为空");
            return Collections.emptyList();
        }
        
        if (limit <= 0) {
            logger.error("获取房间消息历史失败，限制数量必须大于0");
            return Collections.emptyList();
        }
        
        logger.debug("获取房间消息历史: {}, 限制: {}", roomId, limit);
        
        // 使用同步块确保线程安全地读取消息历史
        synchronized (roomMessageHistory) {
            List<Map<String, Object>> messages = roomMessageHistory.get(roomId);
            if (messages == null || messages.isEmpty()) {
                logger.debug("房间没有消息历史: {}", roomId);
                return Collections.emptyList();
            }
            
            // 限制消息数量
            if (messages.size() > limit) {
                return new ArrayList<>(messages.subList(messages.size() - limit, messages.size()));
            } else {
                return new ArrayList<>(messages);
            }
        }
    }

    @Override
    public boolean setRoomQuality(String roomId, ConferenceEnum.QualityLevel qualityLevel, String operatorId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("设置房间质量等级失败，房间ID不能为空");
            return false;
        }
        
        if (qualityLevel == null) {
            logger.error("设置房间质量等级失败，质量等级不能为空");
            return false;
        }
        
        if (operatorId == null || operatorId.trim().isEmpty()) {
            logger.error("设置房间质量等级失败，操作者ID不能为空");
            return false;
        }
        
        logger.info("设置房间质量等级: 房间={}, 质量等级={}, 操作者={}", roomId, qualityLevel, operatorId);
        
        // 检查操作权限
        if (!checkRoomPermission(roomId, operatorId, ConferenceEnum.OperationType.SET_QUALITY)) {
            logger.warn("设置质量等级失败，操作者无权限: {}, 操作者: {}", roomId, operatorId);
            return false;
        }
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("设置质量等级失败，房间不存在: {}", roomId);
            return false;
        }
        
        // 更新质量等级
        roomQualitySettings.put(roomId, qualityLevel);
        
        // 更新房间信息
        roomInfo.put("qualityLevel", qualityLevel.getName());
        getRoomInfoMap().put(roomId, roomInfo);
        
        // 广播质量等级变更消息
        broadcastQualityChanged(roomId, qualityLevel.getName(), operatorId);
        
        logger.info("成功设置房间质量等级: 房间={}, 质量等级={}", roomId, qualityLevel);
        return true;
    }

    @Override
    public Map<String, Object> getRoomQualityStats(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            logger.error("获取房间质量统计失败，房间ID不能为空");
            return Collections.emptyMap();
        }
        
        logger.debug("获取房间质量统计: {}", roomId);
        
        Map<String, Object> roomInfo = (Map<String, Object>) getRoomInfo(roomId);
        if (roomInfo == null) {
            logger.warn("获取质量统计失败，房间不存在: {}", roomId);
            return Collections.emptyMap();
        }
        
        // 获取质量等级
        ConferenceEnum.QualityLevel qualityLevel = roomQualitySettings.getOrDefault(roomId, ConferenceEnum.QualityLevel.MEDIUM);
        
        // 创建质量统计对象
        Map<String, Object> stats = new HashMap<>();
        stats.put("roomId", roomId);
        stats.put("qualityLevel", qualityLevel.getName());
        
        // 根据质量等级设置推荐配置
        Map<String, Object> recommendedConfig = new HashMap<>();
        switch (qualityLevel) {
            case LOW:
                recommendedConfig.put("resolution", "640x480");
                recommendedConfig.put("frameRate", 15);
                recommendedConfig.put("bitrate", 500000);
                break;
            case STANDARD:
                recommendedConfig.put("resolution", "1280x720");
                recommendedConfig.put("frameRate", 30);
                recommendedConfig.put("bitrate", 1500000);
                break;
            case HIGH:
                recommendedConfig.put("resolution", "1920x1080");
                recommendedConfig.put("frameRate", 30);
                recommendedConfig.put("bitrate", 3000000);
                break;
        }
        stats.put("recommendedConfig", recommendedConfig);
        
        logger.debug("成功获取房间质量统计: {}", roomId);
        return stats;
    }

    // 广播方法
    
    private void broadcastRoomUpdate(String roomId, String operation, String value) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "room_update");
            message.put("roomId", roomId);
            message.put("operation", operation);
            message.put("value", value);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播房间更新消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastRoomClosed(String roomId, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "room_closed");
            message.put("roomId", roomId);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播房间关闭消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastUserJoined(String roomId, ConferenceDTO.ParticipantInfo participant) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "user_joined");
            message.put("roomId", roomId);
            message.put("participant", participant);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播用户加入消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastUserLeft(String roomId, String userId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "user_left");
            message.put("roomId", roomId);
            message.put("userId", userId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播用户离开消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastUserKicked(String roomId, String userId, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "user_kicked");
            message.put("roomId", roomId);
            message.put("userId", userId);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播用户踢出消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastMediaStatusChanged(String roomId, String userId, String mediaType, boolean enabled) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "media_status_changed");
            message.put("roomId", roomId);
            message.put("userId", userId);
            message.put("mediaType", mediaType);
            message.put("enabled", enabled);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播媒体状态变更消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastRoleChanged(String roomId, String userId, String role, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "role_changed");
            message.put("roomId", roomId);
            message.put("userId", userId);
            message.put("role", role);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播角色变更消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastRecordingStarted(String roomId, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "recording_started");
            message.put("roomId", roomId);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播录制开始消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastRecordingStopped(String roomId, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "recording_stopped");
            message.put("roomId", roomId);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播录制停止消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastQualityChanged(String roomId, String qualityLevel, String operatorId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "quality_changed");
            message.put("roomId", roomId);
            message.put("qualityLevel", qualityLevel);
            message.put("operatorId", operatorId);
            message.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(message).toString()));
        } catch (Exception e) {
            logger.error("广播质量等级变更消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastChatMessage(String roomId, String senderId, String message) {
        try {
            Map<String, Object> messageObj = new HashMap<>();
            messageObj.put("type", "chat_message");
            messageObj.put("roomId", roomId);
            messageObj.put("senderId", senderId);
            messageObj.put("message", message);
            messageObj.put("timestamp", LocalDateTime.now().toString());
            
            broadcastToRoom(roomId, new TextMessage(new JSONObject(messageObj).toString()));
        } catch (Exception e) {
            logger.error("广播聊天消息失败: {}", roomId, e);
        }
    }
    
    private void broadcastToRoom(String roomId, TextMessage message) {
        if (roomId == null || message == null) {
            logger.warn("广播消息失败，参数为空");
            return;
        }
        
        Map<WebSocketSession, String> sessions = getSessionRoomMap();

        // 使用迭代器避免并发修改异常
        for (Map.Entry<WebSocketSession, String> entry : new HashMap<>(sessions).entrySet()) {
            if (roomId.equals(entry.getValue()) && entry.getKey() != null && entry.getKey().isOpen()) {
                try {
                    entry.getKey().sendMessage(message);
                } catch (IOException e) {
                    logger.error("发送消息到会话失败: {}", entry.getKey().getId(), e);
                    // 移除失效的会话
                    sessions.remove(entry.getKey());
                }
            }
        }
    }
    
    private void closeAllSessionsInRoom(String roomId) {
        if (roomId == null) {
            logger.warn("关闭会话失败，房间ID为空");
            return;
        }
        
        Map<WebSocketSession, String> sessions = getSessionRoomMap();
        if (sessions == null) {
            logger.warn("关闭会话失败，会话映射为空");
            return;
        }
        
        // 使用迭代器避免并发修改异常
        for (Map.Entry<WebSocketSession, String> entry : new HashMap<>(sessions).entrySet()) {
            if (roomId.equals(entry.getValue()) && entry.getKey() != null) {
                try {
                    if (entry.getKey().isOpen()) {
                        entry.getKey().close();
                    }
                    // 移除会话
                    sessions.remove(entry.getKey());
                } catch (IOException e) {
                    logger.error("关闭会话失败: {}", entry.getKey().getId(), e);
                    // 即使关闭失败也要移除会话
                    sessions.remove(entry.getKey());
                }
            }
        }
    }
}