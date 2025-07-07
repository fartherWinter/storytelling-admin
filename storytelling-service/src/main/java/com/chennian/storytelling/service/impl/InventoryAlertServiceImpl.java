package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.InventoryAlert;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.model.Warehouse;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.InventoryAlertMapper;
import com.chennian.storytelling.dao.InventoryMapper;
import com.chennian.storytelling.dao.ProductMapper;
import com.chennian.storytelling.dao.WarehouseMapper;
import com.chennian.storytelling.service.InventoryAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存预警服务实现类
 */
@Service
public class InventoryAlertServiceImpl extends ServiceImpl<InventoryAlertMapper, InventoryAlert> implements InventoryAlertService {

    @Autowired
    private InventoryAlertMapper inventoryAlertMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public IPage<InventoryAlert> getInventoryAlertPage(Integer pageNum, Integer pageSize, Integer alertType, Integer alertLevel, Integer status, Long warehouseId) {
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        
        if (alertType != null) {
            queryWrapper.eq(InventoryAlert::getAlertType, alertType);
        }
        if (alertLevel != null) {
            queryWrapper.eq(InventoryAlert::getAlertLevel, alertLevel);
        }
        if (status != null) {
            queryWrapper.eq(InventoryAlert::getStatus, status);
        }
        if (warehouseId != null) {
            queryWrapper.eq(InventoryAlert::getWarehouseId, warehouseId);
        }
        
        queryWrapper.orderByDesc(InventoryAlert::getAlertLevel)
                   .orderByDesc(InventoryAlert::getCreateTime);
        
        IPage<InventoryAlert> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        
        return page;
    }

