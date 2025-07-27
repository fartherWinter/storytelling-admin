package com.chennian.storytelling.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.payment.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付记录Mapper接口
 *
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 根据支付订单ID查询记录
     *
     * @param paymentOrderId 支付订单ID
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE payment_order_id = #{paymentOrderId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByPaymentOrderId(@Param("paymentOrderId") Long paymentOrderId);

    /**
     * 根据支付订单号查询记录
     *
     * @param paymentOrderNo 支付订单号
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE payment_order_no = #{paymentOrderNo} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByPaymentOrderNo(@Param("paymentOrderNo") String paymentOrderNo);

    /**
     * 根据记录类型查询
     *
     * @param recordType 记录类型
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE record_type = #{recordType} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByRecordType(@Param("recordType") String recordType);

    /**
     * 根据操作类型查询
     *
     * @param operationType 操作类型
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE operation_type = #{operationType} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByOperationType(@Param("operationType") String operationType);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE status = #{status} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByStatus(@Param("status") String status);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE payment_channel = #{paymentChannel} AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectByPaymentChannel(@Param("paymentChannel") String paymentChannel);

    /**
     * 查询失败的记录
     *
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE status = 'FAILED' AND is_deleted = 0 ORDER BY create_time DESC")
    List<PaymentRecord> selectFailedRecords();

    /**
     * 查询需要重试的记录
     *
     * @param currentTime 当前时间
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE status = 'FAILED' AND retry_count < max_retry_count AND next_retry_time <= #{currentTime} AND is_deleted = 0 ORDER BY create_time ASC")
    List<PaymentRecord> selectRetryRecords(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 分页查询支付记录
     *
     * @param page           分页参数
     * @param paymentOrderId 支付订单ID
     * @param recordType     记录类型
     * @param operationType  操作类型
     * @param status         状态
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @return 支付记录分页
     */
    @Select("<script>" +
            "SELECT * FROM payment_record WHERE is_deleted = 0" +
            "<if test='paymentOrderId != null'> AND payment_order_id = #{paymentOrderId}</if>" +
            "<if test='recordType != null and recordType != \"\\'> AND record_type = #{recordType}</if>" +
            "<if test='operationType != null and operationType != \"\\'> AND operation_type = #{operationType}</if>" +
            "<if test='status != null and status != \"\\'> AND status = #{status}</if>" +
            "<if test='startTime != null'> AND create_time >= #{startTime}</if>" +
            "<if test='endTime != null'> AND create_time <= #{endTime}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    IPage<PaymentRecord> selectPageWithConditions(Page<PaymentRecord> page,
                                                 @Param("paymentOrderId") Long paymentOrderId,
                                                 @Param("recordType") String recordType,
                                                 @Param("operationType") String operationType,
                                                 @Param("status") String status,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 统计操作类型分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT operation_type, COUNT(*) as count FROM payment_record WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY operation_type")
    List<Map<String, Object>> statisticsOperationType(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计状态分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM payment_record WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY status")
    List<Map<String, Object>> statisticsStatus(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计渠道分布
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT payment_channel, COUNT(*) as count FROM payment_record WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY payment_channel")
    List<Map<String, Object>> statisticsPaymentChannel(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计平均处理时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 平均处理时间（毫秒）
     */
    @Select("SELECT AVG(process_time) FROM payment_record WHERE create_time BETWEEN #{startTime} AND #{endTime} AND process_time IS NOT NULL AND is_deleted = 0")
    Double statisticsAvgProcessTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计成功率
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 成功率
     */
    @Select("SELECT (COUNT(CASE WHEN status = 'SUCCESS' THEN 1 END) * 100.0 / COUNT(*)) as successRate FROM payment_record WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0")
    Double statisticsSuccessRate(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询慢操作记录
     *
     * @param minProcessTime 最小处理时间（毫秒）
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @return 支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE process_time >= #{minProcessTime} AND create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 ORDER BY process_time DESC")
    List<PaymentRecord> selectSlowRecords(@Param("minProcessTime") Long minProcessTime,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 删除过期记录
     *
     * @param expireTime 过期时间
     * @return 删除行数
     */
    @Select("UPDATE payment_record SET is_deleted = 1, update_time = NOW() WHERE create_time < #{expireTime} AND is_deleted = 0")
    int deleteExpiredRecords(@Param("expireTime") LocalDateTime expireTime);
}