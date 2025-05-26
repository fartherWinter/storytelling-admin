package com.chennian.storytelling.bean.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 会计凭证表
 * @author chen
 * @TableName erp_voucher
 */
@TableName(value ="erp_voucher")
@Data
public class Voucher implements Serializable {
    /**
     * 凭证ID
     */
    @TableId(value = "voucher_id", type = IdType.AUTO)
    private Long voucherId;

    /**
     * 凭证编号
     */
    @TableField(value = "voucher_no")
    private String voucherNo;

    /**
     * 凭证类型（1收款凭证 2付款凭证 3转账凭证 4记账凭证）
     */
    @TableField(value = "voucher_type")
    private String voucherType;

    /**
     * 凭证日期
     */
    @TableField(value = "voucher_date")
    private Date voucherDate;

    /**
     * 会计期间（格式：YYYY-MM）
     */
    @TableField(value = "accounting_period")
    private String accountingPeriod;

    /**
     * 附件数量
     */
    @TableField(value = "attachment_count")
    private Integer attachmentCount;

    /**
     * 借方金额合计
     */
    @TableField(value = "total_debit")
    private BigDecimal totalDebit;

    /**
     * 贷方金额合计
     */
    @TableField(value = "total_credit")
    private BigDecimal totalCredit;

    /**
     * 制单人
     */
    @TableField(value = "preparer")
    private String preparer;

    /**
     * 审核人
     */
    @TableField(value = "reviewer")
    private String reviewer;

    /**
     * 审核时间
     */
    @TableField(value = "review_time")
    private Date reviewTime;

    /**
     * 记账人
     */
    @TableField(value = "bookkeeper")
    private String bookkeeper;

    /**
     * 记账时间
     */
    @TableField(value = "bookkeeping_time")
    private Date bookkeepingTime;

    /**
     * 凭证状态（0草稿 1已审核 2已记账 3已作废）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 凭证分录列表
     */
    @TableField(exist = false)
    private List<VoucherEntry> entries;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}