package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.bean.bo.MallCouponSearchBo;
import com.chennian.storytelling.bean.dto.MallCouponDto;
import com.chennian.storytelling.bean.model.mall.MallCoupon;
import com.chennian.storytelling.bean.vo.MallCouponVO;
import com.chennian.storytelling.bean.vo.MallCouponStatisticsVO;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券管理控制器
 *
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "优惠券管理")
@RestController
@RequestMapping("/admin/coupon")
@Slf4j
@Validated
public class MallCouponController {

    @Autowired
    private MallCouponService mallCouponService;

    /**
     * 分页查询优惠券列表
     */
    @ApiOperation("分页查询优惠券列表")
    @GetMapping("/list")
    public ServerResponseEntity<List<MallCouponVO>> list(MallCouponSearchBo searchBo) {
        try {
            var page = mallCouponService.selectCouponPage(searchBo);
            List<MallCouponVO> list = page.getRecords();
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            log.error("查询优惠券列表失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 根据ID查询优惠券详情
     */
    @ApiOperation("根据ID查询优惠券详情")
    @GetMapping("/{id}")
    public ServerResponseEntity<MallCouponVO> getById(
            @ApiParam("优惠券ID") @PathVariable Long id) {
        try {
            MallCouponVO couponVO = mallCouponService.selectCouponById(id);
            return ServerResponseEntity.success(couponVO);
        } catch (Exception e) {
            log.error("查询优惠券详情失败，ID: {}", id, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 创建优惠券
     */
    @ApiOperation("创建优惠券")
    @PostMapping
    public ServerResponseEntity<String> create(@Valid @RequestBody MallCouponDto mallCouponDto) {
        try {
            boolean result = mallCouponService.createCoupon(mallCouponDto);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_CREATE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_CREATE_FAIL.getCode(), MallResponseEnum.COUPON_CREATE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("创建优惠券失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_CREATE_FAIL.getCode(), MallResponseEnum.COUPON_CREATE_FAIL.getMessage());
        }
    }

    /**
     * 更新优惠券
     */
    @ApiOperation("更新优惠券")
    @PutMapping
    public ServerResponseEntity<String> update(@Valid @RequestBody MallCouponDto mallCouponDto) {
        try {
            boolean result = mallCouponService.updateCoupon(mallCouponDto);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_UPDATE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_UPDATE_FAIL.getCode(), MallResponseEnum.COUPON_UPDATE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("更新优惠券失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_UPDATE_FAIL.getCode(), MallResponseEnum.COUPON_UPDATE_FAIL.getMessage());
        }
    }

    /**
     * 删除优惠券
     */
    @ApiOperation("删除优惠券")
    @DeleteMapping("/{ids}")
    public ServerResponseEntity<String> delete(
            @ApiParam("优惠券ID，多个用逗号分隔") @PathVariable String ids) {
        try {
            // 处理多个ID删除
            String[] idArray = ids.split(",");
            boolean result = true;
            for (String idStr : idArray) {
                Long id = Long.parseLong(idStr.trim());
                result = result && mallCouponService.deleteCoupon(id);
            }
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_DELETE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_DELETE_FAIL.getCode(), MallResponseEnum.COUPON_DELETE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("删除优惠券失败，IDs: {}", ids, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_DELETE_FAIL.getCode(), MallResponseEnum.COUPON_DELETE_FAIL.getMessage());
        }
    }

    /**
     * 发布优惠券
     */
    @ApiOperation("发布优惠券")
    @PostMapping("/publish/{id}")
    public ServerResponseEntity<String> publish(
            @ApiParam("优惠券ID") @PathVariable Long id) {
        try {
            boolean result = mallCouponService.publishCoupon(id);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_PUBLISH_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_PUBLISH_FAIL.getCode(), MallResponseEnum.COUPON_PUBLISH_FAIL.getMessage());
        } catch (Exception e) {
            log.error("发布优惠券失败，ID: {}", id, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_PUBLISH_FAIL.getCode(), MallResponseEnum.COUPON_PUBLISH_FAIL.getMessage());
        }
    }

    /**
     * 停用优惠券
     */
    @ApiOperation("停用优惠券")
    @PostMapping("/disable/{id}")
    public ServerResponseEntity<String> disable(
            @ApiParam("优惠券ID") @PathVariable Long id) {
        try {
            boolean result = mallCouponService.disableCoupon(id);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_DISABLE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_DISABLE_FAIL.getCode(), MallResponseEnum.COUPON_DISABLE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("停用优惠券失败，ID: {}", id, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_DISABLE_FAIL.getCode(), MallResponseEnum.COUPON_DISABLE_FAIL.getMessage());
        }
    }

    /**
     * 查询优惠券统计信息
     */
    @ApiOperation("查询优惠券统计信息")
    @GetMapping("/statistics")
    public ServerResponseEntity<MallCouponStatisticsVO> statistics() {
        try {
            MallCouponStatisticsVO statistics = mallCouponService.getCouponStatistics();
            return ServerResponseEntity.success(statistics);
        } catch (Exception e) {
            log.error("查询优惠券统计信息失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 检查并更新过期的优惠券
     */
    @ApiOperation("检查并更新过期的优惠券")
    @PostMapping("/check-expired")
    public ServerResponseEntity<String> checkExpiredCoupons() {
        try {
            mallCouponService.checkAndUpdateExpiredCoupons();
            return ServerResponseEntity.success("检查过期优惠券完成");
        } catch (Exception e) {
            log.error("检查过期优惠券失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

}