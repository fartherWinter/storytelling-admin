package com.chennian.storytelling.payment.service;

import com.chennian.storytelling.payment.entity.PaymentOrder;
import com.chennian.storytelling.payment.entity.RefundOrder;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付服务核心接口
 *
 * @author chennian
 * @since 2024-01-01
 */
public interface PaymentService {

    /**
     * 创建支付订单
     *
     * @param businessOrderNo 业务订单号
     * @param businessType    业务类型
     * @param userId          用户ID
     * @param paymentChannel  支付渠道
     * @param paymentMethod   支付方式
     * @param paymentAmount   支付金额
     * @param currency        货币类型
     * @param subject         支付主题
     * @param description     支付描述
     * @param notifyUrl       通知地址
     * @param returnUrl       返回地址
     * @param clientIp        客户端IP
     * @param userAgent       用户代理
     * @param deviceInfo      设备信息
     * @param extParams       扩展参数
     * @return 支付订单
     */
    PaymentOrder createPayment(String businessOrderNo, String businessType, Long userId,
                             String paymentChannel, String paymentMethod, BigDecimal paymentAmount,
                             String currency, String subject, String description,
                             String notifyUrl, String returnUrl, String clientIp,
                             String userAgent, String deviceInfo, Map<String, Object> extParams);

    /**
     * 发起支付
     *
     * @param paymentOrderNo 支付订单号
     * @param clientIp       客户端IP
     * @param userAgent      用户代理
     * @param deviceInfo     设备信息
     * @return 支付结果
     */
    Map<String, Object> initiatePayment(String paymentOrderNo, String clientIp, String userAgent, String deviceInfo);

    /**
     * 查询支付状态
     *
     * @param paymentOrderNo 支付订单号
     * @return 支付状态
     */
    Map<String, Object> queryPaymentStatus(String paymentOrderNo);

    /**
     * 处理支付通知
     *
     * @param paymentChannel 支付渠道
     * @param notifyData     通知数据
     * @return 处理结果
     */
    Map<String, Object> handlePaymentNotify(String paymentChannel, Map<String, Object> notifyData);

    /**
     * 处理支付回调
     *
     * @param paymentChannel 支付渠道
     * @param callbackData   回调数据
     * @return 处理结果
     */
    Map<String, Object> handlePaymentCallback(String paymentChannel, Map<String, Object> callbackData);

    /**
     * 取消支付
     *
     * @param paymentOrderNo 支付订单号
     * @param cancelReason   取消原因
     * @return 是否成功
     */
    boolean cancelPayment(String paymentOrderNo, String cancelReason);

    /**
     * 关闭支付
     *
     * @param paymentOrderNo 支付订单号
     * @param closeReason    关闭原因
     * @return 是否成功
     */
    boolean closePayment(String paymentOrderNo, String closeReason);

    /**
     * 申请退款
     *
     * @param paymentOrderNo 支付订单号
     * @param refundAmount   退款金额
     * @param refundReason   退款原因
     * @param refundType     退款类型
     * @param userId         用户ID
     * @param clientIp       客户端IP
     * @param deviceInfo     设备信息
     * @return 退款订单
     */
    RefundOrder applyRefund(String paymentOrderNo, BigDecimal refundAmount, String refundReason,
                          String refundType, Long userId, String clientIp, String deviceInfo);

    /**
     * 处理退款
     *
     * @param refundOrderNo 退款订单号
     * @return 是否成功
     */
    boolean processRefund(String refundOrderNo);

    /**
     * 查询退款状态
     *
     * @param refundOrderNo 退款订单号
     * @return 退款状态
     */
    Map<String, Object> queryRefundStatus(String refundOrderNo);

    /**
     * 处理退款通知
     *
     * @param paymentChannel 支付渠道
     * @param notifyData     通知数据
     * @return 处理结果
     */
    Map<String, Object> handleRefundNotify(String paymentChannel, Map<String, Object> notifyData);

    /**
     * 同步支付状态
     *
     * @param paymentOrderNo 支付订单号
     * @return 是否成功
     */
    boolean syncPaymentStatus(String paymentOrderNo);

    /**
     * 同步退款状态
     *
     * @param refundOrderNo 退款订单号
     * @return 是否成功
     */
    boolean syncRefundStatus(String refundOrderNo);

