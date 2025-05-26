package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.SalesOrderItem;
import com.chennian.storytelling.dao.SalesOrderItemMapper;
import com.chennian.storytelling.service.SalesOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 销售订单明细服务实现类
 * @author chennian
 * @date 2023/5/20
 */
@Service
public class SalesOrderItemServiceImpl extends ServiceImpl<SalesOrderItemMapper, SalesOrderItem> implements SalesOrderItemService {

    /**
     * 根据订单ID查询订单明细
     *
     * @param orderId 订单ID
     * @return 明细列表
     */
    @Override
    public List<SalesOrderItem> selectItemsByOrderId(Long orderId) {
        LambdaQueryWrapper<SalesOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesOrderItem::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 根据订单ID删除订单明细
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int deleteByOrderId(Long orderId) {
        LambdaQueryWrapper<SalesOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesOrderItem::getOrderId, orderId);
        return baseMapper.delete(queryWrapper);
    }
}