package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应链协同计划参与者实体
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
@TableName("supply_chain_plan_participant")
public class SupplyChainPlanParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参与者ID
     */
    @TableId(value = "participant_id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 参与者类型（supplier-供应商，customer-客户，internal-内部部门）
     */
    private String participantType;

    /**
     * 参与者ID（关联到对应类型的表）
     */
    private Long participantId;

    /**
     * 参与者名称
     */
    private String participantName;

    /**
     * 参与者角色
     */
    private String participantRole;
}