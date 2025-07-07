package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.vo.mall.InventoryStatisticsVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.InventoryMapper;
import com.chennian.storytelling.dao.ProductMapper;
import com.chennian.storytelling.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存服务实现类
 * @author chennian
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private ProductMapper productMapper;

    /**
     * 分页查询库存列表
     */
    @Override
    public IPage<Inventory> findByPage(PageParam<Inventory> page, Inventory inventory) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        if (inventory != null) {
            if (inventory.getProductId() != null) {
                queryWrapper.eq(Inventory::getProductId, inventory.getProductId());
            }
            if (inventory.getWarehouseId() != null) {
                queryWrapper.eq(Inventory::getWarehouseId, inventory.getWarehouseId());
            }
            if (StringUtils.hasText(inventory.getLocationCode())) {
                queryWrapper.like(Inventory::getLocationCode, inventory.getLocationCode());
            }
        }
        // 排序
        queryWrapper.orderByDesc(Inventory::getUpdateTime);
        
        // 执行分页查询
        return inventoryMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
    }

    /**
     * 根据ID查询库存
     */
    @Override
    public Inventory selectInventoryById(Long inventoryId) {
        return inventoryMapper.selectById(inventoryId);
    }
    
    /**
     * 根据产品ID查询库存
     */
    @Override
    public Inventory selectInventoryByProductId(Long productId) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getProductId, productId);
        return inventoryMapper.selectOne(queryWrapper);
    }

    /**
     * 库存调整
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int adjustInventory(Inventory inventory) {
        // 查询当前库存
        Inventory existInventory = selectInventoryByProductId(inventory.getProductId());
        
        if (existInventory != null) {
            // 更新库存数量
            existInventory.setQuantity(inventory.getQuantity());
            existInventory.setUpdateTime(new Date());
            existInventory.setUpdateBy(inventory.getUpdateBy());
            existInventory.setRemark(inventory.getRemark());
            return inventoryMapper.updateById(existInventory);
        } else {
            // 新增库存记录
            inventory.setCreateTime(new Date());
            return inventoryMapper.insert(inventory);
        }
    }

    /**
     * 库存盘点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int stocktakeInventory(List<Inventory> inventoryList) {
        int count = 0;
        for (Inventory inventory : inventoryList) {
            count += adjustInventory(inventory);
        }
        return count;
    }
    
    /**
     * 查询库存预警列表
     */
    @Override
    public List<Inventory> selectWarningInventory() {
        return inventoryMapper.selectWarningInventory();
    }
    
    /**
     * 库存调拨
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transferInventory(Inventory inventory) {
        // 源库存减少
        Inventory sourceInventory = selectInventoryById(inventory.getInventoryId());
        if (sourceInventory != null && sourceInventory.getQuantity() >= inventory.getTransferQuantity()) {
            sourceInventory.setQuantity(sourceInventory.getQuantity() - inventory.getTransferQuantity());
            sourceInventory.setUpdateTime(new Date());
            sourceInventory.setUpdateBy(inventory.getUpdateBy());
            inventoryMapper.updateById(sourceInventory);
            
            // 目标库存增加
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, sourceInventory.getProductId());
            queryWrapper.eq(Inventory::getWarehouseId, inventory.getTargetWarehouseId());
            Inventory targetInventory = inventoryMapper.selectOne(queryWrapper);
            
            if (targetInventory != null) {
                targetInventory.setQuantity(targetInventory.getQuantity() + inventory.getTransferQuantity());
                targetInventory.setUpdateTime(new Date());
                targetInventory.setUpdateBy(inventory.getUpdateBy());
                return inventoryMapper.updateById(targetInventory);
            } else {
                // 目标仓库没有该产品库存记录，创建新记录
                Inventory newInventory = new Inventory();
                newInventory.setProductId(sourceInventory.getProductId());
                newInventory.setWarehouseId(inventory.getTargetWarehouseId());
                newInventory.setQuantity(inventory.getTransferQuantity());
                newInventory.setCreateBy(inventory.getUpdateBy());
                newInventory.setCreateTime(new Date());
                return inventoryMapper.insert(newInventory);
            }
        }
        return 0;
    }
    
    /**
     * 查询库存历史记录
     */
    @Override
    public List<Inventory> selectInventoryHistory(Long productId) {
        return inventoryMapper.selectInventoryHistory(productId);
    }
    
    /**
     * 更新库存数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInventoryQuantity(Long productId, Integer quantity) {
        // 查询产品信息，获取库存预警值
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return 0;
        }
        
        // 查询当前库存
        Inventory inventory = selectInventoryByProductId(productId);
        
        if (inventory != null) {
            // 更新库存数量
            int newStock = inventory.getQuantity() + quantity;
            // 库存不能小于0
            if (newStock < 0) {
                return 0;
            }
            inventory.setQuantity(newStock);
            inventory.setUpdateTime(new Date());
            
            // 检查库存状态
            if (newStock <= product.getMinStock()) {
                // 库存不足
                inventory.setStatus("1");
            } else if (newStock >= product.getMaxStock()) {
                // 库存过多
                inventory.setStatus("2");
            } else {
                // 库存正常
                inventory.setStatus("0");
            }
            
            return inventoryMapper.updateById(inventory);
        } else if (quantity > 0) {
            // 如果库存记录不存在且需要增加库存，则创建新记录
            Inventory newInventory = new Inventory();
            newInventory.setProductId(productId);
            newInventory.setQuantity(quantity);
            newInventory.setCreateTime(new Date());
            
            // 设置库存状态
            if (quantity <= product.getMinStock()) {
                // 库存不足
                newInventory.setStatus("1");
            } else if (quantity >= product.getMaxStock()) {
                // 库存过多
                newInventory.setStatus("2");
            } else {
                // 库存正常
                newInventory.setStatus("0");
            }
            
            return inventoryMapper.insert(newInventory);
        }
        
        return 0;
    }
    
    /**
     * 获取库存统计数据
     * @return 库存统计数据
     */
    @Override
    public InventoryStatisticsVO getInventoryStatistics() {
        InventoryStatisticsVO statisticsVO = new InventoryStatisticsVO();
        
        // 查询所有库存记录
        List<Inventory> inventoryList = list();
        
        // 初始化统计数据
        int totalQuantity = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        Map<String, Integer> statusDistribution = new HashMap<>();
        statusDistribution.put("0", 0); // 正常
        statusDistribution.put("1", 0); // 不足
        statusDistribution.put("2", 0); // 过多
        
        // 统计库存数据
        if (inventoryList != null && !inventoryList.isEmpty()) {
            for (Inventory inventory : inventoryList) {
                // 累计总数量
                totalQuantity += inventory.getQuantity();
                
                // 累计库存状态分布
                String status = inventory.getStatus();
                if (status != null) {
                    statusDistribution.put(status, statusDistribution.getOrDefault(status, 0) + 1);
                }
                
                // 计算库存价值
                Product product = productMapper.selectById(inventory.getProductId());
                if (product != null && product.getPurchasePrice() != null) {
                    BigDecimal itemValue = product.getPurchasePrice().multiply(new BigDecimal(inventory.getQuantity()));
                    totalValue = totalValue.add(itemValue);
                }
            }
        }
        
        // 设置统计结果
        statisticsVO.setTotalQuantity(totalQuantity);
        statisticsVO.setTotalValue(totalValue);
        statisticsVO.setStatusDistribution(statusDistribution);
        statisticsVO.setProductCount(inventoryList.size());
        
        // 计算库存预警商品数量
        int warningCount = statusDistribution.getOrDefault("1", 0);
        statisticsVO.setWarningCount(warningCount);
        
        // 计算平均库存量
        if (!inventoryList.isEmpty()) {
            statisticsVO.setAverageQuantity(totalQuantity / inventoryList.size());
        } else {
            statisticsVO.setAverageQuantity(0);
        }
        
        // 库存周转率暂时设置为默认值，实际项目中应该根据销售数据计算
        statisticsVO.setTurnoverRate(new BigDecimal("4.5"));
        
        return statisticsVO;
    }
    
    /**
     * 获取库存预警数据
     * @return 库存预警数据
     */
    @Override
    public Map<String, Object> getInventoryAlerts() {
        Map<String, Object> result = new HashMap<>();
        
        // 查询库存不足的产品
        LambdaQueryWrapper<Inventory> lowStockQuery = new LambdaQueryWrapper<>();
        lowStockQuery.eq(Inventory::getStatus, "1");
        List<Inventory> lowStockList = list(lowStockQuery);
        
        // 查询库存过多的产品
        LambdaQueryWrapper<Inventory> highStockQuery = new LambdaQueryWrapper<>();
        highStockQuery.eq(Inventory::getStatus, "2");
        List<Inventory> highStockList = list(highStockQuery);
        
        // 处理库存不足的产品数据
        List<Map<String, Object>> lowStockData = new ArrayList<>();
        if (lowStockList != null && !lowStockList.isEmpty()) {
            for (Inventory inventory : lowStockList) {
                Map<String, Object> item = new HashMap<>();
                Product product = productMapper.selectById(inventory.getProductId());
                if (product != null) {
                    item.put("productId", product.getProductId());
                    item.put("productName", product.getProductName());
                    item.put("productCode", product.getProductCode());
                    item.put("currentStock", inventory.getQuantity());
                    item.put("minStock", product.getMinStock());
                    item.put("gap", product.getMinStock() - inventory.getQuantity());
                    lowStockData.add(item);
                }
            }
        }
        
        // 处理库存过多的产品数据
        List<Map<String, Object>> highStockData = new ArrayList<>();
        if (highStockList != null && !highStockList.isEmpty()) {
            for (Inventory inventory : highStockList) {
                Map<String, Object> item = new HashMap<>();
                Product product = productMapper.selectById(inventory.getProductId());
                if (product != null) {
                    item.put("productId", product.getProductId());
                    item.put("productName", product.getProductName());
                    item.put("productCode", product.getProductCode());
                    item.put("currentStock", inventory.getQuantity());
                    item.put("maxStock", product.getMaxStock());
                    item.put("excess", inventory.getQuantity() - product.getMaxStock());
                    highStockData.add(item);
                }
            }
        }
        
        // 设置结果
        result.put("lowStockCount", lowStockData.size());
        result.put("lowStockList", lowStockData);
        result.put("highStockCount", highStockData.size());
        result.put("highStockList", highStockData);
        
        return result;
    }
}