package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/12/4 9:14
 */
public enum AppExamineStatus {
    /**
     * 审核状态枚举
     */
    OFF_SHELF("下架", 0),
    GROUNDING("上架", 1),
    AUDIT("待审核", 2),
    APPROVED("已审核", 3),
    REVIEW_REJECT("审核拒绝", 4),
    ;

    private final String type;
    private final Integer method;

    AppExamineStatus(String type, Integer method) {
        this.method = method;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Integer getMethod() {
        return method;
    }
}
