package com.chennian.storytelling.api.controller.mall;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chennian.storytelling.bean.model.mall.UserAddress;
import com.chennian.storytelling.service.mall.UserAddressService;
import com.chennian.storytelling.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 用户收货地址管理控制器
 * @author chen
 */
@Api(tags = "用户收货地址管理")
@RestController
@RequestMapping("/api/user/address")
public class UserAddressController {


    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @ApiOperation("获取用户地址列表")
    @GetMapping("/list")
    public ServerResponseEntity<List<UserAddress>> getUserAddressList(
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        List<UserAddress> addressList = userAddressService.getUserAddresses(userId);
        return ServerResponseEntity.success(addressList);
    }

    @ApiOperation("获取用户默认地址")
    @GetMapping("/default")
    public ServerResponseEntity<UserAddress> getUserDefaultAddress(
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        UserAddress defaultAddress = userAddressService.getDefaultAddress(userId);
        return defaultAddress != null ? ServerResponseEntity.success(defaultAddress) : ServerResponseEntity.showFailMsg("未找到默认地址");
    }

    @ApiOperation("创建收货地址")
    @PostMapping
    public ServerResponseEntity<Boolean> createUserAddress(@RequestBody UserAddress userAddress) {
        boolean success = userAddressService.createAddress(userAddress);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("创建收货地址失败");
    }

    @ApiOperation("更新收货地址")
    @PutMapping("/{id}")
    public ServerResponseEntity<Boolean> updateUserAddress(
            @ApiParam("地址ID") @PathVariable Long id,
            @RequestBody UserAddress userAddress) {
        
        userAddress.setId(id);
        boolean success = userAddressService.updateAddress(userAddress);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("更新收货地址失败");
    }

    @ApiOperation("删除收货地址")
    @DeleteMapping("/{id}")
    public ServerResponseEntity<Boolean> deleteUserAddress(
            @ApiParam("地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        boolean success = userAddressService.deleteAddress(id, userId);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("删除收货地址失败");
    }

    @ApiOperation("设置默认地址")
    @PutMapping("/{id}/default")
    public ServerResponseEntity<Boolean> setDefaultAddress(
            @ApiParam("地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        boolean success = userAddressService.setDefaultAddress(id, userId);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("设置默认地址失败");
    }

    @ApiOperation("验证地址归属")
    @GetMapping("/{id}/verify")
    public ServerResponseEntity<Boolean> verifyAddressOwnership(
            @ApiParam("地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        boolean isOwner = userAddressService.validateAddressOwnership(id, userId);
        return ServerResponseEntity.success(isOwner);
    }

    @ApiOperation("获取地址详情")
    @GetMapping("/{id}")
    public ServerResponseEntity<UserAddress> getUserAddressById(
            @ApiParam("地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        UserAddress address = userAddressService.getAddressDetail(id, userId);
        return address != null ? ServerResponseEntity.success(address) : ServerResponseEntity.showFailMsg("地址不存在或无权限访问");
    }

    @ApiOperation("检查地址数量限制")
    @GetMapping("/count-check")
    public ServerResponseEntity<Boolean> checkAddressLimit(
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        boolean canAdd = userAddressService.checkAddressLimit(userId);
        return ServerResponseEntity.success(canAdd);
    }

    @ApiOperation("验证配送范围")
    @PostMapping("/delivery-check")
    public ServerResponseEntity<Boolean> checkDeliveryRange(@RequestBody UserAddress address) {
        boolean canDeliver = userAddressService.validateDeliveryArea(address.getProvinceCode(), address.getCityCode(), address.getRegionCode());
        return ServerResponseEntity.success(canDeliver);
    }

    @ApiOperation("获取用户地址数量")
    @GetMapping("/count")
    public ServerResponseEntity<Integer> getUserAddressCount(
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        List<UserAddress> addressList = userAddressService.getUserAddresses(userId);
        return ServerResponseEntity.success(addressList.size());
    }

    @ApiOperation("批量删除地址")
    @DeleteMapping("/batch")
    public ServerResponseEntity<Boolean> batchDeleteUserAddress(
            @ApiParam("地址ID列表") @RequestBody List<Long> addressIds,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        int successCount = 0;
        for (Long addressId : addressIds) {
            if (userAddressService.deleteAddress(addressId, userId)) {
                successCount++;
            }
        }
        
        boolean allSuccess = successCount == addressIds.size();
        String message = allSuccess ? "批量删除成功" : String.format("删除成功%d个，失败%d个", successCount, addressIds.size() - successCount);
        
        return allSuccess ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg(message);
    }

    @ApiOperation("根据省市获取地址列表")
    @GetMapping("/by-region")
    public ServerResponseEntity<List<UserAddress>> getUserAddressByRegion(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("省份") @RequestParam(required = false) String province,
            @ApiParam("城市") @RequestParam(required = false) String city) {
        
        List<UserAddress> addressList = userAddressService.getUserAddresses(userId);
        
        // 过滤地址
        List<UserAddress> filteredList = addressList.stream()
            .filter(address -> {
                boolean matchProvince = province == null || province.equals(address.getProvinceName());
                boolean matchCity = city == null || city.equals(address.getCityName());
                return matchProvince && matchCity;
            })
            .toList();
        
        return ServerResponseEntity.success(filteredList);
    }

    @ApiOperation("复制地址")
    @PostMapping("/{id}/copy")
    public ServerResponseEntity<Boolean> copyUserAddress(
            @ApiParam("源地址ID") @PathVariable Long id,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        UserAddress sourceAddress = userAddressService.getAddressDetail(id, userId);
        if (sourceAddress == null) {
            return ServerResponseEntity.showFailMsg("源地址不存在");
        }
        
        // 检查地址数量限制
        if (!userAddressService.checkAddressLimit(userId)) {
            return ServerResponseEntity.showFailMsg("地址数量已达上限");
        }
        
        // 创建新地址
        UserAddress newAddress = BeanUtil.copyProperties(sourceAddress, UserAddress.class);
        newAddress.setDetailAddress(sourceAddress.getDetailAddress() + "(复制)");
        // 复制的地址不设为默认
        newAddress.setIsDefault(0);

        
        boolean success = userAddressService.createAddress(newAddress);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("复制地址失败");
    }
}