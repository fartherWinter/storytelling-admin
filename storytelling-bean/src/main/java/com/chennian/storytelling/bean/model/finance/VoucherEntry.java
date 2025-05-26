package com.chennian.storytelling.bean.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会计凭证分录表
 * @author chen
 * @TableName erp_voucher_entry
 */
@TableName(value ="erp_voucher_entry")
@Data
public class VoucherEntry implements Serializable {
    /**
     * 分录ID
     */
    @TableId(value = "entry_id", type = IdType.AUTO)
    private Long entryId;

    /**
     * 凭证ID
     */
    @TableField(value = "voucher_id")
    private Long voucherId;

    /**
     * 凭证编号
     */
    @TableField(value = "voucher_no")
    private String voucherNo;

    /**
     * 会计科目ID
     */
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 会计科目编码
     */
    @TableField(value = "account_code")
    private String accountCode;

    /**
     * 会计科目名称
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 借方金额
     */
    @TableField(value = "debit_amount")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @TableField(value = "credit_amount")
    private BigDecimal creditAmount;

    /**
     * 摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 关联业务类型（1销售 2采购 3费用 4资产 5其他）
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 关联业务ID
     */
    @TableField(value = "business_id")
    private Long businessId;

    /**
     * 关联业务编号
     */
    @TableField(value = "business_no")
    private String businessNo;

    /**
     * 辅助核算项1
     */
    @TableField(value = "auxiliary_item1")
    private String auxiliaryItem1;

    /**
     * 辅助核算项2
     */
    @TableField(value = "auxiliary_item2")
    private String auxiliaryItem2;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}