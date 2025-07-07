package com.chennian.storytelling.bean.enums;

/**
 * 数据状态枚举
 * @author chennian
 */
public enum DataStatusEnum {
    
    DRAFT(0, "草稿"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    ARCHIVED(3, "归档"),
    DELETED(4, "已删除");
    
    private final Integer code;
    private final String name;
    
    DataStatusEnum(Integer code, String name) {
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
    public static DataStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DataStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
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
        DataStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
    
    /**
     * 判断是否为正常状态
     * @param code 编码
     * @return 是否正常
     */
    public static boolean isNormal(Integer code) {
        return NORMAL.getCode().equals(code);
    }
    
    /**
     * 判断是否为可编辑状态（草稿或正常）
     * @param code 编码
     * @return 是否可编辑
     */
    public static boolean isEditable(Integer code) {
        return DRAFT.getCode().equals(code) || NORMAL.getCode().equals(code);
    }
    
    /**
     * 判断是否为已删除状态
     * @param code 编码
     * @return 是否已删除
     */
    public static boolean isDeleted(Integer code) {
        return DELETED.getCode().equals(code);
    }
}