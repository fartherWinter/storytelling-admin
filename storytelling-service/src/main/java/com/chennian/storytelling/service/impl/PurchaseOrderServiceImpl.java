package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.PurchaseOrder;
import com.chennian.storytelling.bean.model.PurchaseOrderItem;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.PurchaseOrderMapper;
import com.chennian.storytelling.service.PurchaseOrderItemService;
import com.chennian.storytelling.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 采购订单服务实现类
 * @author chennian
 * @date 2023/5/20
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    
    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;

    /**
     * 分页查询采购订单
     *
     * @param page 分页参数
     * @param purchaseOrder 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<PurchaseOrder> findByPage(PageParam<PurchaseOrder> page, PurchaseOrder purchaseOrder) {
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (purchaseOrder != null) {
            // 根据订单编号模糊查询
            if (StringUtils.hasText(purchaseOrder.getOrderNo())) {
                queryWrapper.like(PurchaseOrder::getOrderNo, purchaseOrder.getOrderNo());
            }
            
            // 根据供应商ID查询
            if (purchaseOrder.getSupplierId() != null) {
                queryWrapper.eq(PurchaseOrder::getSupplierId, purchaseOrder.getSupplierId());
            }
            
            // 根据订单状态查询
            if (StringUtils.hasText(purchaseOrder.getStatus())) {
                queryWrapper.eq(PurchaseOrder::getStatus, purchaseOrder.getStatus());
            }
            
            // 根据创建时间范围查询
            if (purchaseOrder.getCreateTime() != null) {
                queryWrapper.ge(PurchaseOrder::getCreateTime, purchaseOrder.getCreateTime());
            }
        }
        
        // 默认按创建时间降序排序
        queryWrapper.orderByDesc(PurchaseOrder::getCreateTime);
        
        // 执行分页查询
        IPage<PurchaseOrder> pageResult = page(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
        
        // 加载每个订单的明细列表
        if (pageResult.getRecords() != null && !pageResult.getRecords().isEmpty()) {
            for (PurchaseOrder order : pageResult.getRecords()) {
                List<PurchaseOrderItem> items = getItems(order);
                order.setItems(items);
            }
        }
        
        return pageResult;
    }

    /**
     * 根据ID查询采购订单
     *
     * @param orderId 订单ID
     * @return 采购订单信息
     */
    @Override
    public PurchaseOrder selectOrderById(Long orderId) {
        PurchaseOrder purchaseOrder = getById(orderId);
        if (purchaseOrder != null) {
            // 获取订单明细列表
            List<PurchaseOrderItem> items = purchaseOrderItemService.selectItemsByOrderId(orderId);
            purchaseOrder.setItems(items);
        }
        return purchaseOrder;
    }

    /**
     * 新增采购订单
     *
     * @param purchaseOrder 采购订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOrder(PurchaseOrder purchaseOrder) {
        // 设置订单状态为草稿
        if (purchaseOrder.getStatus() == null) {
            purchaseOrder.setStatus("0");
        }
        
        // 设置创建时间
        purchaseOrder.setCreateTime(new Date());
        
        // 保存订单主表信息
        boolean result = save(purchaseOrder);
        
        // 保存订单明细信息
        List<PurchaseOrderItem> items = purchaseOrder.getItems();
        if (items != null && !items.isEmpty()) {
            for (PurchaseOrderItem item : items) {
                item.setOrderId(purchaseOrder.getOrderId());
                item.setCreateTime(new Date());
            }
            purchaseOrderItemService.batchInsertOrderItems(items);
        }
        
        return result ? 1 : 0;
    }

    /**
     * 修改采购订单
     *
     * @param purchaseOrder 采购订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrder(PurchaseOrder purchaseOrder) {
        // 设置更新时间
        purchaseOrder.setUpdateTime(new Date());
        
        // 更新订单主表信息
        boolean result = updateById(purchaseOrder);
        
        // 更新订单明细信息
        List<PurchaseOrderItem> items = purchaseOrder.getItems();
        if (items != null && !items.isEmpty()) {
            // 先删除原有明细
            purchaseOrderItemService.deleteOrderItemByOrderId(purchaseOrder.getOrderId());
            
            // 再插入新的明细
            for (PurchaseOrderItem item : items) {
                item.setOrderId(purchaseOrder.getOrderId());
                item.setCreateTime(new Date());
            }
            purchaseOrderItemService.batchInsertOrderItems(items);
        }
        
        return result ? 1 : 0;
    }

    /**
     * 批量删除采购订单
     *
     * @param orderIds 需要删除的订单ID数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOrderByIds(Long[] orderIds) {
        // 删除订单明细
        for (Long orderId : orderIds) {
            purchaseOrderItemService.deleteOrderItemByOrderId(orderId);
        }
        
        // 删除订单主表
        return purchaseOrderMapper.deleteBatchIds(Arrays.asList(orderIds));
    }

    /**
     * 审核采购订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int approveOrder(Long orderId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            return 0;
        }
        
        // 只有状态为已提交(1)的订单才能审核
        if (!"1".equals(order.getStatus())) {
            return 0;
        }
        
        // 更新订单状态为已审核(2)
        PurchaseOrder updateOrder = new PurchaseOrder();
        updateOrder.setOrderId(orderId);
        updateOrder.setStatus("2");
        updateOrder.setApproveTime(new Date());
        // 这里可以设置当前登录用户为审核人
        // updateOrder.setApproverId(SecurityUtils.getUserId());
        
        return updateById(updateOrder) ? 1 : 0;
    }
    
    /**
     * 获取采购订单明细列表
     *
     * @param purchaseOrder 采购订单信息
     * @return 订单明细列表
     */
    private List<PurchaseOrderItem> getItems(PurchaseOrder purchaseOrder) {
        if (purchaseOrder != null && purchaseOrder.getOrderId() != null) {
            return purchaseOrderItemService.selectItemsByOrderId(purchaseOrder.getOrderId());
        }
        return null;
    }

    /**
     * 获取所有待处理采购订单数量
     * @return 待处理订单数量
     */
    @Override
    public Integer getPendingOrderCount() {
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrder::getStatus, "1"); // 1为已提交待处理
        return Math.toIntExact(count(queryWrapper));
    }

    /**
     * 获取指定供应商的待处理订单列表
     * @param supplierId 供应商ID
     * @return 待处理订单列表
     */
    @Override
    public Object getSupplierPendingOrders(Long supplierId) {
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrder::getSupplierId, supplierId);
        queryWrapper.eq(PurchaseOrder::getStatus, "1"); // 1为已提交待处理
        List<PurchaseOrder> orders = list(queryWrapper);
        // 可根据需要返回VO或直接返回订单列表
        return orders;
    }

    /**
     * 获取指定供应商的历史订单记录
     * @param supplierId 供应商ID
     * @return 历史订单列表
     */
    @Override
    public Object getSupplierOrderHistory(Long supplierId) {
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrder::getSupplierId, supplierId);
        queryWrapper.ne(PurchaseOrder::getStatus, "1"); // 非待处理即为历史
        List<PurchaseOrder> orders = list(queryWrapper);
        return orders;
    }
}