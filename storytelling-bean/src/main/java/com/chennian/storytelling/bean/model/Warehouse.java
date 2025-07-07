package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 仓库实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("erp_warehouse")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 仓库类型：1-原料仓库，2-成品仓库，3-半成品仓库，4-退货仓库
     */
    private Integer warehouseType;

    /**
     * 仓库地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 仓库面积（平方米）
     */
    private Double area;

    /**
     * 仓库容量（立方米）
     */
    private Double capacity;

    /**
     * 仓库状态：0-停用，1-启用
     */
    private Integer status;

    /**
     * 是否默认仓库：0-否，1-是
     */
    private Integer isDefault;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}