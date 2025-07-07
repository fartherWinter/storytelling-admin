package com.chennian.storytelling.bean.enums;

/**
 * 审批状态枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum ApprovalStatusEnum {
    
    DRAFT("DRAFT", "草稿"),
    PENDING("PENDING", "待审批"),
    APPROVED("APPROVED", "已通过"),
    REJECTED("REJECTED", "已拒绝"),
    CANCELLED("CANCELLED", "已取消"),
    WITHDRAWN("WITHDRAWN", "已撤回");
    
    private final String code;
    private final String name;
    
    ApprovalStatusEnum(String code, String name) {
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
    public static ApprovalStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ApprovalStatusEnum status : values()) {
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
        ApprovalStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}