package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.WorkOrder;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface WorkOrderService {

    /**
     * 查询工单列表
     */
    IPage<WorkOrder> getWorkOrderList(PageParam<WorkOrder> page, WorkOrder workOrder);
    
    /**
     * 根据ID查询工单信息
     */
    WorkOrder getWorkOrderById(Long id);
    
    /**
     * 新增工单
     */
    void addWorkOrder(WorkOrder workOrder);
    
    /**
     * 修改工单信息
     */
    void updateWorkOrder(WorkOrder workOrder);
    
    /**
     * 删除工单
     */
    void deleteWorkOrder(Long[] ids);
    
    /**
     * 工单派工
     */
    void assignWorkOrder(Long workOrderId, Long operatorId, Long equipmentId);

    /**
     * 删除工单（单个）
     */
    @Transactional
    void deleteWorkOrder(Long id);

    /**
     * 工单派工（简化版）
     */
    @Transactional
    void assignWorkOrder(Long id, Long operatorId);

    /**
     * 工单开工
     */
    void startWorkOrder(Long workOrderId);
    
    /**
     * 工单完工
     */
    void completeWorkOrder(Long workOrderId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime);

    /**
     * 工单报工
     */
    void reportWorkOrder(Long workOrderId, Integer completedQuantity, Integer qualifiedQuantity, String remark);

    /**
     * 根据生产订单生成工单
     */
    void generateWorkOrders(Long productionOrderId);
    
    /**
     * 工单完工（简化版）
     */
    @Transactional
    void completeWorkOrder(Long id);

    /**
     * 工单报工（简化版）
     */
    @Transactional
    void reportWorkOrder(Long id, Integer quantity, String remark);

    /**
     * 根据生产订单生成工单（简化版）
     */
    @Transactional
    void generateWorkOrderFromProductionOrder(Long productionOrderId);
}