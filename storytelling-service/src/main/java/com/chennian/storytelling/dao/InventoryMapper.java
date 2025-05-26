package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存数据访问层
 * @author chennian
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    /**
     * 更新产品库存数量
     * 
     * @param productId 产品ID
     * @param quantity 变动数量（正数增加，负数减少）
     * @return 结果
     */
    int updateInventoryQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    /**
     * 获取产品当前库存
     * 
     * @param productId 产品ID
     * @return 当前库存数量
     */
    Integer getCurrentStock(@Param("productId") Long productId);
    
    /**
     * 查询库存预警列表
     * 
     * @return 库存预警列表
     */
    List<Inventory> selectWarningInventory();
    
    /**
     * 查询库存历史记录
     * 
     * @param productId 产品ID
     * @return 库存历史记录列表
     */
    List<Inventory> selectInventoryHistory(@Param("productId") Long productId);
}