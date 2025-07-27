package com.chennian.storytelling.inventory.exception;

/**
 * 库存不足异常类
 * 
 * @author chennian
 * @since 2024-01-01
 */
public class InsufficientInventoryException extends InventoryException {
    
    private Long productId;
    private Long warehouseId;
    private Integer requiredQuantity;
    private Integer availableQuantity;
    
    public InsufficientInventoryException(String message) {
        super("INSUFFICIENT_INVENTORY", message);
    }
    
    public InsufficientInventoryException(Long productId, Long warehouseId, 
                                        Integer requiredQuantity, Integer availableQuantity) {
        super("INSUFFICIENT_INVENTORY", 
              String.format("库存不足：商品ID=%d，仓库ID=%d，需要数量=%d，可用数量=%d", 
                          productId, warehouseId, requiredQuantity, availableQuantity));
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.requiredQuantity = requiredQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Long getWarehouseId() {
        return warehouseId;
    }
    
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
    
    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }
    
    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    
    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}