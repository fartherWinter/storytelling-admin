package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallSubOrderMapper;
import com.chennian.storytelling.service.mall.MallSubOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城子订单Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallSubOrderServiceImpl extends ServiceImpl<MallSubOrderMapper, MallSubOrder> implements MallSubOrderService {
    
    @Autowired
    private MallSubOrderMapper mallSubOrderMapper;
    
    /**
     * 根据订单ID查询子订单列表
     */
    @Override
    public List<MallSubOrder> selectSubOrdersByOrderId(Long orderId) {
        LambdaQueryWrapper<MallSubOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallSubOrder::getOrderId, orderId)
                   .orderByAsc(MallSubOrder::getCreateTime);
        return mallSubOrderMapper.selectList(queryWrapper);
    }
    
    /**
     * 根据订单编号查询子订单列表
     */
    @Override
    public List<MallSubOrder> selectSubOrdersByOrderSn(String orderSn) {
        LambdaQueryWrapper<MallSubOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallSubOrder::getOrderSn, orderSn)
                   .orderByAsc(MallSubOrder::getCreateTime);
        return mallSubOrderMapper.selectList(queryWrapper);
    }
    
    /**
     * 批量创建子订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateSubOrders(List<MallSubOrder> subOrders) {
        if (subOrders == null || subOrders.isEmpty()) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        for (MallSubOrder subOrder : subOrders) {
            subOrder.setCreateTime(now);
            subOrder.setUpdateTime(now);
        }
        
        // 使用MyBatis-Plus的批量插入
        return saveBatch(subOrders) ? subOrders.size() : 0;
    }
    
    /**
     * 根据订单ID删除子订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSubOrdersByOrderId(Long orderId) {
        LambdaQueryWrapper<MallSubOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallSubOrder::getOrderId, orderId);
        return mallSubOrderMapper.delete(queryWrapper);
    }
    
    /**
     * 分页查询子订单
     */
    @Override
    public IPage<MallSubOrder> findByPage(PageParam<MallSubOrder> page, MallSubOrder mallSubOrder) {
        LambdaQueryWrapper<MallSubOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (mallSubOrder != null) {
            // 根据订单ID查询
            if (mallSubOrder.getOrderId() != null) {
                queryWrapper.eq(MallSubOrder::getOrderId, mallSubOrder.getOrderId());
            }
            // 根据订单编号查询
            if (StringUtils.hasText(mallSubOrder.getOrderSn())) {
                queryWrapper.eq(MallSubOrder::getOrderSn, mallSubOrder.getOrderSn());
            }
            // 根据商品ID查询
            if (mallSubOrder.getProductId() != null) {
                queryWrapper.eq(MallSubOrder::getProductId, mallSubOrder.getProductId());
            }
            // 根据商品名称模糊查询
            if (StringUtils.hasText(mallSubOrder.getProductName())) {
                queryWrapper.like(MallSubOrder::getProductName, mallSubOrder.getProductName());
            }
        }
        queryWrapper.orderByDesc(MallSubOrder::getCreateTime);
        return mallSubOrderMapper.selectPage(page, queryWrapper);
    }
}