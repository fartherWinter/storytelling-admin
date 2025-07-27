package com.chennian.storytelling.payment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.payment.entity.PaymentChannel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付渠道服务接口
 *
 * @author chennian
 * @since 2024-01-01
 */
public interface PaymentChannelService extends IService<PaymentChannel> {

    /**
     * 根据渠道编码查询
     *
     * @param channelCode 渠道编码
     * @return 支付渠道
     */
    PaymentChannel getByChannelCode(String channelCode);

    /**
     * 根据渠道类型查询
     *
     * @param channelType 渠道类型
     * @return 支付渠道列表
     */
    List<PaymentChannel> getByChannelType(String channelType);

    /**
     * 根据支付方式查询
     *
     * @param paymentMethod 支付方式
     * @return 支付渠道列表
     */
    List<PaymentChannel> getByPaymentMethod(String paymentMethod);

    /**
     * 根据货币类型查询
     *
     * @param currency 货币类型
     * @return 支付渠道列表
     */
    List<PaymentChannel> getByCurrency(String currency);

    /**
     * 根据商户号查询
     *
     * @param merchantNo 商户号
     * @return 支付渠道列表
     */
    List<PaymentChannel> getByMerchantNo(String merchantNo);

    /**
     * 查询启用的渠道
     *
     * @return 支付渠道列表
     */
    List<PaymentChannel> getEnabledChannels();

    /**
     * 查询沙箱环境渠道
     *
     * @return 支付渠道列表
     */
    List<PaymentChannel> getSandboxChannels();

    /**
     * 查询生产环境渠道
     *
     * @return 支付渠道列表
     */
    List<PaymentChannel> getProductionChannels();

    /**
     * 分页查询支付渠道
     *
     * @param page 分页参数
     * @return 支付渠道分页
     */
    IPage<PaymentChannel> getChannelPage(Page<PaymentChannel> page);

    /**
     * 检查渠道编码是否存在
     *
     * @param channelCode 渠道编码
     * @return 是否存在
     */
    boolean existsByChannelCode(String channelCode);

    /**
     * 检查渠道名称是否存在
     *
     * @param channelName 渠道名称
     * @return 是否存在
     */
    boolean existsByChannelName(String channelName);

    /**
     * 更新渠道状态
     *
     * @param id      渠道ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean updateChannelStatus(Long id, Boolean enabled);

    /**
     * 更新渠道排序
     *
     * @param id   渠道ID
     * @param sort 排序
     * @return 是否成功
     */
    boolean updateChannelSort(Long id, Integer sort);

    /**
     * 批量更新渠道状态
     *
     * @param ids     渠道ID列表
     * @param enabled 是否启用
     * @return 更新数量
     */
    int batchUpdateChannelStatus(List<Long> ids, Boolean enabled);

    /**
     * 获取最大排序值
     *
     * @return 最大排序值
     */
    Integer getMaxSort();

    /**
     * 创建支付渠道
     *
     * @param paymentChannel 支付渠道
     * @return 支付渠道
     */
    PaymentChannel createPaymentChannel(PaymentChannel paymentChannel);

    /**
     * 更新支付渠道
     *
     * @param paymentChannel 支付渠道
     * @return 是否成功
     */
    boolean updatePaymentChannel(PaymentChannel paymentChannel);

    /**
     * 删除支付渠道
     *
     * @param id 渠道ID
     * @return 是否成功
     */
    boolean deletePaymentChannel(Long id);

    /**
     * 验证支付渠道配置
     *
     * @param paymentChannel 支付渠道
     * @return 验证结果
     */
    boolean validateChannelConfig(PaymentChannel paymentChannel);

    /**
     * 测试支付渠道连接
     *
     * @param channelCode 渠道编码
     * @return 测试结果
     */
    boolean testChannelConnection(String channelCode);

    /**
     * 获取渠道支持的支付方式
     *
     * @param channelCode 渠道编码
     * @return 支付方式列表
     */
    List<String> getChannelPaymentMethods(String channelCode);

    /**
     * 获取渠道支持的货币类型
     *
     * @param channelCode 渠道编码
     * @return 货币类型列表
     */
    List<String> getChannelCurrencies(String channelCode);

    /**
     * 检查金额是否在限制范围内
     *
     * @param channelCode 渠道编码
     * @param amount      金额
     * @return 是否在范围内
     */
    boolean isAmountInLimit(String channelCode, BigDecimal amount);

    /**
     * 计算手续费
     *
     * @param channelCode 渠道编码
     * @param amount      金额
     * @return 手续费
     */
    BigDecimal calculateFee(String channelCode, BigDecimal amount);

    /**
     * 获取渠道配置
     *
     * @param channelCode 渠道编码
     * @param configKey   配置键
     * @return 配置值
     */
    String getChannelConfig(String channelCode, String configKey);

    /**
     * 设置渠道配置
     *
     * @param channelCode  渠道编码
     * @param configKey    配置键
     * @param configValue  配置值
     * @return 是否成功
     */
    boolean setChannelConfig(String channelCode, String configKey, String configValue);

    /**
     * 获取渠道扩展配置
     *
     * @param channelCode 渠道编码
     * @return 扩展配置
     */
    Map<String, Object> getChannelExtConfig(String channelCode);

    /**
     * 设置渠道扩展配置
     *
     * @param channelCode 渠道编码
     * @param extConfig   扩展配置
     * @return 是否成功
     */
    boolean setChannelExtConfig(String channelCode, Map<String, Object> extConfig);

    /**
     * 刷新渠道缓存
     *
     * @param channelCode 渠道编码
     */
    void refreshChannelCache(String channelCode);

    /**
     * 清空渠道缓存
     */
    void clearChannelCache();

    /**
     * 获取推荐的支付渠道
     *
     * @param paymentMethod 支付方式
     * @param amount        金额
     * @param currency      货币类型
     * @return 推荐渠道列表
     */
    List<PaymentChannel> getRecommendedChannels(String paymentMethod, BigDecimal amount, String currency);
}