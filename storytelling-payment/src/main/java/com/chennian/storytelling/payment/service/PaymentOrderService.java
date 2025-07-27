package com.chennian.storytelling.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.payment.entity.PaymentOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付订单服务接口
 *
 * @author chennian
 * @since 2024-01-01
 */
public interface PaymentOrderService extends IService<PaymentOrder> {

    /**
     * 创建支付订单
     *
     * @param paymentOrder 支付订单
     * @return 支付订单
     */
    PaymentOrder createPaymentOrder(PaymentOrder paymentOrder);

    /**
     * 根据支付订单号查询
     *
     * @param paymentOrderNo 支付订单号
     * @return 支付订单
     */
    PaymentOrder getByPaymentOrderNo(String paymentOrderNo);

    /**
     * 根据业务订单号查询
     *
     * @param businessOrderNo 业务订单号
     * @return 支付订单列表
     */
    List<PaymentOrder> getByBusinessOrderNo(String businessOrderNo);

    /**
     * 根据第三方交易号查询
     *
     * @param thirdPartyTradeNo 第三方交易号
     * @return 支付订单
     */
    PaymentOrder getByThirdPartyTradeNo(String thirdPartyTradeNo);

    /**
     * 根据用户ID分页查询
     *
     * @param page   分页参数
     * @param userId 用户ID
     * @return 支付订单分页
     */
    IPage<PaymentOrder> getPageByUserId(Page<PaymentOrder> page, Long userId);

    /**
     * 根据支付状态查询
     *
     * @param paymentStatus 支付状态
     * @return 支付订单列表
     */
    List<PaymentOrder> getByPaymentStatus(String paymentStatus);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 支付订单列表
     */
    List<PaymentOrder> getByPaymentChannel(String paymentChannel);

    /**
     * 查询超时未支付订单
     *
     * @return 支付订单列表
     */
    List<PaymentOrder> getTimeoutOrders();

    /**
     * 更新支付状态
     *
     * @param id            订单ID
     * @param paymentStatus 支付状态
     * @return 是否成功
     */
    boolean updatePaymentStatus(Long id, String paymentStatus);

    /**
     * 更新第三方交易信息
     *
     * @param id                  订单ID
     * @param thirdPartyTradeNo   第三方交易号
     * @param thirdPartyOrderNo   第三方订单号
     * @param actualAmount        实际支付金额
     * @param paymentTime         支付时间
     * @return 是否成功
     */
    boolean updateThirdPartyInfo(Long id, String thirdPartyTradeNo, String thirdPartyOrderNo, 
                                BigDecimal actualAmount, LocalDateTime paymentTime);

    /**
     * 更新退款信息
     *
     * @param id               订单ID
     * @param refundStatus     退款状态
     * @param refundedAmount   已退款金额
     * @param refundableAmount 可退款金额
     * @return 是否成功
     */
    boolean updateRefundInfo(Long id, String refundStatus, BigDecimal refundedAmount, BigDecimal refundableAmount);

    /**
     * 处理支付成功
     *
     * @param paymentOrderNo    支付订单号
     * @param thirdPartyTradeNo 第三方交易号
     * @param thirdPartyOrderNo 第三方订单号
     * @param actualAmount      实际支付金额
     * @param paymentTime       支付时间
     * @return 是否成功
     */
    boolean handlePaymentSuccess(String paymentOrderNo, String thirdPartyTradeNo, String thirdPartyOrderNo,
                               BigDecimal actualAmount, LocalDateTime paymentTime);

    /**
     * 处理支付失败
     *
     * @param paymentOrderNo 支付订单号
     * @param errorCode      错误码
     * @param errorMessage   错误信息
     * @return 是否成功
     */
    boolean handlePaymentFailed(String paymentOrderNo, String errorCode, String errorMessage);

    /**
     * 处理支付超时
     *
     * @param paymentOrderNo 支付订单号
     * @return 是否成功
     */
    boolean handlePaymentTimeout(String paymentOrderNo);

    /**
     * 批量处理超时订单
     *
     * @return 处理数量
     */
    int batchHandleTimeoutOrders();

    /**
     * 统计支付金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    Map<String, Object> statisticsPaymentAmount(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按渠道统计支付金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsPaymentAmountByChannel(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按状态统计订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsOrderCountByStatus(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询用户支付总金额
     *
     * @param userId 用户ID
     * @return 支付总金额
     */
    BigDecimal getUserTotalPaymentAmount(Long userId);

    /**
     * 查询用户支付次数
     *
     * @param userId 用户ID
     * @return 支付次数
     */
    Long getUserPaymentCount(Long userId);

    /**
     * 检查订单是否可以支付
     *
     * @param paymentOrderNo 支付订单号
     * @return 是否可以支付
     */
    boolean canPay(String paymentOrderNo);

    /**
     * 检查订单是否可以退款
     *
     * @param paymentOrderNo 支付订单号
     * @return 是否可以退款
     */
    boolean canRefund(String paymentOrderNo);

    /**
     * 获取可退款金额
     *
     * @param paymentOrderNo 支付订单号
     * @return 可退款金额
     */
    BigDecimal getRefundableAmount(String paymentOrderNo);

    /**
     * 生成支付订单号
     *
     * @return 支付订单号
     */
    String generatePaymentOrderNo();

    /**
     * 验证支付订单
     *
     * @param paymentOrder 支付订单
     * @return 验证结果
     */
    boolean validatePaymentOrder(PaymentOrder paymentOrder);

    /**
     * 计算手续费
     *
     * @param paymentAmount  支付金额
     * @param paymentChannel 支付渠道
     * @return 手续费
     */
    BigDecimal calculateFee(BigDecimal paymentAmount, String paymentChannel);

    /**
     * 设置订单过期时间
     *
     * @param paymentOrder 支付订单
     * @param timeoutMinutes 超时分钟数
     */
    void setExpireTime(PaymentOrder paymentOrder, Integer timeoutMinutes);
}