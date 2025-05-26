package com.chennian.storytelling.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.workflow.domain.WorkflowModelDTO;

/**
 * 工作流模型服务接口
 * 
 * @author chennian
 */
public interface WorkflowModelService {

    /**
     * 创建工作流模型
     * 
     * @param model 工作流模型信息
     * @return 模型ID
     */
    String createModel(WorkflowModelDTO model);
    
    /**
     * 更新工作流模型
     * 
     * @param model 工作流模型信息
     * @return 是否成功
     */
    boolean updateModel(WorkflowModelDTO model);
    
    /**
     * 获取工作流模型
     * 
     * @param modelId 模型ID
     * @return 工作流模型
     */
    WorkflowModelDTO getModel(String modelId);
    
    /**
     * 查询工作流模型列表
     * 
     * @param category 分类（可选）
     * @param key 模型键（可选）
     * @param name 模型名称（可选）
     * @return 模型列表
     */
    List<WorkflowModelDTO> listModels(String category, String key, String name);
    
    /**
     * 分页查询工作流模型列表
     * 
     * @param page 分页参数
     * @param category 分类（可选）
     * @param key 模型键（可选）
     * @param name 模型名称（可选）
     * @return 分页模型列表
     */
    IPage<WorkflowModelDTO> pageModels(PageParam<WorkflowModelDTO> page, String category, String key, String name);
    
    /**
     * 删除工作流模型
     * 
     * @param modelId 模型ID
     * @return 是否成功
     */
    boolean deleteModel(String modelId);
    
    /**
     * 部署工作流模型
     * 
     * @param modelId 模型ID
     * @return 部署ID
     */
    String deployModel(String modelId);
    
    /**
     * 获取模型的BPMN XML
     * 
     * @param modelId 模型ID
     * @return BPMN XML内容
     */
    String getModelXml(String modelId);
    
    /**
     * 保存模型的BPMN XML
     * 
     * @param modelId 模型ID
     * @param xml BPMN XML内容
     * @return 是否成功
     */
    boolean saveModelXml(String modelId, String xml);
    
    /**
     * 导出模型为BPMN XML文件
     * 
     * @param modelId 模型ID
     * @return XML字节数组
     */
    byte[] exportModelXml(String modelId);
    
    /**
     * 导出模型为PNG图片
     * 
     * @param modelId 模型ID
     * @return 图片字节数组
     */
    byte[] exportModelPng(String modelId);
}