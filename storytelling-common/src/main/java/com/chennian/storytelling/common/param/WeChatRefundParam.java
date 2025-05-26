package com.chennian.storytelling.common.param;


import com.chennian.storytelling.common.enums.WxNotifyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;

/**
 * @author by chennian
 * @date 2023/5/16 14:20
 */
@Data
public class WeChatRefundParam {
    /**
     * 微信支付订单号，微信支付订单号和商家订单号二选一
     */
    private String transactionId;

    /**
     * 商家订单号，对应 out_trade_no，
     */
    @NotNull(message = "商户订单号不能为空")
    private String orderId;

    /**
     * 商户退款单号，对应out_refund_no
     */
    private String refundOrderId;

    /**
     * 退款原因，选填
     */
    private String reason;

    /**
     * 回调地址
     */
    private WxNotifyType notify;

    /**
     * 退款金额
     */
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundMoney;

    /**
     * 原订单金额，必填
     */
    private BigDecimal totalMoney;
}
