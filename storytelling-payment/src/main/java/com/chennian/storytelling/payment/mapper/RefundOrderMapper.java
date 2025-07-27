package com.chennian.storytelling.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.payment.entity.RefundOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 退款订单Mapper接口
 *
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

    /**
     * 根据退款订单号查询
     *
     * @param refundOrderNo 退款订单号
     * @return 退款订单
     */
    @Select("SELECT * FROM refund_order WHERE refund_order_no = #{refundOrderNo} AND is_deleted = 0")
    RefundOrder selectByRefundOrderNo(@Param("refundOrderNo") String refundOrderNo);

    /**
     * 根据支付订单ID查询
     *
     * @param paymentOrderId 支付订单ID
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE payment_order_id = #{paymentOrderId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByPaymentOrderId(@Param("paymentOrderId") Long paymentOrderId);

    /**
     * 根据支付订单号查询
     *
     * @param paymentOrderNo 支付订单号
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE payment_order_no = #{paymentOrderNo} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByPaymentOrderNo(@Param("paymentOrderNo") String paymentOrderNo);

    /**
     * 根据业务订单号查询
     *
     * @param businessOrderNo 业务订单号
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE business_order_no = #{businessOrderNo} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByBusinessOrderNo(@Param("businessOrderNo") String businessOrderNo);

    /**
     * 根据第三方退款单号查询
     *
     * @param thirdPartyRefundNo 第三方退款单号
     * @return 退款订单
     */
    @Select("SELECT * FROM refund_order WHERE third_party_refund_no = #{thirdPartyRefundNo} AND is_deleted = 0")
    RefundOrder selectByThirdPartyRefundNo(@Param("thirdPartyRefundNo") String thirdPartyRefundNo);

    /**
     * 根据用户ID分页查询
     *
     * @param page   分页参数
     * @param userId 用户ID
     * @return 退款订单分页
     */
    @Select("SELECT * FROM refund_order WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    IPage<RefundOrder> selectPageByUserId(Page<RefundOrder> page, @Param("userId") Long userId);

    /**
     * 根据退款状态查询
     *
     * @param refundStatus 退款状态
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE refund_status = #{refundStatus} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByRefundStatus(@Param("refundStatus") String refundStatus);

    /**
     * 根据审核状态查询
     *
     * @param auditStatus 审核状态
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE audit_status = #{auditStatus} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByAuditStatus(@Param("auditStatus") String auditStatus);

    /**
     * 根据支付渠道查询
     *
     * @param paymentChannel 支付渠道
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE payment_channel = #{paymentChannel} AND is_deleted = 0 ORDER BY create_time DESC")
    List<RefundOrder> selectByPaymentChannel(@Param("paymentChannel") String paymentChannel);

    /**
     * 查询待审核的退款订单
     *
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE audit_status = 'PENDING' AND is_deleted = 0 ORDER BY create_time ASC")
    List<RefundOrder> selectPendingAuditOrders();

    /**
     * 查询待处理的退款订单
     *
     * @return 退款订单列表
     */
    @Select("SELECT * FROM refund_order WHERE refund_status = 'PENDING' AND audit_status = 'APPROVED' AND is_deleted = 0 ORDER BY create_time ASC")
    List<RefundOrder> selectPendingRefundOrders();

    /**
     * 更新退款状态
     *
     * @param id           订单ID
     * @param refundStatus 退款状态
     * @param version      版本号
     * @return 更新行数
     */
    @Update("UPDATE refund_order SET refund_status = #{refundStatus}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateRefundStatus(@Param("id") Long id, @Param("refundStatus") String refundStatus, @Param("version") Integer version);

    /**
     * 更新审核信息
     *
     * @param id           订单ID
     * @param auditStatus  审核状态
     * @param auditBy      审核人
     * @param auditTime    审核时间
     * @param auditRemark  审核备注
     * @param version      版本号
     * @return 更新行数
     */
    @Update("UPDATE refund_order SET audit_status = #{auditStatus}, audit_by = #{auditBy}, audit_time = #{auditTime}, audit_remark = #{auditRemark}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateAuditInfo(@Param("id") Long id,
                       @Param("auditStatus") String auditStatus,
                       @Param("auditBy") String auditBy,
                       @Param("auditTime") LocalDateTime auditTime,
                       @Param("auditRemark") String auditRemark,
                       @Param("version") Integer version);

    /**
     * 更新第三方退款信息
     *
     * @param id                   订单ID
     * @param thirdPartyRefundNo   第三方退款单号
     * @param actualRefundAmount   实际退款金额
     * @param refundTime           退款时间
     * @param version              版本号
     * @return 更新行数
     */
    @Update("UPDATE refund_order SET third_party_refund_no = #{thirdPartyRefundNo}, actual_refund_amount = #{actualRefundAmount}, refund_time = #{refundTime}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateThirdPartyRefundInfo(@Param("id") Long id,
                                  @Param("thirdPartyRefundNo") String thirdPartyRefundNo,
                                  @Param("actualRefundAmount") BigDecimal actualRefundAmount,
                                  @Param("refundTime") LocalDateTime refundTime,
                                  @Param("version") Integer version);

    /**
     * 分页查询退款订单
     *
     * @param page            分页参数
     * @param userId          用户ID
     * @param refundStatus    退款状态
     * @param auditStatus     审核状态
     * @param paymentChannel  支付渠道
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @return 退款订单分页
     */
    @Select("<script>" +
            "SELECT * FROM refund_order WHERE is_deleted = 0" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='refundStatus != null and refundStatus != \"\\'> AND refund_status = #{refundStatus}</if>" +
            "<if test='auditStatus != null and auditStatus != \"\\'> AND audit_status = #{auditStatus}</if>" +
            "<if test='paymentChannel != null and paymentChannel != \"\\'> AND payment_channel = #{paymentChannel}</if>" +
            "<if test='startTime != null'> AND create_time >= #{startTime}</if>" +
            "<if test='endTime != null'> AND create_time <= #{endTime}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    IPage<RefundOrder> selectPageWithConditions(Page<RefundOrder> page,
                                               @Param("userId") Long userId,
                                               @Param("refundStatus") String refundStatus,
                                               @Param("auditStatus") String auditStatus,
                                               @Param("paymentChannel") String paymentChannel,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 统计退款金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT COUNT(*) as refundCount, COALESCE(SUM(actual_refund_amount), 0) as totalRefundAmount FROM refund_order WHERE refund_status = 'SUCCESS' AND refund_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0")
    Map<String, Object> statisticsRefundAmount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 按渠道统计退款金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT payment_channel, COUNT(*) as refundCount, COALESCE(SUM(actual_refund_amount), 0) as totalRefundAmount FROM refund_order WHERE refund_status = 'SUCCESS' AND refund_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY payment_channel")
    List<Map<String, Object>> statisticsRefundAmountByChannel(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 按状态统计退款订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计结果
     */
    @Select("SELECT refund_status, COUNT(*) as refundCount FROM refund_order WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY refund_status")
    List<Map<String, Object>> statisticsRefundCountByStatus(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户退款总金额
     *
     * @param userId 用户ID
     * @return 退款总金额
     */
    @Select("SELECT COALESCE(SUM(actual_refund_amount), 0) FROM refund_order WHERE user_id = #{userId} AND refund_status = 'SUCCESS' AND is_deleted = 0")
    BigDecimal selectUserTotalRefundAmount(@Param("userId") Long userId);

    /**
     * 查询用户退款次数
     *
     * @param userId 用户ID
     * @return 退款次数
     */
    @Select("SELECT COUNT(*) FROM refund_order WHERE user_id = #{userId} AND refund_status = 'SUCCESS' AND is_deleted = 0")
    Long selectUserRefundCount(@Param("userId") Long userId);

    /**
     * 查询支付订单的退款总金额
     *
     * @param paymentOrderId 支付订单ID
     * @return 退款总金额
     */
    @Select("SELECT COALESCE(SUM(actual_refund_amount), 0) FROM refund_order WHERE payment_order_id = #{paymentOrderId} AND refund_status = 'SUCCESS' AND is_deleted = 0")
    BigDecimal selectRefundAmountByPaymentOrderId(@Param("paymentOrderId") Long paymentOrderId);
}