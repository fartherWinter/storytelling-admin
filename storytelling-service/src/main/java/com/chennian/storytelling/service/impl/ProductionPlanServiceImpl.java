package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.manufacturing.ProductionPlan;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.manufacturing.ProductionPlanMapper;
import com.chennian.storytelling.service.ProductionPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 生产计划管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    @Autowired
    private ProductionPlanMapper productionPlanMapper;

    @Override
    public IPage<ProductionPlan> getProductionPlanList(PageParam<ProductionPlan> pageParam, ProductionPlan productionPlan) {
        Page<ProductionPlan> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<ProductionPlan> queryWrapper = new QueryWrapper<>();
        
        if (productionPlan != null) {
            if (StringUtils.hasText(productionPlan.getPlanCode())) {
                queryWrapper.like("plan_code", productionPlan.getPlanCode());
            }
            if (StringUtils.hasText(productionPlan.getPlanName())) {
                queryWrapper.like("plan_name", productionPlan.getPlanName());
            }
            if (productionPlan.getProductId() != null) {
                queryWrapper.eq("product_id", productionPlan.getProductId());
            }
            if (productionPlan.getPlanStatus() != null) {
                queryWrapper.eq("plan_status", productionPlan.getPlanStatus());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return productionPlanMapper.selectPage(page, queryWrapper);
    }

    @Override
    public ProductionPlan getProductionPlanById(Long id) {
        return productionPlanMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProductionPlan(ProductionPlan productionPlan) {
        productionPlan.setCreateTime(DateTime.now());
        // 初始状态：草稿
        productionPlan.setPlanStatus(0);
        productionPlanMapper.insert(productionPlan);
    }

    @Override
    @Transactional
    public void updateProductionPlan(ProductionPlan productionPlan) {
        productionPlan.setUpdateTime(DateTime.now());
        productionPlanMapper.updateById(productionPlan);
    }

    @Override
    @Transactional
    public void deleteProductionPlan(Long[] ids) {
        productionPlanMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void auditProductionPlan(Long planId, Integer auditStatus, String auditRemark) {
        ProductionPlan plan = new ProductionPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus(auditStatus);
        plan.setUpdateTime(DateTime.now());
        productionPlanMapper.updateById(plan);
        log.info("生产计划审核完成，计划ID：{}，审核状态：{}，审核备注：{}", planId, auditStatus, auditRemark);
    }

    @Override
    @Transactional
    public void deleteProductionPlan(Long id) {
        productionPlanMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void approveProductionPlan(Long id, String approver) {
        ProductionPlan plan = new ProductionPlan();
        plan.setPlanId(id);
        plan.setPlanStatus(1); // 已审批
        plan.setUpdateTime(DateTime.now());
        productionPlanMapper.updateById(plan);
        log.info("生产计划审批完成，计划ID：{}，审批人：{}", id, approver);
    }

    @Override
    @Transactional
    public void executeProductionPlan(Long planId) {
        ProductionPlan plan = new ProductionPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus(2); // 执行中
        plan.setUpdateTime(DateTime.now());
        productionPlanMapper.updateById(plan);
        log.info("生产计划开始执行，计划ID：{}", planId);
    }

    @Override
    @Transactional
    public void completeProductionPlan(Long planId) {
        ProductionPlan plan = new ProductionPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus(3); // 已完成
        plan.setUpdateTime(DateTime.now());
        productionPlanMapper.updateById(plan);
        log.info("生产计划完成，计划ID：{}", planId);
    }
}