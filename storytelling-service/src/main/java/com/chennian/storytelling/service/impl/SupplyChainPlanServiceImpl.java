package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chennian.storytelling.bean.model.SupplyChainPlan;
import com.chennian.storytelling.bean.model.SupplyChainPlanParticipant;
import com.chennian.storytelling.bean.model.SupplyChainPlanMilestone;
import com.chennian.storytelling.bean.vo.SupplyChainEventVO;
import com.chennian.storytelling.dao.SupplyChainPlanMapper;
import com.chennian.storytelling.dao.SupplyChainPlanParticipantMapper;
import com.chennian.storytelling.dao.SupplyChainPlanMilestoneMapper;
import com.chennian.storytelling.service.SupplyChainPlanService;
import com.chennian.storytelling.service.SupplyChainEventService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应链协同计划服务实现
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Service
public class SupplyChainPlanServiceImpl implements SupplyChainPlanService {

    @Resource
    private SupplyChainPlanMapper supplyChainPlanMapper;
    
    @Resource
    private SupplyChainPlanParticipantMapper participantMapper;
    
    @Resource
    private SupplyChainPlanMilestoneMapper milestoneMapper;
    
    @Resource
    private SupplyChainEventService eventService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPlan(Map<String, Object> planMap) {
        // 创建计划实体
        SupplyChainPlan plan = new SupplyChainPlan();
        plan.setPlanName((String) planMap.get("planName"));
        plan.setStartDate((String) planMap.get("startDate"));
        plan.setEndDate((String) planMap.get("endDate"));
        plan.setDescription((String) planMap.get("description"));
        plan.setStatus("未开始");
        plan.setCreateTime(System.currentTimeMillis());
        
        // 保存计划
        supplyChainPlanMapper.insert(plan);
        Long planId = plan.getPlanId();
        
        // 处理参与者
        List<Map<String, Object>> participants = (List<Map<String, Object>>) planMap.get("participants");
        if (participants != null && !participants.isEmpty()) {
            for (Map<String, Object> participant : participants) {
                SupplyChainPlanParticipant participantEntity = new SupplyChainPlanParticipant();
                participantEntity.setPlanId(planId);
                participantEntity.setParticipantType((String) participant.get("type"));
                participantEntity.setParticipantId(Long.valueOf(participant.get("id").toString()));
                participantEntity.setParticipantName((String) participant.get("name"));
                participantEntity.setParticipantRole((String) participant.get("role"));
                
                participantMapper.insert(participantEntity);
            }
        }
        
        // 处理里程碑
        List<Map<String, Object>> milestones = (List<Map<String, Object>>) planMap.get("milestones");
        if (milestones != null && !milestones.isEmpty()) {
            for (Map<String, Object> milestone : milestones) {
                SupplyChainPlanMilestone milestoneEntity = new SupplyChainPlanMilestone();
                milestoneEntity.setPlanId(planId);
                milestoneEntity.setMilestoneName((String) milestone.get("name"));
                milestoneEntity.setMilestoneDate((String) milestone.get("date"));
                milestoneEntity.setStatus((String) milestone.get("status"));
                
                milestoneMapper.insert(milestoneEntity);
            }
        }
        
        // 创建供应链事件
        SupplyChainEventVO eventVO = new SupplyChainEventVO();
        eventVO.setEventType("协同计划");
        eventVO.setDescription("创建了新的供应链协同计划：" + plan.getPlanName());
        eventVO.setRelatedId(planId);
        eventVO.setStatus("已创建");
        eventVO.setEventTime(System.currentTimeMillis());
        
        eventService.createEvent(eventVO);
        
        return planId;
    }
    
