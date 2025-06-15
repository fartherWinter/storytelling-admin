package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfNotificationTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知模板Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfNotificationTemplateMapper extends BaseMapper<WfNotificationTemplate> {

    /**
     * 根据模板编码查询模板
     * 
     * @param templateCode 模板编码
     * @return 通知模板
     */
    WfNotificationTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 根据模板类型查询模板列表
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<WfNotificationTemplate> selectByTemplateType(@Param("templateType") String templateType);

    /**
     * 根据事件类型查询模板列表
     * 
     * @param eventType 事件类型
     * @return 模板列表
     */
    List<WfNotificationTemplate> selectByEventType(@Param("eventType") String eventType);

    /**
     * 根据启用状态查询模板列表
     * 
     * @param enabled 是否启用
     * @return 模板列表
     */
    List<WfNotificationTemplate> selectByEnabled(@Param("enabled") Integer enabled);

    /**
     * 根据模板类型和事件类型查询模板
     * 
     * @param templateType 模板类型
     * @param eventType 事件类型
     * @return 通知模板
     */
    WfNotificationTemplate selectByTemplateTypeAndEventType(@Param("templateType") String templateType, 
                                                            @Param("eventType") String eventType);

    /**
     * 根据模板名称模糊查询
     * 
     * @param templateName 模板名称
     * @return 模板列表
     */
    List<WfNotificationTemplate> selectByTemplateNameLike(@Param("templateName") String templateName);

    /**
     * 检查模板编码是否存在
     * 
     * @param templateCode 模板编码
     * @param excludeId 排除的模板ID
     * @return 存在数量
     */
    int checkTemplateCodeExists(@Param("templateCode") String templateCode, 
                               @Param("excludeId") String excludeId);

    /**
     * 检查模板名称是否存在
     * 
     * @param templateName 模板名称
     * @param excludeId 排除的模板ID
     * @return 存在数量
     */
    int checkTemplateNameExists(@Param("templateName") String templateName, 
                               @Param("excludeId") String excludeId);

    /**
     * 批量更新模板状态
     * 
     * @param templateIds 模板ID列表
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateTemplateStatus(@Param("templateIds") List<String> templateIds, 
                                 @Param("enabled") Integer enabled, 
                                 @Param("updatedBy") String updatedBy);

    /**
     * 查询所有模板类型
     * 
     * @return 模板类型列表
     */
    List<String> selectAllTemplateTypes();

    /**
     * 查询所有事件类型
     * 
     * @return 事件类型列表
     */
    List<String> selectAllEventTypes();

    /**
     * 查询模板统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectTemplateStatistics();

    /**
     * 根据模板类型统计模板数量
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectTemplateCountByType();
}