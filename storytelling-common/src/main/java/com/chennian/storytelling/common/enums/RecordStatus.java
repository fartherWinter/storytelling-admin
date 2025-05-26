package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/5/4 14:24
 */
public enum RecordStatus {
    /**
     * 充值
     */
    RECHARGE(0),
    /**
     * 扣款
     */
    DEDUCTION(1);
    private int value;

    RecordStatus(int value) {
        this.value = value;
    }
    public Integer getValue() {
        return value;
    }
}
