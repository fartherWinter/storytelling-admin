package com.chennian.storytelling.common.enums;

/**
 * 商城订单状态枚举
 * 
 * @author chennian
 * @date 2024-01-01
 */
public enum MallOrderStatus {
    
    /**
     * 待付款
     */
    PENDING_PAYMENT(0, "待付款"),
    
    /**
     * 待发货
     */
    PENDING_DELIVERY(1, "待发货"),
    
    /**
     * 已发货
     */
    DELIVERED(2, "已发货"),
    
    /**
     * 已完成
     */
    COMPLETED(3, "已完成"),
    
    /**
     * 已关闭
     */
    CLOSED(4, "已关闭"),
    
    /**
     * 无效订单
     */
    INVALID(5, "无效订单");
    
    private final Integer code;
    private final String message;
    
    MallOrderStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据状态码获取枚举
     * 
     * @param code 状态码
     * @return 订单状态枚举
     */
    public static MallOrderStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MallOrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 判断是否为有效状态
     * 
     * @param code 状态码
     * @return 是否有效
     */
    public static boolean isValidStatus(Integer code) {
        return getByCode(code) != null;
    }
}