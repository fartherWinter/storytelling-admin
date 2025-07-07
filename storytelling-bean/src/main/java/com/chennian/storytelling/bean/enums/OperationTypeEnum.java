package com.chennian.storytelling.bean.enums;

/**
 * 操作类型枚举
 * @author chennian
 */
public enum OperationTypeEnum {
    
    CREATE(1, "创建"),
    UPDATE(2, "更新"),
    DELETE(3, "删除"),
    QUERY(4, "查询"),
    EXPORT(5, "导出"),
    IMPORT(6, "导入"),
    LOGIN(7, "登录"),
    LOGOUT(8, "登出"),
    APPROVE(9, "审批"),
    REJECT(10, "拒绝");
    
    private final Integer code;
    private final String name;
    
    OperationTypeEnum(Integer code, String name) {
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
    public static OperationTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OperationTypeEnum type : values()) {
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
        OperationTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
    
    /**
     * 判断是否为数据修改操作
     * @param code 编码
     * @return 是否为数据修改操作
     */
    public static boolean isDataModification(Integer code) {
        return CREATE.getCode().equals(code) || 
               UPDATE.getCode().equals(code) || 
               DELETE.getCode().equals(code);
    }
    
    /**
     * 判断是否为查询操作
     * @param code 编码
     * @return 是否为查询操作
     */
    public static boolean isQuery(Integer code) {
        return QUERY.getCode().equals(code);
    }
    
    /**
     * 判断是否为审批相关操作
     * @param code 编码
     * @return 是否为审批相关操作
     */
    public static boolean isApprovalRelated(Integer code) {
        return APPROVE.getCode().equals(code) || REJECT.getCode().equals(code);
    }
}