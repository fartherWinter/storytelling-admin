package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfInstance;
import com.chennian.storytelling.bean.dto.InstanceStatisticsDTO;
import com.chennian.storytelling.bean.dto.TodayCompletedInstanceDTO;
import com.chennian.storytelling.bean.dto.AverageProcessingTimeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 工作流实例Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfInstanceMapper extends BaseMapper<WfInstance> {

    /**
     * 根据业务键查询实例
     * 
     * @param businessKey 业务键
     * @return 工作流实例
     */
    WfInstance selectByBusinessKey(@Param("businessKey") String businessKey);

    /**
     * 根据流程定义ID查询实例列表
     * 
     * @param processDefinitionId 流程定义ID
     * @return 实例列表
     */
    List<WfInstance> selectByProcessDefinitionId(@Param("processDefinitionId") String processDefinitionId);

    /**
     * 根据流程定义键查询实例列表
     * 
     * @param processDefinitionKey 流程定义键
     * @return 实例列表
     */
    List<WfInstance> selectByProcessDefinitionKey(@Param("processDefinitionKey") String processDefinitionKey);

    /**
     * 根据实例状态查询实例列表
     * 
     * @param instanceStatus 实例状态
     * @return 实例列表
     */
    List<WfInstance> selectByInstanceStatus(@Param("instanceStatus") String instanceStatus);

    /**
     * 根据发起人ID查询实例列表
     * 
     * @param startUserId 发起人ID
     * @return 实例列表
     */
    List<WfInstance> selectByStartUserId(@Param("startUserId") String startUserId);

    /**
     * 根据发起部门ID查询实例列表
     * 
     * @param startDeptId 发起部门ID
     * @return 实例列表
     */
    List<WfInstance> selectByStartDeptId(@Param("startDeptId") String startDeptId);

    /**
     * 根据优先级查询实例列表
     * 
     * @param priority 优先级
     * @return 实例列表
     */
    List<WfInstance> selectByPriority(@Param("priority") Integer priority);

    /**
     * 根据租户ID查询实例列表
     * 
     * @param tenantId 租户ID
     * @return 实例列表
     */
    List<WfInstance> selectByTenantId(@Param("tenantId") String tenantId);

    /**
     * 查询运行中的实例列表
     * 
     * @return 实例列表
     */
    List<WfInstance> selectRunningInstances();

    /**
     * 查询已完成的实例列表
     * 
     * @param processDefinitionId 流程定义ID
     * @param processDefinitionKey 流程定义键
     * @param businessKey 业务键
     * @param startUserId 发起人ID
     * @param startDeptId 发起部门ID
     * @param tenantId 租户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 实例列表
     */
    List<WfInstance> selectCompletedInstances(@Param("processDefinitionId") String processDefinitionId,
                                             @Param("processDefinitionKey") String processDefinitionKey,
                                             @Param("businessKey") String businessKey,
                                             @Param("startUserId") String startUserId,
                                             @Param("startDeptId") String startDeptId,
                                             @Param("tenantId") String tenantId,
                                             @Param("startTime") java.util.Date startTime,
                                             @Param("endTime") java.util.Date endTime);

    /**
     * 查询实例详情
     * 
     * @param instanceId 实例ID
     * @return 工作流实例
     */
    WfInstance selectInstanceDetail(@Param("instanceId") String instanceId);

    /**
     * 根据实例ID列表批量查询实例
     * 
     * @param instanceIds 实例ID列表
     * @return 实例列表
     */
    List<WfInstance> selectByInstanceIds(@Param("instanceIds") List<String> instanceIds);

    /**
     * 查询实例统计信息
     * 
     * @param processDefinitionKey 流程定义键
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectInstanceStatistics(@Param("processDefinitionKey") String processDefinitionKey,
                                                                 @Param("startTime") java.util.Date startTime,
                                                                 @Param("endTime") java.util.Date endTime);

    /**
     * 查询超时实例
     * 
     * @param timeoutMinutes 超时分钟数
     * @return 实例列表
     */
    List<WfInstance> selectTimeoutInstances(@Param("timeoutMinutes") Integer timeoutMinutes);

    /**
     * 更新实例状态
     * 
     * @param instanceId 实例ID
     * @param status 状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int updateInstanceStatus(@Param("instanceId") String instanceId, 
                            @Param("status") String status, 
                            @Param("updatedBy") String updatedBy);

    /**
     * 查询用户参与的实例
     * 
     * @param userId 用户ID
     * @param status 实例状态
     * @return 实例列表
     */
    List<WfInstance> selectUserInstances(@Param("userId") String userId, 
                                        @Param("status") String status);

    /**
     * 查询已挂起的实例列表
     * 
     * @return 实例列表
     */
    List<WfInstance> selectSuspendedInstances();

    /**
     * 查询已终止的实例列表
     * 
     * @return 实例列表
     */
    List<WfInstance> selectTerminatedInstances();

    /**
     * 根据时间范围查询实例列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 实例列表
     */
    List<WfInstance> selectByTimeRange(@Param("startTime") Date startTime, 
                                      @Param("endTime") Date endTime);

    /**
     * 查询超时的实例列表
     * 
     * @return 实例列表
     */
    List<WfInstance> selectOverdueInstances();

    /**
     * 查询即将超时的实例列表
     * 
     * @param hours 小时数
     * @return 实例列表
     */
    List<WfInstance> selectSoonOverdueInstances(@Param("hours") Integer hours);

    /**
     * 根据实例名称模糊查询
     * 
     * @param instanceName 实例名称
     * @return 实例列表
     */
    List<WfInstance> selectByInstanceNameLike(@Param("instanceName") String instanceName);

    /**
     * 查询实例统计信息
     * 
     * @return 统计信息
     */
    List<InstanceStatisticsDTO> selectInstanceStatistics();

    /**
     * 根据状态统计实例数量
     * 
     * @return 统计信息
     */
    List<InstanceStatisticsDTO> selectInstanceCountByStatus();

    /**
     * 根据流程定义统计实例数量
     * 
     * @return 统计信息
     */
    List<InstanceStatisticsDTO> selectInstanceCountByProcessDefinition();

    /**
     * 根据发起人统计实例数量
     * 
     * @return 统计信息
     */
    List<InstanceStatisticsDTO> selectInstanceCountByStartUser();

    /**
     * 根据部门统计实例数量
     * 
     * @return 统计信息
     */
    List<InstanceStatisticsDTO> selectInstanceCountByDept();

    /**
     * 查询平均处理时长
     * 
     * @return 平均时长（毫秒）
     */
    Long selectAverageDuration();

    /**
     * 根据流程定义查询平均处理时长
     * 
     * @param processDefinitionKey 流程定义键
     * @return 平均时长（毫秒）
     */
    Long selectAverageDurationByProcessDefinition(@Param("processDefinitionKey") String processDefinitionKey);

    /**
     * 批量更新实例状态
     * 
     * @param instanceIds 实例ID列表
     * @param instanceStatus 实例状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateInstanceStatus(@Param("instanceIds") List<String> instanceIds, 
                                 @Param("instanceStatus") String instanceStatus, 
                                 @Param("updatedBy") String updatedBy);

    /**
     * 查询今日完成的实例统计
     * 
     * @return 今日完成实例统计信息
     */
    List<TodayCompletedInstanceDTO> selectTodayCompletedInstances();

    /**
     * 查询平均处理时间统计
     * 
     * @return 平均处理时间统计信息
     */
    List<AverageProcessingTimeDTO> selectAverageProcessingTime();
}