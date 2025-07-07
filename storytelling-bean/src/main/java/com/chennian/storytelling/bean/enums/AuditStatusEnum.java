package com.chennian.storytelling.bean.enums;

/**
 * 审核状态枚举
 * @author chennian
 * @date 2024-01-01
 */
public enum AuditStatusEnum {
    
    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝"),
    CANCELLED(3, "已取消"),
    WITHDRAWN(4, "已撤回");
    
    private final Integer code;
    private final String name;
    
    AuditStatusEnum(Integer code, String name) {
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
     * @return 枚举
     */
    public static AuditStatusEnum getByCode(Integer code) {
        for (AuditStatusEnum auditStatusEnum : AuditStatusEnum.values()) {
            if (auditStatusEnum.getCode().equals(code)) {
                return auditStatusEnum;
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
        AuditStatusEnum auditStatusEnum = getByCode(code);
        return auditStatusEnum != null ? auditStatusEnum.getName() : null;
    }
}