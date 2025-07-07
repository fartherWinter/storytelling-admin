package com.chennian.storytelling.bean.enums;

/**
 * 流程状态枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum ProcessStatusEnum {
    
    ACTIVE("ACTIVE", "活跃"),
    SUSPENDED("SUSPENDED", "已挂起"),
    COMPLETED("COMPLETED", "已完成"),
    EXTERNALLY_TERMINATED("EXTERNALLY_TERMINATED", "外部终止"),
    INTERNALLY_TERMINATED("INTERNALLY_TERMINATED", "内部终止"),
    RUNNING("RUNNING", "运行中"),
    PENDING("PENDING", "待处理"),
    ERROR("ERROR", "错误"),
    CANCELLED("CANCELLED", "已取消");
    
    private final String code;
    private final String name;
    
    ProcessStatusEnum(String code, String name) {
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
    public static ProcessStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProcessStatusEnum status : values()) {
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
        ProcessStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}