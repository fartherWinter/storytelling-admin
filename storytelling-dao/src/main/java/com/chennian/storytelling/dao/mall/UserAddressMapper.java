package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收货地址Mapper接口
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    /**
     * 根据用户ID查询地址列表
     */
    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询默认地址
     */
    UserAddress selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 清除用户的默认地址
     */
    int clearDefaultByUserId(@Param("userId") Long userId);

    /**
     * 设置默认地址
     */
    int setDefaultAddress(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 统计用户地址数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 验证地址所有权
     */
    int validateOwnership(@Param("id") Long id, @Param("userId") Long userId);
}