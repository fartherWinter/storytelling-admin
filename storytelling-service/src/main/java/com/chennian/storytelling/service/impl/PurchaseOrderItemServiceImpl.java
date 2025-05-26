package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.PurchaseOrderItem;
import com.chennian.storytelling.dao.PurchaseOrderItemMapper;
import com.chennian.storytelling.service.PurchaseOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 采购订单明细服务实现类
 * @author chennian
 * @date 2023/5/20
 */
@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItem> implements PurchaseOrderItemService {

    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    /**
     * 根据采购订单ID查询明细列表
     *
     * @param orderId 采购订单ID
     * @return 明细列表
     */
    @Override
    public List<PurchaseOrderItem> selectItemsByOrderId(Long orderId) {
        LambdaQueryWrapper<PurchaseOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrderItem::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 新增采购订单明细
     *
     * @param item 采购订单明细信息
     * @return 结果
     */
    @Override
    public int insertOrderItem(PurchaseOrderItem item) {
        return save(item) ? 1 : 0;
    }

    /**
     * 修改采购订单明细
     *
     * @param item 采购订单明细信息
     * @return 结果
     */
    @Override
    public int updateOrderItem(PurchaseOrderItem item) {
        return updateById(item) ? 1 : 0;
    }

    /**
     * 批量删除采购订单明细
     *
     * @param itemIds 需要删除的明细ID数组
     * @return 结果
     */
    @Override
    public int deleteOrderItemByIds(Long[] itemIds) {
        return purchaseOrderItemMapper.deleteByIds(Arrays.asList(itemIds));
    }

    /**
     * 批量新增采购订单明细
     *
     * @param items 采购订单明细列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertOrderItems(List<PurchaseOrderItem> items) {
        if (items != null && !items.isEmpty()) {
            saveBatch(items);
            return items.size();
        }
        return 0;
    }

    /**
     * 根据采购订单ID删除明细
     *
     * @param orderId 采购订单ID
     * @return 结果
     */
    @Override
    public int deleteOrderItemByOrderId(Long orderId) {
        LambdaQueryWrapper<PurchaseOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrderItem::getOrderId, orderId);
        return purchaseOrderItemMapper.delete(queryWrapper);
    }
}