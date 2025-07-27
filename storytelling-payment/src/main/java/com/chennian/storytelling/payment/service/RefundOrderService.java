package com.chennian.storytelling.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.payment.entity.RefundOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 退款订单服务接口
 *
 * @author chennian
 * @since 2024-01-01
 */
public interface RefundOrderService extends IService<RefundOrder> {

    /**
     * 根据退款订单号查询
     *
     * @param refundOrderNo 退款订单号
     * @return 退款订单
     */
    RefundOrder getByRefundOrderNo(String refundOrderNo);

    /**
     * 根据支付订单ID查询
     *
     * @param paymentOrderId 支付订单ID
     * @return 退款订单列表
     */
    List<RefundOrder> getByPaymentOrderId(Long paymentOrderId);

    /**
     * 根据支付订单号查询
     *
     * @param paymentOrderNo 支付订单号
     * @return 退款订单列表
     */
    List<RefundOrder> getByPaymentOrderNo(String paymentOrderNo);

    /**
     * 根据业务订单号查询
     *
     * @param businessOrderNo 业务订单号
     * @return 退款订单列表
     */
    List<RefundOrder> getByBusinessOrderNo(String businessOrderNo);

    /**
     * 根据第三方退款单号查询
     *
     * @param thirdPartyRefundNo 第三方退款单号
     * @return 退款订单
     */
    RefundOrder getByThirdPartyRefundNo(String thirdPartyRefundNo);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 退款订单列表
     */
    List<RefundOrder> getByUserId(Long userId);

    /**
     * 根据退款状态查询
     *
     * @param refundStatus 退款状态
     * @return 退款订单列表
     */
    List<RefundOrder> getByRefundStatus(String refundStatus);

    /**
     * 根据审核状态查询
     *
     * @param auditStatus 审核状态
     * @return 退款订单列表
     */
    List<RefundOrder> getByAuditStatus(String auditStatus);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 退款订单列表
     */
    List<RefundOrder> getByPaymentChannel(String paymentChannel);

    /**
     * 查询待审核的退款订单
     *
     * @return 退款订单列表
     */
    List<RefundOrder> getPendingAuditOrders();

    /**
     * 查询待处理的退款订单
     *
     * @return 退款订单列表
     */
    List<RefundOrder> getPendingProcessOrders();

