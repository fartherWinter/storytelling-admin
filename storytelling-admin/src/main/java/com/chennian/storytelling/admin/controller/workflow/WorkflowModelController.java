package com.chennian.storytelling.admin.controller.workflow;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.enums.WorkflowResponseEnum;
import com.chennian.storytelling.bean.dto.WorkflowModelDTO;
import com.chennian.storytelling.service.WorkflowModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 工作流模型控制器
 * 
 * @author chennian
 */
@RestController
@RequestMapping("/sys/workflow/model")
@Tag(name = "工作流模型管理", description = "工作流模型的创建、修改、查询和部署等操作")
@Validated
public class WorkflowModelController {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowModelController.class);
    
    // 缓存过期时间（分钟）
    private static final int CACHE_EXPIRE_TIME = 30;
    
    // 缓存前缀
    private static final String CACHE_PREFIX = "workflow:model:";
    private static final String CACHE_MODEL_PREFIX = CACHE_PREFIX + "detail:";
    private static final String CACHE_MODEL_XML_PREFIX = CACHE_PREFIX + "xml:";
    private static final String CACHE_MODEL_LIST = CACHE_PREFIX + "list";
    
    @Autowired
    private WorkflowModelService workflowModelService;
    
    @Autowired
    private RedisCache redisCache;
    
    /**
     * 创建工作流模型
     */
    @PostMapping
    @Operation(summary = "创建工作流模型", description = "创建一个新的工作流模型")
    public ServerResponseEntity<String> createModel(@Valid @RequestBody WorkflowModelDTO model) {
        logger.info("创建工作流模型: {}", model.getName());
        try {
            String modelId = workflowModelService.createModel(model);
            logger.info("工作流模型创建成功, ID: {}", modelId);
            
            // 清除模型列表缓存，因为新增了模型
            redisCache.deleteObject(CACHE_MODEL_LIST);
            logger.info("已清除工作流模型列表缓存");
            
            return ServerResponseEntity.success(modelId);
        } catch (Exception e) {
            logger.error("创建工作流模型失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_CREATE_FAILED);
        }
    }
    
    /**
     * 更新工作流模型
     */
    @PutMapping("/{modelId}")
    @Operation(summary = "更新工作流模型", description = "根据模型ID更新工作流模型信息")
    public ServerResponseEntity<Boolean> updateModel(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId, 
            @Valid @RequestBody WorkflowModelDTO model) {
        logger.info("更新工作流模型, ID: {}", modelId);
        model.setId(modelId);
        try {
            boolean result = workflowModelService.updateModel(model);
            logger.info("工作流模型更新结果: {}", result);
            
            if (result) {
                // 清除相关缓存
                String modelKey = CACHE_MODEL_PREFIX + modelId;
                String xmlKey = CACHE_MODEL_XML_PREFIX + modelId;
                redisCache.deleteObject(modelKey);
                redisCache.deleteObject(xmlKey);
                redisCache.deleteObject(CACHE_MODEL_LIST);
                logger.info("已清除工作流模型缓存, 模型ID: {}", modelId);
            }
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            logger.error("更新工作流模型失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_UPDATE_FAILED);
        }
    }
    
    /**
     * 获取工作流模型
     */
    @GetMapping("/{modelId}")
    @Operation(summary = "获取工作流模型", description = "根据模型ID获取工作流模型详情")
    public ServerResponseEntity<WorkflowModelDTO> getModel(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("获取工作流模型, ID: {}", modelId);
        try {
            // 缓存键
            String cacheKey = CACHE_MODEL_PREFIX + modelId;
            
            // 从缓存中获取数据
            WorkflowModelDTO model = redisCache.getCacheObject(cacheKey);
            if (model != null) {
                logger.info("从缓存中获取工作流模型, ID: {}", modelId);
                return ServerResponseEntity.success(model);
            }
            
            // 缓存中不存在，从数据库获取
            logger.info("缓存中不存在工作流模型，从数据库获取, ID: {}", modelId);
            model = workflowModelService.getModel(modelId);
            if (model == null) {
                logger.warn("未找到工作流模型, ID: {}", modelId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_NOT_FOUND);
            }
            
            // 将数据存入缓存
            redisCache.setCacheObject(cacheKey, model, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            logger.info("工作流模型已存入缓存, ID: {}", modelId);
            
            return ServerResponseEntity.success(model);
        } catch (Exception e) {
            logger.error("获取工作流模型失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 查询工作流模型列表
     */
    @GetMapping
    @Operation(summary = "查询工作流模型列表", description = "根据条件查询工作流模型列表")
    public ServerResponseEntity<List<WorkflowModelDTO>> listModels(
            @Parameter(description = "模型分类") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "模型键") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "模型名称") @RequestParam(value = "name", required = false) String name) {
        logger.info("查询工作流模型列表, 分类: {}, 键: {}, 名称: {}", category, key, name);
        try {
            // 如果有查询条件，则不使用缓存
            if (category != null || key != null || name != null) {
                logger.info("有查询条件，不使用缓存");
                List<WorkflowModelDTO> models = workflowModelService.listModels(category, key, name);
                return ServerResponseEntity.success(models);
            }
            
            // 从缓存中获取数据
            List<WorkflowModelDTO> models = redisCache.getCacheObject(CACHE_MODEL_LIST);
            if (models != null) {
                logger.info("从缓存中获取工作流模型列表");
                return ServerResponseEntity.success(models);
            }
            
            // 缓存中不存在，从数据库获取
            logger.info("缓存中不存在工作流模型列表，从数据库获取");
            models = workflowModelService.listModels(category, key, name);
            
            // 将数据存入缓存
            redisCache.setCacheObject(CACHE_MODEL_LIST, models, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            logger.info("工作流模型列表已存入缓存");
            
            return ServerResponseEntity.success(models);
        } catch (Exception e) {
            logger.error("查询工作流模型列表失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 分页查询工作流模型列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询工作流模型列表", description = "根据条件分页查询工作流模型列表")
    public ServerResponseEntity<IPage<WorkflowModelDTO>> pageModels(
            @Parameter(description = "模型分类") @RequestParam(value = "category", required = false) String category,
            @Parameter(description = "模型键") @RequestParam(value = "key", required = false) String key,
            @Parameter(description = "模型名称") @RequestParam(value = "name", required = false) String name,
            PageParam<WorkflowModelDTO> page) {
        logger.info("分页查询工作流模型列表, 分类: {}, 键: {}, 名称: {}, 页码: {}, 每页数量: {}", 
                category, key, name, page.getCurrent(), page.getSize());
        try {
            // 构建缓存键，包含分页参数
            String cacheKey = CACHE_MODEL_LIST + ":page:" + page.getCurrent() + ":" + page.getSize();
            if (category != null) cacheKey += ":category:" + category;
            if (key != null) cacheKey += ":key:" + key;
            if (name != null) cacheKey += ":name:" + name;
            
            // 从缓存中获取数据
            IPage<WorkflowModelDTO> pageResult = redisCache.getCacheObject(cacheKey);
            if (pageResult != null) {
                logger.info("从缓存中获取工作流模型分页列表");
                return ServerResponseEntity.success(pageResult);
            }
            
            // 缓存中不存在，从数据库获取
            logger.info("缓存中不存在工作流模型分页列表，从数据库获取");
            pageResult = workflowModelService.pageModels(page, category, key, name);
            
            // 将数据存入缓存
            redisCache.setCacheObject(cacheKey, pageResult, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            logger.info("工作流模型分页列表已存入缓存");
            
            return ServerResponseEntity.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询工作流模型列表失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 删除工作流模型
     */
    @DeleteMapping("/{modelId}")
    @Operation(summary = "删除工作流模型", description = "根据模型ID删除工作流模型")
    public ServerResponseEntity<Boolean> deleteModel(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("删除工作流模型, ID: {}", modelId);
        try {
            boolean result = workflowModelService.deleteModel(modelId);
            logger.info("工作流模型删除结果: {}", result);
            
            if (result) {
                // 清除相关缓存
                String modelKey = CACHE_MODEL_PREFIX + modelId;
                String xmlKey = CACHE_MODEL_XML_PREFIX + modelId;
                redisCache.deleteObject(modelKey);
                redisCache.deleteObject(xmlKey);
                redisCache.deleteObject(CACHE_MODEL_LIST);
                logger.info("已清除工作流模型缓存, 模型ID: {}", modelId);
            }
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            logger.error("删除工作流模型失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_DELETE_FAILED);
        }
    }
    
    /**
     * 部署工作流模型
     */
    @PostMapping("/{modelId}/deploy")
    @Operation(summary = "部署工作流模型", description = "将工作流模型部署为可执行流程")
    public ServerResponseEntity<String> deployModel(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("部署工作流模型, ID: {}", modelId);
        try {
            String deploymentId = workflowModelService.deployModel(modelId);
            logger.info("工作流模型部署成功, 部署ID: {}", deploymentId);
            
            // 部署后模型状态可能会变化，清除相关缓存
            String modelKey = CACHE_MODEL_PREFIX + modelId;
            redisCache.deleteObject(modelKey);
            redisCache.deleteObject(CACHE_MODEL_LIST);
            logger.info("已清除工作流模型缓存, 模型ID: {}", modelId);
            
            return ServerResponseEntity.success(deploymentId);
        } catch (Exception e) {
            logger.error("部署工作流模型失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_DEPLOY_FAILED);
        }
    }
    
    /**
     * 获取模型的BPMN XML
     */
    @GetMapping("/{modelId}/xml")
    @Operation(summary = "获取模型的BPMN XML", description = "获取工作流模型的BPMN XML定义")
    public ServerResponseEntity<String> getModelXml(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("获取工作流模型XML, ID: {}", modelId);
        try {
            // 缓存键
            String cacheKey = CACHE_MODEL_XML_PREFIX + modelId;
            
            // 从缓存中获取数据
            String xml = redisCache.getCacheObject(cacheKey);
            if (xml != null) {
                logger.info("从缓存中获取工作流模型XML, ID: {}", modelId);
                return ServerResponseEntity.success(xml);
            }
            
            // 缓存中不存在，从数据库获取
            logger.info("缓存中不存在工作流模型XML，从数据库获取, ID: {}", modelId);
            xml = workflowModelService.getModelXml(modelId);
            if (xml == null) {
                logger.warn("未找到工作流模型XML, ID: {}", modelId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_XML_GET_FAILED);
            }
            
            // 将数据存入缓存
            redisCache.setCacheObject(cacheKey, xml, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            logger.info("工作流模型XML已存入缓存, ID: {}", modelId);
            
            return ServerResponseEntity.success(xml);
        } catch (Exception e) {
            logger.error("获取工作流模型XML失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_XML_GET_FAILED);
        }
    }
    
    /**
     * 保存模型的BPMN XML
     */
    @PostMapping("/{modelId}/xml")
    @Operation(summary = "保存模型的BPMN XML", description = "保存工作流模型的BPMN XML定义")
    public ServerResponseEntity<Boolean> saveModelXml(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId, 
            @RequestBody String xml) {
        logger.info("保存工作流模型XML, ID: {}", modelId);
        try {
            boolean result = workflowModelService.saveModelXml(modelId, xml);
            logger.info("工作流模型XML保存结果: {}", result);
            
            if (result) {
                // 清除相关缓存
                String modelKey = CACHE_MODEL_PREFIX + modelId;
                String xmlKey = CACHE_MODEL_XML_PREFIX + modelId;
                redisCache.deleteObject(modelKey);
                redisCache.deleteObject(xmlKey);
                logger.info("已清除工作流模型XML缓存, 模型ID: {}", modelId);
                
                // 更新缓存
                redisCache.setCacheObject(xmlKey, xml, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
                logger.info("已更新工作流模型XML缓存, 模型ID: {}", modelId);
            }
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            logger.error("保存工作流模型XML失败", e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_MODEL_XML_SAVE_FAILED);
        }
    }
    
    /**
     * 导出模型为BPMN XML文件
     */
    @GetMapping("/{modelId}/export-xml")
    @Operation(summary = "导出模型为BPMN XML文件", description = "将工作流模型导出为BPMN XML文件")
    public ResponseEntity<byte[]> exportModelXml(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("导出工作流模型XML, ID: {}", modelId);
        try {
            // 缓存键
            String cacheKey = CACHE_PREFIX + "export-xml:" + modelId;
            
            // 从缓存中获取数据
            byte[] bytes = redisCache.getCacheObject(cacheKey);
            if (bytes != null) {
                logger.info("从缓存中获取工作流模型导出XML数据, ID: {}", modelId);
            } else {
                // 缓存中不存在，从数据库获取
                logger.info("缓存中不存在工作流模型导出XML数据，从数据库获取, ID: {}", modelId);
                bytes = workflowModelService.exportModelXml(modelId);
                
                // 将数据存入缓存
                if (bytes != null) {
                    redisCache.setCacheObject(cacheKey, bytes, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
                    logger.info("工作流模型导出XML数据已存入缓存, ID: {}", modelId);
                }
            }
            
            if (bytes == null) {
                logger.warn("未找到工作流模型XML数据, ID: {}", modelId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "model.bpmn20.xml");
            
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出工作流模型XML失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 导出模型为PNG图片
     */
    @GetMapping("/{modelId}/export-png")
    @Operation(summary = "导出模型为PNG图片", description = "将工作流模型导出为PNG图片")
    public ResponseEntity<byte[]> exportModelPng(
            @Parameter(description = "模型ID", required = true) @PathVariable("modelId") @NotBlank(message = "模型ID不能为空") String modelId) {
        logger.info("导出工作流模型PNG, ID: {}", modelId);
        try {
            // 缓存键
            String cacheKey = CACHE_PREFIX + "export-png:" + modelId;
            
            // 从缓存中获取数据
            byte[] bytes = redisCache.getCacheObject(cacheKey);
            if (bytes != null) {
                logger.info("从缓存中获取工作流模型导出PNG数据, ID: {}", modelId);
            } else {
                // 缓存中不存在，从数据库获取
                logger.info("缓存中不存在工作流模型导出PNG数据，从数据库获取, ID: {}", modelId);
                bytes = workflowModelService.exportModelPng(modelId);
                
                // 将数据存入缓存
                if (bytes != null) {
                    redisCache.setCacheObject(cacheKey, bytes, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
                    logger.info("工作流模型导出PNG数据已存入缓存, ID: {}", modelId);
                }
            }
            
            if (bytes == null) {
                logger.warn("未找到工作流模型PNG数据, ID: {}", modelId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "model.png");
            
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("导出工作流模型PNG失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}