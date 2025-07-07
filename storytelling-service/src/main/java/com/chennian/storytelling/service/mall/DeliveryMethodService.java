package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.DeliveryMethod;
import com.chennian.storytelling.common.utils.PageParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送方式服务接口
 */
public interface DeliveryMethodService extends IService<DeliveryMethod> {

    /**
     * 分页查询配送方式
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param name     配送方式名称（可选）
     * @param status   状态（可选）
     * @return 分页结果
     */
    PageParam<DeliveryMethod> getDeliveryMethodPage(Integer pageNum, Integer pageSize, String name, Integer status);

    /**
     * 获取启用的配送方式列表
     *
     * @return 配送方式列表
     */
    List<DeliveryMethod> getEnabledDeliveryMethods();

    /**
     * 根据地区获取可用的配送方式
     *
     * @param provinceCode 省份编码
     * @param cityCode     城市编码
     * @return 配送方式列表
     */
    List<DeliveryMethod> getAvailableDeliveryMethods(String provinceCode, String cityCode);

    /**
     * 计算运费
     *
     * @param deliveryMethodId 配送方式ID
     * @param weight           重量（克）
     * @param orderAmount      订单金额
     * @param provinceCode     省份编码
     * @param cityCode         城市编码
     * @return 运费
     */
    BigDecimal calculateShippingFee(Long deliveryMethodId, Integer weight, BigDecimal orderAmount, String provinceCode, String cityCode);

    /**
     * 创建配送方式
     *
     * @param deliveryMethod 配送方式信息
     * @return 是否成功
     */
    boolean createDeliveryMethod(DeliveryMethod deliveryMethod);

    /**
     * 更新配送方式
     *
     * @param deliveryMethod 配送方式信息
     * @return 是否成功
     */
    boolean updateDeliveryMethod(DeliveryMethod deliveryMethod);

    /**
     * 删除配送方式
     *
     * @param id 配送方式ID
     * @return 是否成功
     */
    boolean deleteDeliveryMethod(Long id);

    /**
     * 启用/禁用配送方式
     *
     * @param id     配送方式ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 计算运费（控制器调用的方法名）
     *
     * @param deliveryMethodId 配送方式ID
     * @param weight           重量（克）
     * @param orderAmount      订单金额
     * @param province         省份
     * @param city             城市
     * @return 运费
     */
    BigDecimal calculateDeliveryFee(Long deliveryMethodId, Integer weight, BigDecimal orderAmount, String province, String city);
    
    /**
     * 更新配送方式状态（控制器调用的方法名）
     *
     * @param id     配送方式ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateDeliveryMethodStatus(Long id, Integer status);
}