package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.UserAddress;

import java.util.List;

/**
 * 用户收货地址服务接口
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 获取用户的收货地址列表
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<UserAddress> getUserAddresses(Long userId);

    /**
     * 获取用户的默认收货地址
     *
     * @param userId 用户ID
     * @return 默认收货地址
     */
    UserAddress getDefaultAddress(Long userId);

    /**
     * 创建收货地址
     *
     * @param userAddress 收货地址信息
     * @return 是否成功
     */
    boolean createAddress(UserAddress userAddress);

    /**
     * 更新收货地址
     *
     * @param userAddress 收货地址信息
     * @return 是否成功
     */
    boolean updateAddress(UserAddress userAddress);

    /**
     * 删除收货地址
     *
     * @param id     地址ID
     * @param userId 用户ID（用于权限验证）
     * @return 是否成功
     */
    boolean deleteAddress(Long id, Long userId);

    /**
     * 设置默认地址
     *
     * @param id     地址ID
     * @param userId 用户ID（用于权限验证）
     * @return 是否成功
     */
    boolean setDefaultAddress(Long id, Long userId);

    /**
     * 验证地址是否属于用户
     *
     * @param id     地址ID
     * @param userId 用户ID
     * @return 是否属于该用户
     */
    boolean validateAddressOwnership(Long id, Long userId);

    /**
     * 获取地址详情
     *
     * @param id     地址ID
     * @param userId 用户ID（用于权限验证）
     * @return 地址详情
     */
    UserAddress getAddressDetail(Long id, Long userId);

    /**
     * 检查用户地址数量限制
     *
     * @param userId 用户ID
     * @return 是否超过限制
     */
    boolean checkAddressLimit(Long userId);

    /**
     * 根据地区编码验证配送范围
     *
     * @param provinceCode 省份编码
     * @param cityCode     城市编码
     * @param regionCode   区县编码
     * @return 是否在配送范围内
     */
    boolean validateDeliveryArea(String provinceCode, String cityCode, String regionCode);
}