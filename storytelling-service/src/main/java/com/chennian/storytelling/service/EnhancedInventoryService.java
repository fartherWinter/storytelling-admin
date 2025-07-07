package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.InventoryLog;

import java.util.List;
import java.util.Map;

/**
 * 增强的库存服务接口（包含分布式锁和同步机制）
 */
public interface EnhancedInventoryService {

    /**
     * 安全的库存扣减（带分布式锁防止超卖）
     *
     * @param productId    产品ID
     * @param warehouseId  仓库ID
     * @param quantity     扣减数量
     * @param orderSn      订单编号
     * @param operatorId   操作人ID
     * @return 是否成功
     */
    boolean safeReduceStock(Long productId, Long warehouseId, Integer quantity, String orderSn, Long operatorId);

    /**
     * 安全的库存增加（带分布式锁）
     *
     * @param productId    产品ID
     * @param warehouseId  仓库ID
     * @param quantity     增加数量
     * @param documentType 单据类型
     * @param documentNumber 单据号
     * @param operatorId   操作人ID
     * @return 是否成功
     */
    boolean safeIncreaseStock(Long productId, Long warehouseId, Integer quantity, Integer documentType, String documentNumber, Long operatorId);

    /**
     * 批量安全库存操作
     *
     * @param operations 库存操作列表
     * @return 操作结果
     */
    boolean batchSafeStockOperation(List<StockOperation> operations);

    /**
     * 库存同步（从其他系统同步库存数据）
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean syncInventoryFromExternalSystem(Long productId, Long warehouseId);

    /**
     * 批量库存同步
     *
     * @param syncRequests 同步请求列表
     * @return 同步成功的数量
     */
    int batchSyncInventory(List<InventorySyncRequest> syncRequests);

    /**
     * 获取库存锁状态
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @return 是否被锁定
     */
    boolean isInventoryLocked(Long productId, Long warehouseId);

    /**
     * 释放库存锁
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean releaseInventoryLock(Long productId, Long warehouseId);

    /**
     * 记录库存操作日志
     *
     * @param inventoryLog 库存日志
     * @return 是否成功
     */
    boolean recordInventoryLog(InventoryLog inventoryLog);

    /**
     * 获取库存操作历史
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @param days        查询天数
     * @return 操作历史列表
     */
    List<InventoryLog> getInventoryOperationHistory(Long productId, Long warehouseId, Integer days);

    /**
     * 库存一致性检查
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @return 检查结果
     */
    InventoryConsistencyResult checkInventoryConsistency(Long productId, Long warehouseId);

    /**
     * 修复库存不一致问题
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @param operatorId  操作人ID
     * @return 是否成功
     */
    boolean fixInventoryInconsistency(Long productId, Long warehouseId, Long operatorId);

    /**
     * 获取操作类型列表
     *
     * @return 操作类型映射
     */
    Map<String, String> getOperationTypes();

