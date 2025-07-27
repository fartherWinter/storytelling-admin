package com.chennian.storytelling.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.payment.entity.PaymentChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 支付渠道配置Mapper接口
 *
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface PaymentChannelMapper extends BaseMapper<PaymentChannel> {

    /**
     * 根据渠道编码查询
     *
     * @param channelCode 渠道编码
     * @return 支付渠道配置
     */
    @Select("SELECT * FROM payment_channel WHERE channel_code = #{channelCode} AND is_deleted = 0")
    PaymentChannel selectByChannelCode(@Param("channelCode") String channelCode);

    /**
     * 根据渠道类型查询
     *
     * @param channelType 渠道类型
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE channel_type = #{channelType} AND is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectByChannelType(@Param("channelType") String channelType);

    /**
     * 查询启用的支付渠道
     *
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectEnabledChannels();

    /**
     * 根据支付方式查询支持的渠道
     *
     * @param paymentMethod 支付方式
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE FIND_IN_SET(#{paymentMethod}, payment_methods) > 0 AND is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectByPaymentMethod(@Param("paymentMethod") String paymentMethod);

    /**
     * 根据货币类型查询支持的渠道
     *
     * @param currency 货币类型
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE FIND_IN_SET(#{currency}, supported_currencies) > 0 AND is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectByCurrency(@Param("currency") String currency);

    /**
     * 检查渠道编码是否存在
     *
     * @param channelCode 渠道编码
     * @param excludeId   排除的ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM payment_channel WHERE channel_code = #{channelCode} AND id != #{excludeId} AND is_deleted = 0")
    boolean existsByChannelCode(@Param("channelCode") String channelCode, @Param("excludeId") Long excludeId);

    /**
     * 检查渠道名称是否存在
     *
     * @param channelName 渠道名称
     * @param excludeId   排除的ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM payment_channel WHERE channel_name = #{channelName} AND id != #{excludeId} AND is_deleted = 0")
    boolean existsByChannelName(@Param("channelName") String channelName, @Param("excludeId") Long excludeId);

    /**
     * 更新渠道状态
     *
     * @param id        渠道ID
     * @param isEnabled 是否启用
     * @param version   版本号
     * @return 更新行数
     */
    @Update("UPDATE payment_channel SET is_enabled = #{isEnabled}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateChannelStatus(@Param("id") Long id, @Param("isEnabled") Boolean isEnabled, @Param("version") Integer version);

    /**
     * 更新排序
     *
     * @param id        渠道ID
     * @param sortOrder 排序
     * @param version   版本号
     * @return 更新行数
     */
    @Update("UPDATE payment_channel SET sort_order = #{sortOrder}, version = version + 1, update_time = NOW() WHERE id = #{id} AND version = #{version} AND is_deleted = 0")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder, @Param("version") Integer version);

    /**
     * 批量更新渠道状态
     *
     * @param channelIds 渠道ID列表
     * @param isEnabled  是否启用
     * @return 更新行数
     */
    @Update("<script>" +
            "UPDATE payment_channel SET is_enabled = #{isEnabled}, version = version + 1, update_time = NOW() " +
            "WHERE id IN " +
            "<foreach collection='channelIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND is_deleted = 0" +
            "</script>")
    int batchUpdateChannelStatus(@Param("channelIds") List<Long> channelIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 获取最大排序值
     *
     * @return 最大排序值
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM payment_channel WHERE is_deleted = 0")
    Integer selectMaxSortOrder();

    /**
     * 根据商户号查询渠道
     *
     * @param merchantId 商户号
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE merchant_id = #{merchantId} AND is_deleted = 0")
    List<PaymentChannel> selectByMerchantId(@Param("merchantId") String merchantId);

    /**
     * 查询沙箱环境的渠道
     *
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE is_sandbox = 1 AND is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectSandboxChannels();

    /**
     * 查询生产环境的渠道
     *
     * @return 支付渠道配置列表
     */
    @Select("SELECT * FROM payment_channel WHERE is_sandbox = 0 AND is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order ASC")
    List<PaymentChannel> selectProductionChannels();
}