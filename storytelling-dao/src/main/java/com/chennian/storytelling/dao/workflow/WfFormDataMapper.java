package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfFormData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作流表单数据Mapper接口
 * 
 * @author chennian
 */
@Mapper
public interface WfFormDataMapper extends BaseMapper<WfFormData> {

    /**
     * 根据表单ID和实例ID查询表单数据
     * 
     * @param formId 表单ID
     * @param instanceId 实例ID
     * @return 表单数据
     */
    WfFormData selectByFormIdAndInstanceId(@Param("formId") String formId, @Param("instanceId") String instanceId);

    /**
     * 根据表单ID查询表单数据列表
     * 
     * @param formId 表单ID
     * @return 表单数据列表
     */
    List<WfFormData> selectByFormId(@Param("formId") String formId);

    /**
     * 根据实例ID查询表单数据列表
     * 
     * @param instanceId 实例ID
     * @return 表单数据列表
     */
    List<WfFormData> selectByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 根据任务ID查询表单数据
     * 
     * @param taskId 任务ID
     * @return 表单数据
     */
    WfFormData selectByTaskId(@Param("taskId") String taskId);

    /**
     * 根据业务键查询表单数据列表
     * 
     * @param businessKey 业务键
     * @return 表单数据列表
     */
    List<WfFormData> selectByBusinessKey(@Param("businessKey") String businessKey);

    /**
     * 根据创建者ID查询表单数据列表
     * 
     * @param creatorId 创建者ID
     * @return 表单数据列表
     */
    List<WfFormData> selectByCreatorId(@Param("creatorId") String creatorId);

    /**
     * 根据状态查询表单数据列表
     * 
     * @param status 状态
     * @return 表单数据列表
     */
    List<WfFormData> selectByStatus(@Param("status") Integer status);

    /**
     * 更新表单数据状态
     * 
     * @param id 主键ID
     * @param status 状态
     * @return 更新行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据表单ID和实例ID删除表单数据
     * 
     * @param formId 表单ID
     * @param instanceId 实例ID
     * @return 删除行数
     */
    int deleteByFormIdAndInstanceId(@Param("formId") String formId, @Param("instanceId") String instanceId);

    /**
     * 根据实例ID批量删除表单数据
     * 
     * @param instanceId 实例ID
     * @return 删除行数
     */
    int deleteByInstanceId(@Param("instanceId") String instanceId);

}