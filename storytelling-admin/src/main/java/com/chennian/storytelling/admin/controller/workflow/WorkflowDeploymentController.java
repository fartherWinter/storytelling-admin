package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.common.enums.WorkflowResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流部署管理控制器
 * 专门处理流程定义的部署、管理和资源获取
 * 
 * @author chennian
 */
@Api(tags = "工作流部署管理")
@RestController
@RequestMapping("/sys/workflow/deployment")
@RequiredArgsConstructor
@Slf4j
public class WorkflowDeploymentController {

    private final WorkflowService workflowService;

    /**
     * 部署流程定义
     * 需要管理员权限
     */
    @ApiOperation("部署流程定义")
    @PostMapping("/deploy")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:deploy')")
    @CacheEvict(value = {"processDefinitions", "workflowStats"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> deployProcess(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name) {
        
        log.info("开始部署流程定义，文件名: {}, 自定义名称: {}", file.getOriginalFilename(), name);
        
        try {
            String deploymentId = workflowService.deployProcess(file, name);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程部署成功");
            result.put("deploymentId", deploymentId);
            result.put("fileName", file.getOriginalFilename());
            
            log.info("流程部署成功，部署ID: {}", deploymentId);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("流程部署失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEPLOY_FAILED);
        }
    }

    /**
     * 获取流程定义列表
     * 支持缓存
     */
    @ApiOperation("获取流程定义列表")
    @GetMapping("/definitions")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    @Cacheable(value = "processDefinitions", key = "#page + '_' + #size + '_' + #category")
    public ServerResponseEntity<Map<String, Object>> getProcessDefinitions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "category", required = false) String category) {
        
        log.debug("获取流程定义列表，页码: {}, 大小: {}, 分类: {}", page, size, category);
        
        Map<String, Object> result = workflowService.getProcessDefinitions(page, size, category);
        return ServerResponseEntity.success(result);
    }

    /**
     * 获取流程定义详情
     * 支持缓存
     */
    @ApiOperation("获取流程定义详情")
    @GetMapping("/definitions/{processDefinitionId}")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    @Cacheable(value = "processDefinition", key = "#processDefinitionId")
    public ServerResponseEntity<Map<String, Object>> getProcessDefinition(
            @PathVariable("processDefinitionId") String processDefinitionId) {
        
        log.debug("获取流程定义详情，ID: {}", processDefinitionId);
        
        Map<String, Object> result = workflowService.getProcessDefinitionDetail(processDefinitionId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 获取流程定义XML
     */
    @ApiOperation("获取流程定义XML")
    @GetMapping("/definitions/{processDefinitionId}/xml")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    public ResponseEntity<String> getProcessDefinitionXml(
            @PathVariable("processDefinitionId") String processDefinitionId) {
        
        log.debug("获取流程定义XML，ID: {}", processDefinitionId);
        
        try {
            String xml = workflowService.getProcessDefinitionXml(processDefinitionId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "process-definition.xml");
            
            return new ResponseEntity<>(xml, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("获取流程定义XML失败: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 获取流程定义图片
     */
    @ApiOperation("获取流程定义图片")
    @GetMapping("/definitions/{processDefinitionId}/image")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    public ResponseEntity<byte[]> getProcessDefinitionImage(
            @PathVariable("processDefinitionId") String processDefinitionId) {
        
        log.debug("获取流程定义图片，ID: {}", processDefinitionId);
        
        try {
            byte[] bytes = workflowService.getProcessDefinitionImage(processDefinitionId);
            if (bytes == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "process-definition.png");
            
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("获取流程定义图片失败: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 删除流程部署
     * 需要管理员权限
     */
    @ApiOperation("删除流程部署")
    @DeleteMapping("/deployments/{deploymentId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:delete')")
    @CacheEvict(value = {"processDefinitions", "processDefinition", "workflowStats"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> deleteDeployment(
            @PathVariable("deploymentId") String deploymentId,
            @RequestParam(value = "cascade", defaultValue = "false") boolean cascade) {
        
        log.info("删除流程部署，部署ID: {}, 级联删除: {}", deploymentId, cascade);
        
        try {
            workflowService.deleteDeployment(deploymentId, cascade);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程部署删除成功");
            result.put("deploymentId", deploymentId);
            
            log.info("流程部署删除成功，部署ID: {}", deploymentId);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("删除流程部署失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DELETE_FAILED);
        }
    }

    /**
     * 挂起流程定义
     * 需要管理员权限
     */
    @ApiOperation("挂起流程定义")
    @PostMapping("/definitions/{processDefinitionId}/suspend")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:manage')")
    @CacheEvict(value = {"processDefinitions", "processDefinition"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> suspendProcessDefinition(
            @PathVariable("processDefinitionId") String processDefinitionId) {
        
        log.info("挂起流程定义，ID: {}", processDefinitionId);
        
        try {
            workflowService.suspendProcessDefinition(processDefinitionId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程定义挂起成功");
            result.put("processDefinitionId", processDefinitionId);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("挂起流程定义失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEFINITION_SUSPEND_FAILED);
        }
    }

    /**
     * 激活流程定义
     * 需要管理员权限
     */
    @ApiOperation("激活流程定义")
    @PostMapping("/definitions/{processDefinitionId}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:manage')")
    @CacheEvict(value = {"processDefinitions", "processDefinition"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> activateProcessDefinition(
            @PathVariable("processDefinitionId") String processDefinitionId) {
        
        log.info("激活流程定义，ID: {}", processDefinitionId);
        
        try {
            workflowService.activateProcessDefinition(processDefinitionId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程定义激活成功");
            result.put("processDefinitionId", processDefinitionId);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("激活流程定义失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEFINITION_ACTIVATE_FAILED);
        }
    }

    /**
     * 获取部署列表
     */
    @ApiOperation("获取部署列表")
    @GetMapping("/deployments")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    public ServerResponseEntity<Map<String, Object>> getDeployments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        log.debug("获取部署列表，页码: {}, 大小: {}", page, size);
        
        Map<String, Object> result = workflowService.getDeployments(page, size);
        return ServerResponseEntity.success(result);
    }
}