    @Override
    public Map<String, Object> getPlanDetail(Long planId) {
        // 查询计划基本信息
        SupplyChainPlan plan = supplyChainPlanMapper.selectById(planId);
        if (plan == null) {
            return null;
        }
        
        Map<String, Object> planDetail = new HashMap<>();
        planDetail.put("planId", plan.getPlanId());
        planDetail.put("planName", plan.getPlanName());
        planDetail.put("startDate", plan.getStartDate());
        planDetail.put("endDate", plan.getEndDate());
        planDetail.put("status", plan.getStatus());
        planDetail.put("description", plan.getDescription());
        
        // 查询参与者
        LambdaQueryWrapper<SupplyChainPlanParticipant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(SupplyChainPlanParticipant::getPlanId, planId);
        List<SupplyChainPlanParticipant> participantEntities = participantMapper.selectList(participantQuery);
        
        List<Map<String, Object>> participants = new ArrayList<>();
        for (SupplyChainPlanParticipant participant : participantEntities) {
            Map<String, Object> participantMap = new HashMap<>();
            participantMap.put("type", participant.getParticipantType());
            participantMap.put("id", participant.getParticipantId());
            participantMap.put("name", participant.getParticipantName());
            participantMap.put("role", participant.getParticipantRole());
            
            participants.add(participantMap);
        }
        planDetail.put("participants", participants);
        
        // 查询里程碑
        LambdaQueryWrapper<SupplyChainPlanMilestone> milestoneQuery = new LambdaQueryWrapper<>();
        milestoneQuery.eq(SupplyChainPlanMilestone::getPlanId, planId);
        milestoneQuery.orderByAsc(SupplyChainPlanMilestone::getMilestoneDate);
        List<SupplyChainPlanMilestone> milestoneEntities = milestoneMapper.selectList(milestoneQuery);
        
        List<Map<String, Object>> milestones = new ArrayList<>();
        for (SupplyChainPlanMilestone milestone : milestoneEntities) {
            Map<String, Object> milestoneMap = new HashMap<>();
            milestoneMap.put("name", milestone.getMilestoneName());
            milestoneMap.put("date", milestone.getMilestoneDate());
            milestoneMap.put("status", milestone.getStatus());
            
            milestones.add(milestoneMap);
        }
        planDetail.put("milestones", milestones);
        
        return planDetail;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePlanStatus(Long planId, String status, String comment) {
        // 查询计划
        SupplyChainPlan plan = supplyChainPlanMapper.selectById(planId);
        if (plan == null) {
            return false;
        }
        
        // 更新状态
        plan.setStatus(status);
        plan.setUpdateTime(System.currentTimeMillis());
        
        int rows = supplyChainPlanMapper.updateById(plan);
        
        if (rows > 0) {
            // 创建状态变更事件
            SupplyChainEventVO eventVO = new SupplyChainEventVO();
            eventVO.setEventType("计划状态变更");
            eventVO.setDescription("供应链协同计划 " + plan.getPlanName() + " 状态变更为：" + status);
            if (comment != null && !comment.isEmpty()) {
                eventVO.setDescription(eventVO.getDescription() + "，备注：" + comment);
            }
            eventVO.setRelatedId(planId);
            eventVO.setStatus(status);
            
            eventService.createEvent(eventVO);
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public Map<String, Object> getSupplierPlans(Long supplierId) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询供应商参与的计划
        LambdaQueryWrapper<SupplyChainPlanParticipant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(SupplyChainPlanParticipant::getParticipantType, "supplier");
        participantQuery.eq(SupplyChainPlanParticipant::getParticipantId, supplierId);
        List<SupplyChainPlanParticipant> participants = participantMapper.selectList(participantQuery);
        
        List<Long> planIds = new ArrayList<>();
        for (SupplyChainPlanParticipant participant : participants) {
            planIds.add(participant.getPlanId());
        }
        
        if (!planIds.isEmpty()) {
            // 查询计划详情
            List<SupplyChainPlan> plans = supplyChainPlanMapper.selectBatchIds(planIds);
            
            // 按状态分类
            List<Map<String, Object>> activePlans = new ArrayList<>();
            List<Map<String, Object>> completedPlans = new ArrayList<>();
            
            for (SupplyChainPlan plan : plans) {
                Map<String, Object> planMap = new HashMap<>();
                planMap.put("planId", plan.getPlanId());
                planMap.put("planName", plan.getPlanName());
                planMap.put("startDate", plan.getStartDate());
                planMap.put("endDate", plan.getEndDate());
                planMap.put("status", plan.getStatus());
                
                if ("已完成".equals(plan.getStatus())) {
                    completedPlans.add(planMap);
                } else {
                    activePlans.add(planMap);
                }
            }
            
            result.put("activePlans", activePlans);
            result.put("completedPlans", completedPlans);
        } else {
            result.put("activePlans", new ArrayList<>());
            result.put("completedPlans", new ArrayList<>());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getCustomerPlans(Long customerId) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询客户参与的计划
        LambdaQueryWrapper<SupplyChainPlanParticipant> participantQuery = new LambdaQueryWrapper<>();
        participantQuery.eq(SupplyChainPlanParticipant::getParticipantType, "customer");
        participantQuery.eq(SupplyChainPlanParticipant::getParticipantId, customerId);
        List<SupplyChainPlanParticipant> participants = participantMapper.selectList(participantQuery);
        
        List<Long> planIds = new ArrayList<>();
        for (SupplyChainPlanParticipant participant : participants) {
            planIds.add(participant.getPlanId());
        }
        
        if (!planIds.isEmpty()) {
            // 查询计划详情
            List<SupplyChainPlan> plans = supplyChainPlanMapper.selectBatchIds(planIds);
            
            // 按状态分类
            List<Map<String, Object>> activePlans = new ArrayList<>();
            List<Map<String, Object>> completedPlans = new ArrayList<>();
            
            for (SupplyChainPlan plan : plans) {
                Map<String, Object> planMap = new HashMap<>();
                planMap.put("planId", plan.getPlanId());
                planMap.put("planName", plan.getPlanName());
                planMap.put("startDate", plan.getStartDate());
                planMap.put("endDate", plan.getEndDate());
                planMap.put("status", plan.getStatus());
                
                if ("已完成".equals(plan.getStatus())) {
                    completedPlans.add(planMap);
                } else {
                    activePlans.add(planMap);
                }
            }
            
            result.put("activePlans", activePlans);
            result.put("completedPlans", completedPlans);
        } else {
            result.put("activePlans", new ArrayList<>());
            result.put("completedPlans", new ArrayList<>());
        }
        
        return result;
    }
}