package com.chennian.storytelling.api.feign;

import com.chennian.storytelling.bean.model.mall.DeliveryMethod;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送服务Feign客户端
 */
@FeignClient(name = "delivery-service", path = "/delivery")
public interface DeliveryServiceClient {

    /**
     * 分页查询配送方式
     */
    @GetMapping("/methods")
    ServerResponseEntity<PageParam<DeliveryMethod>> getDeliveryMethodPage(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "methodName", required = false) String methodName,
            @RequestParam(value = "status", required = false) Integer status);

    /**
     * 获取启用的配送方式列表
     */
    @GetMapping("/methods/enabled")
    ServerResponseEntity<List<DeliveryMethod>> getEnabledDeliveryMethods();

    /**
     * 根据地区获取可用配送方式
     */
    @GetMapping("/methods/available")
    ServerResponseEntity<List<DeliveryMethod>> getAvailableDeliveryMethods(
            @RequestParam("province") String province,
            @RequestParam("city") String city);

    /**
     * 计算运费
     */
    @PostMapping("/calculate-fee")
    ServerResponseEntity<BigDecimal> calculateDeliveryFee(
            @RequestParam("deliveryMethodId") Long deliveryMethodId,
            @RequestParam("weight") Integer weight,
            @RequestParam("orderAmount") BigDecimal orderAmount,
            @RequestParam("province") String province,
            @RequestParam("city") String city);

    /**
     * 创建配送方式
     */
    @PostMapping("/methods")
    ServerResponseEntity<Boolean> createDeliveryMethod(@RequestBody DeliveryMethod deliveryMethod);

    /**
     * 更新配送方式
     */
    @PutMapping("/methods/{id}")
    ServerResponseEntity<Boolean> updateDeliveryMethod(@RequestBody DeliveryMethod deliveryMethod);

    /**
     * 删除配送方式
     */
    @DeleteMapping("/methods/{id}")
    ServerResponseEntity<Boolean> deleteDeliveryMethod(@PathVariable("id") Long id);

    /**
     * 更新配送方式状态
     */
    @PutMapping("/methods/{id}/status")
    ServerResponseEntity<Boolean> updateDeliveryMethodStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") Integer status);

    /**
     * 获取配送方式详情
     */
    @GetMapping("/methods/{id}")
    ServerResponseEntity<DeliveryMethod> getDeliveryMethodById(@PathVariable("id") Long id);
}