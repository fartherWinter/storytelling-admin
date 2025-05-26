package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.service.FormTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单模板服务实现类
 *
 * @author chennian
 */
@Service
public class FormTemplateServiceImpl implements FormTemplateService {

    @Autowired
    private Configuration freemarkerConfig;

    // 模拟模板存储，实际项目中应该使用数据库存储
    private final Map<String, String> templateStore = new HashMap<>();

    @Override
    public String renderForm(String templateName, Map<String, Object> data) {
        try {
            // 尝试从配置的模板路径加载模板
            Template template;
            try {
                template = freemarkerConfig.getTemplate(templateName + ".ftl");
            } catch (IOException e) {
                // 如果模板不存在，尝试从内存中获取
                String templateContent = getTemplateContent(templateName);
                if (templateContent == null) {
                    return "模板不存在: " + templateName;
                }
                template = new Template(templateName, new StringReader(templateContent), freemarkerConfig);
            }

            // 渲染模板
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (IOException | TemplateException e) {
            return "模板渲染失败: " + e.getMessage();
        }
    }

    @Override
    public String generateDocument(String templateName, Map<String, Object> data) {
        // 文档生成与表单渲染类似，但可能需要不同的处理逻辑
        try {
            String templateContent = getTemplateContent(templateName);
            if (templateContent == null) {
                return "文档模板不存在: " + templateName;
            }

            Template template = new Template(templateName, new StringReader(templateContent), freemarkerConfig);
            StringWriter writer = new StringWriter();
            template.process(data, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            return "文档生成失败: " + e.getMessage();
        }
    }

    @Override
    public boolean saveTemplate(String templateName, String templateContent) {
        try {
            // 验证模板是否有效
            new Template(templateName, new StringReader(templateContent), freemarkerConfig);

            // 保存模板
            templateStore.put(templateName, templateContent);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getTemplateContent(String templateName) {
        return templateStore.get(templateName);
    }
}