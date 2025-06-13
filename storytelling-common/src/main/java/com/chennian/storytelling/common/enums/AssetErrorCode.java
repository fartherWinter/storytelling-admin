package com.chennian.storytelling.common.enums;

/**
 * 资产管理错误码枚举
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public enum AssetErrorCode {
    
    // 通用操作错误码
    SAVE_FAILED("A001", "保存失败"),
    UPDATE_FAILED("A002", "更新失败"),
    DELETE_FAILED("A003", "删除失败"),
    
    // 资产信息相关错误码
    ASSET_NOT_FOUND("A101", "资产信息不存在"),
    ASSET_NO_DUPLICATE("A102", "资产编号已存在"),
    ASSET_IN_USE("A103", "资产正在使用中，无法删除"),
    ASSET_EXPIRED("A104", "资产已过期"),
    
    // 资产分类相关错误码
    CATEGORY_NOT_FOUND("A201", "资产分类不存在"),
    CATEGORY_HAS_CHILDREN("A202", "该分类下存在子分类，无法删除"),
    CATEGORY_HAS_ASSETS("A203", "该分类下存在资产，无法删除"),
    CATEGORY_CODE_DUPLICATE("A204", "分类编码已存在"),
    
    // 资产维护相关错误码
    MAINTENANCE_NOT_FOUND("A301", "维护记录不存在"),
    MAINTENANCE_PLAN_CONFLICT("A302", "维护计划时间冲突"),
    MAINTENANCE_OVERDUE("A303", "维护任务已逾期"),
    
    // 资产盘点相关错误码
    INVENTORY_NOT_FOUND("A401", "盘点记录不存在"),
    INVENTORY_BATCH_DUPLICATE("A402", "盘点批次号已存在"),
    INVENTORY_IN_PROGRESS("A403", "盘点正在进行中"),
    INVENTORY_COMPLETED("A404", "盘点已完成，无法修改"),
    
    // 资产折旧相关错误码
    DEPRECIATION_NOT_FOUND("A501", "折旧记录不存在"),
    DEPRECIATION_CALCULATED("A502", "该期间折旧已计算"),
    DEPRECIATION_CALCULATE_FAILED("A503", "计算折旧失败"),
    DEPRECIATION_BATCH_CALCULATE_FAILED("A504", "批量计算折旧失败"),
    
    // 权限相关错误码
    NO_PERMISSION("A901", "无操作权限"),
    DEPARTMENT_NO_PERMISSION("A902", "无该部门资产操作权限");
    
    private final String code;
    private final String message;
    
    AssetErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误码获取枚举
     */
    public static AssetErrorCode getByCode(String code) {
        for (AssetErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "AssetErrorCode{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}