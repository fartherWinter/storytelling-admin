package com.chennian.storytelling.bean.enums;

/**
 * 工作流任务状态枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum TaskStatusEnum {
    
    CREATED("CREATED", "已创建"),
    READY("READY", "就绪"),
    RESERVED("RESERVED", "已保留"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    SUSPENDED("SUSPENDED", "已挂起"),
    COMPLETED("COMPLETED", "已完成"),
    FAILED("FAILED", "失败"),
    ERROR("ERROR", "错误"),
    EXITED("EXITED", "已退出"),
    OBSOLETE("OBSOLETE", "已废弃"),
    PENDING("PENDING", "待办"),
    CANCELLED("CANCELLED", "已取消"),
    REJECTED("REJECTED", "已拒绝");
    
    private final String code;
    private final String name;
    
    TaskStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
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
    public static TaskStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TaskStatusEnum status : values()) {
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
    public static String getNameByCode(String code) {
        TaskStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}