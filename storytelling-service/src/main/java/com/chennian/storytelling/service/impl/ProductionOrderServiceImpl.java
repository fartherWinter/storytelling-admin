package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.manufacturing.ProductionOrder;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.manufacturing.ProductionOrderMapper;
import com.chennian.storytelling.service.ProductionOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 生产订单管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class ProductionOrderServiceImpl implements ProductionOrderService {

    @Autowired
    private ProductionOrderMapper productionOrderMapper;

    @Override
    public IPage<ProductionOrder> getProductionOrderList(PageParam<ProductionOrder> pageParam, ProductionOrder productionOrder) {
        Page<ProductionOrder> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<ProductionOrder> queryWrapper = new QueryWrapper<>();
        
        if (productionOrder != null) {
            if (StringUtils.hasText(productionOrder.getOrderCode())) {
                queryWrapper.like("order_code", productionOrder.getOrderCode());
            }
            if (StringUtils.hasText(productionOrder.getOrderName())) {
                queryWrapper.like("order_name", productionOrder.getOrderName());
            }
            if (productionOrder.getPlanId() != null) {
                queryWrapper.eq("production_plan_id", productionOrder.getPlanId());
            }
            if (productionOrder.getProductId() != null) {
                queryWrapper.eq("product_id", productionOrder.getProductId());
            }
            if (productionOrder.getOrderStatus() != null) {
                queryWrapper.eq("order_status", productionOrder.getOrderStatus());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return productionOrderMapper.selectPage(page, queryWrapper);
    }

    @Override
    public ProductionOrder getProductionOrderById(Long id) {
        return productionOrderMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProductionOrder(ProductionOrder productionOrder) {
        productionOrder.setCreateTime(DateTime.now());
        // 初始状态：待开工
        productionOrder.setOrderStatus(0);
        productionOrder.setCompletedQuantity(0);
        productionOrder.setQualifiedQuantity(0);
        productionOrderMapper.insert(productionOrder);
    }

    @Override
    @Transactional
    public void updateProductionOrder(ProductionOrder productionOrder) {
        productionOrder.setUpdateTime(DateTime.now());
        productionOrderMapper.updateById(productionOrder);
    }

    @Override
    @Transactional
    public void deleteProductionOrder(Long[] ids) {
        productionOrderMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void deleteProductionOrder(Long id) {
        productionOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void startProductionOrder(Long orderId) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderId(orderId);
        order.setOrderStatus(1); // 生产中
        order.setUpdateTime(DateTime.now());
        productionOrderMapper.updateById(order);
        log.info("生产订单开工，订单ID：{}", orderId);
    }

    @Override
    @Transactional
    public void pauseProductionOrder(Long orderId, String pauseReason) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderId(orderId);
        order.setOrderStatus(2); // 暂停
        order.setUpdateTime(DateTime.now());
        productionOrderMapper.updateById(order);
        log.info("生产订单暂停，订单ID：{}，暂停原因：{}", orderId, pauseReason);
    }

    @Override
    @Transactional
    public void completeProductionOrder(Long orderId, Integer actualQuantity, Integer qualifiedQuantity, String remark) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderId(orderId);
        order.setOrderStatus(3); // 已完工
        order.setCompletedQuantity(actualQuantity);
        order.setQualifiedQuantity(qualifiedQuantity);
        order.setUpdateTime(DateTime.now());
        productionOrderMapper.updateById(order);
        log.info("生产订单完工，订单ID：{}，实际产量：{}，合格产量：{}，备注：{}", orderId, actualQuantity, qualifiedQuantity, remark);
    }

    @Override
    @Transactional
    public void pauseProductionOrder(Long id) {
        ProductionOrder order = new ProductionOrder();
        order.setOrderId(id);
        order.setOrderStatus(2); // 暂停
        order.setUpdateTime(DateTime.now());
        productionOrderMapper.updateById(order);
        log.info("生产订单暂停，订单ID：{}", id);
    }
}