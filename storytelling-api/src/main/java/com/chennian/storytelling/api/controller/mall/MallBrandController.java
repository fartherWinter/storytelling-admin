package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallBrand;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.chennian.storytelling.service.mall.MallBrandService;


import java.util.List;
import java.util.Map;

/**
 * 商品品牌Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Api(tags = "商品品牌管理")
@RestController
@RequestMapping("/mall/brand")
public class MallBrandController {

    @Autowired
    private MallBrandService mallBrandService;

    /**
     * 分页查询品牌列表
     */
    @ApiOperation("分页查询品牌列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallBrand>> getBrandPage(
            @RequestBody PageParam<MallBrand> page,
            @ApiParam("查询条件") MallBrand brand) {
        IPage<MallBrand> result = mallBrandService.findByPage(page, brand);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 根据ID查询品牌详情
     */
    @ApiOperation("根据ID查询品牌详情")
    @GetMapping("/{brandId}")
    public ServerResponseEntity<MallBrand> getBrandById(
            @ApiParam("品牌ID") @PathVariable Long brandId) {
        MallBrand brand = mallBrandService.getBrandDetail(brandId);
        return ServerResponseEntity.success(brand);
    }
    
    /**
     * 获取所有启用的品牌列表
     */
    @ApiOperation("获取所有启用的品牌列表")
    @GetMapping("/enabled")
    public ServerResponseEntity<List<MallBrand>> getEnabledBrands() {
        List<MallBrand> brands = mallBrandService.getEnabledBrands();
        return ServerResponseEntity.success(brands);
    }
    
    /**
     * 获取推荐品牌列表
     */
    @ApiOperation("获取推荐品牌列表")
    @GetMapping("/recommend")
    public ServerResponseEntity<List<MallBrand>> getRecommendBrands(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<MallBrand> brands = mallBrandService.getRecommendBrands(limit);
        return ServerResponseEntity.success(brands);
    }
    
    /**
     * 根据品牌ID查询商品列表
     */
    @ApiOperation("根据品牌ID查询商品列表")
    @PostMapping("/{brandId}/products")
    public ServerResponseEntity<IPage<MallProduct>> getProductsByBrandId(
            @ApiParam("品牌ID") @PathVariable Long brandId,
            @RequestBody PageParam<MallProduct> page) {
        IPage<MallProduct> result = mallBrandService.getProductsByBrand(brandId, page);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 创建品牌
     */
    @ApiOperation("创建品牌")
    @PostMapping
    public ServerResponseEntity<String> createBrand(
            @ApiParam("品牌信息") @RequestBody MallBrand brand) {
        boolean result = mallBrandService.createBrand(brand);
        return result ? ServerResponseEntity.success("品牌创建成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_CREATE_FAIL);
    }
    
    /**
     * 更新品牌
     */
    @ApiOperation("更新品牌")
    @PutMapping("/{id}")
    public ServerResponseEntity<String> updateBrand(
            @ApiParam("品牌ID") @PathVariable Long id,
            @ApiParam("品牌信息") @RequestBody MallBrand brand) {
        brand.setId(id);
        boolean result = mallBrandService.updateBrand(brand);
        return result ? ServerResponseEntity.success("品牌更新成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_UPDATE_FAIL);
    }
    
    /**
     * 删除品牌
     */
    @ApiOperation("删除品牌")
    @DeleteMapping("/{brandId}")
    public ServerResponseEntity<String> deleteBrand(
            @ApiParam("品牌ID") @PathVariable Long brandId) {
        try {
            boolean result = mallBrandService.deleteBrand(brandId);
            return result ? ServerResponseEntity.success("品牌删除成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_DELETE_FAIL);
        } catch (RuntimeException e) {
            log.error("删除品牌失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.BRAND_DELETE_FAIL);
        }
    }
    
    /**
     * 启用品牌
     */
    @ApiOperation("启用品牌")
    @PutMapping("/{brandId}/enable")
    public ServerResponseEntity<String> enableBrand(
            @ApiParam("品牌ID") @PathVariable Long brandId) {
        boolean result = mallBrandService.enableBrand(brandId);
        return result ? ServerResponseEntity.success("品牌启用成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_ENABLE_FAIL);
    }
    
    /**
     * 禁用品牌
     */
    @ApiOperation("禁用品牌")
    @PutMapping("/{brandId}/disable")
    public ServerResponseEntity<String> disableBrand(
            @ApiParam("品牌ID") @PathVariable Long brandId) {
        boolean result = mallBrandService.disableBrand(brandId);
        return result ? ServerResponseEntity.success("品牌禁用成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_DISABLE_FAIL);
    }
    
    /**
     * 设置推荐品牌
     */
    @ApiOperation("设置推荐品牌")
    @PutMapping("/{brandId}/recommend")
    public ServerResponseEntity<String> setRecommendBrand(
            @ApiParam("品牌ID") @PathVariable Long brandId,
            @ApiParam("是否推荐") @RequestParam Integer isRecommend) {
        boolean result = mallBrandService.setRecommendBrand(brandId, isRecommend);
        return result ? ServerResponseEntity.success("设置成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_SET_RECOMMEND_FAIL);
    }
    
    /**
     * 获取品牌统计信息
     */
    @ApiOperation("获取品牌统计信息")
    @GetMapping("/{brandId}/stats")
    public ServerResponseEntity<Map<String, Object>> getBrandStats(
            @ApiParam("品牌ID") @PathVariable Long brandId) {
        Map<String, Object> statistics = mallBrandService.getBrandStatistics(brandId);
        return ServerResponseEntity.success(statistics);
    }
    
    /**
     * 搜索品牌
     */
    @ApiOperation("搜索品牌")
    @PostMapping("/search")
    public ServerResponseEntity<IPage<MallBrand>> searchBrands(
            @ApiParam("搜索关键词") @RequestParam String keyword,
            @RequestBody PageParam<MallBrand> page) {
        IPage<MallBrand> result = mallBrandService.searchBrands(keyword, page);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 批量操作品牌状态
     */
    @ApiOperation("批量操作品牌状态")
    @PutMapping("/batch/status")
    public ServerResponseEntity<String> batchUpdateStatus(
            @ApiParam("品牌ID列表") @RequestParam List<Long> brandIds,
            @ApiParam("状态") @RequestParam Integer status) {
        boolean result = mallBrandService.batchUpdateStatus(brandIds, status);
        return result ? ServerResponseEntity.success("批量操作成功") : ServerResponseEntity.fail(MallResponseEnum.BRAND_BATCH_UPDATE_FAIL);
    }
}