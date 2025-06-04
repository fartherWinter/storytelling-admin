package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;

/**
 * 商城子订单Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallSubOrderService extends IService<MallSubOrder> {
    
    /**
     * 根据订单ID查询子订单列表
     * 
     * @param orderId 订单ID
     * @return 子订单列表
     */
    List<MallSubOrder> selectSubOrdersByOrderId(Long orderId);
    
    /**
     * 根据订单编号查询子订单列表
     * 
     * @param orderSn 订单编号
     * @return 子订单列表
     */
    List<MallSubOrder> selectSubOrdersByOrderSn(String orderSn);
    
    /**
     * 批量创建子订单
     * 
     * @param subOrders 子订单列表
     * @return 结果
     */
    int batchCreateSubOrders(List<MallSubOrder> subOrders);
    
    /**
     * 根据订单ID删除子订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    int deleteSubOrdersByOrderId(Long orderId);
    
    /**
     * 分页查询子订单
     * 
     * @param page 分页参数
     * @param mallSubOrder 查询条件
     * @return 子订单分页数据
     */
    IPage<MallSubOrder> findByPage(PageParam<MallSubOrder> page, MallSubOrder mallSubOrder);
}