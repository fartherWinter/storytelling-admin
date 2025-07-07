package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.vo.mall.InventoryStatisticsVO;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 库存服务接口
 * @author chennian
 */
public interface InventoryService extends IService<Inventory> {

    /**
     * 分页查询库存列表
     * @param page 分页参数
     * @param inventory 查询条件
     * @return 库存分页数据
     */
    IPage<Inventory> findByPage(PageParam<Inventory> page, Inventory inventory);

    /**
     * 根据ID查询库存
     * @param inventoryId 库存ID
     * @return 库存信息
     */
    Inventory selectInventoryById(Long inventoryId);
    
    /**
     * 根据产品ID查询库存
     * @param productId 产品ID
     * @return 库存信息
     */
    Inventory selectInventoryByProductId(Long productId);

    /**
     * 库存调整
     * @param inventory 库存信息
     * @return 结果
     */
    int adjustInventory(Inventory inventory);

    /**
     * 库存盘点
     * @param inventoryList 库存列表
     * @return 结果
     */
    int stocktakeInventory(List<Inventory> inventoryList);
    
    /**
     * 查询库存预警列表
     * @return 库存预警列表
     */
    List<Inventory> selectWarningInventory();
    
    /**
     * 库存调拨
     * @param inventory 库存信息
     * @return 结果
     */
    int transferInventory(Inventory inventory);
    
    /**
     * 查询库存历史记录
     * @param productId 产品ID
     * @return 库存历史记录
     */
    List<Inventory> selectInventoryHistory(Long productId);
    
    /**
     * 更新库存数量
     * @param productId 产品ID
     * @param quantity 变动数量（正数增加，负数减少）
     * @return 结果
     */
    int updateInventoryQuantity(Long productId, Integer quantity);
    
    /**
     * 获取库存统计数据
     * @return 库存统计数据
     */
    InventoryStatisticsVO getInventoryStatistics();
    
    /**
     * 获取库存预警数据
     * @return 库存预警数据
     */
    Map<String, Object> getInventoryAlerts();
}