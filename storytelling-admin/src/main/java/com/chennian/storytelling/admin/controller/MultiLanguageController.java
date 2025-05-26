package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/12/15
 */
@RestController
@RequestMapping("/erp/i18n")
@Tag(name = "多语言支持")
public class MultiLanguageController {

    /**
     * 获取支持的语言列表
     */
    @GetMapping("/languages")
    @Operation(summary = "获取支持的语言列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取支持的语言列表")
    public ServerResponseEntity<List<Map<String, String>>> getSupportedLanguages() {
        // 这里应该从配置或数据库中获取支持的语言列表
        List<Map<String, String>> languages = List.of(
            Map.of("code", "zh_CN", "name", "简体中文"),
            Map.of("code", "en_US", "name", "English"),
            Map.of("code", "ja_JP", "name", "日本語")
        );
        return ServerResponseEntity.success(languages);
    }

    /**
     * 获取指定语言的翻译资源
     */
    @GetMapping("/resources/{lang}")
    @Operation(summary = "获取语言资源")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取语言资源")
    public ServerResponseEntity<Map<String, Object>> getLanguageResources(@PathVariable("lang") String lang) {
        // 这里应该从配置或数据库中获取指定语言的翻译资源
        Map<String, Object> resources = new HashMap<>();
        
        // 根据语言代码返回不同的翻译资源
        if ("zh_CN".equals(lang)) {
            resources.put("common", Map.of(
                "save", "保存",
                "cancel", "取消",
                "confirm", "确认",
                "delete", "删除",
                "edit", "编辑"
            ));
            resources.put("customer", Map.of(
                "title", "客户管理",
                "add", "新增客户",
                "info", "客户详情",
                "list", "客户列表"
            ));
            // 其他模块的翻译...
        } else if ("en_US".equals(lang)) {
            resources.put("common", Map.of(
                "save", "Save",
                "cancel", "Cancel",
                "confirm", "Confirm",
                "delete", "Delete",
                "edit", "Edit"
            ));
            resources.put("customer", Map.of(
                "title", "Customer Management",
                "add", "Add Customer",
                "info", "Customer Details",
                "list", "Customer List"
            ));
            // 其他模块的翻译...
        } else if ("ja_JP".equals(lang)) {
            resources.put("common", Map.of(
                "save", "保存",
                "cancel", "キャンセル",
                "confirm", "確認",
                "delete", "削除",
                "edit", "編集"
            ));
            resources.put("customer", Map.of(
                "title", "顧客管理",
                "add", "顧客を追加",
                "info", "顧客詳細",
                "list", "顧客リスト"
            ));
            // 其他模块的翻译...
        }
        
        return ServerResponseEntity.success(resources);
    }

    /**
     * 获取用户语言偏好设置
     */
    @GetMapping("/preference")
    @Operation(summary = "获取用户语言偏好")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取用户语言偏好")
    public ServerResponseEntity<Map<String, String>> getUserLanguagePreference() {
        // 这里应该从用户配置中获取语言偏好
        Map<String, String> preference = Map.of(
            "language", "zh_CN",
            "dateFormat", "yyyy-MM-dd",
            "timeFormat", "HH:mm:ss"
        );
        return ServerResponseEntity.success(preference);
    }

    /**
     * 设置用户语言偏好
     */
    @PostMapping("/preference")
    @Operation(summary = "设置用户语言偏好")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "设置用户语言偏好")
    public ServerResponseEntity<Boolean> setUserLanguagePreference(@RequestBody Map<String, String> preference) {
        // 这里应该保存用户的语言偏好设置
        // 实际实现中需要保存到用户配置或数据库中
        return ServerResponseEntity.success(true);
    }

    /**
     * 获取系统默认语言设置
     */
    @GetMapping("/default")
    @Operation(summary = "获取系统默认语言")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取系统默认语言")
    public ServerResponseEntity<Map<String, String>> getSystemDefaultLanguage() {
        Map<String, String> defaultSettings = Map.of(
            "language", "zh_CN",
            "dateFormat", "yyyy-MM-dd",
            "timeFormat", "HH:mm:ss"
        );
        return ServerResponseEntity.success(defaultSettings);
    }
}