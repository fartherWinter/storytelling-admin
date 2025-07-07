package com.chennian.storytelling.bean.enums;

/**
 * 供应商状态枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum SupplierStatusEnum {
    
    NORMAL("0", "正常"),
    DISABLED("1", "禁用"),
    ACTIVE("active", "活跃"),
    INACTIVE("inactive", "非活跃"),
    PENDING("pending", "待审核");
    
    private final String code;
    private final String name;
    
    SupplierStatusEnum(String code, String name) {
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
    public static SupplierStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (SupplierStatusEnum status : values()) {
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
        SupplierStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}