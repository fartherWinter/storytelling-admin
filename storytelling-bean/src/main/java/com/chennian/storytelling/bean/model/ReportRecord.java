package com.chennian.storytelling.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表记录实体类
 * 用于记录用户生成的报表信息
 *
 * @author chennian
 */
public class ReportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;
    
    /** 报表名称 */
    private String reportName;
    
    /** 关联的模板ID */
    private Long templateId;
    
    /** 关联的模板名称 */
    private String templateName;
    
    /** 报表参数（JSON格式） */
    private String reportParams;
    
    /** 报表格式（xlsx/pdf/csv等） */
    private String reportFormat;
    
    /** 报表文件路径 */
    private String filePath;
    
    /** 文件大小（字节） */
    private Long fileSize;
    
    /** 生成状态（0生成中 1已完成 2失败） */
    private String status;
    
    /** 错误信息 */
    private String errorMsg;
    
    /** 生成时间 */
    private Date generateTime;
    
    /** 过期时间 */
    private Date expireTime;
    
    /** 下载次数 */
    private Integer downloadCount;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    private Date updateTime;
    
    /** 备注 */
    private String remark;
    
    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getReportParams() {
        return reportParams;
    }

    public void setReportParams(String reportParams) {
        this.reportParams = reportParams;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "ReportRecord{" +
                "id=" + id +
                ", reportName='" + reportName + '\'' +
                ", templateId=" + templateId +
                ", templateName='" + templateName + '\'' +
                ", reportFormat='" + reportFormat + '\'' +
                ", status='" + status + '\'' +
                ", generateTime=" + generateTime +
                "}";
    }
}