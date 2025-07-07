package com.chennian.storytelling.admin.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

/**
 * 工作流缓存配置
 * 统一管理工作流模块的缓存策略
 * 
 * @author chennian
 */
@Configuration
@EnableCaching
public class WorkflowCacheConfig {

    /**
     * 工作流专用缓存管理器
     * 配置所有工作流相关的缓存区域
     */
    @Bean("workflowCacheManager")
    @Primary
    public CacheManager workflowCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // 配置所有工作流缓存区域
        cacheManager.setCacheNames(Arrays.asList(
            // 流程定义相关缓存
            "processDefinitions",      // 流程定义列表
            "processDefinition",       // 单个流程定义详情
            "processDefinitionXml",    // 流程定义XML
            "processDefinitionImage",  // 流程定义图片
            
            // 流程实例相关缓存
            "processInstances",        // 流程实例列表
            "processInstanceDetail",   // 流程实例详情
            "processInstanceHistory",  // 流程实例历史
            
            // 任务相关缓存
            "userTasks",              // 用户任务列表
            "taskDetail",             // 任务详情
            "taskHistory",            // 任务历史
            "candidateTasks",         // 候选任务
            
            // 表单相关缓存
            "formDefinitions",        // 表单定义列表
            "formDefinition",         // 单个表单定义
            "formTemplates",          // 表单模板
            "formData",               // 表单数据
            
            // 统计相关缓存
            "workflowStats",          // 工作流统计数据
            "processStats",           // 流程统计数据
            "taskStats",              // 任务统计数据
            "userStats",              // 用户统计数据
            
            // 配置相关缓存
            "workflowConfig",         // 工作流配置
            "processConfig",          // 流程配置
            "taskConfig",             // 任务配置
            
            // 权限相关缓存
            "userPermissions",        // 用户权限
            "rolePermissions",        // 角色权限
            "processPermissions"      // 流程权限
        ));
        
        // 允许运行时创建新的缓存区域
        cacheManager.setAllowNullValues(false);
        
        return cacheManager;
    }

    /**
     * 缓存键生成器
     * 为工作流缓存生成统一的键格式
     */
    @Bean("workflowKeyGenerator")
    public org.springframework.cache.interceptor.KeyGenerator workflowKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName()).append(".");
            sb.append(method.getName()).append("(");
            
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                if (params[i] != null) {
                    sb.append(params[i].toString());
                } else {
                    sb.append("null");
                }
            }
            
            sb.append(")");
            return sb.toString();
        };
    }

    /**
     * 缓存解析器
     * 根据不同的操作类型选择合适的缓存策略
     */
    @Bean("workflowCacheResolver")
    public org.springframework.cache.interceptor.CacheResolver workflowCacheResolver() {
        return new org.springframework.cache.interceptor.SimpleCacheResolver(workflowCacheManager());
    }
}