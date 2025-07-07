package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 库存数据访问层
 * @author chennian
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    /**
     * 更新产品库存数量（带乐观锁）
     * 
     * @param productId 产品ID
     * @param quantity 变动数量（正数增加，负数减少）
     * @param version 版本号
     * @return 更新成功返回1，否则返回0
     */
    int updateInventoryQuantityWithVersion(@Param("productId") Long productId, 
                                         @Param("quantity") Integer quantity,
                                         @Param("version") Integer version);
    
    /**
     * 锁定产品库存（用于下单）
     * 
     * @param productId 产品ID
     * @param quantity 锁定数量
     * @param orderId 订单ID
     * @param expireTime 锁定过期时间
     * @return 锁定成功返回1，否则返回0
     */
    int lockInventory(@Param("productId") Long productId,
                     @Param("quantity") Integer quantity,
                     @Param("orderId") Long orderId,
                     @Param("expireTime") Date expireTime);
    
    /**
     * 解锁产品库存
     * 
     * @param orderId 订单ID
     * @return 解锁成功的记录数
     */
    int unlockInventory(@Param("orderId") Long orderId);
    
    /**
     * 清理过期的库存锁定
     * 
     * @param now 当前时间
     * @return 清理的记录数
     */
    int cleanExpiredLocks(@Param("now") Date now);
    
    /**
     * 获取产品当前可用库存（总库存 - 锁定库存）
     * 
     * @param productId 产品ID
     * @return 当前可用库存数量
     */
    Integer getAvailableStock(@Param("productId") Long productId);
    
    /**
     * 批量更新产品库存
     * 
     * @param items 库存更新项列表，每项包含productId和quantity
     * @return 更新成功的记录数
     */
    int batchUpdateInventory(@Param("items") List<Inventory> items);
    
    /**
     * 查询库存预警列表（支持分页）
     * 
     * @param page 分页参数
     * @param warningThreshold 预警阈值
     * @return 库存预警列表
     */
    IPage<Inventory> selectWarningInventory(IPage<Inventory> page, @Param("warningThreshold") Integer warningThreshold);
    
    /**
     * 查询库存变动历史记录（支持分页）
     * 
     * @param page 分页参数
     * @param productId 产品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存历史记录列表
     */
    IPage<Inventory> selectInventoryHistory(IPage<Inventory> page,
                                          @Param("productId") Long productId,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime);
    
    /**
     * 插入库存变动记录
     * 
     * @param record 变动记录
     * @return 插入成功返回1
     */
    int insertInventoryRecord(@Param("record") Inventory record);
    
    /**
     * 批量插入库存记录
     * 
     * @param records 记录列表
     * @return 插入成功的记录数
     */
    int batchInsertInventoryRecords(@Param("records") List<Inventory> records);
}