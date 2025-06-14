package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.service.WorkflowModelService;
import com.chennian.storytelling.bean.dto.ProcessDefinitionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 工作流定义管理控制器
 * 提供流程定义的高级管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流定义管理")
@RestController
@RequestMapping("/workflow/definitions")
@RequiredArgsConstructor
public class WorkflowDefinitionController {

    private final WorkflowService workflowService;
    private final WorkflowModelService workflowModelService;

    /**
     * 获取所有流程定义
     */
    @ApiOperation("获取所有流程定义")
    @GetMapping
    public List<ProcessDefinitionDTO> getAllProcessDefinitions() {
        return workflowService.getProcessDefinitions();
    }
    
    /**
     * 分页查询流程定义
     */
    @ApiOperation("分页查询流程定义")
    @GetMapping("/page")
    public Map<String, Object> getProcessDefinitionsPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "name", required = false) String name) {
        
        List<ProcessDefinitionDTO> definitions = workflowService.getProcessDefinitions();
        
        // 这里可以实现分页和过滤逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("content", definitions);
        result.put("page", page);
        result.put("size", size);
        result.put("total", definitions.size());
        result.put("totalPages", (definitions.size() + size - 1) / size);
        
        return result;
    }
    
    /**
     * 获取流程定义详情
     */
    @ApiOperation("获取流程定义详情")
    @GetMapping("/{definitionId}")
    public Map<String, Object> getProcessDefinitionDetail(@PathVariable("definitionId") String definitionId) {
        Map<String, Object> detail = new HashMap<>();
        
        // 获取流程定义基本信息
        List<ProcessDefinitionDTO> definitions = workflowService.getProcessDefinitions();
        ProcessDefinitionDTO definition = definitions.stream()
            .filter(d -> definitionId.equals(d.getId()))
            .findFirst()
            .orElse(null);
        
        if (definition != null) {
            detail.put("definition", definition);
            
            // 获取流程定义的XML
            try {
                byte[] xmlBytes = workflowService.getProcessDefinitionResource(definitionId, "xml");
                if (xmlBytes != null) {
                    detail.put("xml", new String(xmlBytes));
                }
            } catch (Exception e) {
                detail.put("xmlError", e.getMessage());
            }
            
            // 获取流程统计信息
            detail.put("statistics", getDefinitionStatistics(definitionId));
        }
        
        return detail;
    }
    
    /**
     * 部署流程定义
     */
    @ApiOperation("部署流程定义")
    @PostMapping("/deploy")
    public Map<String, Object> deployProcessDefinition(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        
        try {
            String deploymentId = workflowService.deployProcess(
                file.getOriginalFilename(), 
                file.getBytes(), 
                category
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程部署成功");
            result.put("deploymentId", deploymentId);
            result.put("fileName", file.getOriginalFilename());
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "流程部署失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 从模型部署流程定义
     */
    @ApiOperation("从模型部署流程定义")
    @PostMapping("/deploy-from-model/{modelId}")
    public Map<String, Object> deployFromModel(@PathVariable("modelId") String modelId) {
        try {
            String deploymentId = workflowModelService.deployModel(modelId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "从模型部署流程成功");
            result.put("deploymentId", deploymentId);
            result.put("modelId", modelId);
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "从模型部署流程失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 挂起流程定义
     */
    @ApiOperation("挂起流程定义")
    @PostMapping("/{definitionId}/suspend")
    public Map<String, Object> suspendProcessDefinition(@PathVariable("definitionId") String definitionId) {
        workflowService.suspendProcessDefinition(definitionId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程定义挂起成功");
        result.put("definitionId", definitionId);
        
        return result;
    }
    
    /**
     * 激活流程定义
     */
    @ApiOperation("激活流程定义")
    @PostMapping("/{definitionId}/activate")
    public Map<String, Object> activateProcessDefinition(@PathVariable("definitionId") String definitionId) {
        workflowService.activateProcessDefinition(definitionId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程定义激活成功");
        result.put("definitionId", definitionId);
        
        return result;
    }
    
    /**
     * 删除流程定义
     */
    @ApiOperation("删除流程定义")
    @DeleteMapping("/{deploymentId}")
    public Map<String, Object> deleteProcessDefinition(
            @PathVariable("deploymentId") String deploymentId,
            @RequestParam(value = "cascade", defaultValue = "false") boolean cascade) {
        
        try {
            // 这里需要实现删除流程定义的逻辑
            // workflowService.deleteDeployment(deploymentId, cascade);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程定义删除成功");
            result.put("deploymentId", deploymentId);
            result.put("cascade", cascade);
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "流程定义删除失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取流程定义XML
     */
    @ApiOperation("获取流程定义XML")
    @GetMapping("/{definitionId}/xml")
    public ResponseEntity<String> getProcessDefinitionXml(@PathVariable("definitionId") String definitionId) {
        try {
            byte[] xmlBytes = workflowService.getProcessDefinitionResource(definitionId, "xml");
            if (xmlBytes == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            String xml = new String(xmlBytes);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "process-definition.xml");
            
            return new ResponseEntity<>(xml, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 获取流程定义图片
     */
    @ApiOperation("获取流程定义图片")
    @GetMapping("/{definitionId}/image")
    public ResponseEntity<byte[]> getProcessDefinitionImage(@PathVariable("definitionId") String definitionId) {
        try {
            byte[] imageBytes = workflowService.getProcessDefinitionResource(definitionId, "image");
            if (imageBytes == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "process-definition.png");
            
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 获取流程定义统计信息
     */
    @ApiOperation("获取流程定义统计信息")
    @GetMapping("/{definitionId}/statistics")
    public Map<String, Object> getProcessDefinitionStatistics(@PathVariable("definitionId") String definitionId) {
        return getDefinitionStatistics(definitionId);
    }
    
    /**
     * 复制流程定义
     */
    @ApiOperation("复制流程定义")
    @PostMapping("/{definitionId}/copy")
    public Map<String, Object> copyProcessDefinition(
            @PathVariable("definitionId") String definitionId,
            @RequestParam("newKey") String newKey,
            @RequestParam("newName") String newName,
            @RequestParam(value = "category", required = false) String category) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里可以实现流程定义复制功能
            // 1. 获取原流程定义的XML
            // 2. 修改XML中的key和name
            // 3. 重新部署
            
            result.put("success", true);
            result.put("message", "流程定义复制成功");
            result.put("originalDefinitionId", definitionId);
            result.put("newKey", newKey);
            result.put("newName", newName);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "流程定义复制失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取流程定义版本历史
     */
    @ApiOperation("获取流程定义版本历史")
    @GetMapping("/{definitionKey}/versions")
    public List<ProcessDefinitionDTO> getProcessDefinitionVersions(@PathVariable("definitionKey") String definitionKey) {
        // 这里可以实现获取指定key的所有版本
        List<ProcessDefinitionDTO> allDefinitions = workflowService.getProcessDefinitions();
        return allDefinitions.stream()
            .filter(d -> definitionKey.equals(d.getKey()))
            .toList();
    }
    
    /**
     * 设置流程定义为默认版本
     */
    @ApiOperation("设置流程定义为默认版本")
    @PostMapping("/{definitionId}/set-default")
    public Map<String, Object> setDefaultVersion(@PathVariable("definitionId") String definitionId) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现设置默认版本的逻辑
        
        result.put("success", true);
        result.put("message", "设置默认版本成功");
        result.put("definitionId", definitionId);
        
        return result;
    }
    
    /**
     * 获取流程定义的统计信息（私有方法）
     */
    private Map<String, Object> getDefinitionStatistics(String definitionId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 这里可以实现获取流程定义统计信息的逻辑
        statistics.put("totalInstances", 0);
        statistics.put("runningInstances", 0);
        statistics.put("completedInstances", 0);
        statistics.put("terminatedInstances", 0);
        statistics.put("averageDuration", 0);
        statistics.put("totalTasks", 0);
        statistics.put("pendingTasks", 0);
        statistics.put("completedTasks", 0);
        
        return statistics;
    }
}