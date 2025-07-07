package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 支付方式枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum PaymentTypeEnum {

    /**
     * 支付宝
     */
    ALIPAY(1, "支付宝"),

    /**
     * 微信支付
     */
    WECHAT_PAY(2, "微信支付"),

    /**
     * 银行卡
     */
    BANK_CARD(3, "银行卡"),

    /**
     * 现金
     */
    CASH(4, "现金"),

    /**
     * 余额支付
     */
    BALANCE(5, "余额支付");

    private final Integer code;
    private final String name;

    PaymentTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static PaymentTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PaymentTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(Integer code) {
        PaymentTypeEnum typeEnum = getByCode(code);
        return typeEnum != null ? typeEnum.getName() : "未知支付方式";
    }

}