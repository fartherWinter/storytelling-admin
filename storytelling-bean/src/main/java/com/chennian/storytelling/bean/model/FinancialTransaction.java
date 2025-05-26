package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财务交易记录表
 * @author chen
 * @TableName erp_financial_transaction
 */
@TableName(value ="erp_financial_transaction")
@Data
public class FinancialTransaction implements Serializable {
    /**
     * 交易ID
     */
    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Long transactionId;

    /**
     * 交易编号
     */
    @TableField(value = "transaction_no")
    private String transactionNo;

    /**
     * 交易类型（1收入 2支出 3转账 4其他）
     */
    @TableField(value = "transaction_type")
    private String transactionType;

    /**
     * 关联单据类型（1采购订单 2销售订单 3费用报销 4工资发放 5其他）
     */
    @TableField(value = "document_type")
    private String documentType;

    /**
     * 关联单据ID
     */
    @TableField(value = "document_id")
    private Long documentId;

    /**
     * 关联单据编号
     */
    @TableField(value = "document_no")
    private String documentNo;

    /**
     * 交易金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 交易方向（1收入 2支出）
     */
    @TableField(value = "direction")
    private String direction;

    /**
     * 交易账户
     */
    @TableField(value = "account")
    private String account;

    /**
     * 交易对象（供应商/客户/员工等）
     */
    @TableField(value = "counterparty")
    private String counterparty;

    /**
     * 交易日期
     */
    @TableField(value = "transaction_date")
    private Date transactionDate;

    /**
     * 交易状态（0待处理 1已完成 2已取消）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 交易描述
     */
    @TableField(value = "description")
    private String description;

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