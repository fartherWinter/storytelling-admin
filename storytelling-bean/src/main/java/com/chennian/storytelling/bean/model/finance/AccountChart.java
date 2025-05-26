package com.chennian.storytelling.bean.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会计科目表
 * @author chen
 * @TableName erp_account_chart
 */
@TableName(value ="erp_account_chart")
@Data
public class AccountChart implements Serializable {
    /**
     * 科目ID
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

    /**
     * 科目编码
     */
    @TableField(value = "account_code")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 科目类型（1资产 2负债 3权益 4收入 5费用）
     */
    @TableField(value = "account_type")
    private String accountType;

    /**
     * 父级科目ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 科目层级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 借贷方向（1借方 2贷方）
     */
    @TableField(value = "balance_direction")
    private String balanceDirection;

    /**
     * 是否启用（0禁用 1启用）
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<AccountChart> children;
}