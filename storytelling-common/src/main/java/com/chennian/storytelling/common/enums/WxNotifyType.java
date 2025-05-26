package com.chennian.storytelling.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author by chennian
 * @date 2023/3/30 19:03
 */
@Getter
@AllArgsConstructor
public enum WxNotifyType {

    /**
     * 微信支付通知
     */
    PAY_BACK("/common/pay/wx/callback"),



    ;

    /**
     * 类型
     */
    private final String type;
}
