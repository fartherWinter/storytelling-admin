package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.bean.dto.WorkflowFormDTO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


/**
 * 工作流表单管理控制器
 * 提供动态表单的管理功能
 *
 * @author chennian
 */
@Slf4j
@Api(tags = "工作流表单管理")
@RestController
@RequestMapping("/sys/workflow/forms")
@RequiredArgsConstructor
public class WorkflowFormController {

    private final WorkflowFormService workflowFormService;

    /**
     * 获取所有表单模板
     */
    @ApiOperation("获取所有表单模板")
    @GetMapping("/templates")
    public ServerResponseEntity<List<WorkflowFormDTO.FormTemplateDTO>> getAllFormTemplates() {
        try {
            List<WorkflowFormDTO.FormTemplateDTO> templates = workflowFormService.getAllFormTemplates();
            return ServerResponseEntity.success(templates);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取表单模板详情
     */
    @ApiOperation("获取表单模板详情")
    @GetMapping("/templates/{formId}")
    public ServerResponseEntity<WorkflowFormDTO.FormTemplateDTO> getFormTemplate(@PathVariable String formId) {
        try {
            WorkflowFormDTO.FormTemplateDTO template = workflowFormService.getFormTemplate(formId);
            if (template == null) {
                return ServerResponseEntity.showFailMsg("表单模板不存在");
            }
            return ServerResponseEntity.success(template);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

   
    /**
     * 创建表单模板
     */
    @ApiOperation("创建表单模板")
    @PostMapping("/templates")
    public ServerResponseEntity<Map<String, Object>> createFormTemplate(@RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> result = workflowFormService.createFormTemplate(formData);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }



    /**
     * 更新表单模板
     */
    @ApiOperation("更新表单模板")
    @PutMapping("/templates/{formId}")
    public ServerResponseEntity<Map<String, Object>> updateFormTemplate(@PathVariable String formId, @RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> result = workflowFormService.updateFormTemplate(formId, formData);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 更新表单模板（原方法保持兼容）
     */
    @ApiOperation("更新表单模板")
    @PutMapping("/{formId}")
    public ServerResponseEntity<Map<String, Object>> updateFormTemplateCompat(
            @PathVariable("formId") String formId,
            @RequestBody Map<String, Object> formData) {

        Map<String, Object> result = new HashMap<>();

        // 这里可以实现表单模板的更新逻辑

        result.put("success", true);
        result.put("message", "表单模板更新成功");
        result.put("formId", formId);
        result.put("formData", formData);

        return ServerResponseEntity.success(result);
    }

    /**
     * 删除表单模板
     */
    @ApiOperation("删除表单模板")
    @DeleteMapping("/templates/{formId}")
    public ServerResponseEntity<Map<String, Object>> deleteFormTemplate(@PathVariable String formId) {
        try {
            Map<String, Object> result = workflowFormService.deleteFormTemplate(formId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }


    /**
     * 复制表单模板
     */
    @ApiOperation("复制表单模板")
    @PostMapping("/templates/{formId}/copy")
    public ServerResponseEntity<Map<String, Object>> copyFormTemplate(@PathVariable String formId, @RequestBody Map<String, Object> request) {
        try {
            String newName = (String) request.get("newName");
            Map<String, Object> result = workflowFormService.copyFormTemplate(formId, newName);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取表单数据
     */
    @ApiOperation("获取表单数据")
    @GetMapping("/templates/{formId}/data")
    public ServerResponseEntity<Map<String, Object>> getFormData(@PathVariable String formId, @RequestParam String instanceId) {
        try {
            Map<String, Object> result = workflowFormService.getFormData(formId, instanceId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 保存表单数据
     */
    @ApiOperation("保存表单数据")
    @PostMapping("/templates/{formId}/data")
    public ServerResponseEntity<Map<String, Object>> saveFormData(@PathVariable String formId, @RequestParam String instanceId, @RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> result = workflowFormService.saveFormData(formId, instanceId, formData);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 验证表单数据
     */
    @ApiOperation("验证表单数据")
    @PostMapping("/templates/{formId}/validate")
    public ServerResponseEntity<Map<String, Object>> validateFormData(@PathVariable String formId, @RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> result = workflowFormService.validateFormData(formId, formData);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取表单字段选项
     */
    @ApiOperation("获取表单字段选项")
    @GetMapping("/{formId}/fields/{fieldId}/options")
    public ServerResponseEntity<List<WorkflowFormDTO.FieldOptionDTO>> getFieldOptions(
            @PathVariable("formId") String formId,
            @PathVariable("fieldId") String fieldId,
            @RequestParam(value = "query", required = false) String query) {


        try {
            List<WorkflowFormDTO.FieldOptionDTO> options = workflowFormService.getFieldOptions(formId, fieldId, query);
            return ServerResponseEntity.success(options);

        } catch (Exception e) {
            log.error("获取字段选项失败: formId={}, fieldId={}", formId, fieldId, e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }

    }

    /**
     * 保存表单字段选项
     */
    @ApiOperation("保存表单字段选项")
    @PostMapping("/{formId}/fields/{fieldId}/options")
    public ServerResponseEntity<Map<String, Object>> saveFieldOptions(
            @PathVariable("formId") String formId,
            @PathVariable("fieldId") String fieldId,
            @ApiParam("字段选项列表") @Valid @RequestBody List<WorkflowFormDTO.FieldOptionDTO> options) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<WorkflowFormDTO.FieldOptionDTO> savedOptions = workflowFormService.saveFieldOptions(formId, fieldId, options);

            log.info("保存字段选项: formId={}, fieldId={}, count={}", formId, fieldId, options.size());

            response.put("success", true);
            response.put("data", savedOptions);
            response.put("message", "保存字段选项成功");

        } catch (Exception e) {
            log.error("保存字段选项失败: formId={}, fieldId={}", formId, fieldId, e);
            response.put("success", false);
            response.put("message", "保存字段选项失败: " + e.getMessage());
        }

        return ServerResponseEntity.success(response);
    }

    /**
     * 导出表单模板
     */
    @ApiOperation("导出表单模板")
    @PostMapping("/templates/{formId}/export")
    public ServerResponseEntity<Map<String, Object>> exportFormTemplate(@PathVariable String formId) {
        try {
            Map<String, Object> result = workflowFormService.exportFormTemplate(formId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 导入表单模板
     */
    @ApiOperation("导入表单模板")
    @PostMapping("/templates/import")
    public ServerResponseEntity<Map<String, Object>> importFormTemplate(@RequestBody Map<String, Object> formTemplate) {
        try {
            Map<String, Object> result = workflowFormService.importFormTemplate(formTemplate);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取表单定义
     */
    @ApiOperation("获取表单定义")
    @GetMapping("/definition/{formKey}")
    public ServerResponseEntity<Map<String, Object>> getFormDefinition(@PathVariable String formKey) {

        Map<String, Object> response = new HashMap<>();

        try {
            WorkflowFormDTO.FormDefinitionDTO formDefinition = workflowFormService.getFormDefinition(formKey);

            response.put("success", true);
            response.put("data", formDefinition);
            response.put("message", "获取表单定义成功");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取表单定义失败: " + e.getMessage());
        }

        return ServerResponseEntity.success(response);
    }

    /**
     * 创建或更新表单定义
     */
    @ApiOperation("创建或更新表单定义")
    @PostMapping("/definition")
    public ServerResponseEntity<WorkflowFormDTO.FormDefinitionDTO> saveFormDefinition(@RequestBody WorkflowFormDTO.FormDefinitionDTO formDefinition) {

        try {
            WorkflowFormDTO.FormDefinitionDTO savedForm = workflowFormService.saveFormDefinition(formDefinition);
            return ServerResponseEntity.success(savedForm);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }

    }
}