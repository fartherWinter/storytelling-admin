package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应链协同计划里程碑实体
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
@TableName("supply_chain_plan_milestone")
public class SupplyChainPlanMilestone implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 里程碑ID
     */
    @TableId(value = "milestone_id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 里程碑名称
     */
    private String milestoneName;

    /**
     * 里程碑日期
     */
    private String milestoneDate;

    /**
     * 里程碑状态
     */
    private String status;
}