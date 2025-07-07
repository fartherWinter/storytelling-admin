package com.chennian.storytelling.bean.enums;

/**
 * 发布状态枚举
 * @author chennian
 */
public enum PublishStatusEnum {
    
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    UNPUBLISHED(2, "已下线"),
    SCHEDULED(3, "定时发布"),
    REVIEWING(4, "审核中");
    
    private final Integer code;
    private final String name;
    
    PublishStatusEnum(Integer code, String name) {
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
    public static PublishStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PublishStatusEnum status : values()) {
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
        PublishStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
    
    /**
     * 判断是否已发布
     * @param code 编码
     * @return 是否已发布
     */
    public static boolean isPublished(Integer code) {
        return PUBLISHED.getCode().equals(code);
    }
    
    /**
     * 判断是否为草稿状态
     * @param code 编码
     * @return 是否为草稿
     */
    public static boolean isDraft(Integer code) {
        return DRAFT.getCode().equals(code);
    }
    
    /**
     * 判断是否可编辑（草稿或已下线）
     * @param code 编码
     * @return 是否可编辑
     */
    public static boolean isEditable(Integer code) {
        return DRAFT.getCode().equals(code) || UNPUBLISHED.getCode().equals(code);
    }
    
    /**
     * 判断是否对外可见（已发布）
     * @param code 编码
     * @return 是否对外可见
     */
    public static boolean isVisible(Integer code) {
        return PUBLISHED.getCode().equals(code);
    }
}