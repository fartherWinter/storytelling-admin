package com.chennian.storytelling.bean.enums;

/**
 * 工作流任务类型枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum TaskTypeEnum {
    
    USER_TASK("USER_TASK", "用户任务"),
    SERVICE_TASK("SERVICE_TASK", "服务任务"),
    SCRIPT_TASK("SCRIPT_TASK", "脚本任务"),
    MANUAL_TASK("MANUAL_TASK", "手工任务"),
    RECEIVE_TASK("RECEIVE_TASK", "接收任务"),
    SEND_TASK("SEND_TASK", "发送任务"),
    BUSINESS_RULE_TASK("BUSINESS_RULE_TASK", "业务规则任务");
    
    private final String code;
    private final String name;
    
    TaskTypeEnum(String code, String name) {
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
     * @param code 类型码
     * @return 枚举实例
     */
    public static TaskTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TaskTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取名称
     * 
     * @param code 类型码
     * @return 类型名称
     */
    public static String getNameByCode(String code) {
        TaskTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
}