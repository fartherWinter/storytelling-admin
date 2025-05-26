package com.chennian.storytelling.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.utils.PageParam;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chennian.storytelling.workflow.domain.WorkflowModelDTO;
import com.chennian.storytelling.service.WorkflowModelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 工作流模型服务实现类
 * 
 * @author chennian
 */
@Service
public class WorkflowModelServiceImpl implements WorkflowModelService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowModelServiceImpl.class);
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createModel(WorkflowModelDTO modelDTO) {
        try {
            // 创建模型元数据
            ObjectNode metaInfo = objectMapper.createObjectNode();
            metaInfo.put("name", modelDTO.getName());
            metaInfo.put("description", modelDTO.getDescription());
            metaInfo.put("revision", 1);
            
            // 创建模型初始化数据
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            
            // 创建Flowable模型对象
            Model model = repositoryService.newModel();
            model.setName(modelDTO.getName());
            model.setKey(modelDTO.getKey());
            model.setCategory(modelDTO.getCategory());
            model.setMetaInfo(metaInfo.toString());
            model.setVersion(1);
            
            // 保存模型基本信息
            repositoryService.saveModel(model);
            
            // 保存模型编辑器数据
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));
            
            return model.getId();
        } catch (Exception e) {
            logger.error("创建工作流模型失败", e);
            throw new RuntimeException("创建工作流模型失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateModel(WorkflowModelDTO modelDTO) {
        try {
            // 获取模型对象
            Model model = repositoryService.getModel(modelDTO.getId());
            if (model == null) {
                return false;
            }
            
            // 更新模型基本信息
            model.setName(modelDTO.getName());
            model.setCategory(modelDTO.getCategory());
            
            // 更新模型元数据
            ObjectNode metaInfoNode;
            if (model.getMetaInfo() != null) {
                metaInfoNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            } else {
                metaInfoNode = objectMapper.createObjectNode();
            }
            metaInfoNode.put("name", modelDTO.getName());
            metaInfoNode.put("description", modelDTO.getDescription());
            model.setMetaInfo(metaInfoNode.toString());
            
            // 保存模型基本信息
            repositoryService.saveModel(model);
            
            // 如果有编辑器JSON数据，则更新
            if (modelDTO.getEditorJson() != null && !modelDTO.getEditorJson().isEmpty()) {
                repositoryService.addModelEditorSource(model.getId(), modelDTO.getEditorJson().getBytes("utf-8"));
            }
            
            return true;
        } catch (Exception e) {
            logger.error("更新工作流模型失败", e);
            return false;
        }
    }

    @Override
    public WorkflowModelDTO getModel(String modelId) {
        // 获取模型对象
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            return null;
        }
        
        return convertToModelDTO(model);
    }

    @Override
    public List<WorkflowModelDTO> listModels(String category, String key, String name) {
        // 构建查询条件
        ModelQuery modelQuery = repositoryService.createModelQuery();
        
        if (category != null && !category.isEmpty()) {
            modelQuery.modelCategoryLike("%" + category + "%");
        }
        
        if (key != null && !key.isEmpty()) {
            modelQuery.modelKey(key);
        }

        if (name != null && !name.isEmpty()) {
            modelQuery.modelNameLike("%" + name + "%");
        }

        // 按最后更新时间倒序排列
        modelQuery.orderByLastUpdateTime().desc();
        
        // 查询模型列表
        List<Model> models = modelQuery.list();
        List<WorkflowModelDTO> modelDTOs = new ArrayList<>();
        
        // 转换为DTO对象
        for (Model model : models) {
            modelDTOs.add(convertToModelDTO(model));
        }
        
        return modelDTOs;
    }
    
    @Override
    public IPage<WorkflowModelDTO> pageModels(PageParam<WorkflowModelDTO> page, String category, String key, String name) {
        // 构建查询条件
        ModelQuery modelQuery = repositoryService.createModelQuery();
        
        if (category != null && !category.isEmpty()) {
            modelQuery.modelCategoryLike("%" + category + "%");
        }
        
        if (key != null && !key.isEmpty()) {
            modelQuery.modelKey(key);
        }

        if (name != null && !name.isEmpty()) {
            modelQuery.modelNameLike("%" + name + "%");
        }

        // 按最后更新时间倒序排列
        modelQuery.orderByLastUpdateTime().desc();
        
        // 获取总记录数
        long total = modelQuery.count();
        
        // 计算分页参数
        int firstResult = (int) ((page.getCurrent() - 1) * page.getSize());
        int maxResults = (int) page.getSize();
        
        // 查询分页数据
        List<Model> models = modelQuery.listPage(firstResult, maxResults);
        List<WorkflowModelDTO> modelDTOs = new ArrayList<>();
        
        // 转换为DTO对象
        for (Model model : models) {
            modelDTOs.add(convertToModelDTO(model));
        }
        
        // 设置分页结果
        page.setRecords(modelDTOs);
        page.setTotal(total);
        
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteModel(String modelId) {
        try {
            repositoryService.deleteModel(modelId);
            return true;
        } catch (Exception e) {
            logger.error("删除工作流模型失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deployModel(String modelId) {
        try {
            // 获取模型对象
            Model model = repositoryService.getModel(modelId);
            if (model == null) {
                throw new RuntimeException("模型不存在: " + modelId);
            }
            
            // 获取模型的XML内容
            byte[] xmlBytes = getModelXmlBytes(modelId);
            if (xmlBytes == null) {
                throw new RuntimeException("模型数据为空，无法部署");
            }
            
            // 部署模型
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(model.getName())
                    .category(model.getCategory())
                    .addBytes(processName, xmlBytes)
                    .deploy();
            
            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();
            
            // 更新模型的部署ID和流程定义ID
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
            
            return deployment.getId();
        } catch (Exception e) {
            logger.error("部署工作流模型失败", e);
            throw new RuntimeException("部署工作流模型失败: " + e.getMessage());
        }
    }

    @Override
    public String getModelXml(String modelId) {
        try {
            byte[] xmlBytes = getModelXmlBytes(modelId);
            return xmlBytes != null ? new String(xmlBytes, "utf-8") : null;
        } catch (Exception e) {
            logger.error("获取模型XML失败", e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveModelXml(String modelId, String xml) {
        try {
            // 获取模型对象
            Model model = repositoryService.getModel(modelId);
            if (model == null) {
                return false;
            }
            
            // 将XML字符串转换为XMLStreamReader
            XMLInputFactory factory = XMLInputFactory.newInstance();
            StringReader reader = new StringReader(xml);
            XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(reader);
            // 将XML转换为JSON
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xmlStreamReader);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
            
            // 保存模型编辑器数据
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
            
            return true;
        } catch (Exception e) {
            logger.error("保存模型XML失败", e);
            return false;
        }
    }

    @Override
    public byte[] exportModelXml(String modelId) {
        try {
            return getModelXmlBytes(modelId);
        } catch (Exception e) {
            logger.error("导出模型XML失败", e);
            return null;
        }
    }

    @Override
    public byte[] exportModelPng(String modelId) {
        try {
            // 获取模型的BpmnModel对象
            BpmnModel bpmnModel = getBpmnModel(modelId);
            if (bpmnModel == null) {
                return null;
            }
            
            // 生成流程图
            ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // 生成PNG图片
            diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    processEngine.getProcessEngineConfiguration().getLabelFontName(),
                    processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                    processEngine.getProcessEngineConfiguration().getClassLoader(),
                    1.0,
                    false);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("导出模型PNG失败", e);
            return null;
        }
    }
    
    /**
     * 获取模型的XML字节数组
     */
    private byte[] getModelXmlBytes(String modelId) throws IOException {
        // 获取模型的JSON数据
        byte[] jsonBytes = repositoryService.getModelEditorSource(modelId);
        if (jsonBytes == null) {
            return null;
        }
        
        // 将JSON转换为BpmnModel
        JsonNode editorNode = objectMapper.readTree(jsonBytes);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
        
        // 将BpmnModel转换为XML
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        return xmlConverter.convertToXML(bpmnModel);
    }
    
    /**
     * 获取模型的BpmnModel对象
     */
    private BpmnModel getBpmnModel(String modelId) throws IOException {
        // 获取模型的JSON数据
        byte[] jsonBytes = repositoryService.getModelEditorSource(modelId);
        if (jsonBytes == null) {
            return null;
        }
        
        // 将JSON转换为BpmnModel
        JsonNode editorNode = objectMapper.readTree(jsonBytes);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        return jsonConverter.convertToBpmnModel(editorNode);
    }
    
    /**
     * 将Model对象转换为DTO
     */
    private WorkflowModelDTO convertToModelDTO(Model model) {
        WorkflowModelDTO dto = new WorkflowModelDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setKey(model.getKey());
        dto.setCategory(model.getCategory());
        dto.setVersion(model.getVersion());
        dto.setMetaInfo(model.getMetaInfo());
        dto.setDeploymentId(model.getDeploymentId());
        dto.setCreateTime(model.getCreateTime());
        dto.setLastUpdateTime(model.getLastUpdateTime());
        dto.setDeployed(model.getDeploymentId() != null);
        
        // 解析元数据获取描述信息
        try {
            if (model.getMetaInfo() != null) {
                JsonNode metaInfoNode = objectMapper.readTree(model.getMetaInfo());
                if (metaInfoNode.has("description")) {
                    dto.setDescription(metaInfoNode.get("description").asText());
                }
            }
        } catch (Exception e) {
            logger.warn("解析模型元数据失败", e);
        }
        
        return dto;
    }
}