package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/product")
@Tag(name = "产品管理")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * 查询产品列表
     */
    @PostMapping("/page")
    @Operation(summary = "产品列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品列表")
    public ServerResponseEntity<IPage<Product>> page(Product product, PageParam<Product> page) {
        IPage<Product> productPage = productService.findByPage(page, product);
        return ServerResponseEntity.success(productPage);
    }

    /**
     * 获取产品详细信息
     */
    @PostMapping("/info/{productId}")
    @Operation(summary = "产品详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品详情")
    public ServerResponseEntity<Product> info(@PathVariable("productId") Long productId) {
        Product product = productService.selectProductById(productId);
        return ServerResponseEntity.success(product);
    }

    /**
     * 新增产品
     */
    @PostMapping("/add")
    @Operation(summary = "新增产品")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增产品")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody Product product) {
        return ServerResponseEntity.success(productService.insertProduct(product));
    }

    /**
     * 修改产品
     */
    @PostMapping("/update")
    @Operation(summary = "修改产品")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改产品")
    public ServerResponseEntity<Integer> update(@RequestBody Product product) {
        return ServerResponseEntity.success(productService.updateProduct(product));
    }

    /**
     * 删除产品
     */
    @PostMapping("/remove/{productIds}")
    @Operation(summary = "删除产品")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除产品")
    public ServerResponseEntity<Integer> remove(@PathVariable("productIds") Long[] productIds) {
        return ServerResponseEntity.success(productService.deleteProductByIds(productIds));
    }

    /**
     * 产品状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "产品状态修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "产品状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody Product product) {
        return ServerResponseEntity.success(productService.updateProductStatus(product));
    }

    /**
     * 获取产品库存信息
     */
    @PostMapping("/inventory/{productId}")
    @Operation(summary = "产品库存信息")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品库存信息")
    public ServerResponseEntity<Map<String, Object>> inventory(@PathVariable("productId") Long productId) {
        Map<String, Object> inventory = productService.getProductInventory(productId);
        return ServerResponseEntity.success(inventory);
    }

    /**
     * 获取产品销售记录
     */
    @PostMapping("/sales/{productId}")
    @Operation(summary = "产品销售记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品销售记录")
    public ServerResponseEntity<List<Map<String, Object>>> salesRecords(@PathVariable("productId") Long productId) {
        List<Map<String, Object>> salesRecords = productService.getProductSalesRecords(productId);
        return ServerResponseEntity.success(salesRecords);
    }
}