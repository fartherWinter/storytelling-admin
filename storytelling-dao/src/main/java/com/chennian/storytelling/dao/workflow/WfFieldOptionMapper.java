package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.workflow.WfFieldOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流表单字段选项Mapper接口
 * 
 * @author chennian
 */
@Mapper
public interface WfFieldOptionMapper extends BaseMapper<WfFieldOption> {

    /**
     * 分页查询字段选项（支持高级搜索）
     *
     * @param page 分页参数
     * @param params 查询参数
     * @return 分页数据
     */
    IPage<WfFieldOption> selectOptionPage(IPage<WfFieldOption> page, @Param("params") Map<String, Object> params);

    /**
     * 根据表单ID和字段ID查询字段选项列表（支持排序和缓存）
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param orderBy 排序字段
     * @param orderType 排序类型（ASC/DESC）
     * @return 字段选项列表
     */
    List<WfFieldOption> selectByFormIdAndFieldId(@Param("formId") String formId,
                                                @Param("fieldId") String fieldId,
                                                @Param("orderBy") String orderBy,
                                                @Param("orderType") String orderType);

    /**
     * 根据表单ID查询所有字段选项（支持分页）
     * 
     * @param page 分页参数
     * @param formId 表单ID
     * @param enabled 是否启用
     * @return 字段选项列表
     */
    IPage<WfFieldOption> selectByFormId(IPage<WfFieldOption> page,
                                      @Param("formId") String formId,
                                      @Param("enabled") Boolean enabled);

    /**
     * 根据字段ID查询字段选项列表（支持分页和排序）
     * 
     * @param page 分页参数
     * @param fieldId 字段ID
     * @param orderBy 排序字段
     * @return 字段选项列表
     */
    IPage<WfFieldOption> selectByFieldId(IPage<WfFieldOption> page,
                                       @Param("fieldId") String fieldId,
                                       @Param("orderBy") String orderBy);

    /**
     * 根据表单ID、字段ID和查询条件搜索字段选项（支持分页和高级搜索）
     * 
     * @param page 分页参数
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param params 查询参数
     * @return 字段选项列表
     */
    IPage<WfFieldOption> searchOptions(IPage<WfFieldOption> page,
                                     @Param("formId") String formId,
                                     @Param("fieldId") String fieldId,
                                     @Param("params") Map<String, Object> params);

    /**
     * 根据选项分组查询字段选项（支持分页和排序）
     * 
     * @param page 分页参数
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param optionGroup 选项分组
     * @param orderBy 排序字段
     * @return 字段选项列表
     */
    IPage<WfFieldOption> selectByGroup(IPage<WfFieldOption> page,
                                     @Param("formId") String formId,
                                     @Param("fieldId") String fieldId,
                                     @Param("optionGroup") String optionGroup,
                                     @Param("orderBy") String orderBy);

    /**
     * 根据表单ID和字段ID查询启用的字段选项（支持缓存）
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param useCache 是否使用缓存
     * @return 字段选项列表
     */
    List<WfFieldOption> selectEnabledOptions(@Param("formId") String formId,
                                           @Param("fieldId") String fieldId,
                                           @Param("useCache") Boolean useCache);

    /**
     * 根据表单ID和字段ID查询默认选中的选项（支持缓存）
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param useCache 是否使用缓存
     * @return 字段选项列表
     */
    List<WfFieldOption> selectDefaultOptions(@Param("formId") String formId,
                                           @Param("fieldId") String fieldId,
                                           @Param("useCache") Boolean useCache);

    /**
     * 批量插入字段选项
     * 
     * @param options 字段选项列表
     * @return 插入行数
     */
    int batchInsert(@Param("options") List<WfFieldOption> options);

    /**
     * 批量更新字段选项
     * 
     * @param options 字段选项列表
     * @return 更新行数
     */
    int batchUpdate(@Param("options") List<WfFieldOption> options);

    /**
     * 根据表单ID和字段ID删除字段选项
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @return 删除行数
     */
    int deleteByFormIdAndFieldId(@Param("formId") String formId, @Param("fieldId") String fieldId);

    /**
     * 根据表单ID删除所有字段选项
     * 
     * @param formId 表单ID
     * @return 删除行数
     */
    int deleteByFormId(@Param("formId") String formId);

    /**
     * 批量更新字段选项状态
     * 
     * @param ids ID列表
     * @param enabled 是否启用
     * @param updaterId 更新人ID
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                         @Param("enabled") Boolean enabled,
                         @Param("updaterId") Long updaterId);

    /**
     * 更新字段选项排序
     * 
     * @param id 选项ID
     * @param sort 排序号
     * @param updaterId 更新人ID
     * @return 更新行数
     */
    int updateSort(@Param("id") Long id,
                  @Param("sort") Integer sort,
                  @Param("updaterId") Long updaterId);

    /**
     * 批量更新字段选项排序
     * 
     * @param sortMap ID和排序号的映射
     * @param updaterId 更新人ID
     * @return 更新行数
     */
    int batchUpdateSort(@Param("sortMap") Map<Long, Integer> sortMap,
                       @Param("updaterId") Long updaterId);

    /**
     * 配置字段选项联动规则
     * 
     * @param id 选项ID
     * @param linkageConfig 联动配置（JSON格式）
     * @param updaterId 更新人ID
     * @return 更新行数
     */
    int updateLinkageConfig(@Param("id") Long id,
                           @Param("linkageConfig") String linkageConfig,
                           @Param("updaterId") Long updaterId);

    /**
     * 统计字段选项使用情况
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> countOptionUsage(@Param("formId") String formId,
                                             @Param("fieldId") String fieldId,
                                             @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);

    /**
     * 统计字段选项分组情况
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @return 统计结果
     */
    List<Map<String, Object>> countOptionGroups(@Param("formId") String formId,
                                              @Param("fieldId") String fieldId);

    /**
     * 统计字段选项状态分布
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @return 统计结果
     */
    List<Map<String, Object>> countOptionStatus(@Param("formId") String formId,
                                              @Param("fieldId") String fieldId);
}