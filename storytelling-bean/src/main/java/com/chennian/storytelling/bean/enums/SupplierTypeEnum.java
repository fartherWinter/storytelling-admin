package com.chennian.storytelling.bean.enums;

/**
 * 供应商类型枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum SupplierTypeEnum {
    
    RAW_MATERIAL("1", "原材料"),
    EQUIPMENT("2", "设备"),
    SERVICE("3", "服务"),
    OTHER("4", "其他");
    
    private final String code;
    private final String name;
    
    SupplierTypeEnum(String code, String name) {
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
    public static SupplierTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (SupplierTypeEnum type : values()) {
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
        SupplierTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
}