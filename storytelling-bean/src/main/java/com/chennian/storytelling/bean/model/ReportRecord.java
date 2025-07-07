package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 报表记录
 * @author chen
 * @TableName report_record
 */
@TableName(value = "report_record")
@Data
public class ReportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报表ID
     */
    @TableField(value = "report_id")
    private Long reportId;

    /**
     * 报表名称
     */
    @TableField(value = "report_name")
    private String reportName;

    /**
     * 生成时间
     */
    @TableField(value = "generate_time")
    private Date generateTime;

    /**
     * 生成人
     */
    @TableField(value = "generate_by")
    private String generateBy;

    /**
     * 报表数据（JSON格式）
     */
    @TableField(value = "report_data")
    private String reportData;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private Long templateId;

    /**
     * 报表格式
     */
    @TableField(value = "report_format")
    private String reportFormat;

    /**
     * 报表参数
     */
    @TableField(value = "report_params")
    private String reportParams;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

        /**
     * 模板名称
     */
    @TableField(value = "template_name")
    private String templateName;

    /**
     * 删除标志（0正常 1删除）
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     * 下载次数
     */
    @TableField(value = "download_count")
    private Integer downloadCount;

    /**
     * 过期时间
     */
    @TableField(value = "expire_time")
    private Date expireTime;

    /**
     * 文件路径
     */
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 错误信息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 报表状态（1正常 0异常）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}