package com.chennian.storytelling.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.common.util.SecurityUtils;
import com.chennian.storytelling.inventory.entity.Inventory;
import com.chennian.storytelling.inventory.entity.InventoryRecord;
import com.chennian.storytelling.inventory.mapper.InventoryMapper;
import com.chennian.storytelling.inventory.service.InventoryLogService;
import com.chennian.storytelling.inventory.service.InventoryRecordService;
import com.chennian.storytelling.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryRecordService inventoryRecordService;
    private final InventoryLogService inventoryLogService;

    @Override
    public Inventory getInventoryById(Long inventoryId) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        return inventoryMapper.selectById(inventoryId);
    }

    @Override
    public Inventory getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            throw new BusinessException("商品ID和仓库ID不能为空");
        }
        return inventoryMapper.selectInventoryByProductAndWarehouse(productId, warehouseId);
    }

    @Override
    public Inventory getInventoryBySkuCode(String skuCode) {
        if (!StringUtils.hasText(skuCode)) {
            throw new BusinessException("SKU编码不能为空");
        }
        return inventoryMapper.selectInventoryBySkuCode(skuCode);
    }

    @Override
    public IPage<Inventory> getInventoryPage(Page<Inventory> page, Long productId, Long warehouseId,
                                           String skuCode, String productName, Integer status,
                                           Boolean enableWarning, LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryMapper.selectInventoryPage(page, productId, warehouseId, skuCode, productName,
                status, enableWarning, startTime, endTime);
    }

    @Override
    public List<Inventory> getInventoryListByProductId(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        return inventoryMapper.selectInventoryListByProductId(productId);
    }

    @Override
    public List<Inventory> getInventoryListByWarehouseId(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        return inventoryMapper.selectInventoryListByWarehouseId(warehouseId);
    }

    @Override
    public List<Inventory> getLowStockInventoryList(Integer threshold) {
        return inventoryMapper.selectLowStockInventoryList(threshold);
    }

    @Override
    public List<Inventory> getOutOfStockInventoryList() {
        return inventoryMapper.selectOutOfStockInventoryList();
    }

    @Override
    public List<Inventory> getInventoryWarningList() {
        return inventoryMapper.selectInventoryWarningList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventory(Inventory inventory) {
        if (inventory == null) {
            throw new BusinessException("库存信息不能为空");
        }
        
        // 校验必填字段
        validateInventory(inventory);
        
        // 检查SKU编码是否已存在
        if (checkSkuCodeExists(inventory.getSkuCode())) {
            throw new BusinessException("SKU编码已存在");
        }
        
        // 检查商品和仓库组合是否已存在
        if (checkProductWarehouseExists(inventory.getProductId(), inventory.getWarehouseId())) {
            throw new BusinessException("该商品在此仓库中已有库存记录");
        }
        
        // 设置创建信息
        inventory.setCreateTime(LocalDateTime.now());
        inventory.setCreateBy(SecurityUtils.getCurrentUserId());
        inventory.setUpdateTime(LocalDateTime.now());
        inventory.setUpdateBy(SecurityUtils.getCurrentUserId());
        inventory.setDeleted(false);
        inventory.setVersion(1L);
        
        boolean result = inventoryMapper.insert(inventory) > 0;
        
        if (result) {
            // 记录库存变动
            recordInventoryChange(inventory.getId(), inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), 1, "创建库存", BigDecimal.ZERO, inventory.getCurrentStock(),
                    inventory.getCurrentStock(), null, null);
            
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventory.getId(), 1, "创建库存",
                    null, inventory.toString(), 1, null, null, null, 0L, "创建库存记录");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInventory(Inventory inventory) {
        if (inventory == null || inventory.getId() == null) {
            throw new BusinessException("库存信息或ID不能为空");
        }
        
        // 获取原库存信息
        Inventory oldInventory = inventoryMapper.selectById(inventory.getId());
        if (oldInventory == null) {
            throw new BusinessException("库存记录不存在");
        }
        
        // 校验必填字段
        validateInventory(inventory);
        
        // 检查SKU编码是否重复（排除自己）
        if (!oldInventory.getSkuCode().equals(inventory.getSkuCode()) && checkSkuCodeExists(inventory.getSkuCode())) {
            throw new BusinessException("SKU编码已存在");
        }
        
        // 设置更新信息
        inventory.setUpdateTime(LocalDateTime.now());
        inventory.setUpdateBy(SecurityUtils.getCurrentUserId());
        inventory.setVersion(oldInventory.getVersion() + 1);
        
        boolean result = inventoryMapper.updateById(inventory) > 0;
        
        if (result) {
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventory.getId(), 2, "更新库存",
                    oldInventory.toString(), inventory.toString(), 1, null, null, null, 0L, "更新库存信息");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventory(Long inventoryId) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new BusinessException("库存记录不存在");
        }
        
        // 检查是否有库存
        if (inventory.getCurrentStock().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("库存数量大于0，不能删除");
        }
        
        // 逻辑删除
        inventory.setDeleted(true);
        inventory.setUpdateTime(LocalDateTime.now());
        inventory.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = inventoryMapper.updateById(inventory) > 0;
        
        if (result) {
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventoryId, 3, "删除库存",
                    inventory.toString(), null, 1, null, null, null, 0L, "删除库存记录");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInventory(List<Long> inventoryIds) {
        if (CollectionUtils.isEmpty(inventoryIds)) {
            throw new BusinessException("库存ID列表不能为空");
        }
        
        for (Long inventoryId : inventoryIds) {
            deleteInventory(inventoryId);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableInventory(Long inventoryId) {
        return updateInventoryStatus(inventoryId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableInventory(Long inventoryId) {
        return updateInventoryStatus(inventoryId, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateInventoryStatus(List<Long> inventoryIds, Integer status) {
        if (CollectionUtils.isEmpty(inventoryIds) || status == null) {
            throw new BusinessException("库存ID列表和状态不能为空");
        }
        
        return inventoryMapper.batchUpdateInventoryStatus(inventoryIds, status, SecurityUtils.getCurrentUserId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inStock(Long inventoryId, BigDecimal quantity, String reason, String businessNo, Integer businessType) {
        return updateStock(inventoryId, quantity, reason, businessNo, businessType, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean outStock(Long inventoryId, BigDecimal quantity, String reason, String businessNo, Integer businessType) {
        return updateStock(inventoryId, quantity.negate(), reason, businessNo, businessType, 2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInStock(List<Map<String, Object>> stockList) {
        if (CollectionUtils.isEmpty(stockList)) {
            throw new BusinessException("入库列表不能为空");
        }
        
        for (Map<String, Object> stockInfo : stockList) {
            Long inventoryId = (Long) stockInfo.get("inventoryId");
            BigDecimal quantity = (BigDecimal) stockInfo.get("quantity");
            String reason = (String) stockInfo.get("reason");
            String businessNo = (String) stockInfo.get("businessNo");
            Integer businessType = (Integer) stockInfo.get("businessType");
            
            inStock(inventoryId, quantity, reason, businessNo, businessType);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchOutStock(List<Map<String, Object>> stockList) {
        if (CollectionUtils.isEmpty(stockList)) {
            throw new BusinessException("出库列表不能为空");
        }
        
        for (Map<String, Object> stockInfo : stockList) {
            Long inventoryId = (Long) stockInfo.get("inventoryId");
            BigDecimal quantity = (BigDecimal) stockInfo.get("quantity");
            String reason = (String) stockInfo.get("reason");
            String businessNo = (String) stockInfo.get("businessNo");
            Integer businessType = (Integer) stockInfo.get("businessType");
            
            outStock(inventoryId, quantity, reason, businessNo, businessType);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockStock(Long inventoryId, BigDecimal quantity, String businessNo, Integer businessType) {
        if (inventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("库存ID和锁定数量不能为空且必须大于0");
        }
        
        boolean result = inventoryMapper.lockStock(inventoryId, quantity) > 0;
        
        if (result) {
            Inventory inventory = inventoryMapper.selectById(inventoryId);
            // 记录库存变动
            recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), 5, "锁定库存", BigDecimal.ZERO, BigDecimal.ZERO,
                    quantity, businessNo, businessType);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockStock(Long inventoryId, BigDecimal quantity, String businessNo, Integer businessType) {
        if (inventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("库存ID和解锁数量不能为空且必须大于0");
        }
        
        boolean result = inventoryMapper.unlockStock(inventoryId, quantity) > 0;
        
        if (result) {
            Inventory inventory = inventoryMapper.selectById(inventoryId);
            // 记录库存变动
            recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), 6, "解锁库存", BigDecimal.ZERO, BigDecimal.ZERO,
                    quantity.negate(), businessNo, businessType);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reserveStock(Long inventoryId, BigDecimal quantity, String businessNo, Integer businessType) {
        if (inventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("库存ID和预占数量不能为空且必须大于0");
        }
        
        boolean result = inventoryMapper.reserveStock(inventoryId, quantity) > 0;
        
        if (result) {
            Inventory inventory = inventoryMapper.selectById(inventoryId);
            // 记录库存变动
            recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), 7, "预占库存", BigDecimal.ZERO, BigDecimal.ZERO,
                    quantity, businessNo, businessType);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean releaseReservedStock(Long inventoryId, BigDecimal quantity, String businessNo, Integer businessType) {
        if (inventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("库存ID和释放数量不能为空且必须大于0");
        }
        
        boolean result = inventoryMapper.releaseReservedStock(inventoryId, quantity) > 0;
        
        if (result) {
            Inventory inventory = inventoryMapper.selectById(inventoryId);
            // 记录库存变动
            recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), 8, "释放预占库存", BigDecimal.ZERO, BigDecimal.ZERO,
                    quantity.negate(), businessNo, businessType);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferStock(Long fromInventoryId, Long toInventoryId, BigDecimal quantity,
                               String reason, String businessNo, Integer businessType) {
        if (fromInventoryId == null || toInventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("调拨参数不能为空且数量必须大于0");
        }
        
        // 出库
        outStock(fromInventoryId, quantity, reason, businessNo, businessType);
        
        // 入库
        inStock(toInventoryId, quantity, reason, businessNo, businessType);
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inventoryCheck(Long inventoryId, BigDecimal actualQuantity, String reason, String businessNo) {
        if (inventoryId == null || actualQuantity == null || actualQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("库存ID和实际数量不能为空且数量不能小于0");
        }
        
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new BusinessException("库存记录不存在");
        }
        
        BigDecimal difference = actualQuantity.subtract(inventory.getCurrentStock());
        
        if (difference.compareTo(BigDecimal.ZERO) != 0) {
            // 更新库存数量
            inventory.setCurrentStock(actualQuantity);
            inventory.setAvailableStock(actualQuantity.subtract(inventory.getLockedStock()).subtract(inventory.getReservedStock()));
            inventory.setLastCheckTime(LocalDateTime.now());
            inventory.setUpdateTime(LocalDateTime.now());
            inventory.setUpdateBy(SecurityUtils.getCurrentUserId());
            
            boolean result = inventoryMapper.updateById(inventory) > 0;
            
            if (result) {
                // 记录库存变动
                recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                        inventory.getSkuCode(), 4, "盘点调整", inventory.getCurrentStock().subtract(difference),
                        actualQuantity, difference, businessNo, 4);
            }
            
            return result;
        }
        
        return true;
    }

    @Override
    public boolean checkSkuCodeExists(String skuCode) {
        if (!StringUtils.hasText(skuCode)) {
            return false;
        }
        return inventoryMapper.checkSkuCodeExists(skuCode) > 0;
    }

    @Override
    public boolean checkProductWarehouseExists(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            return false;
        }
        return inventoryMapper.checkProductWarehouseExists(productId, warehouseId) > 0;
    }

    @Override
    public BigDecimal getProductTotalStock(Long productId) {
        if (productId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalStock = inventoryMapper.getProductTotalStock(productId);
        return totalStock != null ? totalStock : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getProductAvailableStock(Long productId) {
        if (productId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal availableStock = inventoryMapper.getProductAvailableStock(productId);
        return availableStock != null ? availableStock : BigDecimal.ZERO;
    }

    @Override
    public Map<String, Object> getInventoryStatistics() {
        return inventoryMapper.getInventoryStatistics();
    }

    /**
     * 校验库存信息
     */
    private void validateInventory(Inventory inventory) {
        if (inventory.getProductId() == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (inventory.getWarehouseId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        if (!StringUtils.hasText(inventory.getSkuCode())) {
            throw new BusinessException("SKU编码不能为空");
        }
        if (!StringUtils.hasText(inventory.getProductName())) {
            throw new BusinessException("商品名称不能为空");
        }
        if (inventory.getCurrentStock() == null || inventory.getCurrentStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("当前库存数量不能为空且不能小于0");
        }
        if (inventory.getAvailableStock() == null || inventory.getAvailableStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("可用库存数量不能为空且不能小于0");
        }
        if (inventory.getLockedStock() == null || inventory.getLockedStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("锁定库存数量不能为空且不能小于0");
        }
        if (inventory.getReservedStock() == null || inventory.getReservedStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("预占库存数量不能为空且不能小于0");
        }
        if (inventory.getSafetyStock() == null || inventory.getSafetyStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("安全库存数量不能为空且不能小于0");
        }
        if (inventory.getMinStock() == null || inventory.getMinStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("最小库存数量不能为空且不能小于0");
        }
        if (inventory.getMaxStock() == null || inventory.getMaxStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("最大库存数量不能为空且不能小于0");
        }
        if (inventory.getCostPrice() == null || inventory.getCostPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("成本价不能为空且不能小于0");
        }
    }

    /**
     * 更新库存状态
     */
    private boolean updateInventoryStatus(Long inventoryId, Integer status) {
        if (inventoryId == null || status == null) {
            throw new BusinessException("库存ID和状态不能为空");
        }
        
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new BusinessException("库存记录不存在");
        }
        
        inventory.setStatus(status);
        inventory.setUpdateTime(LocalDateTime.now());
        inventory.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = inventoryMapper.updateById(inventory) > 0;
        
        if (result) {
            String operation = status == 1 ? "启用库存" : "禁用库存";
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventoryId, 4, operation,
                    String.valueOf(inventory.getStatus() == 1 ? 0 : 1), String.valueOf(status),
                    1, null, null, null, 0L, operation);
        }
        
        return result;
    }

    /**
     * 更新库存数量
     */
    private boolean updateStock(Long inventoryId, BigDecimal quantity, String reason,
                              String businessNo, Integer businessType, Integer changeType) {
        if (inventoryId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("库存ID和变动数量不能为空且数量不能为0");
        }
        
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new BusinessException("库存记录不存在");
        }
        
        // 检查库存状态
        if (inventory.getStatus() != 1) {
            throw new BusinessException("库存状态异常，不能进行库存操作");
        }
        
        BigDecimal beforeStock = inventory.getCurrentStock();
        BigDecimal afterStock = beforeStock.add(quantity);
        
        // 出库时检查库存是否充足
        if (quantity.compareTo(BigDecimal.ZERO) < 0 && afterStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("库存不足，当前库存：" + beforeStock + "，出库数量：" + quantity.abs());
        }
        
        // 更新库存数量
        boolean result;
        if (quantity.compareTo(BigDecimal.ZERO) > 0) {
            result = inventoryMapper.increaseStock(inventoryId, quantity) > 0;
        } else {
            result = inventoryMapper.decreaseStock(inventoryId, quantity.abs()) > 0;
        }
        
        if (result) {
            // 记录库存变动
            recordInventoryChange(inventoryId, inventory.getProductId(), inventory.getWarehouseId(),
                    inventory.getSkuCode(), changeType, reason, beforeStock, afterStock,
                    quantity, businessNo, businessType);
        }
        
        return result;
    }

    /**
     * 记录库存变动
     */
    private void recordInventoryChange(Long inventoryId, Long productId, Long warehouseId, String skuCode,
                                     Integer changeType, String changeReason, BigDecimal beforeStock,
                                     BigDecimal afterStock, BigDecimal changeQuantity, String businessNo, Integer businessType) {
        InventoryRecord record = new InventoryRecord();
        record.setInventoryId(inventoryId);
        record.setProductId(productId);
        record.setWarehouseId(warehouseId);
        record.setSkuCode(skuCode);
        record.setChangeType(changeType);
        record.setChangeReason(changeReason);
        record.setBeforeStock(beforeStock);
        record.setAfterStock(afterStock);
        record.setChangeQuantity(changeQuantity);
        record.setBusinessNo(businessNo);
        record.setBusinessType(businessType);
        record.setOperatorId(SecurityUtils.getCurrentUserId());
        record.setOperatorName(SecurityUtils.getCurrentUsername());
        record.setOperationTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        record.setCreateBy(SecurityUtils.getCurrentUserId());
        
        inventoryRecordService.createInventoryRecord(record);
    }
}