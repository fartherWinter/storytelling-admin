package com.chennian.storytelling.inventory.constant;

/**
 * 库存模块常量类
 * 
 * @author chennian
 * @since 2024-01-01
 */
public class InventoryConstants {
    
    /**
     * 库存状态
     */
    public static class InventoryStatus {
        /** 启用 */
        public static final Integer ENABLED = 1;
        /** 禁用 */
        public static final Integer DISABLED = 0;
    }
    
    /**
     * 仓库状态
     */
    public static class WarehouseStatus {
        /** 启用 */
        public static final Integer ENABLED = 1;
        /** 禁用 */
        public static final Integer DISABLED = 0;
    }
    
    /**
     * 仓库类型
     */
    public static class WarehouseType {
        /** 实体仓库 */
        public static final String PHYSICAL = "PHYSICAL";
        /** 虚拟仓库 */
        public static final String VIRTUAL = "VIRTUAL";
        /** 中转仓库 */
        public static final String TRANSIT = "TRANSIT";
        /** 退货仓库 */
        public static final String RETURN = "RETURN";
    }
    
    /**
     * 库存变动类型
     */
    public static class InventoryChangeType {
        /** 入库 */
        public static final String IN = "IN";
        /** 出库 */
        public static final String OUT = "OUT";
        /** 调拨 */
        public static final String TRANSFER = "TRANSFER";
        /** 盘点 */
        public static final String STOCKTAKING = "STOCKTAKING";
        /** 锁定 */
        public static final String LOCK = "LOCK";
        /** 解锁 */
        public static final String UNLOCK = "UNLOCK";
        /** 预占 */
        public static final String RESERVE = "RESERVE";
        /** 释放预占 */
        public static final String RELEASE = "RELEASE";
    }
    
    /**
     * 库存操作类型
     */
    public static class InventoryOperationType {
        /** 查询 */
        public static final String QUERY = "QUERY";
        /** 创建 */
        public static final String CREATE = "CREATE";
        /** 更新 */
        public static final String UPDATE = "UPDATE";
        /** 删除 */
        public static final String DELETE = "DELETE";
        /** 入库 */
        public static final String STOCK_IN = "STOCK_IN";
        /** 出库 */
        public static final String STOCK_OUT = "STOCK_OUT";
        /** 调拨 */
        public static final String TRANSFER = "TRANSFER";
        /** 盘点 */
        public static final String STOCKTAKING = "STOCKTAKING";
        /** 锁定 */
        public static final String LOCK = "LOCK";
        /** 解锁 */
        public static final String UNLOCK = "UNLOCK";
        /** 预占 */
        public static final String RESERVE = "RESERVE";
        /** 释放预占 */
        public static final String RELEASE = "RELEASE";
    }
    
    /**
     * 操作结果
     */
    public static class OperationResult {
        /** 成功 */
        public static final String SUCCESS = "SUCCESS";
        /** 失败 */
        public static final String FAILURE = "FAILURE";
    }
    
    /**
     * 默认值
     */
    public static class DefaultValues {
        /** 默认页大小 */
        public static final Integer DEFAULT_PAGE_SIZE = 20;
        /** 最大页大小 */
        public static final Integer MAX_PAGE_SIZE = 1000;
        /** 默认低库存阈值 */
        public static final Integer DEFAULT_LOW_STOCK_THRESHOLD = 10;
        /** 默认库存预警阈值 */
        public static final Integer DEFAULT_WARNING_THRESHOLD = 20;
        /** 默认仓库容量 */
        public static final Long DEFAULT_WAREHOUSE_CAPACITY = 10000L;
        /** 默认排序值 */
        public static final Integer DEFAULT_SORT_ORDER = 0;
        /** 默认层级 */
        public static final Integer DEFAULT_LEVEL = 1;
        /** 根节点父ID */
        public static final Long ROOT_PARENT_ID = 0L;
    }
    
    /**
     * 缓存键前缀
     */
    public static class CachePrefix {
        /** 库存缓存前缀 */
        public static final String INVENTORY = "inventory:";
        /** 仓库缓存前缀 */
        public static final String WAREHOUSE = "warehouse:";
        /** 库存统计缓存前缀 */
        public static final String INVENTORY_STATS = "inventory:stats:";
        /** 仓库统计缓存前缀 */
        public static final String WAREHOUSE_STATS = "warehouse:stats:";
    }
    
    /**
     * 错误码
     */
    public static class ErrorCode {
        /** 库存不存在 */
        public static final String INVENTORY_NOT_FOUND = "INVENTORY_NOT_FOUND";
        /** 仓库不存在 */
        public static final String WAREHOUSE_NOT_FOUND = "WAREHOUSE_NOT_FOUND";
        /** 库存不足 */
        public static final String INSUFFICIENT_INVENTORY = "INSUFFICIENT_INVENTORY";
        /** 仓库容量不足 */
        public static final String INSUFFICIENT_WAREHOUSE_CAPACITY = "INSUFFICIENT_WAREHOUSE_CAPACITY";
        /** SKU编码已存在 */
        public static final String SKU_CODE_EXISTS = "SKU_CODE_EXISTS";
        /** 仓库编码已存在 */
        public static final String WAREHOUSE_CODE_EXISTS = "WAREHOUSE_CODE_EXISTS";
        /** 仓库名称已存在 */
        public static final String WAREHOUSE_NAME_EXISTS = "WAREHOUSE_NAME_EXISTS";
        /** 仓库有库存不能删除 */
        public static final String WAREHOUSE_HAS_INVENTORY = "WAREHOUSE_HAS_INVENTORY";
        /** 仓库有子仓库不能删除 */
        public static final String WAREHOUSE_HAS_CHILDREN = "WAREHOUSE_HAS_CHILDREN";
        /** 库存已锁定 */
        public static final String INVENTORY_LOCKED = "INVENTORY_LOCKED";
        /** 库存未锁定 */
        public static final String INVENTORY_NOT_LOCKED = "INVENTORY_NOT_LOCKED";
        /** 预占数量不足 */
        public static final String INSUFFICIENT_RESERVED_QUANTITY = "INSUFFICIENT_RESERVED_QUANTITY";
    }
}