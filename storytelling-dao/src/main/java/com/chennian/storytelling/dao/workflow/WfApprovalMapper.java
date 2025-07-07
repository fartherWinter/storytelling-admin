package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.workflow.WfApproval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流审批 Mapper 接口
 * 
 * @author chennian
 */
@Mapper
public interface WfApprovalMapper extends BaseMapper<WfApproval> {

    /**
     * 分页查询审批记录
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfApproval> selectApprovalPage(IPage<WfApproval> page, @Param("params") Map<String, Object> params);

    /**
     * 查询用户的审批记录
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfApproval> selectUserApprovals(IPage<WfApproval> page,
                                        @Param("userId") Long userId,
                                        @Param("params") Map<String, Object> params);

    /**
     * 查询流程实例的审批记录
     *
     * @param processInstanceId 流程实例ID
     * @return 审批记录列表
     */
    List<WfApproval> selectProcessApprovals(@Param("processInstanceId") String processInstanceId);

    /**
     * 查询任务的审批记录
     *
     * @param taskId 任务ID
     * @return 审批记录列表
     */
    List<WfApproval> selectTaskApprovals(@Param("taskId") String taskId);

    /**
     * 批量插入审批记录
     *
     * @param approvals 审批记录列表
     * @return 插入行数
     */
    int batchInsert(@Param("approvals") List<WfApproval> approvals);

    /**
     * 更新审批状态
     *
     * @param approvalId 审批ID
     * @param status 状态
     * @param updateBy 更新人
     * @return 更新行数
     */
    int updateStatus(@Param("approvalId") Long approvalId,
                    @Param("status") String status,
                    @Param("updateBy") String updateBy);

    /**
     * 批量更新审批状态
     *
     * @param approvalIds 审批ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("approvalIds") List<Long> approvalIds,
                         @Param("status") String status,
                         @Param("updateBy") String updateBy);

    /**
     * 添加审批意见
     *
     * @param approvalId 审批ID
     * @param comment 审批意见
     * @param updateBy 更新人
     * @return 更新行数
     */
    int addComment(@Param("approvalId") Long approvalId,
                  @Param("comment") String comment,
                  @Param("updateBy") String updateBy);

    /**
     * 查询审批意见历史
     *
     * @param approvalId 审批ID
     * @return 意见历史列表
     */
    List<Map<String, Object>> selectCommentHistory(@Param("approvalId") Long approvalId);

    /**
     * 统计用户审批情况
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> countUserApprovals(@Param("userId") Long userId,
                                         @Param("startTime") Date startTime,
                                         @Param("endTime") Date endTime);

    /**
     * 统计流程审批情况
     *
     * @param processKey 流程标识
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> countProcessApprovals(@Param("processKey") String processKey,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

    /**
     * 查询待办审批任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfApproval> selectTodoApprovals(IPage<WfApproval> page,
                                        @Param("userId") Long userId,
                                        @Param("params") Map<String, Object> params);

    /**
     * 查询已办审批任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfApproval> selectDoneApprovals(IPage<WfApproval> page,
                                        @Param("userId") Long userId,
                                        @Param("params") Map<String, Object> params);

    /**
     * 查询抄送审批任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfApproval> selectCcApprovals(IPage<WfApproval> page,
                                      @Param("userId") Long userId,
                                      @Param("params") Map<String, Object> params);

    /**
     * 查询审批任务处理时长统计
     *
     * @param processKey 流程标识
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> selectApprovalDurationStats(@Param("processKey") String processKey,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 查询审批任务驳回率统计
     *
     * @param processKey 流程标识
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> selectApprovalRejectStats(@Param("processKey") String processKey,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    /**
     * 查询审批任务超时统计
     *
     * @param processKey 流程标识
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> selectApprovalTimeoutStats(@Param("processKey") String processKey,
                                                      @Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime);

    /**
     * 查询审批任务委托记录
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<Map<String, Object>> selectDelegateRecords(IPage<Map<String, Object>> page,
                                                   @Param("userId") Long userId,
                                                   @Param("params") Map<String, Object> params);

    /**
     * 查询审批任务转办记录
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<Map<String, Object>> selectTransferRecords(IPage<Map<String, Object>> page,
                                                   @Param("userId") Long userId,
                                                   @Param("params") Map<String, Object> params);
} 