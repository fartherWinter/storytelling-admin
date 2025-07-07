package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.InventoryLog;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.model.Warehouse;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.InventoryLogMapper;
import com.chennian.storytelling.dao.InventoryMapper;
import com.chennian.storytelling.dao.ProductMapper;
import com.chennian.storytelling.dao.WarehouseMapper;
import com.chennian.storytelling.service.EnhancedInventoryService;
import com.chennian.storytelling.service.InventoryAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 增强库存服务实现类
 * 包含分布式锁、库存同步等高级功能
 */
@Service
public class EnhancedInventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements EnhancedInventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private InventoryLogMapper inventoryLogMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    @Autowired
    private InventoryAlertService inventoryAlertService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String INVENTORY_LOCK_PREFIX = "inventory:lock:";
    private static final String INVENTORY_SYNC_LOCK_PREFIX = "inventory:sync:lock:";
    private static final int DEFAULT_LOCK_TIMEOUT = 30; // 秒
    private static final int DEFAULT_WAIT_TIME = 10; // 秒

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean safeReduceStock(Long productId, Long warehouseId, Integer quantity, String orderSn, Long operatorId) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        
        try {
            // 获取分布式锁
            if (!acquireInventoryLock(lockKey, DEFAULT_LOCK_TIMEOUT)) {
                return false;
            }
            
            // 查询当前库存
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId)
                       .eq(Inventory::getWarehouseId, warehouseId);
            Inventory inventory = getOne(queryWrapper);
            
            if (inventory == null || inventory.getQuantity() < quantity) {
                return false; // 库存不足
            }
            
            // 扣减库存
            Integer oldQuantity = inventory.getQuantity();
            inventory.setQuantity(oldQuantity - quantity);
            inventory.setUpdateTime(new Date());
            
            boolean success = updateById(inventory);
            
            if (success) {
                // 记录库存操作日志
                recordInventoryLog(productId, warehouseId, "STOCK_REDUCE", 
                    oldQuantity, inventory.getQuantity(), -quantity, 
                    orderSn, "库存扣减", "订单扣减库存", operatorId);
                
                // 检查库存预警
                inventoryAlertService.checkInventoryAndGenerateAlerts(productId, warehouseId);
            }
            
            return success;
            
        } finally {
            // 释放分布式锁
            releaseInventoryLockByKey(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean safeIncreaseStock(Long productId, Long warehouseId, Integer quantity, Integer documentType, String documentNumber, Long operatorId) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        
        try {
            // 获取分布式锁
            if (!acquireInventoryLock(lockKey, DEFAULT_LOCK_TIMEOUT)) {
                return false;
            }
            
            // 查询当前库存
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId)
                       .eq(Inventory::getWarehouseId, warehouseId);
            Inventory inventory = getOne(queryWrapper);
            
            Integer oldQuantity = 0;
            if (inventory == null) {
                // 创建新的库存记录
                inventory = new Inventory();
                inventory.setProductId(productId);
                inventory.setWarehouseId(warehouseId);
                inventory.setQuantity(quantity);
                inventory.setCreateTime(new Date());
                inventory.setUpdateTime(new Date());
                save(inventory);
            } else {
                // 增加库存
                oldQuantity = inventory.getQuantity();
                inventory.setQuantity(oldQuantity + quantity);
                inventory.setUpdateTime(new Date());
                updateById(inventory);
            }
            
            // 记录库存操作日志
            recordInventoryLog(productId, warehouseId, "STOCK_INCREASE", 
                oldQuantity, inventory.getQuantity(), quantity, 
                documentNumber, "库存增加", "单据类型:" + documentType, operatorId);
            
            // 检查库存预警
            inventoryAlertService.checkInventoryAndGenerateAlerts(productId, warehouseId);
            
            return true;
            
        } finally {
            // 释放分布式锁
            releaseInventoryLockByKey(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSafeStockOperation(List<StockOperation> operations) {
        try {
            // 按产品和仓库分组，避免死锁
            Map<String, List<StockOperation>> groupedOps = operations.stream()
                .collect(Collectors.groupingBy(op -> op.getProductId() + ":" + op.getWarehouseId()));
            
            for (Map.Entry<String, List<StockOperation>> entry : groupedOps.entrySet()) {
                List<StockOperation> ops = entry.getValue();
                
                // 计算总的库存变化量
                int totalChange = ops.stream().mapToInt(StockOperation::getQuantity).sum();
                
                StockOperation firstOp = ops.get(0);
                boolean success;
                
                if (totalChange > 0) {
                    success = safeIncreaseStock(firstOp.getProductId(), firstOp.getWarehouseId(), 
                        totalChange, firstOp.getOperationType(), firstOp.getDocumentNumber(), firstOp.getOperatorId());
                } else if (totalChange < 0) {
                    success = safeReduceStock(firstOp.getProductId(), firstOp.getWarehouseId(), 
                        Math.abs(totalChange), firstOp.getDocumentNumber(), firstOp.getOperatorId());
                } else {
                    success = true; // 无变化
                }
                
                if (!success) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncInventoryFromExternalSystem(Long productId, Long warehouseId) {
        String lockKey = INVENTORY_SYNC_LOCK_PREFIX + productId + ":" + warehouseId;
        
        try {
            // 获取同步锁
            if (!acquireInventoryLock(lockKey, DEFAULT_LOCK_TIMEOUT)) {
                return false;
            }
            
            // 模拟从外部系统获取库存数据
            Integer externalQuantity = getExternalSystemStock(productId, warehouseId);
            if (externalQuantity == null) {
                return false;
            }
            
            // 查询当前库存
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId)
                       .eq(Inventory::getWarehouseId, warehouseId);
            Inventory inventory = getOne(queryWrapper);
            
            Integer oldQuantity = 0;
            if (inventory != null) {
                oldQuantity = inventory.getQuantity();
            }
            
            if (inventory == null) {
                // 创建新的库存记录
                inventory = new Inventory();
                inventory.setProductId(productId);
                inventory.setWarehouseId(warehouseId);
                inventory.setQuantity(externalQuantity);
                inventory.setCreateTime(new Date());
                inventory.setUpdateTime(new Date());
                save(inventory);
            } else {
                // 更新库存
                inventory.setQuantity(externalQuantity);
                inventory.setUpdateTime(new Date());
                updateById(inventory);
            }
            
            // 记录同步日志
            recordInventoryLog(productId, warehouseId, "EXTERNAL_SYNC", 
                oldQuantity, externalQuantity, externalQuantity - oldQuantity, 
                null, "外部系统同步", 
                "同步前：" + oldQuantity + "，同步后：" + externalQuantity, 1L);
            
            // 检查库存预警
            inventoryAlertService.checkInventoryAndGenerateAlerts(productId, warehouseId);
            
            return true;
            
        } finally {
            // 释放同步锁
            releaseInventoryLockByKey(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSyncInventory(List<InventorySyncRequest> syncRequests) {
        int successCount = 0;
        
        for (InventorySyncRequest request : syncRequests) {
            boolean success = syncInventoryFromExternalSystem(
                request.getProductId(), 
                request.getWarehouseId()
            );
            
            if (success) {
                successCount++;
            }
        }
        
        return successCount;
    }

    @Override
    public boolean isInventoryLocked(Long productId, Long warehouseId) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean acquireInventoryLock(String lockKey, int timeoutSeconds) {
        try {
            String lockValue = UUID.randomUUID().toString();
            Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, timeoutSeconds, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(success);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean releaseInventoryLockByKey(String lockKey) {
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean recordInventoryLog(InventoryLog inventoryLog) {
        try {
            inventoryLogMapper.insert(inventoryLog);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void recordInventoryLog(Long productId, Long warehouseId, String operationType, Integer beforeQuantity, Integer afterQuantity, Integer changeQuantity, String documentNumber, String reason, String remark, Long operatorId) {
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setWarehouseId(warehouseId);
        log.setOperationType(mapOperationType(operationType));
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setChangeQuantity(changeQuantity);
        log.setDocumentNumber(documentNumber);
        log.setReason(reason);
        log.setRemark(remark);
        log.setOperatorId(operatorId);
        log.setOperationTime(LocalDateTime.now());
        
        // 获取产品和仓库信息
        Product product = productMapper.selectById(productId);
        if (product != null) {
            log.setProductName(product.getProductName());
            log.setProductCode(product.getProductCode());
        }
        
        // 获取仓库信息
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse != null) {
            log.setWarehouseName(warehouse.getWarehouseName());
        }
        
        inventoryLogMapper.insert(log);
    }

    @Override
    public List<InventoryLog> getInventoryOperationHistory(Long productId, Long warehouseId, Integer days) {
        LambdaQueryWrapper<InventoryLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryLog::getProductId, productId)
                   .eq(InventoryLog::getWarehouseId, warehouseId);
        
        if (days != null && days > 0) {
            LocalDateTime startTime = LocalDateTime.now().minusDays(days);
            queryWrapper.ge(InventoryLog::getOperationTime, startTime);
        }
        
        queryWrapper.orderByDesc(InventoryLog::getOperationTime);
        
        return inventoryLogMapper.selectList(queryWrapper);
    }

    public PageParam<InventoryLog> getInventoryLogs(Integer pageNum, Integer pageSize, Long productId, Long warehouseId, String operationType, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<InventoryLog> queryWrapper = new LambdaQueryWrapper<>();
        
        if (productId != null) {
            queryWrapper.eq(InventoryLog::getProductId, productId);
        }
        if (warehouseId != null) {
            queryWrapper.eq(InventoryLog::getWarehouseId, warehouseId);
        }
        if (operationType != null && !operationType.isEmpty()) {
            queryWrapper.eq(InventoryLog::getOperationType, operationType);
        }
        if (startTime != null) {
            queryWrapper.ge(InventoryLog::getOperationTime, startTime);
        }
        if (endTime != null) {
            queryWrapper.le(InventoryLog::getOperationTime, endTime);
        }
        
        queryWrapper.orderByDesc(InventoryLog::getOperationTime);
        
        IPage<InventoryLog> page = inventoryLogMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 转换为PageParam
        PageParam<InventoryLog> pageParam = new PageParam<>();
        pageParam.setRecords(page.getRecords());
        pageParam.setTotal(page.getTotal());
        pageParam.setSize(page.getSize());
        pageParam.setCurrent(page.getCurrent());
        pageParam.setPages(page.getPages());
        
        return pageParam;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryConsistencyResult checkInventoryConsistency(Long productId, Long warehouseId) {
        InventoryConsistencyResult result = new InventoryConsistencyResult();
        
        try {
            // 查询当前库存记录
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId)
                       .eq(Inventory::getWarehouseId, warehouseId);
            Inventory inventory = getOne(queryWrapper);
            
            if (inventory == null) {
                result.setConsistent(false);
                result.setDescription("库存记录不存在");
                return result;
            }
            
            result.setSystemStock(inventory.getQuantity());
            
            // 通过库存日志计算理论库存
            LambdaQueryWrapper<InventoryLog> logQuery = new LambdaQueryWrapper<>();
            logQuery.eq(InventoryLog::getProductId, productId)
                   .eq(InventoryLog::getWarehouseId, warehouseId)
                   .orderByAsc(InventoryLog::getOperationTime);
            
            List<InventoryLog> logs = inventoryLogMapper.selectList(logQuery);
            
            Integer calculatedQuantity = 0;
            for (InventoryLog log : logs) {
                if (log.getChangeQuantity() != null) {
                    calculatedQuantity += log.getChangeQuantity();
                }
            }
            
            result.setActualStock(calculatedQuantity);
            result.setDifference(inventory.getQuantity() - calculatedQuantity);
            result.setConsistent(Objects.equals(inventory.getQuantity(), calculatedQuantity));
            
            if (!result.isConsistent()) {
                result.setDescription(String.format("库存不一致，当前库存：%d，计算库存：%d，差异：%d", 
                    inventory.getQuantity(), calculatedQuantity, result.getDifference()));
            }
            
        } catch (Exception e) {
            result.setConsistent(false);
            result.setDescription("检查过程中发生异常：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean fixInventoryInconsistency(Long productId, Long warehouseId, Long operatorId) {
        // 先检查一致性
        InventoryConsistencyResult result = checkInventoryConsistency(productId, warehouseId);
        if (result.isConsistent()) {
            return true; // 已经一致，无需修复
        }
        
        Integer correctQuantity = result.getActualStock(); // 使用计算出的正确库存
        String reason = "自动修复库存不一致问题";
        
        return fixInventoryWithCorrectQuantity(productId, warehouseId, correctQuantity, operatorId, reason);
     }

    /**
     * 模拟从外部系统获取库存数据
     */
    private Integer getExternalSystemStock(Long productId, Long warehouseId) {
        // 这里应该调用外部系统API获取库存数据
        // 暂时返回随机数模拟
        return new Random().nextInt(1000);
    }
    
    /**
     * 将字符串操作类型映射为整数类型
     * @param operationType 字符串操作类型
     * @return 整数操作类型
     */
    private Integer mapOperationType(String operationType) {
        if (operationType == null) {
            return 0;
        }
        
        switch (operationType) {
            case "STOCK_INCREASE":
                return 1; // 入库
            case "STOCK_REDUCE":
                return 2; // 出库
            case "TRANSFER":
                return 3; // 调拨
            case "INVENTORY_CHECK":
                return 4; // 盘点
            case "DAMAGE":
                return 5; // 报损
            case "RETURN":
                return 6; // 退货
            case "EXTERNAL_SYNC":
                return 7; // 外部同步
            case "CONSISTENCY_FIX":
                return 8; // 一致性修复
            default:
                return 0; // 未知类型
        }
    }
 
     private boolean fixInventoryWithCorrectQuantity(Long productId, Long warehouseId, Integer correctQuantity, Long operatorId, String reason) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        
        try {
            // 获取分布式锁
            if (!acquireInventoryLock(lockKey, DEFAULT_LOCK_TIMEOUT)) {
                return false;
            }
            
            // 查询当前库存
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId)
                       .eq(Inventory::getWarehouseId, warehouseId);
            Inventory inventory = getOne(queryWrapper);
            
            if (inventory == null) {
                return false;
            }
            
            Integer oldQuantity = inventory.getQuantity();
            Integer changeQuantity = correctQuantity - oldQuantity;
            
            // 更新库存
            inventory.setQuantity(correctQuantity);
            inventory.setUpdateTime(new Date());
            boolean success = updateById(inventory);
            
            if (success) {
                // 记录修复日志
                recordInventoryLog(productId, warehouseId, "CONSISTENCY_FIX", 
                    oldQuantity, correctQuantity, changeQuantity, 
                    null, "库存一致性修复", reason, operatorId);
                
                // 检查库存预警
                inventoryAlertService.checkInventoryAndGenerateAlerts(productId, warehouseId);
            }
            
            return success;
            
        } finally {
            // 释放分布式锁
            releaseInventoryLockByKey(lockKey);
        }
    }

    /**
     * 批量库存操作（控制器调用的方法名）
     */
    public boolean batchStockOperation(List<StockOperation> operations) {
        return batchSafeStockOperation(operations);
    }

    /**
     * 从外部系统同步库存（控制器调用的方法名）
     */
    public boolean syncInventoryFromExternal(Long productId, Long warehouseId) {
        return syncInventoryFromExternalSystem(productId, warehouseId);
    }

    /**
     * 获取库存锁（控制器调用的方法名）
     */
    public boolean acquireInventoryLock(Long productId, Long warehouseId) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        return acquireInventoryLock(lockKey, DEFAULT_LOCK_TIMEOUT);
    }

    /**
     * 释放库存锁（控制器调用的方法名）
     */
    public boolean releaseInventoryLock(Long productId, Long warehouseId) {
        if (productId == null || warehouseId == null) {
            return false;
        }
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        return releaseInventoryLockByKey(lockKey);
    }

    /**
     * 获取库存操作历史（控制器调用的方法名）
     */
    public List<InventoryLog> getInventoryLogs(Long productId, Long warehouseId, Integer days) {
        return getInventoryOperationHistory(productId, warehouseId, days);
    }

    /**
     * 修复库存一致性（控制器调用的方法名）
     */
    public boolean fixInventoryConsistency(Long productId, Long warehouseId, Long operatorId) {
        return fixInventoryInconsistency(productId, warehouseId, operatorId);
    }

    @Override
    public Map<String, String> getOperationTypes() {
        Map<String, String> types = new HashMap<>();
        types.put("SALE", "销售出库");
        types.put("PURCHASE", "采购入库");
        types.put("RETURN", "退货入库");
        types.put("REFUND", "退款出库");
        types.put("TRANSFER_IN", "调拨入库");
        types.put("TRANSFER_OUT", "调拨出库");
        types.put("INVENTORY_CHECK", "盘点调整");
        types.put("DAMAGE", "损耗出库");
        types.put("EXTERNAL_SYNC", "外部同步");
        types.put("CONSISTENCY_FIX", "一致性修复");
        types.put("BATCH_OPERATION", "批量操作");
        types.put("OTHER", "其他");
        return types;
    }

    @Override
    public OperationStatistics getOperationStatistics(Long productId, Long warehouseId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<InventoryLog> queryWrapper = new LambdaQueryWrapper<>();
        
        if (productId != null) {
            queryWrapper.eq(InventoryLog::getProductId, productId);
        }
        if (warehouseId != null) {
            queryWrapper.eq(InventoryLog::getWarehouseId, warehouseId);
        }
        if (startTime != null) {
            queryWrapper.ge(InventoryLog::getOperationTime, startTime);
        }
        if (endTime != null) {
            queryWrapper.le(InventoryLog::getOperationTime, endTime);
        }
        
        List<InventoryLog> logs = inventoryLogMapper.selectList(queryWrapper);
        
        int totalOperations = logs.size();
        int inOperations = 0;
        int outOperations = 0;
        Map<String, Integer> operationTypes = new HashMap<>();
        
        for (InventoryLog log : logs) {
            if (log.getChangeQuantity() != null) {
                if (log.getChangeQuantity() > 0) {
                    inOperations++;
                } else if (log.getChangeQuantity() < 0) {
                    outOperations++;
                }
            }
            
            String operationType = log.getOperationType() != null ? log.getOperationType().toString() : "UNKNOWN";
            operationTypes.put(operationType, operationTypes.getOrDefault(operationType, 0) + 1);
        }
        
        return new OperationStatistics(totalOperations, inOperations, outOperations, operationTypes);
    }

    @Override
    public LockStatusDetail getLockStatusDetail(Long productId, Long warehouseId) {
        String lockKey = INVENTORY_LOCK_PREFIX + productId + ":" + warehouseId;
        
        try {
            Boolean isLocked = redisTemplate.hasKey(lockKey);
            String lockOwner = null;
            Long expireTime = null;
            LocalDateTime lockTime = null;
            
            if (Boolean.TRUE.equals(isLocked)) {
                lockOwner = (String) redisTemplate.opsForValue().get(lockKey);
                expireTime = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
                // 锁时间可以通过其他方式获取，这里简化处理
                lockTime = LocalDateTime.now().minusSeconds(DEFAULT_LOCK_TIMEOUT - (expireTime != null ? expireTime : 0));
            }
            
            return new LockStatusDetail(lockKey, isLocked, lockTime, lockOwner, expireTime);
        } catch (Exception e) {
            return new LockStatusDetail(lockKey, false, null, null, null);
        }
    }
}