package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.bean.model.mall.DeliveryMethod;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.DeliveryMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送方式管理控制器
 */
@Api(tags = "配送方式管理")
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryMethodService deliveryMethodService;

    @ApiOperation("分页查询配送方式")
    @GetMapping("/methods")
    public ServerResponseEntity<PageParam<DeliveryMethod>> getDeliveryMethodPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("配送方式名称") @RequestParam(required = false) String methodName,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        
        PageParam<DeliveryMethod> pageParam = deliveryMethodService.getDeliveryMethodPage(pageNum, pageSize, methodName, status);
        return ServerResponseEntity.success(pageParam);
    }

    @ApiOperation("获取启用的配送方式列表")
    @GetMapping("/methods/enabled")
    public ServerResponseEntity<List<DeliveryMethod>> getEnabledDeliveryMethods() {
        List<DeliveryMethod> methods = deliveryMethodService.getEnabledDeliveryMethods();
        return ServerResponseEntity.success(methods);
    }

    @ApiOperation("根据地区获取可用配送方式")
    @GetMapping("/methods/available")
    public ServerResponseEntity<List<DeliveryMethod>> getAvailableDeliveryMethods(
            @ApiParam("省份") @RequestParam String province,
            @ApiParam("城市") @RequestParam String city) {
        
        List<DeliveryMethod> methods = deliveryMethodService.getAvailableDeliveryMethods(province, city);
        return ServerResponseEntity.success(methods);
    }

    @ApiOperation("计算运费")
    @PostMapping("/calculate-fee")
    public ServerResponseEntity<BigDecimal> calculateDeliveryFee(
            @ApiParam("配送方式ID") @RequestParam Long deliveryMethodId,
            @ApiParam("重量(克)") @RequestParam Integer weight,
            @ApiParam("订单金额") @RequestParam BigDecimal orderAmount,
            @ApiParam("省份") @RequestParam String province,
            @ApiParam("城市") @RequestParam String city) {
        
        BigDecimal fee = deliveryMethodService.calculateDeliveryFee(deliveryMethodId, weight, orderAmount, province, city);
        return ServerResponseEntity.success(fee);
    }

    @ApiOperation("创建配送方式")
    @PostMapping("/methods")
    public ServerResponseEntity<Boolean> createDeliveryMethod(@RequestBody DeliveryMethod deliveryMethod) {
        boolean success = deliveryMethodService.createDeliveryMethod(deliveryMethod);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("创建配送方式失败");
    }

    @ApiOperation("更新配送方式")
    @PutMapping("/methods/{id}")
    public ServerResponseEntity<Boolean> updateDeliveryMethod(
            @ApiParam("配送方式ID") @PathVariable Long id,
            @RequestBody DeliveryMethod deliveryMethod) {
        
        deliveryMethod.setId(id);
        boolean success = deliveryMethodService.updateDeliveryMethod(deliveryMethod);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("更新配送方式失败");
    }

    @ApiOperation("删除配送方式")
    @DeleteMapping("/methods/{id}")
    public ServerResponseEntity<Boolean> deleteDeliveryMethod(@ApiParam("配送方式ID") @PathVariable Long id) {
        boolean success = deliveryMethodService.deleteDeliveryMethod(id);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("删除配送方式失败");
    }

    @ApiOperation("更新配送方式状态")
    @PutMapping("/methods/{id}/status")
    public ServerResponseEntity<Boolean> updateDeliveryMethodStatus(
            @ApiParam("配送方式ID") @PathVariable Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        
        boolean success = deliveryMethodService.updateDeliveryMethodStatus(id, status);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("更新配送方式状态失败");
    }

    @ApiOperation("获取配送方式详情")
    @GetMapping("/methods/{id}")
    public ServerResponseEntity<DeliveryMethod> getDeliveryMethodById(@ApiParam("配送方式ID") @PathVariable Long id) {
        DeliveryMethod deliveryMethod = deliveryMethodService.getById(id);
        return deliveryMethod != null ? ServerResponseEntity.success(deliveryMethod) : ServerResponseEntity.showFailMsg("配送方式不存在");
    }
}