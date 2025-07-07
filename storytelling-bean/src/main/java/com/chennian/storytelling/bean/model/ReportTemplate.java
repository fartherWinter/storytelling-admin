package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 报表模板
 * @author chen
 * @TableName report_template
 */
@TableName(value = "report_template")
@Data
public class ReportTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    @TableField(value = "template_name")
    private String templateName;

    /**
     * 模板描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 模板类型（1财务报表 2业务报表 3统计报表）
     */
    @TableField(value = "template_type")
    private String templateType;

    /**
     * 模板内容（JSON格式）
     */
    @TableField(value = "template_content")
    private String templateContent;

    /**
     * 数据源类型（1数据库 2API）
     */
    @TableField(value = "data_source_type")
    private String dataSourceType;

    /**
     * 数据源配置
     */
    @TableField(value = "data_source")
    private String dataSource;

    /**
     * 状态（1启用 0禁用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "column_config")
    private String columnConfig;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 删除标记
     */
    @TableField(value = "del_flag")
    public String delFlag;

}