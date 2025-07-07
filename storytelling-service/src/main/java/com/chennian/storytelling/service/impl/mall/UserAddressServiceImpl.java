package com.chennian.storytelling.service.impl.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.UserAddress;
import com.chennian.storytelling.dao.mall.UserAddressMapper;
import com.chennian.storytelling.service.mall.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户收货地址服务实现类
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    
    // 用户地址数量限制
    private static final int MAX_ADDRESS_COUNT = 20;
    
    // 支持配送的地区（示例数据，实际应该从配置或数据库获取）
    private static final List<String> SUPPORTED_PROVINCES = Arrays.asList(
        "110000", "120000", "130000", "140000", "150000", // 北京、天津、河北、山西、内蒙古
        "210000", "220000", "230000", "310000", "320000", // 辽宁、吉林、黑龙江、上海、江苏
        "330000", "340000", "350000", "360000", "370000", // 浙江、安徽、福建、江西、山东
        "410000", "420000", "430000", "440000", "450000", // 河南、湖北、湖南、广东、广西
        "460000", "500000", "510000", "520000", "530000", // 海南、重庆、四川、贵州、云南
        "540000", "610000", "620000", "630000", "640000", // 西藏、陕西、甘肃、青海、宁夏
        "650000" // 新疆
    );

    @Override
    public List<UserAddress> getUserAddresses(Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId)
                   .eq(UserAddress::getStatus, 1)
                   .orderByDesc(UserAddress::getIsDefault)
                   .orderByDesc(UserAddress::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId)
                   .eq(UserAddress::getIsDefault, 1)
                   .eq(UserAddress::getStatus, 1);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAddress(UserAddress userAddress) {
        // 检查地址数量限制
        if (checkAddressLimit(userAddress.getUserId())) {
            return false;
        }
        
        // 验证配送范围
        if (!validateDeliveryArea(userAddress.getProvinceCode(), userAddress.getCityCode(), userAddress.getRegionCode())) {
            return false;
        }
        
        userAddress.setCreateTime(LocalDateTime.now());
        userAddress.setUpdateTime(LocalDateTime.now());
        userAddress.setStatus(1);
        
        // 如果设置为默认地址，需要先取消其他默认地址
        if (userAddress.getIsDefault() != null && userAddress.getIsDefault() == 1) {
            clearDefaultAddress(userAddress.getUserId());
        } else {
            // 如果用户没有默认地址，将第一个地址设为默认
            UserAddress defaultAddress = getDefaultAddress(userAddress.getUserId());
            if (defaultAddress == null) {
                userAddress.setIsDefault(1);
            } else {
                userAddress.setIsDefault(0);
            }
        }
        
        return save(userAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(UserAddress userAddress) {
        // 验证地址所有权
        if (!validateAddressOwnership(userAddress.getId(), userAddress.getUserId())) {
            return false;
        }
        
        // 验证配送范围
        if (!validateDeliveryArea(userAddress.getProvinceCode(), userAddress.getCityCode(), userAddress.getRegionCode())) {
            return false;
        }
        
        userAddress.setUpdateTime(LocalDateTime.now());
        
        // 如果设置为默认地址，需要先取消其他默认地址
        if (userAddress.getIsDefault() != null && userAddress.getIsDefault() == 1) {
            clearDefaultAddress(userAddress.getUserId());
        }
        
        return updateById(userAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAddress(Long id, Long userId) {
        // 验证地址所有权
        if (!validateAddressOwnership(id, userId)) {
            return false;
        }
        
        UserAddress address = getById(id);
        if (address == null) {
            return false;
        }
        
        // 如果删除的是默认地址，需要设置新的默认地址
        if (address.getIsDefault() == 1) {
            // 删除地址
            removeById(id);
            
            // 设置新的默认地址（选择最新的一个地址）
            LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserAddress::getUserId, userId)
                       .eq(UserAddress::getStatus, 1)
                       .orderByDesc(UserAddress::getCreateTime)
                       .last("LIMIT 1");
            UserAddress newDefaultAddress = getOne(queryWrapper);
            if (newDefaultAddress != null) {
                newDefaultAddress.setIsDefault(1);
                newDefaultAddress.setUpdateTime(LocalDateTime.now());
                updateById(newDefaultAddress);
            }
        } else {
            removeById(id);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long id, Long userId) {
        // 验证地址所有权
        if (!validateAddressOwnership(id, userId)) {
            return false;
        }
        
        // 先取消其他默认地址
        clearDefaultAddress(userId);
        
        // 设置新的默认地址
        UserAddress address = new UserAddress();
        address.setId(id);
        address.setIsDefault(1);
        address.setUpdateTime(LocalDateTime.now());
        
        return updateById(address);
    }

    @Override
    public boolean validateAddressOwnership(Long id, Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getId, id)
                   .eq(UserAddress::getUserId, userId)
                   .eq(UserAddress::getStatus, 1);
        return count(queryWrapper) > 0;
    }

    @Override
    public UserAddress getAddressDetail(Long id, Long userId) {
        if (!validateAddressOwnership(id, userId)) {
            return null;
        }
        return getById(id);
    }

    @Override
    public boolean checkAddressLimit(Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId)
                   .eq(UserAddress::getStatus, 1);
        return count(queryWrapper) >= MAX_ADDRESS_COUNT;
    }

    @Override
    public boolean validateDeliveryArea(String provinceCode, String cityCode, String regionCode) {
        // 检查省份是否在支持范围内
        if (provinceCode == null || !SUPPORTED_PROVINCES.contains(provinceCode)) {
            return false;
        }
        
        // 这里可以添加更详细的城市和区县验证逻辑
        // 实际项目中应该从配置表或第三方服务获取支持的地区列表
        
        return true;
    }
    
    /**
     * 清除用户的默认地址
     */
    private void clearDefaultAddress(Long userId) {
        LambdaUpdateWrapper<UserAddress> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAddress::getUserId, userId)
                    .eq(UserAddress::getIsDefault, 1)
                    .set(UserAddress::getIsDefault, 0)
                    .set(UserAddress::getUpdateTime, LocalDateTime.now());
        update(updateWrapper);
    }

    /**
     * 获取用户地址列表（控制器调用的方法名）
     */
    public List<UserAddress> getUserAddressList(Long userId) {
        return getUserAddresses(userId);
    }

    /**
     * 获取用户默认地址（控制器调用的方法名）
     */
    public UserAddress getUserDefaultAddress(Long userId) {
        return getDefaultAddress(userId);
    }

    /**
     * 创建用户地址（控制器调用的方法名）
     */
    public boolean createUserAddress(UserAddress userAddress) {
        return createAddress(userAddress);
    }

    /**
     * 更新用户地址（控制器调用的方法名）
     */
    public boolean updateUserAddress(UserAddress userAddress) {
        return updateAddress(userAddress);
    }

    /**
     * 删除用户地址（控制器调用的方法名）
     */
    public boolean deleteUserAddress(Long id, Long userId) {
        return deleteAddress(id, userId);
    }

    /**
     * 设置默认地址（控制器调用的方法名）
     */
    public boolean setUserDefaultAddress(Long id, Long userId) {
        return setDefaultAddress(id, userId);
    }

    /**
     * 验证地址归属（控制器调用的方法名）
     */
    public boolean validateUserAddressOwnership(Long id, Long userId) {
        return validateAddressOwnership(id, userId);
    }

    /**
     * 获取地址详情（控制器调用的方法名）
     */
    public UserAddress getUserAddressDetail(Long id, Long userId) {
        return getAddressDetail(id, userId);
    }

    /**
     * 检查地址数量限制（控制器调用的方法名）
     */
    public boolean checkUserAddressLimit(Long userId) {
        return checkAddressLimit(userId);
    }

    /**
     * 验证配送范围（控制器调用的方法名）
     */
    public boolean validateUserDeliveryArea(String provinceCode, String cityCode, String regionCode) {
        return validateDeliveryArea(provinceCode, cityCode, regionCode);
    }
}