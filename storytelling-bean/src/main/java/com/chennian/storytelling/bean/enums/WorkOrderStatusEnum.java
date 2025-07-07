package com.chennian.storytelling.bean.enums;

/**
 * 工单状态枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum WorkOrderStatusEnum {
    
    PENDING_DISPATCH(0, "待派工"),
    DISPATCHED(1, "已派工"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "已完工"),
    CANCELLED(4, "已取消");
    
    private final Integer code;
    private final String name;
    
    WorkOrderStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据code获取枚举
     * 
     * @param code 状态码
     * @return 枚举实例
     */
    public static WorkOrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WorkOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取名称
     * 
     * @param code 状态码
     * @return 状态名称
     */
    public static String getNameByCode(Integer code) {
        WorkOrderStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}