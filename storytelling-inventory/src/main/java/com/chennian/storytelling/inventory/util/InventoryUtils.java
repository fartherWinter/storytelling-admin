package com.chennian.storytelling.inventory.util;

import com.chennian.storytelling.inventory.constant.InventoryConstants;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 库存工具类
 * 
 * @author chennian
 * @since 2024-01-01
 */
public class InventoryUtils {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成SKU编码
     * 
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return SKU编码
     */
    public static String generateSkuCode(Long productId, Long warehouseId) {
        return String.format("SKU%d_%d_%s", productId, warehouseId, 
                           LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }
    
    /**
     * 生成仓库编码
     * 
     * @param warehouseName 仓库名称
     * @return 仓库编码
     */
    public static String generateWarehouseCode(String warehouseName) {
        String prefix = "WH";
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        if (StringUtils.hasText(warehouseName) && warehouseName.length() >= 2) {
            prefix += warehouseName.substring(0, 2).toUpperCase();
        }
        return prefix + timestamp;
    }
    
    /**
     * 生成业务单号
     * 
     * @param prefix 前缀
     * @return 业务单号
     */
    public static String generateBusinessNo(String prefix) {
        return prefix + LocalDateTime.now().format(DATE_TIME_FORMATTER) + 
               String.format("%04d", (int)(Math.random() * 10000));
    }
    
    /**
     * 生成库存变动单号
     * 
     * @return 变动单号
     */
    public static String generateInventoryChangeNo() {
        return generateBusinessNo("IC");
    }
    
    /**
     * 生成调拨单号
     * 
     * @return 调拨单号
     */
    public static String generateTransferNo() {
        return generateBusinessNo("TF");
    }
    
    /**
     * 生成盘点单号
     * 
     * @return 盘点单号
     */
    public static String generateStocktakingNo() {
        return generateBusinessNo("ST");
    }
    
    /**
     * 计算库存周转率
     * 
     * @param outQuantity 出库数量
     * @param avgInventory 平均库存
     * @return 周转率
     */
    public static BigDecimal calculateTurnoverRate(Integer outQuantity, Integer avgInventory) {
        if (avgInventory == null || avgInventory == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(outQuantity)
                .divide(BigDecimal.valueOf(avgInventory), 4, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算容量使用率
     * 
     * @param usedCapacity 已用容量
     * @param totalCapacity 总容量
     * @return 使用率（百分比）
     */
    public static BigDecimal calculateCapacityUsageRate(Long usedCapacity, Long totalCapacity) {
        if (totalCapacity == null || totalCapacity == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(usedCapacity)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalCapacity), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * 检查是否为低库存
     * 
     * @param availableQuantity 可用数量
     * @param lowStockThreshold 低库存阈值
     * @return 是否为低库存
     */
    public static boolean isLowStock(Integer availableQuantity, Integer lowStockThreshold) {
        if (lowStockThreshold == null) {
            lowStockThreshold = InventoryConstants.DefaultValues.DEFAULT_LOW_STOCK_THRESHOLD;
        }
        return availableQuantity != null && availableQuantity <= lowStockThreshold;
    }
    
    /**
     * 检查是否缺货
     * 
     * @param availableQuantity 可用数量
     * @return 是否缺货
     */
    public static boolean isOutOfStock(Integer availableQuantity) {
        return availableQuantity == null || availableQuantity <= 0;
    }
    
    /**
     * 检查是否需要库存预警
     * 
     * @param availableQuantity 可用数量
     * @param warningThreshold 预警阈值
     * @return 是否需要预警
     */
    public static boolean needsWarning(Integer availableQuantity, Integer warningThreshold) {
        if (warningThreshold == null) {
            warningThreshold = InventoryConstants.DefaultValues.DEFAULT_WARNING_THRESHOLD;
        }
        return availableQuantity != null && availableQuantity <= warningThreshold;
    }
    
    /**
     * 生成仓库路径
     * 
     * @param parentPath 父路径
     * @param warehouseId 仓库ID
     * @return 仓库路径
     */
    public static String generateWarehousePath(String parentPath, Long warehouseId) {
        if (!StringUtils.hasText(parentPath) || "/".equals(parentPath)) {
            return "/" + warehouseId;
        }
        return parentPath + "/" + warehouseId;
    }
    
    /**
     * 解析仓库路径获取层级
     * 
     * @param path 仓库路径
     * @return 层级
     */
    public static Integer parseWarehouseLevel(String path) {
        if (!StringUtils.hasText(path) || "/".equals(path)) {
            return 1;
        }
        return path.split("/").length;
    }
    
    /**
     * 验证数量是否有效
     * 
     * @param quantity 数量
     * @return 是否有效
     */
    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity >= 0;
    }
    
    /**
     * 验证容量是否有效
     * 
     * @param capacity 容量
     * @return 是否有效
     */
    public static boolean isValidCapacity(Long capacity) {
        return capacity != null && capacity > 0;
    }
    
    /**
     * 生成唯一标识
     * 
     * @return 唯一标识
     */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 计算总数量
     * 
     * @param quantities 数量列表
     * @return 总数量
     */
    public static Integer calculateTotalQuantity(List<Integer> quantities) {
        if (quantities == null || quantities.isEmpty()) {
            return 0;
        }
        return quantities.stream().mapToInt(q -> q == null ? 0 : q).sum();
    }
    
    /**
     * 计算平均数量
     * 
     * @param quantities 数量列表
     * @return 平均数量
     */
    public static BigDecimal calculateAverageQuantity(List<Integer> quantities) {
        if (quantities == null || quantities.isEmpty()) {
            return BigDecimal.ZERO;
        }
        int total = calculateTotalQuantity(quantities);
        return BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(quantities.size()), 2, RoundingMode.HALF_UP);
    }
}