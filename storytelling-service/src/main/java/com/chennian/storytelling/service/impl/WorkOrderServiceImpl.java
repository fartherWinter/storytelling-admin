package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.manufacturing.WorkOrder;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.manufacturing.WorkOrderMapper;
import com.chennian.storytelling.service.WorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 工单管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Override
    public IPage<WorkOrder> getWorkOrderList(PageParam<WorkOrder> pageParam, WorkOrder workOrder) {
        Page<WorkOrder> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<WorkOrder> queryWrapper = new QueryWrapper<>();
        
        if (workOrder != null) {
            if (StringUtils.hasText(workOrder.getWorkOrderCode())) {
                queryWrapper.like("work_order_code", workOrder.getWorkOrderCode());
            }
            if (StringUtils.hasText(workOrder.getWorkOrderName())) {
                queryWrapper.like("work_order_name", workOrder.getWorkOrderName());
            }
            if (workOrder.getProductionOrderId() != null) {
                queryWrapper.eq("production_order_id", workOrder.getProductionOrderId());
            }
            if (workOrder.getProductId() != null) {
                queryWrapper.eq("product_id", workOrder.getProductId());
            }
            if (workOrder.getWorkOrderStatus() != null) {
                queryWrapper.eq("work_order_status", workOrder.getWorkOrderStatus());
            }
            if (workOrder.getOperatorId() != null) {
                queryWrapper.eq("operator_id", workOrder.getOperatorId());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return workOrderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public WorkOrder getWorkOrderById(Long id) {
        return workOrderMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addWorkOrder(WorkOrder workOrder) {
        workOrder.setCreateTime(DateTime.now());
        // 初始状态：待派工
        workOrder.setWorkOrderStatus(0);
        workOrder.setCompletedQuantity(0);
        workOrderMapper.insert(workOrder);
    }

    @Override
    @Transactional
    public void updateWorkOrder(WorkOrder workOrder) {
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
    }

    @Override
    @Transactional
    public void deleteWorkOrder(Long[] ids) {
        workOrderMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void assignWorkOrder(Long workOrderId, Long operatorId, Long equipmentId) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(workOrderId);
        workOrder.setOperatorId(operatorId);
        workOrder.setEquipmentId(equipmentId);
        workOrder.setWorkOrderStatus(1); // 已派工
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单派工完成，工单ID：{}，操作员ID：{}，设备ID：{}", workOrderId, operatorId, equipmentId);
    }

    @Override
    @Transactional
    public void deleteWorkOrder(Long id) {
        workOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignWorkOrder(Long id, Long operatorId) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(id);
        workOrder.setOperatorId(operatorId);
        workOrder.setWorkOrderStatus(1); // 已派工
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单派工完成，工单ID：{}，操作员ID：{}", id, operatorId);
    }

    @Override
    @Transactional
    public void startWorkOrder(Long workOrderId) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(workOrderId);
        workOrder.setWorkOrderStatus(2); // 生产中
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单开工，工单ID：{}", workOrderId);
    }

    @Override
    @Transactional
    public void completeWorkOrder(Long workOrderId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(workOrderId);
        workOrder.setWorkOrderStatus(3); // 已完工
        workOrder.setCompletedQuantity(completedQuantity);
        workOrder.setQualifiedQuantity(qualifiedQuantity);
        workOrder.setActualWorkHours(actualTime);
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单完工，工单ID：{}，完成数量：{}，合格数量：{}，实际用时：{}", workOrderId, completedQuantity, qualifiedQuantity, actualTime);
    }

    @Override
    @Transactional
    public void reportWorkOrder(Long workOrderId, Integer completedQuantity, Integer qualifiedQuantity, String remark) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(workOrderId);
        workOrder.setCompletedQuantity(completedQuantity);
        workOrder.setQualifiedQuantity(qualifiedQuantity);
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单报工，工单ID：{}，完成数量：{}，合格数量：{}，备注：{}", workOrderId, completedQuantity, qualifiedQuantity, remark);
    }

    @Override
    @Transactional
    public void generateWorkOrders(Long productionOrderId) {
        // 根据生产订单生成工单的逻辑
        log.info("根据生产订单生成工单，生产订单ID：{}", productionOrderId);
        // 这里应该包含具体的工单生成逻辑
    }

    @Override
    @Transactional
    public void completeWorkOrder(Long id) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(id);
        workOrder.setWorkOrderStatus(3); // 已完工
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单完工，工单ID：{}", id);
    }

    @Override
    @Transactional
    public void reportWorkOrder(Long id, Integer quantity, String remark) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderId(id);
        workOrder.setCompletedQuantity(quantity);
        workOrder.setUpdateTime(DateTime.now());
        workOrderMapper.updateById(workOrder);
        log.info("工单报工，工单ID：{}，完成数量：{}，备注：{}", id, quantity, remark);
    }

    @Override
    @Transactional
    public void generateWorkOrderFromProductionOrder(Long productionOrderId) {
        // 根据生产订单生成工单的逻辑
        log.info("根据生产订单生成工单，生产订单ID：{}", productionOrderId);
        // 这里应该包含具体的工单生成逻辑
    }
}