package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.model.mall.MallProductSku;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallProductSkuService;
import com.chennian.storytelling.common.enums.MallResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商品SKU管理")
@RestController
@RequestMapping("/mall/product-sku")
public class MallProductSkuController {

    private final MallProductSkuService mallProductSkuService;

    public MallProductSkuController(MallProductSkuService mallProductSkuService) {
        this.mallProductSkuService = mallProductSkuService;
    }


    /**
     * 分页查询SKU列表
     */
    @ApiOperation("分页查询SKU列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallProductSku>> getSkuPage(
            @RequestBody PageParam<MallProductSku> page,
            @ApiParam("查询条件") MallProductSku sku) {
        try {
            IPage<MallProductSku> skuPage = mallProductSkuService.getSkuPage(page, sku);
            return ServerResponseEntity.success(skuPage);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_LIST_FAIL);
        }
    }
    
    /**
     * 根据商品ID获取SKU列表
     */
    @ApiOperation("根据商品ID获取SKU列表")
    @GetMapping("/product/{productId}")
    public ServerResponseEntity<List<MallProductSku>> getSkusByProductId(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            List<MallProductSku> skus = mallProductSkuService.getSkusByProductId(productId);
            return ServerResponseEntity.success(skus);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_LIST_FAIL);
        }
    }
    
    /**
     * 根据SKU ID查询详情
     */
    @ApiOperation("根据SKU ID查询详情")
    @GetMapping("/{skuId}")
    public ServerResponseEntity<MallProductSku> getSkuById(
            @ApiParam("SKU ID") @PathVariable Long skuId) {
        try {
            MallProductSku sku = mallProductSkuService.getById(skuId);
            if (sku == null) {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_NOT_FOUND);
            }
            return ServerResponseEntity.success(sku);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_DETAIL_FAIL);
        }
    }
    
    /**
     * 根据SKU编码查询详情
     */
    @ApiOperation("根据SKU编码查询详情")
    @GetMapping("/code/{skuCode}")
    public ServerResponseEntity<MallProductSku> getSkuByCode(
            @ApiParam("SKU编码") @PathVariable String skuCode) {
        try {
            MallProductSku sku = mallProductSkuService.getBySkuCode(skuCode);
            if (sku == null) {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_NOT_FOUND);
            }
            return ServerResponseEntity.success(sku);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_DETAIL_FAIL);
        }
    }
    
    /**
     * 创建商品SKU
     */
    @ApiOperation("创建商品SKU")
    @PostMapping
    public ServerResponseEntity<String> createSku(@RequestBody MallProductSku sku) {
        try {
            sku.setCreateBy(SecurityUtils.getUserName());
            sku.setCreateTime(LocalDateTime.now());
            boolean result = mallProductSkuService.save(sku);
            if (result) {
                return ServerResponseEntity.success("创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_CREATE_FAIL);
        }
    }
    
    /**
     * 批量创建商品SKU
     */
    @ApiOperation("批量创建商品SKU")
    @PostMapping("/batch")
    public ServerResponseEntity<String> batchCreateSkus(
            @RequestBody List<MallProductSku> skus) {
        try {
            String username = SecurityUtils.getUserName();
            LocalDateTime now = LocalDateTime.now();
            for (MallProductSku sku : skus) {
                sku.setCreateBy(username);
                sku.setCreateTime(now);
            }
            boolean result = mallProductSkuService.saveBatch(skus);
            if (result) {
                return ServerResponseEntity.success("批量创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_CREATE_FAIL);
        }
    }
    
    /**
     * 更新商品SKU
     */
    @ApiOperation("更新商品SKU")
    @PutMapping("/{skuId}")
    public ServerResponseEntity<String> updateSku(
            @ApiParam("SKU ID") @PathVariable Long skuId,
            @RequestBody MallProductSku sku) {
        try {
            sku.setId(skuId);
            sku.setUpdateBy(SecurityUtils.getUserName());
            sku.setUpdateTime(LocalDateTime.now());
            boolean result = mallProductSkuService.updateById(sku);
            if (result) {
                return ServerResponseEntity.success("更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_UPDATE_FAIL);
        }
    }
    
    /**
     * 删除商品SKU
     */
    @ApiOperation("删除商品SKU")
    @DeleteMapping("/{skuId}")
    public ServerResponseEntity<String> deleteSku(
            @ApiParam("SKU ID") @PathVariable Long skuId) {
        try {
            boolean result = mallProductSkuService.removeById(skuId);
            if (result) {
                return ServerResponseEntity.success("删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_DELETE_FAIL);
        }
    }
    
    /**
     * 批量删除SKU
     */
    @ApiOperation("批量删除SKU")
    @DeleteMapping("/batch")
    public ServerResponseEntity<String> batchDeleteSkus(
            @ApiParam("SKU ID列表") @RequestParam List<Long> skuIds) {
        try {
            boolean result = mallProductSkuService.removeByIds(skuIds);
            if (result) {
                return ServerResponseEntity.success("批量删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_DELETE_FAIL);
        }
    }
    
    /**
     * 启用/禁用SKU
     */
    @ApiOperation("启用/禁用SKU")
    @PutMapping("/{skuId}/status")
    public ServerResponseEntity<String> updateSkuStatus(
            @ApiParam("SKU ID") @PathVariable Long skuId,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            boolean result = mallProductSkuService.updateSkuStatus(skuId, status, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("状态更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_STATUS_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_STATUS_UPDATE_FAIL);
        }
    }
    
    /**
     * 批量更新SKU状态
     */
    @ApiOperation("批量更新SKU状态")
    @PutMapping("/batch/status")
    public ServerResponseEntity<String> batchUpdateSkuStatus(
            @ApiParam("SKU ID列表") @RequestParam List<Long> skuIds,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            boolean result = mallProductSkuService.batchUpdateSkuStatus(skuIds, status, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("批量状态更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_STATUS_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_STATUS_UPDATE_FAIL);
        }
    }
    
    /**
     * 更新SKU库存
     */
    @ApiOperation("更新SKU库存")
    @PutMapping("/{skuId}/stock")
    public ServerResponseEntity<String> updateSkuStock(
            @ApiParam("SKU ID") @PathVariable Long skuId,
            @ApiParam("库存数量") @RequestParam Integer stockQuantity,
            @ApiParam("操作类型") @RequestParam String operationType,
            @ApiParam("操作备注") @RequestParam(required = false) String remark) {
        try {
            boolean result = mallProductSkuService.updateSkuStock(skuId, stockQuantity, operationType, remark, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("库存更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_STOCK_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_STOCK_UPDATE_FAIL);
        }
    }
    
    /**
     * 批量更新SKU库存
     */
    @ApiOperation("批量更新SKU库存")
    @PutMapping("/batch/stock")
    public ServerResponseEntity<String> batchUpdateSkuStock(
            @RequestBody List<Map<String, Object>> stockUpdates) {
        try {
            boolean result = mallProductSkuService.batchUpdateSkuStock(stockUpdates, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("批量库存更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_STOCK_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_STOCK_UPDATE_FAIL);
        }
    }
    
    /**
     * 更新SKU价格
     */
    @ApiOperation("更新SKU价格")
    @PutMapping("/{skuId}/price")
    public ServerResponseEntity<String> updateSkuPrice(
            @ApiParam("SKU ID") @PathVariable Long skuId,
            @ApiParam("市场价格") @RequestParam(required = false) BigDecimal marketPrice,
            @ApiParam("销售价格") @RequestParam(required = false) BigDecimal salePrice,
            @ApiParam("成本价格") @RequestParam(required = false) BigDecimal costPrice) {
        try {
            boolean result = mallProductSkuService.updateSkuPrice(skuId, marketPrice, salePrice, costPrice, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("价格更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_PRICE_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_PRICE_UPDATE_FAIL);
        }
    }
    
    /**
     * 批量更新SKU价格
     */
    @ApiOperation("批量更新SKU价格")
    @PutMapping("/batch/price")
    public ServerResponseEntity<String> batchUpdateSkuPrice(
            @RequestBody List<Map<String, Object>> priceUpdates) {
        try {
            boolean result = mallProductSkuService.batchUpdateSkuPrice(priceUpdates, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("批量价格更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_PRICE_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_BATCH_PRICE_UPDATE_FAIL);
        }
    }
    
    /**
     * 设置默认SKU
     */
    @ApiOperation("设置默认SKU")
    @PutMapping("/{skuId}/default")
    public ServerResponseEntity<String> setDefaultSku(
            @ApiParam("SKU ID") @PathVariable Long skuId) {
        try {
            boolean result = mallProductSkuService.setDefaultSku(skuId, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("设置默认SKU成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_SET_DEFAULT_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_SET_DEFAULT_FAIL);
        }
    }
    
    /**
     * 获取库存预警SKU列表
     */
    @ApiOperation("获取库存预警SKU列表")
    @PostMapping("/stock-alert")
    public ServerResponseEntity<IPage<MallProductSku>> getStockAlertSkus(
            @RequestBody PageParam<MallProductSku> page) {
        try {
            IPage<MallProductSku> stockAlertSkus = mallProductSkuService.getStockAlertSkus(page);
            return ServerResponseEntity.success(stockAlertSkus);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_STOCK_ALERT_FAIL);
        }
    }
    
    /**
     * 获取SKU库存统计
     */
    @ApiOperation("获取SKU库存统计")
    @GetMapping("/stock/stats")
    public ServerResponseEntity<Map<String, Object>> getSkuStockStats(
            @ApiParam("商品ID") @RequestParam(required = false) Long productId) {
        try {
            Map<String, Object> stockStats = mallProductSkuService.getSkuStockStats(productId);
            return ServerResponseEntity.success(stockStats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_STOCK_STATS_FAIL);
        }
    }
    
    /**
     * 获取SKU销量统计
     */
    @ApiOperation("获取SKU销量统计")
    @GetMapping("/sales/stats")
    public ServerResponseEntity<Map<String, Object>> getSkuSalesStats(
            @ApiParam("商品ID") @RequestParam(required = false) Long productId,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> salesStats = mallProductSkuService.getSkuSalesStats(productId, startDate, endDate);
            return ServerResponseEntity.success(salesStats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_SALES_STATS_FAIL);
        }
    }
    
    /**
     * 复制SKU配置
     */
    @ApiOperation("复制SKU配置")
    @PostMapping("/copy")
    public ServerResponseEntity<String> copySkuConfig(
            @ApiParam("源商品ID") @RequestParam Long sourceProductId,
            @ApiParam("目标商品ID") @RequestParam Long targetProductId) {
        try {
            boolean result = mallProductSkuService.copySkuConfig(sourceProductId, targetProductId, SecurityUtils.getUserName());
            if (result) {
                return ServerResponseEntity.success("复制SKU配置成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_COPY_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_COPY_FAIL);
        }
    }
    
    /**
     * 生成SKU编码
     */
    @ApiOperation("生成SKU编码")
    @GetMapping("/generate-code")
    public ServerResponseEntity<String> generateSkuCode(
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("规格属性") @RequestParam String specAttributes) {
        try {
            String skuCode = mallProductSkuService.generateSkuCode(productId, specAttributes);
            return ServerResponseEntity.success(skuCode);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GENERATE_CODE_FAIL);
        }
    }
    
    /**
     * 检查SKU编码是否存在
     */
    @ApiOperation("检查SKU编码是否存在")
    @GetMapping("/check-code")
    public ServerResponseEntity<Boolean> checkSkuCodeExists(
            @ApiParam("SKU编码") @RequestParam String skuCode,
            @ApiParam("排除的SKU ID") @RequestParam(required = false) Long excludeSkuId) {
        try {
            boolean exists = mallProductSkuService.checkSkuCodeExists(skuCode, excludeSkuId);
            return ServerResponseEntity.success(exists);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.CHECK_CODE_FAIL);
        }
    }
    
    /**
     * 获取SKU价格区间
     */
    @ApiOperation("获取SKU价格区间")
    @GetMapping("/price-range/{productId}")
    public ServerResponseEntity<Map<String, BigDecimal>> getSkuPriceRange(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            Map<String, BigDecimal> priceRange = mallProductSkuService.getSkuPriceRange(productId);
            return ServerResponseEntity.success(priceRange);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_PRICE_RANGE_FAIL);
        }
    }
    
    /**
     * 导入SKU数据
     */
    @ApiOperation("导入SKU数据")
    @PostMapping("/import")
    public ServerResponseEntity<String> importSkus(
            @RequestBody List<MallProductSku> skus) {
        try {
            String username = SecurityUtils.getUserName();
            boolean result = mallProductSkuService.importSkus(skus, username);
            if (result) {
                return ServerResponseEntity.success("导入SKU数据成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_IMPORT_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_IMPORT_FAIL);
        }
    }
    
    /**
     * 导出SKU数据
     */
    @ApiOperation("导出SKU数据")
    @GetMapping("/export")
    public ServerResponseEntity<String> exportSkus(
            @ApiParam("商品ID") @RequestParam(required = false) Long productId,
            @ApiParam("SKU ID列表") @RequestParam(required = false) List<Long> skuIds) {
        try {
            String exportUrl = mallProductSkuService.exportSkus(productId, skuIds);
            return ServerResponseEntity.success(exportUrl);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_EXPORT_FAIL);
        }
    }
}