    /**
     * 验证支付签名
     *
     * @param paymentChannel 支付渠道
     * @param data           数据
     * @param signature      签名
     * @return 验证结果
     */
    boolean verifyPaymentSignature(String paymentChannel, Map<String, Object> data, String signature);

    /**
     * 生成支付签名
     *
     * @param paymentChannel 支付渠道
     * @param data           数据
     * @return 签名
     */
    String generatePaymentSignature(String paymentChannel, Map<String, Object> data);

    /**
     * 获取支付渠道配置
     *
     * @param paymentChannel 支付渠道
     * @return 渠道配置
     */
    Map<String, Object> getPaymentChannelConfig(String paymentChannel);

    /**
     * 检查支付渠道是否可用
     *
     * @param paymentChannel 支付渠道
     * @return 是否可用
     */
    boolean isPaymentChannelAvailable(String paymentChannel);

    /**
     * 获取支持的支付方式
     *
     * @param paymentChannel 支付渠道
     * @return 支付方式列表
     */
    java.util.List<String> getSupportedPaymentMethods(String paymentChannel);

    /**
     * 获取支付限额
     *
     * @param paymentChannel 支付渠道
     * @param paymentMethod  支付方式
     * @return 支付限额
     */
    Map<String, BigDecimal> getPaymentLimits(String paymentChannel, String paymentMethod);

    /**
     * 计算支付手续费
     *
     * @param paymentChannel 支付渠道
     * @param paymentMethod  支付方式
     * @param paymentAmount  支付金额
     * @return 手续费
     */
    BigDecimal calculatePaymentFee(String paymentChannel, String paymentMethod, BigDecimal paymentAmount);

    /**
     * 计算退款手续费
     *
     * @param paymentChannel 支付渠道
     * @param paymentMethod  支付方式
     * @param refundAmount   退款金额
     * @return 退款手续费
     */
    BigDecimal calculateRefundFee(String paymentChannel, String paymentMethod, BigDecimal refundAmount);

    /**
     * 获取推荐的支付渠道
     *
     * @param paymentMethod 支付方式
     * @param paymentAmount 支付金额
     * @param currency      货币类型
     * @return 推荐渠道列表
     */
    java.util.List<String> getRecommendedPaymentChannels(String paymentMethod, BigDecimal paymentAmount, String currency);

    /**
     * 预处理支付订单
     *
     * @param paymentOrder 支付订单
     * @return 预处理结果
     */
    Map<String, Object> preprocessPaymentOrder(PaymentOrder paymentOrder);

    /**
     * 后处理支付结果
     *
     * @param paymentOrder 支付订单
     * @param paymentResult 支付结果
     * @return 后处理结果
     */
    Map<String, Object> postprocessPaymentResult(PaymentOrder paymentOrder, Map<String, Object> paymentResult);

    /**
     * 处理支付异常
     *
     * @param paymentOrderNo 支付订单号
     * @param exception      异常信息
     * @return 处理结果
     */
    Map<String, Object> handlePaymentException(String paymentOrderNo, Exception exception);

    /**
     * 处理退款异常
     *
     * @param refundOrderNo 退款订单号
     * @param exception     异常信息
     * @return 处理结果
     */
    Map<String, Object> handleRefundException(String refundOrderNo, Exception exception);

    /**
     * 重试支付
     *
     * @param paymentOrderNo 支付订单号
     * @return 重试结果
     */
    Map<String, Object> retryPayment(String paymentOrderNo);

    /**
     * 重试退款
     *
     * @param refundOrderNo 退款订单号
     * @return 重试结果
     */
    Map<String, Object> retryRefund(String refundOrderNo);

    /**
     * 批量处理超时订单
     *
     * @return 处理结果
     */
    Map<String, Object> batchProcessTimeoutOrders();

    /**
     * 批量同步支付状态
     *
     * @param paymentOrderNos 支付订单号列表
     * @return 同步结果
     */
    Map<String, Object> batchSyncPaymentStatus(java.util.List<String> paymentOrderNos);

    /**
     * 批量同步退款状态
     *
     * @param refundOrderNos 退款订单号列表
     * @return 同步结果
     */
    Map<String, Object> batchSyncRefundStatus(java.util.List<String> refundOrderNos);
}