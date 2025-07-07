package com.chennian.storytelling.common.enums;

/**
 * 库存相关错误码枚举
 */
public enum InventoryErrorCode {
    
    // 库存预警相关错误
    ALERT_CREATE_FAILED("A005", "创建库存预警失败"),
    ALERT_PROCESS_FAILED("A001", "处理预警失败"),
    ALERT_IGNORE_FAILED("A002", "忽略预警失败"),
    ALERT_NOT_FOUND("A006", "预警不存在"),
    ALERT_STATISTICS_FAILED("A003", "获取预警统计失败"),
    ALERT_NOTIFICATION_FAILED("A004", "发送预警通知失败"),
    
    // 参数相关错误
    INVALID_PARAMETER("P001", "参数无效"),
    
    // 库存操作相关错误
    STOCK_REDUCE_FAILED("INVENTORY_STOCK_001", "库存扣减失败，可能库存不足或系统繁忙"),
    STOCK_INCREASE_FAILED("INVENTORY_STOCK_002", "库存增加失败，系统繁忙请稍后重试"),
    STOCK_SYNC_FAILED("INVENTORY_STOCK_003", "库存同步失败"),
    STOCK_CONSISTENCY_FIX_FAILED("INVENTORY_STOCK_004", "修复库存一致性失败"),
    INVENTORY_LOG_RECORD_FAILED("INVENTORY_LOG_001", "记录库存日志失败");
    
    private final String code;
    private final String message;
    
    InventoryErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误码获取枚举
     */
    public static InventoryErrorCode getByCode(String code) {
        for (InventoryErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "InventoryErrorCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}