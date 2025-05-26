package com.chennian.storytelling.report.mapper;

import com.chennian.storytelling.bean.model.ReportTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表模板数据访问层
 *
 * @author chennian
 */
@Mapper
public interface ReportTemplateMapper {

    /**
     * 查询报表模板列表
     *
     * @param templateName 模板名称
     * @param templateType 模板类型
     * @param status       状态
     * @return 报表模板列表
     */
    List<ReportTemplate> selectTemplateList(@Param("templateName") String templateName,
                                           @Param("templateType") String templateType,
                                           @Param("status") String status);

    /**
     * 根据ID查询报表模板
     *
     * @param id 模板ID
     * @return 报表模板
     */
    ReportTemplate selectTemplateById(Long id);

    /**
     * 新增报表模板
     *
     * @param template 报表模板
     * @return 影响行数
     */
    int insertTemplate(ReportTemplate template);

    /**
     * 修改报表模板
     *
     * @param template 报表模板
     * @return 影响行数
     */
    int updateTemplate(ReportTemplate template);

    /**
     * 删除报表模板
     *
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteTemplateById(Long id);

    /**
     * 批量删除报表模板
     *
     * @param ids 需要删除的模板ID数组
     * @return 影响行数
     */
    int deleteTemplateByIds(Long[] ids);
}