    @Override
    public List<InventoryAlert> getUnhandledAlerts() {
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryAlert::getStatus, 0)
                   .orderByDesc(InventoryAlert::getAlertLevel)
                   .orderByDesc(InventoryAlert::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public List<InventoryAlert> getHighPriorityAlerts() {
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryAlert::getStatus, 0)
                   .in(InventoryAlert::getAlertLevel, 3, 4) // 高级和紧急
                   .orderByDesc(InventoryAlert::getAlertLevel)
                   .orderByDesc(InventoryAlert::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int checkInventoryAndGenerateAlerts(Long productId, Long warehouseId) {
        int alertCount = 0;
        
        // 构建查询条件
        LambdaQueryWrapper<Inventory> inventoryQuery = new LambdaQueryWrapper<>();
        if (productId != null) {
            inventoryQuery.eq(Inventory::getProductId, productId);
        }
        if (warehouseId != null) {
            inventoryQuery.eq(Inventory::getWarehouseId, warehouseId);
        }
        
        List<Inventory> inventoryList = inventoryMapper.selectList(inventoryQuery);
        
        for (Inventory inventory : inventoryList) {
            Product product = productMapper.selectById(inventory.getProductId());
            if (product == null) {
                continue;
            }
            
            // 检查是否已存在相同的预警
            if (hasExistingAlert(inventory.getProductId(), inventory.getWarehouseId())) {
                continue;
            }
            
            Integer currentStock = inventory.getQuantity();
            Integer minStock = product.getMinStock();
            Integer maxStock = product.getMaxStock();
            
            // 检查库存不足
            if (minStock != null && currentStock <= minStock) {
                int alertLevel = calculateAlertLevel(currentStock, minStock, true);
                if (createInventoryAlert(inventory.getProductId(), inventory.getWarehouseId(), 
                    currentStock, minStock, maxStock, 1, alertLevel)) {
                    alertCount++;
                }
            }
            // 检查库存过多
            else if (maxStock != null && currentStock >= maxStock) {
                int alertLevel = calculateAlertLevel(currentStock, maxStock, false);
                if (createInventoryAlert(inventory.getProductId(), inventory.getWarehouseId(), 
                    currentStock, minStock, maxStock, 2, alertLevel)) {
                    alertCount++;
                }
            }
        }
        
        return alertCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventoryAlert(Long productId, Long warehouseId, Integer currentStock, Integer minStockThreshold, Integer maxStockThreshold, Integer alertType, Integer alertLevel) {
        Product product = productMapper.selectById(productId);
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        
        if (product == null || warehouse == null) {
            return false;
        }
        
        InventoryAlert alert = new InventoryAlert();
        alert.setProductId(productId);
        alert.setProductName(product.getProductName());
        alert.setProductCode(product.getProductCode());
        alert.setWarehouseId(warehouseId);
        alert.setWarehouseName(warehouse.getWarehouseName());
        alert.setCurrentStock(currentStock);
        alert.setMinStockThreshold(minStockThreshold);
        alert.setMaxStockThreshold(maxStockThreshold);
        alert.setAlertType(alertType);
        alert.setAlertLevel(alertLevel);
        alert.setStatus(0); // 未处理
        alert.setCreateTime(LocalDateTime.now());
        alert.setUpdateTime(LocalDateTime.now());
        
        // 生成预警消息和建议操作
        generateAlertMessage(alert);
        
        return save(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleAlert(Long alertId, Long handledBy, String handledRemark) {
        InventoryAlert alert = getById(alertId);
        if (alert == null) {
            return false;
        }
        
        alert.setStatus(1); // 已处理
        alert.setHandledBy(handledBy);
        alert.setHandledTime(LocalDateTime.now());
        alert.setHandledRemark(handledRemark);
        alert.setUpdateTime(LocalDateTime.now());
        
        return updateById(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ignoreAlert(Long alertId, Long handledBy, String handledRemark) {
        InventoryAlert alert = getById(alertId);
        if (alert == null) {
            return false;
        }
        
        alert.setStatus(2); // 已忽略
        alert.setHandledBy(handledBy);
        alert.setHandledTime(LocalDateTime.now());
        alert.setHandledRemark(handledRemark);
        alert.setUpdateTime(LocalDateTime.now());
        
        return updateById(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchHandleAlerts(List<Long> alertIds, Long handledBy, String handledRemark) {
        int successCount = 0;
        for (Long alertId : alertIds) {
            if (handleAlert(alertId, handledBy, handledRemark)) {
                successCount++;
            }
        }
        return successCount;
    }

    @Override
    public Object getAlertStatistics(Long warehouseId) {
        Map<String, Object> statistics = new HashMap<>();
        
        LambdaQueryWrapper<InventoryAlert> baseQuery = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            baseQuery.eq(InventoryAlert::getWarehouseId, warehouseId);
        }
        
        // 总预警数
        statistics.put("totalAlerts", count(baseQuery));
        
        // 未处理预警数
        LambdaQueryWrapper<InventoryAlert> unhandledQuery = baseQuery.clone();
        unhandledQuery.eq(InventoryAlert::getStatus, 0);
        statistics.put("unhandledAlerts", count(unhandledQuery));
        
        // 按预警类型统计
        Map<String, Long> alertTypeStats = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            LambdaQueryWrapper<InventoryAlert> typeQuery = baseQuery.clone();
            typeQuery.eq(InventoryAlert::getAlertType, i);
            alertTypeStats.put("type" + i, count(typeQuery));
        }
        statistics.put("alertTypeStats", alertTypeStats);
        
        // 按预警级别统计
        Map<String, Long> alertLevelStats = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            LambdaQueryWrapper<InventoryAlert> levelQuery = baseQuery.clone();
            levelQuery.eq(InventoryAlert::getAlertLevel, i);
            alertLevelStats.put("level" + i, count(levelQuery));
        }
        statistics.put("alertLevelStats", alertLevelStats);
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanupExpiredAlerts(int days) {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(days);
        
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(InventoryAlert::getStatus, 1, 2) // 已处理或已忽略
                   .lt(InventoryAlert::getHandledTime, expiredTime);
        
        List<InventoryAlert> expiredAlerts = list(queryWrapper);
        if (!expiredAlerts.isEmpty()) {
            removeBatchByIds(expiredAlerts.stream().map(InventoryAlert::getId).toList());
        }
        
        return expiredAlerts.size();
    }

    @Override
    public boolean sendAlertNotification(Long alertId) {
        InventoryAlert alert = getById(alertId);
        if (alert == null || alert.getStatus() != 0) {
            return false;
        }
        
        try {
            // 这里应该实现具体的通知逻辑，比如发送邮件、短信、系统消息等
            // 示例：发送邮件通知
            // emailService.sendInventoryAlert(alert);
            
            // 示例：发送系统消息
            // messageService.sendSystemMessage(alert);
            
            return true;
        } catch (Exception e) {
            // 记录日志
            return false;
        }
    }

    @Override
    public int batchSendAlertNotifications(Integer alertLevel) {
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryAlert::getStatus, 0);
        if (alertLevel != null) {
            queryWrapper.eq(InventoryAlert::getAlertLevel, alertLevel);
        }
        
        List<InventoryAlert> alerts = list(queryWrapper);
        int successCount = 0;
        
        for (InventoryAlert alert : alerts) {
            if (sendAlertNotification(alert.getId())) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 检查是否已存在相同的预警
     */
    private boolean hasExistingAlert(Long productId, Long warehouseId) {
        LambdaQueryWrapper<InventoryAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryAlert::getProductId, productId)
                   .eq(InventoryAlert::getWarehouseId, warehouseId)
                   .eq(InventoryAlert::getStatus, 0);
        return count(queryWrapper) > 0;
    }
    
    /**
     * 计算预警级别
     */
    private int calculateAlertLevel(Integer currentStock, Integer threshold, boolean isLowStock) {
        if (isLowStock) {
            // 库存不足的情况
            if (currentStock == 0) {
                return 4; // 紧急：零库存
            } else if (currentStock <= threshold * 0.3) {
                return 3; // 高：库存极低
            } else if (currentStock <= threshold * 0.6) {
                return 2; // 中：库存偏低
            } else {
                return 1; // 低：接近最低库存
            }
        } else {
            // 库存过多的情况
            if (currentStock >= threshold * 2) {
                return 3; // 高：库存严重过多
            } else if (currentStock >= threshold * 1.5) {
                return 2; // 中：库存过多
            } else {
                return 1; // 低：库存稍多
            }
        }
    }
    
    /**
     * 生成预警消息和建议操作
     */
    private void generateAlertMessage(InventoryAlert alert) {
        String message;
        String suggestedAction;
        
        switch (alert.getAlertType()) {
            case 1: // 库存不足
                message = String.format("产品 %s 在仓库 %s 的库存不足，当前库存：%d，最低库存：%d", 
                    alert.getProductName(), alert.getWarehouseName(), 
                    alert.getCurrentStock(), alert.getMinStockThreshold());
                suggestedAction = "建议及时补货，避免缺货影响销售";
                break;
            case 2: // 库存过多
                message = String.format("产品 %s 在仓库 %s 的库存过多，当前库存：%d，最高库存：%d", 
                    alert.getProductName(), alert.getWarehouseName(), 
                    alert.getCurrentStock(), alert.getMaxStockThreshold());
                suggestedAction = "建议调整采购计划或促销处理";
                break;
            default:
                message = "库存异常";
                suggestedAction = "请检查库存状态";
        }
        
        alert.setAlertMessage(message);
        alert.setSuggestedAction(suggestedAction);
    }

    // /**
    //  * 分页查询库存预警（控制器调用的方法名）
    //  */
    // public PageParam<InventoryAlert> getInventoryAlertPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId, Integer alertType, Integer alertLevel, Integer status) {
    //     return getInventoryAlerts(pageNum, pageSize, productId, warehouseId, alertType, alertLevel, status);
    // }

    // /**
    //  * 获取未处理的预警（控制器调用的方法名）
    //  */
    // public List<InventoryAlert> getUnhandledAlerts(Long warehouseId, Integer limit) {
    //     return getUnhandledInventoryAlerts(warehouseId, limit);
    // }

    // /**
    //  * 获取高优先级预警（控制器调用的方法名）
    //  */
    // public List<InventoryAlert> getHighPriorityAlerts(Long warehouseId, Integer limit) {
    //     return getHighPriorityInventoryAlerts(warehouseId, limit);
    // }

    /**
     * 创建库存预警（控制器调用的方法名）
     */
    public boolean createInventoryAlert(InventoryAlert alert) {
        return save(alert);
    }

    /**
     * 清理过期预警（控制器调用的方法名）
     */
    public int cleanupExpiredAlerts(Integer days) {
        return cleanupExpiredAlerts(days.intValue());
    }

    /**
     * 批量发送预警通知（控制器调用的方法名）
     */
    public int batchSendAlertNotifications(List<Long> alertIds) {
        int successCount = 0;
        for (Long alertId : alertIds) {
            if (sendAlertNotification(alertId)) {
                successCount++;
            }
        }
        return successCount;
    }
}