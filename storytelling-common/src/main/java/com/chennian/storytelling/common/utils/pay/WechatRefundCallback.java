package com.chennian.storytelling.common.utils.pay;

/**
 * 退款处理接口，为了防止项目开发人员，不手动判断退款失败的情况
 *
 * @author by chennian
 * @date 2023/5/18 14:34
 */
public interface WechatRefundCallback {
    /**
     * 退款成功处理情况
     */
    void success(WxchatCallbackRefundData refundData);

    /**
     * 退款失败处理情况
     */
    void fail(WxchatCallbackRefundData refundData);

}
