package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.workflow.WfFormTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流表单模板Mapper接口
 * 
 * @author chennian
 */
@Mapper
public interface WfFormTemplateMapper extends BaseMapper<WfFormTemplate> {

    /**
     * 分页查询表单模板（支持高级搜索）
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfFormTemplate> selectTemplatePage(IPage<WfFormTemplate> page, @Param("params") Map<String, Object> params);

    /**
     * 根据表单ID查询表单模板（支持版本）
     * 
     * @param formId 表单ID
     * @param version 版本号（为空时查询最新版本）
     * @return 表单模板
     */
    WfFormTemplate selectByFormId(@Param("formId") String formId, @Param("version") Integer version);

    /**
     * 根据流程键查询表单模板列表（支持分页）
     * 
     * @param page 分页参数
     * @param processKey 流程键
     * @param enabled 是否启用
     * @return 表单模板列表
     */
    IPage<WfFormTemplate> selectByProcessKey(IPage<WfFormTemplate> page,
                                           @Param("processKey") String processKey,
                                           @Param("enabled") Boolean enabled);

    /**
     * 根据分类查询表单模板列表（支持分页和标签）
     * 
     * @param page 分页参数
     * @param category 分类
     * @param tags 标签列表
     * @param enabled 是否启用
     * @return 表单模板列表
     */
    IPage<WfFormTemplate> selectByCategory(IPage<WfFormTemplate> page,
                                         @Param("category") String category,
                                         @Param("tags") List<String> tags,
                                         @Param("enabled") Boolean enabled);

    /**
     * 查询启用的表单模板列表（支持分页和权限）
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 表单模板列表
     */
    IPage<WfFormTemplate> selectEnabledTemplates(IPage<WfFormTemplate> page,
                                               @Param("userId") Long userId,
                                               @Param("roleIds") List<Long> roleIds);

    /**
     * 查询公开的表单模板列表（支持分页和搜索）
     * 
     * @param page 分页参数
     * @param keyword 关键字
     * @param tags 标签列表
     * @return 表单模板列表
     */
    IPage<WfFormTemplate> selectPublicTemplates(IPage<WfFormTemplate> page,
                                              @Param("keyword") String keyword,
                                              @Param("tags") List<String> tags);

    /**
     * 根据创建者查询表单模板列表（支持分页和时间范围）
     * 
     * @param page 分页参数
     * @param creatorId 创建者ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 表单模板列表
     */
    IPage<WfFormTemplate> selectByCreator(IPage<WfFormTemplate> page,
                                        @Param("creatorId") Long creatorId,
                                        @Param("startTime") Date startTime,
                                        @Param("endTime") Date endTime);

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

    /**
     * 创建新版本
     * 
     * @param formId 表单ID
     * @param version 新版本号
     * @param content 表单内容
     * @param remark 版本说明
     * @param creatorId 创建者ID
     * @return 新版本ID
     */
    Long createNewVersion(@Param("formId") String formId,
                         @Param("version") Integer version,
                         @Param("content") String content,
                         @Param("remark") String remark,
                         @Param("creatorId") Long creatorId);

    /**
     * 查询表单模板版本历史
     * 
     * @param page 分页参数
     * @param formId 表单ID
     * @return 版本历史列表
     */
    IPage<WfFormTemplate> selectVersionHistory(IPage<WfFormTemplate> page, @Param("formId") String formId);

    /**
     * 更新表单模板标签
     * 
     * @param id 模板ID
     * @param tags 标签列表
     * @param updaterId 更新者ID
     * @return 更新结果
     */
    int updateTemplateTags(@Param("id") Long id,
                          @Param("tags") List<String> tags,
                          @Param("updaterId") Long updaterId);

    /**
     * 统计各分类的模板数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> countByCategory(@Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);

    /**
     * 统计模板使用情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 统计结果
     */
    List<Map<String, Object>> countTemplateUsage(@Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("limit") Integer limit);

    /**
     * 统计用户创建的模板数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 统计结果
     */
    List<Map<String, Object>> countTemplatesByCreator(@Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime,
                                                    @Param("limit") Integer limit);
}