package com.chennian.storytelling.common.enums;

/**
 * 权限管理错误码枚举
 * 
 * @author chennian
 * @date 2024-01-20
 */
public enum PermissionErrorCode {
    
    // 角色相关错误码
    ROLE_NOT_FOUND("P001", "角色不存在"),
    ROLE_NAME_EXISTS("P002", "角色名称已存在"),
    ROLE_KEY_EXISTS("P003", "角色权限字符已存在"),
    ROLE_SAVE_FAILED("P004", "新增角色失败"),
    ROLE_UPDATE_FAILED("P005", "修改角色失败"),
    ROLE_DELETE_FAILED("P006", "删除角色失败"),
    ROLE_IN_USE("P007", "角色已分配给用户，无法删除"),
    
    // 权限相关错误码
    PERMISSION_CODE_EXISTS("P101", "权限编码已存在"),
    PERMISSION_SAVE_FAILED("P102", "新增权限失败"),
    PERMISSION_UPDATE_FAILED("P103", "修改权限失败"),
    PERMISSION_DELETE_FAILED("P104", "删除权限失败"),
    PERMISSION_HAS_CHILDREN("P105", "存在子权限，无法删除"),
    PERMISSION_IN_USE("P106", "权限已分配给角色，无法删除"),
    
    // 角色权限关联相关错误码
    ASSIGN_PERMISSION_FAILED("P201", "分配权限失败"),
    ASSIGN_ROLE_FAILED("P202", "分配角色失败");
    
    private final String code;
    private final String message;
    
    PermissionErrorCode(String code, String message) {
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
     * 
     * @param code 错误码
     * @return 枚举对象
     */
    public static PermissionErrorCode getByCode(String code) {
        for (PermissionErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}