    /**
     * 获取库存操作统计
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 统计结果
     */
    OperationStatistics getOperationStatistics(Long productId, Long warehouseId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 获取库存锁详细状态
     *
     * @param productId   产品ID
     * @param warehouseId 仓库ID
     * @return 锁状态详情
     */
    LockStatusDetail getLockStatusDetail(Long productId, Long warehouseId);

    /**
     * 库存操作内部类
     */
    class StockOperation {
        private Long productId;
        private Long warehouseId;
        private Integer quantity; // 正数为增加，负数为减少
        private Integer operationType;
        private String documentNumber;
        private Long operatorId;
        private String reason;

        // 构造函数、getter和setter
        public StockOperation() {}

        public StockOperation(Long productId, Long warehouseId, Integer quantity, Integer operationType, String documentNumber, Long operatorId, String reason) {
            this.productId = productId;
            this.warehouseId = warehouseId;
            this.quantity = quantity;
            this.operationType = operationType;
            this.documentNumber = documentNumber;
            this.operatorId = operatorId;
            this.reason = reason;
        }

        // Getters and Setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Long getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Integer getOperationType() { return operationType; }
        public void setOperationType(Integer operationType) { this.operationType = operationType; }
        public String getDocumentNumber() { return documentNumber; }
        public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
        public Long getOperatorId() { return operatorId; }
        public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    /**
     * 库存同步请求内部类
     */
    class InventorySyncRequest {
        private Long productId;
        private Long warehouseId;
        private String externalSystemCode;
        private boolean forceSync;

        // 构造函数、getter和setter
        public InventorySyncRequest() {}

        public InventorySyncRequest(Long productId, Long warehouseId, String externalSystemCode, boolean forceSync) {
            this.productId = productId;
            this.warehouseId = warehouseId;
            this.externalSystemCode = externalSystemCode;
            this.forceSync = forceSync;
        }

        // Getters and Setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Long getWarehouseId() { return warehouseId; }
        public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
        public String getExternalSystemCode() { return externalSystemCode; }
        public void setExternalSystemCode(String externalSystemCode) { this.externalSystemCode = externalSystemCode; }
        public boolean isForceSync() { return forceSync; }
        public void setForceSync(boolean forceSync) { this.forceSync = forceSync; }
    }

    /**
     * 库存一致性检查结果内部类
     */
    class InventoryConsistencyResult {
        private boolean consistent;
        private Integer systemStock;
        private Integer actualStock;
        private Integer difference;
        private String description;

        // 构造函数、getter和setter
        public InventoryConsistencyResult() {}

        public InventoryConsistencyResult(boolean consistent, Integer systemStock, Integer actualStock, Integer difference, String description) {
            this.consistent = consistent;
            this.systemStock = systemStock;
            this.actualStock = actualStock;
            this.difference = difference;
            this.description = description;
        }

        // Getters and Setters
        public boolean isConsistent() { return consistent; }
        public void setConsistent(boolean consistent) { this.consistent = consistent; }
        public Integer getSystemStock() { return systemStock; }
        public void setSystemStock(Integer systemStock) { this.systemStock = systemStock; }
        public Integer getActualStock() { return actualStock; }
        public void setActualStock(Integer actualStock) { this.actualStock = actualStock; }
        public Integer getDifference() { return difference; }
        public void setDifference(Integer difference) { this.difference = difference; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * 操作统计结果内部类
     */
    class OperationStatistics {
        private Integer totalOperations;
        private Integer inOperations;
        private Integer outOperations;
        private Map<String, Integer> operationTypes;

        public OperationStatistics() {}

        public OperationStatistics(Integer totalOperations, Integer inOperations, Integer outOperations, Map<String, Integer> operationTypes) {
            this.totalOperations = totalOperations;
            this.inOperations = inOperations;
            this.outOperations = outOperations;
            this.operationTypes = operationTypes;
        }

        // Getters and Setters
        public Integer getTotalOperations() { return totalOperations; }
        public void setTotalOperations(Integer totalOperations) { this.totalOperations = totalOperations; }
        public Integer getInOperations() { return inOperations; }
        public void setInOperations(Integer inOperations) { this.inOperations = inOperations; }
        public Integer getOutOperations() { return outOperations; }
        public void setOutOperations(Integer outOperations) { this.outOperations = outOperations; }
        public Map<String, Integer> getOperationTypes() { return operationTypes; }
        public void setOperationTypes(Map<String, Integer> operationTypes) { this.operationTypes = operationTypes; }
    }

    /**
     * 锁状态详情内部类
     */
    class LockStatusDetail {
        private String lockKey;
        private Boolean isLocked;
        private java.time.LocalDateTime lockTime;
        private String lockOwner;
        private Long expireTime;

        public LockStatusDetail() {}

        public LockStatusDetail(String lockKey, Boolean isLocked, java.time.LocalDateTime lockTime, String lockOwner, Long expireTime) {
            this.lockKey = lockKey;
            this.isLocked = isLocked;
            this.lockTime = lockTime;
            this.lockOwner = lockOwner;
            this.expireTime = expireTime;
        }

        // Getters and Setters
        public String getLockKey() { return lockKey; }
        public void setLockKey(String lockKey) { this.lockKey = lockKey; }
        public Boolean getIsLocked() { return isLocked; }
        public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
        public java.time.LocalDateTime getLockTime() { return lockTime; }
        public void setLockTime(java.time.LocalDateTime lockTime) { this.lockTime = lockTime; }
        public String getLockOwner() { return lockOwner; }
        public void setLockOwner(String lockOwner) { this.lockOwner = lockOwner; }
        public Long getExpireTime() { return expireTime; }
        public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
    }
}