package com.chennian.storytelling.inventory.exception;

/**
 * 库存业务异常类
 * 
 * @author chennian
 * @since 2024-01-01
 */
public class InventoryException extends RuntimeException {
    
    private String code;
    
    public InventoryException(String message) {
        super(message);
    }
    
    public InventoryException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public InventoryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InventoryException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}