    /**
     * 分页查询退款订单
     *
     * @param page            分页参数
     * @param refundOrderNo   退款订单号
     * @param paymentOrderNo  支付订单号
     * @param businessOrderNo 业务订单号
     * @param userId          用户ID
     * @param refundStatus    退款状态
     * @param auditStatus     审核状态
     * @param paymentChannel  支付渠道
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @return 退款订单分页
     */
    IPage<RefundOrder> getRefundOrderPage(Page<RefundOrder> page, String refundOrderNo, String paymentOrderNo,
                                        String businessOrderNo, Long userId, String refundStatus,
                                        String auditStatus, String paymentChannel,
                                        LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 创建退款订单
     *
     * @param refundOrder 退款订单
     * @return 退款订单
     */
    RefundOrder createRefundOrder(RefundOrder refundOrder);

    /**
     * 更新退款状态
     *
     * @param id           订单ID
     * @param refundStatus 退款状态
     * @return 是否成功
     */
    boolean updateRefundStatus(Long id, String refundStatus);

    /**
     * 更新审核信息
     *
     * @param id          订单ID
     * @param auditStatus 审核状态
     * @param auditBy     审核人
     * @param auditTime   审核时间
     * @param auditRemark 审核备注
     * @return 是否成功
     */
    boolean updateAuditInfo(Long id, String auditStatus, String auditBy, LocalDateTime auditTime, String auditRemark);

    /**
     * 更新第三方退款信息
     *
     * @param id                   订单ID
     * @param thirdPartyRefundNo   第三方退款单号
     * @param thirdPartyTradeNo    第三方交易号
     * @param actualRefundAmount   实际退款金额
     * @param refundTime           退款时间
     * @param arrivalTime          到账时间
     * @return 是否成功
     */
    boolean updateThirdPartyRefundInfo(Long id, String thirdPartyRefundNo, String thirdPartyTradeNo,
                                     BigDecimal actualRefundAmount, LocalDateTime refundTime,
                                     LocalDateTime arrivalTime);

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
     * 审核退款
     *
     * @param refundOrderNo 退款订单号
     * @param auditStatus   审核状态
     * @param auditBy       审核人
     * @param auditRemark   审核备注
     * @return 是否成功
     */
    boolean auditRefund(String refundOrderNo, String auditStatus, String auditBy, String auditRemark);

    /**
     * 处理退款
     *
     * @param refundOrderNo 退款订单号
     * @return 是否成功
     */
    boolean processRefund(String refundOrderNo);

    /**
     * 处理退款成功
     *
     * @param refundOrderNo        退款订单号
     * @param thirdPartyRefundNo   第三方退款单号
     * @param thirdPartyTradeNo    第三方交易号
     * @param actualRefundAmount   实际退款金额
     * @param refundTime           退款时间
     * @return 是否成功
     */
    boolean handleRefundSuccess(String refundOrderNo, String thirdPartyRefundNo, String thirdPartyTradeNo,
                              BigDecimal actualRefundAmount, LocalDateTime refundTime);

    /**
     * 处理退款失败
     *
     * @param refundOrderNo 退款订单号
     * @param errorCode     错误码
     * @param errorMessage  错误信息
     * @return 是否成功
     */
    boolean handleRefundFailed(String refundOrderNo, String errorCode, String errorMessage);

    /**
     * 取消退款
     *
     * @param refundOrderNo 退款订单号
     * @param cancelReason  取消原因
     * @return 是否成功
     */
    boolean cancelRefund(String refundOrderNo, String cancelReason);

    /**
     * 统计退款金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    Map<String, Object> statisticsRefundAmount(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按渠道统计退款金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsRefundAmountByChannel(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按状态统计退款订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsRefundOrderCountByStatus(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询用户退款总金额
     *
     * @param userId 用户ID
     * @return 退款总金额
     */
    BigDecimal getUserTotalRefundAmount(Long userId);

    /**
     * 查询用户退款次数
     *
     * @param userId 用户ID
     * @return 退款次数
     */
    Long getUserRefundCount(Long userId);

    /**
     * 查询支付订单的退款总金额
     *
     * @param paymentOrderNo 支付订单号
     * @return 退款总金额
     */
    BigDecimal getPaymentOrderRefundAmount(String paymentOrderNo);

    /**
     * 检查是否可以退款
     *
     * @param paymentOrderNo 支付订单号
     * @param refundAmount   退款金额
     * @return 是否可以退款
     */
    boolean canRefund(String paymentOrderNo, BigDecimal refundAmount);

    /**
     * 获取可退款金额
     *
     * @param paymentOrderNo 支付订单号
     * @return 可退款金额
     */
    BigDecimal getRefundableAmount(String paymentOrderNo);

    /**
     * 生成退款订单号
     *
     * @return 退款订单号
     */
    String generateRefundOrderNo();

    /**
     * 验证退款订单
     *
     * @param refundOrder 退款订单
     * @return 验证结果
     */
    boolean validateRefundOrder(RefundOrder refundOrder);

    /**
     * 计算退款手续费
     *
     * @param refundAmount   退款金额
     * @param paymentChannel 支付渠道
     * @return 退款手续费
     */
    BigDecimal calculateRefundFee(BigDecimal refundAmount, String paymentChannel);

    /**
     * 查询退款进度
     *
     * @param refundOrderNo 退款订单号
     * @return 退款进度
     */
    Map<String, Object> getRefundProgress(String refundOrderNo);

    /**
     * 同步第三方退款状态
     *
     * @param refundOrderNo 退款订单号
     * @return 是否成功
     */
    boolean syncThirdPartyRefundStatus(String refundOrderNo);

    /**
     * 批量处理待审核订单
     *
     * @return 处理数量
     */
    int batchProcessPendingAuditOrders();

    /**
     * 批量处理待处理订单
     *
     * @return 处理数量
     */
    int batchProcessPendingOrders();
}