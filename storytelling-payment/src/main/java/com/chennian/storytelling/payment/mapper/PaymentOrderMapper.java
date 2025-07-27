package com.chennian.storytelling.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.payment.entity.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付订单Mapper接口
 *
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {

    /**
     * 根据支付订单号查询
     *
     * @param paymentOrderNo 支付订单号
     * @return 支付订单
     */
    @Select("SELECT * FROM payment_order WHERE payment_order_no = #{paymentOrderNo} AND is_deleted = 0")
    PaymentOrder selectByPaymentOrderNo(@Param("paymentOrderNo") String paymentOrderNo);

    /**
     * 根据业务订单号查询
     *
     * @param businessOrderNo 业务订单号
     * @return 支付订单列表
     */
    @Select("SELECT * FROM payment_order WHERE business_order_no = #{businessOrderNo} AND is_deleted = 0")
    List<PaymentOrder> selectByBusinessOrderNo(@Param("businessOrderNo") String businessOrderNo);

    /**
     * 根据第三方交易号查询
     *
     * @param thirdPartyTradeNo 第三方交易号
     * @return 支付订单
     */
    @Select("SELECT * FROM payment_order WHERE third_party_trade_no = #{thirdPartyTradeNo} AND is_deleted = 0")
    PaymentOrder selectByThirdPartyTradeNo(@Param("thirdPartyTradeNo") String thirdPartyTradeNo);

    /**
     * 根据用户ID分页查询
     *
     * @param page   分页参数
     * @param userId 用户ID
     * @return 支付订单分页
     */
    @Select("SELECT * FROM payment_order WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<PaymentOrder> selectPageByUserId(Page<PaymentOrder> page, @Param("userId") Long userId);

    /**
     * 根据支付状态查询
     *
     * @param paymentStatus 支付状态
     * @return 支付订单列表
     */
    @Select("SELECT * FROM payment_order WHERE payment_status = #{paymentStatus} AND is_deleted = 0")
    List<PaymentOrder> selectByPaymentStatus(@Param("paymentStatus") String paymentStatus);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 支付订单列表
     */
    @Select("SELECT * FROM payment_order WHERE payment_channel = #{paymentChannel} AND is_deleted = 0")
    List<PaymentOrder> selectByPaymentChannel(@Param("paymentChannel") String paymentChannel);

    /**
     * 查询超时未支付订单
     *
     * @param expireTime 过期时间
     * @return 支付订单列表
     */
    @Select("SELECT * FROM payment_order WHERE payment_status = 'PENDING' AND expire_time < #{expireTime} AND is_deleted = 0")
    List<PaymentOrder> selectTimeoutOrders(@Param("expireTime") LocalDateTime expireTime);

    /**
     * 更新支付状态
     *
     * @param id            订单ID
     * @param paymentStatus 支付状态
     * @param version       版本号
     * @return 更新行数
     */
    @Update("UPDATE payment_order SET payment_status = #{paymentStatus}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updatePaymentStatus(@Param("id") Long id, @Param("paymentStatus") String paymentStatus, @Param("version") Integer version);

    /**
     * 更新第三方交易信息
     *
     * @param id                  订单ID
     * @param thirdPartyTradeNo   第三方交易号
     * @param thirdPartyOrderNo   第三方订单号
     * @param actualAmount        实际支付金额
     * @param paymentTime         支付时间
     * @param version             版本号
     * @return 更新行数
     */
    @Update("UPDATE payment_order SET third_party_trade_no = #{thirdPartyTradeNo}, third_party_order_no = #{thirdPartyOrderNo}, actual_amount = #{actualAmount}, payment_time = #{paymentTime}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateThirdPartyInfo(@Param("id") Long id, 
                           @Param("thirdPartyTradeNo") String thirdPartyTradeNo,
                           @Param("thirdPartyOrderNo") String thirdPartyOrderNo,
                           @Param("actualAmount") BigDecimal actualAmount,
                           @Param("paymentTime") LocalDateTime paymentTime,
                           @Param("version") Integer version);

    /**
     * 更新退款信息
     *
     * @param id               订单ID
     * @param refundStatus     退款状态
     * @param refundedAmount   已退款金额
     * @param refundableAmount 可退款金额
     * @param version          版本号
     * @return 更新行数
     */
    @Update("UPDATE payment_order SET refund_status = #{refundStatus}, refunded_amount = #{refundedAmount}, refundable_amount = #{refundableAmount}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateRefundInfo(@Param("id") Long id,
                        @Param("refundStatus") String refundStatus,
                        @Param("refundedAmount") BigDecimal refundedAmount,
                        @Param("refundableAmount") BigDecimal refundableAmount,
                        @Param("version") Integer version);

    /**
     * 统计支付金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT COUNT(*) as orderCount, COALESCE(SUM(actual_amount), 0) as totalAmount FROM payment_order WHERE payment_status = 'SUCCESS' AND payment_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0")
    Map<String, Object> statisticsPaymentAmount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 按渠道统计支付金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT payment_channel, COUNT(*) as orderCount, COALESCE(SUM(actual_amount), 0) as totalAmount FROM payment_order WHERE payment_status = 'SUCCESS' AND payment_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY payment_channel")
    List<Map<String, Object>> statisticsPaymentAmountByChannel(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 按状态统计订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT payment_status, COUNT(*) as orderCount FROM payment_order WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY payment_status")
    List<Map<String, Object>> statisticsOrderCountByStatus(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户支付总金额
     *
     * @param userId 用户ID
     * @return 支付总金额
     */
    @Select("SELECT COALESCE(SUM(actual_amount), 0) FROM payment_order WHERE user_id = #{userId} AND payment_status = 'SUCCESS' AND is_deleted = 0")
    BigDecimal selectUserTotalPaymentAmount(@Param("userId") Long userId);

    /**
     * 查询用户支付次数
     *
     * @param userId 用户ID
     * @return 支付次数
     */
    @Select("SELECT COUNT(*) FROM payment_order WHERE user_id = #{userId} AND payment_status = 'SUCCESS' AND is_deleted = 0")
    Long selectUserPaymentCount(@Param("userId") Long userId);

    /**
     * 批量更新订单状态为超时
     *
     * @param orderIds 订单ID列表
     * @return 更新行数
     */
    @Update("<script>" +
            "UPDATE payment_order SET payment_status = 'TIMEOUT', version = version + 1, update_time = NOW() " +
            "WHERE id IN " +
            "<foreach collection='orderIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND payment_status = 'PENDING' AND is_deleted = 0" +
            "</script>")
    int batchUpdateTimeoutStatus(@Param("orderIds") List<Long> orderIds);
}