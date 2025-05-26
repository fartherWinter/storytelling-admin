package com.chennian.storytelling.common.bean;

import com.chennian.storytelling.common.enums.WxNotifyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 回调地址枚举类
 *
 * @author by chennian
 * @date 2023/3/30 19:01
 */
@Data
public class WeChatBasePayData {
    /**
     * 商品描述
     */
    private String title;

    /**
     * 商家订单号，对应 out_trade_no
     */
    private String orderId;

    /**
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 回调地址
     */
    private WxNotifyType notify;
}
