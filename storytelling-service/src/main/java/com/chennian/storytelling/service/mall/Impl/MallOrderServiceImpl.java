package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallOrderMapper;
import com.chennian.storytelling.service.mall.MallOrderService;
import com.chennian.storytelling.common.enums.MallOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 商城订单Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallOrderServiceImpl extends ServiceImpl<MallOrderMapper, MallOrder> implements MallOrderService {
    
    @Autowired
    private MallOrderMapper mallOrderMapper;
    
    /**
     * 分页查询订单列表
     */
    @Override
    public IPage<MallOrder> findByPage(PageParam<MallOrder> page, MallOrder mallOrder) {
        LambdaQueryWrapper<MallOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (mallOrder != null) {
            // 根据订单编号查询
            if (StringUtils.hasText(mallOrder.getOrderSn())) {
                queryWrapper.eq(MallOrder::getOrderSn, mallOrder.getOrderSn());
            }
            // 根据用户ID查询
            if (mallOrder.getUserId() != null) {
                queryWrapper.eq(MallOrder::getUserId, mallOrder.getUserId());
            }
            // 根据订单状态查询
            if (mallOrder.getStatus() != null) {
                queryWrapper.eq(MallOrder::getStatus, mallOrder.getStatus());
            }
            // 根据支付方式查询
            if (mallOrder.getPayType() != null) {
                queryWrapper.eq(MallOrder::getPayType, mallOrder.getPayType());
            }
        }
        queryWrapper.orderByDesc(MallOrder::getCreateTime);
        return mallOrderMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询订单
     */
    @Override
    public MallOrder selectOrderById(Long orderId) {
        return mallOrderMapper.selectById(orderId);
    }
    
    /**
     * 根据用户ID查询订单列表
     */
    @Override
    public IPage<MallOrder> selectOrdersByUserId(Long userId, PageParam<MallOrder> page) {
        LambdaQueryWrapper<MallOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallOrder::getUserId, userId)
                   .orderByDesc(MallOrder::getCreateTime);
        return mallOrderMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 创建订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallOrder createOrder(MallOrder mallOrder) {
        mallOrder.setCreateTime(LocalDateTime.now());
        mallOrder.setUpdateTime(LocalDateTime.now());
        // 默认状态为待付款
        if (mallOrder.getStatus() == null) {
            mallOrder.setStatus(0);
        }
        return mallOrderMapper.insert(mallOrder) >0 ? mallOrder : null;
    }
    
    /**
     * 更新订单状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderStatus(Long orderId, Integer status) {
        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(orderId);
        mallOrder.setStatus(status);
        mallOrder.setUpdateTime(LocalDateTime.now());
        return mallOrderMapper.updateById(mallOrder);
    }
    
    /**
     * 取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrder(Long orderId) {
        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(orderId);
        mallOrder.setStatus(MallOrderStatus.CLOSED.getCode()); // 已关闭
        mallOrder.setUpdateTime(LocalDateTime.now());
        return mallOrderMapper.updateById(mallOrder);
    }
    
    /**
     * 支付订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int payOrder(Long orderId, Integer payType) {
        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(orderId);
        mallOrder.setStatus(MallOrderStatus.PENDING_DELIVERY.getCode()); // 待发货
        mallOrder.setPayType(payType);
        mallOrder.setPaymentTime(LocalDateTime.now());
        mallOrder.setUpdateTime(LocalDateTime.now());
        return mallOrderMapper.updateById(mallOrder);
    }
    
    /**
     * 发货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deliverOrder(Long orderId) {
        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(orderId);
        mallOrder.setStatus(MallOrderStatus.DELIVERED.getCode()); // 已发货
        mallOrder.setDeliveryTime(LocalDateTime.now());
        mallOrder.setUpdateTime(LocalDateTime.now());
        return mallOrderMapper.updateById(mallOrder) > 0;
    }
    
    /**
     * 确认收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmReceive(Long orderId) {
        MallOrder mallOrder = new MallOrder();
        mallOrder.setId(orderId);
        mallOrder.setStatus(MallOrderStatus.COMPLETED.getCode()); // 已完成
        mallOrder.setReceiveTime(LocalDateTime.now());
        mallOrder.setUpdateTime(LocalDateTime.now());
        return mallOrderMapper.updateById(mallOrder)>0;
    }
}