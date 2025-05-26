package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 表单模板服务接口
 * 
 * @author chennian
 */
public interface FormTemplateService {

    /**
     * 根据模板名称和数据渲染表单
     * 
     * @param templateName 模板名称
     * @param data 表单数据
     * @return 渲染后的表单HTML
     */
    String renderForm(String templateName, Map<String, Object> data);
    
    /**
     * 根据模板名称和数据生成文档
     * 
     * @param templateName 模板名称
     * @param data 文档数据
     * @return 生成的文档内容
     */
    String generateDocument(String templateName, Map<String, Object> data);
    
    /**
     * 保存表单模板
     * 
     * @param templateName 模板名称
     * @param templateContent 模板内容
     * @return 是否保存成功
     */
    boolean saveTemplate(String templateName, String templateContent);
    
    /**
     * 获取表单模板内容
     * 
     * @param templateName 模板名称
     * @return 模板内容
     */
    String getTemplateContent(String templateName);
}