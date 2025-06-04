package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.common.utils.PageParam;

/**
 * 商城订单Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallOrderService extends IService<MallOrder> {
    
    /**
     * 分页查询订单列表
     * 
     * @param page 分页参数
     * @param mallOrder 查询条件
     * @return 订单分页数据
     */
    IPage<MallOrder> findByPage(PageParam<MallOrder> page, MallOrder mallOrder);
    
    /**
     * 根据ID查询订单
     * 
     * @param orderId 订单ID
     * @return 订单信息
     */
    MallOrder selectOrderById(Long orderId);
    
    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    IPage<MallOrder> selectOrdersByUserId(Long userId, PageParam<MallOrder> page);
    
    /**
     * 创建订单
     * 
     * @param mallOrder 订单信息
     * @return 结果
     */
    MallOrder createOrder(MallOrder mallOrder);
    
    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 结果
     */
    int updateOrderStatus(Long orderId, Integer status);
    
    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    int cancelOrder(Long orderId);
    
    /**
     * 支付订单
     * 
     * @param orderId 订单ID
     * @param payType 支付方式
     * @return 结果
     */
    int payOrder(Long orderId, Integer payType);
    
    /**
     * 发货
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    Boolean deliverOrder(Long orderId);
    
    /**
     * 确认收货
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    Boolean confirmReceive(Long orderId);
}