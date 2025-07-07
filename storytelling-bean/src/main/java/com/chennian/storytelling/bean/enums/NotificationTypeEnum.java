package com.chennian.storytelling.bean.enums;

/**
 * 通知类型枚举
 * @author chennian
 */
public enum NotificationTypeEnum {
    
    SYSTEM(1, "系统通知"),
    WORKFLOW(2, "工作流通知"),
    TASK(3, "任务通知"),
    APPROVAL(4, "审批通知"),
    REMINDER(5, "提醒通知"),
    WARNING(6, "警告通知"),
    ERROR(7, "错误通知"),
    INFO(8, "信息通知");
    
    private final Integer code;
    private final String name;
    
    NotificationTypeEnum(Integer code, String name) {
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
     * @param code 编码
     * @return 枚举实例
     */
    public static NotificationTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NotificationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取名称
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(Integer code) {
        NotificationTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
    
    /**
     * 判断是否为紧急通知类型
     * @param code 编码
     * @return 是否紧急
     */
    public static boolean isUrgent(Integer code) {
        return WARNING.getCode().equals(code) || ERROR.getCode().equals(code);
    }
    
    /**
     * 判断是否为业务通知类型
     * @param code 编码
     * @return 是否业务通知
     */
    public static boolean isBusiness(Integer code) {
        return WORKFLOW.getCode().equals(code) || 
               TASK.getCode().equals(code) || 
               APPROVAL.getCode().equals(code);
    }
    
    /**
     * 判断是否为系统级通知
     * @param code 编码
     * @return 是否系统级通知
     */
    public static boolean isSystemLevel(Integer code) {
        return SYSTEM.getCode().equals(code) || 
               WARNING.getCode().equals(code) || 
               ERROR.getCode().equals(code);
    }
}