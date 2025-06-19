package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfFormTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作流表单模板Mapper接口
 * 
 * @author chennian
 */
@Mapper
public interface WfFormTemplateMapper extends BaseMapper<WfFormTemplate> {

    /**
     * 根据表单ID查询表单模板
     * 
     * @param formId 表单ID
     * @return 表单模板
     */
    WfFormTemplate selectByFormId(@Param("formId") String formId);

    /**
     * 根据流程键查询表单模板列表
     * 
     * @param processKey 流程键
     * @return 表单模板列表
     */
    List<WfFormTemplate> selectByProcessKey(@Param("processKey") String processKey);

    /**
     * 根据分类查询表单模板列表
     * 
     * @param category 分类
     * @return 表单模板列表
     */
    List<WfFormTemplate> selectByCategory(@Param("category") String category);

    /**
     * 查询启用的表单模板列表
     * 
     * @return 表单模板列表
     */
    List<WfFormTemplate> selectEnabledTemplates();

    /**
     * 查询公开的表单模板列表
     * 
     * @return 表单模板列表
     */
    List<WfFormTemplate> selectPublicTemplates();

    /**
     * 根据创建者查询表单模板列表
     * 
     * @param creatorId 创建者ID
     * @return 表单模板列表
     */
    List<WfFormTemplate> selectByCreator(@Param("creatorId") Long creatorId);

    /**
     * 检查表单ID是否存在
     * 
     * @param formId 表单ID
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 存在数量
     */
    int checkFormIdExists(@Param("formId") String formId, @Param("excludeId") Long excludeId);

    /**
     * 获取表单模板的最大版本号
     * 
     * @param formId 表单ID
     * @return 最大版本号
     */
    Integer getMaxVersion(@Param("formId") String formId);

    /**
     * 批量更新状态
     * 
     * @param ids 表单模板ID列表
     * @param enabled 启用状态
     * @param updaterId 更新者ID
     * @param updaterName 更新者姓名
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("enabled") Integer enabled,
                         @Param("updaterId") Long updaterId,
                         @Param("updaterName") String updaterName);

    /**
     * 复制表单模板
     * 
     * @param sourceId 源表单模板ID
     * @param newFormId 新表单ID
     * @param newFormName 新表单名称
     * @param creatorId 创建者ID
     * @param creatorName 创建者姓名
     * @return 新表单模板ID
     */
    Long copyFormTemplate(@Param("sourceId") Long sourceId,
                         @Param("newFormId") String newFormId,
                         @Param("newFormName") String newFormName,
                         @Param("creatorId") Long creatorId,
                         @Param("creatorName") String creatorName);
}