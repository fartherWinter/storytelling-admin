package com.chennian.storytelling.admin.controller.workflow;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chennian.storytelling.service.WorkflowModelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 工作流设计器控制器
 * 
 * @author chennian
 */
@RestController
@RequestMapping("/sys/workflow/designer")
public class WorkflowDesignerController {

    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private WorkflowModelService workflowModelService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 获取流程设计器编辑器数据
     */
    @GetMapping("/editor/stencilset")
    public JsonNode getStencilset() {
        try {
            // 返回流程设计器所需的模型集合定义
            ObjectNode stencilNode = objectMapper.createObjectNode();
            stencilNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            return stencilNode;
        } catch (Exception e) {
            throw new RuntimeException("获取流程设计器模型集合失败", e);
        }
    }
    
    /**
     * 获取模型的编辑器数据
     */
    @GetMapping("/model/{modelId}/json")
    public JsonNode getEditorJson(@PathVariable String modelId) {
        try {
            // 获取模型的编辑器源数据
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
            if (modelEditorSource == null) {
                // 如果没有编辑器数据，创建一个空的模型
                Model model = repositoryService.getModel(modelId);
                if (model != null) {
                    ObjectNode editorNode = objectMapper.createObjectNode();
                    editorNode.put("id", "canvas");
                    editorNode.put("resourceId", "canvas");
                    ObjectNode stencilSetNode = objectMapper.createObjectNode();
                    stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
                    editorNode.set("stencilset", stencilSetNode);
                    return editorNode;
                }
                return objectMapper.createObjectNode();
            }
            
            // 解析JSON数据
            return objectMapper.readTree(modelEditorSource);
        } catch (Exception e) {
            throw new RuntimeException("获取模型编辑器数据失败", e);
        }
    }
    
    /**
     * 保存模型的编辑器数据
     */
    @PostMapping("/model/{modelId}/save")
    public void saveModel(@PathVariable String modelId, @RequestBody Map<String, Object> values) {
        try {
            // 获取模型对象
            Model model = repositoryService.getModel(modelId);
            if (model == null) {
                throw new RuntimeException("模型不存在: " + modelId);
            }
            
            // 获取模型名称和描述
            String name = (String) values.get("name");
            String description = (String) values.get("description");
            String jsonXml = (String) values.get("json_xml");
            String svgXml = (String) values.get("svg_xml");
            
            // 更新模型元数据
            ObjectNode metaInfoNode;
            if (model.getMetaInfo() != null) {
                metaInfoNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            } else {
                metaInfoNode = objectMapper.createObjectNode();
            }
            metaInfoNode.put("name", name);
            metaInfoNode.put("description", description);
            model.setMetaInfo(metaInfoNode.toString());
            model.setName(name);
            
            // 保存模型基本信息
            repositoryService.saveModel(model);
            
            // 保存模型编辑器数据
            repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes("utf-8"));
            
            // 保存模型图片数据
            if (svgXml != null && !svgXml.isEmpty()) {
                repositoryService.addModelEditorSourceExtra(model.getId(), svgXml.getBytes("utf-8"));
            }
        } catch (Exception e) {
            throw new RuntimeException("保存模型失败", e);
        }
    }
    
    /**
     * 将模型转换为可部署的流程定义
     */
    @PostMapping("/model/{modelId}/convert-to-xml")
    public Map<String, Object> convertToXml(@PathVariable String modelId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取模型的XML内容
            String xml = workflowModelService.getModelXml(modelId);
            if (xml == null) {
                result.put("success", false);
                result.put("message", "模型数据为空，无法转换");
                return result;
            }
            
            result.put("success", true);
            result.put("xml", xml);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "转换失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 部署模型为流程定义
     */
    @PostMapping("/model/{modelId}/deploy")
    public Map<String, Object> deployModel(@PathVariable String modelId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 部署模型
            String deploymentId = workflowModelService.deployModel(modelId);
            
            result.put("success", true);
            result.put("deploymentId", deploymentId);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "部署失败: " + e.getMessage());
            return result;
        }
    }
}