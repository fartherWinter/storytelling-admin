package com.chennian.storytelling.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.payment.entity.PaymentRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付记录服务接口
 *
 * @author chennian
 * @since 2024-01-01
 */
public interface PaymentRecordService extends IService<PaymentRecord> {

    /**
     * 根据支付订单ID查询
     *
     * @param paymentOrderId 支付订单ID
     * @return 支付记录列表
     */
    List<PaymentRecord> getByPaymentOrderId(Long paymentOrderId);

    /**
     * 根据支付订单号查询
     *
     * @param paymentOrderNo 支付订单号
     * @return 支付记录列表
     */
    List<PaymentRecord> getByPaymentOrderNo(String paymentOrderNo);

    /**
     * 根据记录类型查询
     *
     * @param recordType 记录类型
     * @return 支付记录列表
     */
    List<PaymentRecord> getByRecordType(String recordType);

    /**
     * 根据操作类型查询
     *
     * @param operationType 操作类型
     * @return 支付记录列表
     */
    List<PaymentRecord> getByOperationType(String operationType);

    /**
     * 根据操作状态查询
     *
     * @param operationStatus 操作状态
     * @return 支付记录列表
     */
    List<PaymentRecord> getByOperationStatus(String operationStatus);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 支付记录列表
     */
    List<PaymentRecord> getByPaymentChannel(String paymentChannel);

    /**
     * 查询失败的记录
     *
     * @return 支付记录列表
     */
    List<PaymentRecord> getFailedRecords();

    /**
     * 查询需要重试的记录
     *
     * @return 支付记录列表
     */
    List<PaymentRecord> getRetryRecords();

    /**
     * 分页查询支付记录
     *
     * @param page           分页参数
     * @param paymentOrderId 支付订单ID
     * @param recordType     记录类型
     * @param operationType  操作类型
     * @param operationStatus 操作状态
     * @param paymentChannel 支付渠道
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @return 支付记录分页
     */
    IPage<PaymentRecord> getRecordPage(Page<PaymentRecord> page, Long paymentOrderId, String recordType,
                                     String operationType, String operationStatus, String paymentChannel,
                                     LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 创建支付记录
     *
     * @param paymentRecord 支付记录
     * @return 支付记录
     */
    PaymentRecord createPaymentRecord(PaymentRecord paymentRecord);

    /**
     * 更新支付记录
     *
     * @param paymentRecord 支付记录
     * @return 是否成功
     */
    boolean updatePaymentRecord(PaymentRecord paymentRecord);

    /**
     * 记录支付请求
     *
     * @param paymentOrderId   支付订单ID
     * @param paymentOrderNo   支付订单号
     * @param paymentChannel   支付渠道
     * @param paymentMethod    支付方式
     * @param requestParams    请求参数
     * @param clientIp         客户端IP
     * @param userAgent        用户代理
     * @param deviceInfo       设备信息
     * @return 支付记录
     */
    PaymentRecord recordPaymentRequest(Long paymentOrderId, String paymentOrderNo, String paymentChannel,
                                     String paymentMethod, String requestParams, String clientIp,
                                     String userAgent, String deviceInfo);

    /**
     * 记录支付响应
     *
     * @param recordId           记录ID
     * @param operationStatus    操作状态
     * @param thirdPartyTradeNo  第三方交易号
     * @param thirdPartyOrderNo  第三方订单号
     * @param responseParams     响应参数
     * @param errorCode          错误码
     * @param errorMessage       错误信息
     * @param processingTime     处理时长
     * @return 是否成功
     */
    boolean recordPaymentResponse(Long recordId, String operationStatus, String thirdPartyTradeNo,
                                String thirdPartyOrderNo, String responseParams, String errorCode,
                                String errorMessage, Long processingTime);

    /**
     * 记录退款请求
     *
     * @param paymentOrderId   支付订单ID
     * @param paymentOrderNo   支付订单号
     * @param paymentChannel   支付渠道
     * @param paymentMethod    支付方式
     * @param requestParams    请求参数
     * @param clientIp         客户端IP
     * @param userAgent        用户代理
     * @param deviceInfo       设备信息
     * @return 支付记录
     */
    PaymentRecord recordRefundRequest(Long paymentOrderId, String paymentOrderNo, String paymentChannel,
                                    String paymentMethod, String requestParams, String clientIp,
                                    String userAgent, String deviceInfo);

    /**
     * 记录退款响应
     *
     * @param recordId           记录ID
     * @param operationStatus    操作状态
     * @param thirdPartyTradeNo  第三方交易号
     * @param thirdPartyOrderNo  第三方订单号
     * @param responseParams     响应参数
     * @param errorCode          错误码
     * @param errorMessage       错误信息
     * @param processingTime     处理时长
     * @return 是否成功
     */
    boolean recordRefundResponse(Long recordId, String operationStatus, String thirdPartyTradeNo,
                               String thirdPartyOrderNo, String responseParams, String errorCode,
                               String errorMessage, Long processingTime);

    /**
     * 记录查询请求
     *
     * @param paymentOrderId   支付订单ID
     * @param paymentOrderNo   支付订单号
     * @param paymentChannel   支付渠道
     * @param paymentMethod    支付方式
     * @param requestParams    请求参数
     * @param clientIp         客户端IP
     * @param userAgent        用户代理
     * @param deviceInfo       设备信息
     * @return 支付记录
     */
    PaymentRecord recordQueryRequest(Long paymentOrderId, String paymentOrderNo, String paymentChannel,
                                   String paymentMethod, String requestParams, String clientIp,
                                   String userAgent, String deviceInfo);

    /**
     * 记录查询响应
     *
     * @param recordId           记录ID
     * @param operationStatus    操作状态
     * @param thirdPartyTradeNo  第三方交易号
     * @param thirdPartyOrderNo  第三方订单号
     * @param responseParams     响应参数
     * @param errorCode          错误码
     * @param errorMessage       错误信息
     * @param processingTime     处理时长
     * @return 是否成功
     */
    boolean recordQueryResponse(Long recordId, String operationStatus, String thirdPartyTradeNo,
                              String thirdPartyOrderNo, String responseParams, String errorCode,
                              String errorMessage, Long processingTime);

    /**
     * 增加重试次数
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean incrementRetryCount(Long recordId);

    /**
     * 统计操作类型分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsOperationTypeDistribution(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计操作状态分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsOperationStatusDistribution(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计渠道分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsChannelDistribution(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计平均处理时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 平均处理时间
     */
    Double getAverageProcessingTime(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计成功率
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 成功率
     */
    Double getSuccessRate(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询慢操作记录
     *
     * @param threshold 阈值（毫秒）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 支付记录列表
     */
    List<PaymentRecord> getSlowOperationRecords(Long threshold, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除过期记录
     *
     * @param expireTime 过期时间
     * @return 删除数量
     */
    int deleteExpiredRecords(LocalDateTime expireTime);

    /**
     * 导出支付记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param recordType 记录类型
     * @param operationType 操作类型
     * @param operationStatus 操作状态
     * @param paymentChannel 支付渠道
     * @return 支付记录列表
     */
    List<PaymentRecord> exportPaymentRecords(LocalDateTime startTime, LocalDateTime endTime, String recordType,
                                           String operationType, String operationStatus, String paymentChannel);

    /**
     * 获取记录详情
     *
     * @param recordId 记录ID
     * @return 记录详情
     */
    Map<String, Object> getRecordDetail(Long recordId);

    /**
     * 验证记录完整性
     *
     * @param recordId 记录ID
     * @return 验证结果
     */
    boolean validateRecordIntegrity(Long recordId);
}