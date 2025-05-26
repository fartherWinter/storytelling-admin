package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.SalesOrderItem;

import java.util.List;

/**
 * 销售订单明细服务接口
 * @author chennian
 * @date 2023/5/20
 */
public interface SalesOrderItemService extends IService<SalesOrderItem> {

    /**
     * 根据订单ID查询订单明细
     * @param orderId 订单ID
     * @return 明细列表
     */
    List<SalesOrderItem> selectItemsByOrderId(Long orderId);

    /**
     * 根据订单ID删除订单明细
     * @param orderId 订单ID
     * @return 结果
     */
    int deleteByOrderId(Long orderId);
}