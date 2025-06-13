package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.AssetErrorCode;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.asset.AssetCategory;
import com.chennian.storytelling.bean.model.asset.AssetInfo;
import com.chennian.storytelling.bean.model.asset.AssetInventory;
import com.chennian.storytelling.bean.model.asset.AssetMaintenance;
import com.chennian.storytelling.bean.model.asset.AssetDepreciation;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产管理控制器
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Tag(name = "资产管理")
@RestController
@RequestMapping("/erp/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    // ==================== 资产信息管理 ====================

    @Operation(summary = "分页查询资产列表")
    @PostMapping("/info/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询资产列表")
    public ServerResponseEntity<IPage<AssetInfo>> getAssetList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "资产名称") @RequestParam(required = false) String assetName,
            @Parameter(description = "资产编号") @RequestParam(required = false) String assetNo,
            @Parameter(description = "类别ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<AssetInfo> page = new Page<>(current, size);
        AssetInfo asset = new AssetInfo();
        asset.setAssetName(assetName);
        asset.setAssetNo(assetNo);
        asset.setCategoryId(categoryId);
        asset.setDepartmentId(departmentId);
        asset.setStatus(status);
        
        IPage<AssetInfo> result = assetService.getAssetList(page, asset);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询资产信息")
    @GetMapping("/info/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询资产信息")
    public ServerResponseEntity<AssetInfo> getAssetById(@Parameter(description = "资产ID") @PathVariable Long id) {
        AssetInfo asset = assetService.getAssetById(id);
        return ServerResponseEntity.success(asset);
    }

    @Operation(summary = "保存资产信息")
    @PostMapping("/info")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存资产信息")
    public ServerResponseEntity<Boolean> saveAsset(@Validated @RequestBody AssetInfo asset) {
        boolean success = assetService.saveAsset(asset);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.SAVE_FAILED.getCode(), AssetErrorCode.SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新资产信息")
    @PutMapping("/info")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新资产信息")
    public ServerResponseEntity<Boolean> updateAsset(@Validated @RequestBody AssetInfo asset) {
        boolean success = assetService.saveAsset(asset);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.UPDATE_FAILED.getCode(), AssetErrorCode.UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除资产信息")
    @DeleteMapping("/info/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除资产信息")
    public ServerResponseEntity<Boolean> deleteAsset(@Parameter(description = "资产ID") @PathVariable Long id) {
        boolean success = assetService.deleteAsset(id);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DELETE_FAILED.getCode(), AssetErrorCode.DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据类别查询资产列表")
    @GetMapping("/info/category/{categoryId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据类别查询资产列表")
    public ServerResponseEntity<List<AssetInfo>> getAssetsByCategory(@Parameter(description = "类别ID") @PathVariable Long categoryId) {
        List<AssetInfo> assets = assetService.getAssetsByCategory(categoryId);
        return ServerResponseEntity.success(assets);
    }

    @Operation(summary = "根据部门查询资产列表")
    @GetMapping("/info/department/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据部门查询资产列表")
    public ServerResponseEntity<List<AssetInfo>> getAssetsByDepartment(@Parameter(description = "部门ID") @PathVariable Long departmentId) {
        List<AssetInfo> assets = assetService.getAssetsByDepartment(departmentId);
        return ServerResponseEntity.success(assets);
    }

    @Operation(summary = "查询即将到期的资产")
    @GetMapping("/info/expiring")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询即将到期的资产")
    public ServerResponseEntity<List<AssetInfo>> getExpiringAssets(@Parameter(description = "提前月数") @RequestParam(defaultValue = "3") Integer months) {
        List<AssetInfo> assets = assetService.getExpiringAssets(months);
        return ServerResponseEntity.success(assets);
    }

    @Operation(summary = "获取资产统计信息")
    @GetMapping("/info/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取资产统计信息")
    public ServerResponseEntity<Map<String, Object>> getAssetStatistics() {
        Map<String, Object> statistics = assetService.getAssetStatistics();
        return ServerResponseEntity.success(statistics);
    }

    @Operation(summary = "计算资产折旧")
    @PostMapping("/info/depreciation")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "计算资产折旧")
    public ServerResponseEntity<Boolean> calculateDepreciation() {
        boolean success = assetService.calculateDepreciation();
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DEPRECIATION_CALCULATE_FAILED.getCode(), AssetErrorCode.DEPRECIATION_CALCULATE_FAILED.getMessage());
    }

    // ==================== 资产类别管理 ====================

    @Operation(summary = "查询资产类别列表")
    @GetMapping("/category/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询资产类别列表")
    public ServerResponseEntity<List<AssetCategory>> getCategoryList(
            @Parameter(description = "类别名称") @RequestParam(required = false) String categoryName,
            @Parameter(description = "类别编码") @RequestParam(required = false) String categoryCode,
            @Parameter(description = "上级类别ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        AssetCategory category = new AssetCategory();
        category.setCategoryName(categoryName);
        category.setCategoryCode(categoryCode);
        category.setParentId(parentId);
        category.setStatus(status);
        
        List<AssetCategory> categories = assetService.getCategoryList(category);
        return ServerResponseEntity.success(categories);
    }

    @Operation(summary = "根据ID查询资产类别")
    @GetMapping("/category/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询资产类别")
    public ServerResponseEntity<AssetCategory> getCategoryById(@Parameter(description = "类别ID") @PathVariable Long id) {
        AssetCategory category = assetService.getCategoryById(id);
        return ServerResponseEntity.success(category);
    }

    @Operation(summary = "保存资产类别")
    @PostMapping("/category")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存资产类别")
    public ServerResponseEntity<Boolean> saveCategory(@Validated @RequestBody AssetCategory category) {
        boolean success = assetService.saveCategory(category);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.SAVE_FAILED.getCode(), AssetErrorCode.SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新资产类别")
    @PutMapping("/category")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新资产类别")
    public ServerResponseEntity<Boolean> updateCategory(@Validated @RequestBody AssetCategory category) {
        boolean success = assetService.saveCategory(category);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.UPDATE_FAILED.getCode(), AssetErrorCode.UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除资产类别")
    @DeleteMapping("/category/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除资产类别")
    public ServerResponseEntity<Boolean> deleteCategory(@Parameter(description = "类别ID") @PathVariable Long id) {
        boolean success = assetService.deleteCategory(id);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DELETE_FAILED.getCode(), AssetErrorCode.DELETE_FAILED.getMessage());
    }

    @Operation(summary = "获取类别树形结构")
    @GetMapping("/category/tree")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取类别树形结构")
    public ServerResponseEntity<List<AssetCategory>> getCategoryTree() {
        List<AssetCategory> tree = assetService.getCategoryTree();
        return ServerResponseEntity.success(tree);
    }

    // ==================== 资产维护管理 ====================

    @Operation(summary = "分页查询维护记录列表")
    @PostMapping("/maintenance/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询维护记录列表")
    public ServerResponseEntity<IPage<AssetMaintenance>> getMaintenanceList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "资产ID") @RequestParam(required = false) Long assetId,
            @Parameter(description = "资产名称") @RequestParam(required = false) String assetName,
            @Parameter(description = "维护类型") @RequestParam(required = false) Integer maintenanceType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<AssetMaintenance> page = new Page<>(current, size);
        AssetMaintenance maintenance = new AssetMaintenance();
        maintenance.setAssetId(assetId);
        maintenance.setAssetName(assetName);
        maintenance.setMaintenanceType(maintenanceType);
        maintenance.setStatus(status);
        
        IPage<AssetMaintenance> result = assetService.getMaintenanceList(page, maintenance);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询维护记录")
    @GetMapping("/maintenance/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询维护记录")
    public ServerResponseEntity<AssetMaintenance> getMaintenanceById(@Parameter(description = "维护记录ID") @PathVariable Long id) {
        AssetMaintenance maintenance = assetService.getMaintenanceById(id);
        return ServerResponseEntity.success(maintenance);
    }

    @Operation(summary = "保存维护记录")
    @PostMapping("/maintenance")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存维护记录")
    public ServerResponseEntity<Boolean> saveMaintenance(@Validated @RequestBody AssetMaintenance maintenance) {
        boolean success = assetService.saveMaintenance(maintenance);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.SAVE_FAILED.getCode(), AssetErrorCode.SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新维护记录")
    @PutMapping("/maintenance")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新维护记录")
    public ServerResponseEntity<Boolean> updateMaintenance(@Validated @RequestBody AssetMaintenance maintenance) {
        boolean success = assetService.saveMaintenance(maintenance);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.UPDATE_FAILED.getCode(), AssetErrorCode.UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除维护记录")
    @DeleteMapping("/maintenance/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除维护记录")
    public ServerResponseEntity<Boolean> deleteMaintenance(@Parameter(description = "维护记录ID") @PathVariable Long id) {
        boolean success = assetService.deleteMaintenance(id);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DELETE_FAILED.getCode(), AssetErrorCode.DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据资产查询维护记录")
    @GetMapping("/maintenance/asset/{assetId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据资产查询维护记录")
    public ServerResponseEntity<List<AssetMaintenance>> getMaintenanceByAsset(@Parameter(description = "资产ID") @PathVariable Long assetId) {
        List<AssetMaintenance> maintenances = assetService.getMaintenanceByAsset(assetId);
        return ServerResponseEntity.success(maintenances);
    }

    @Operation(summary = "查询即将到期的维护任务")
    @GetMapping("/maintenance/upcoming")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询即将到期的维护任务")
    public ServerResponseEntity<List<AssetMaintenance>> getUpcomingMaintenance(@Parameter(description = "提前天数") @RequestParam(defaultValue = "7") Integer days) {
        List<AssetMaintenance> maintenances = assetService.getUpcomingMaintenance(days);
        return ServerResponseEntity.success(maintenances);
    }

    @Operation(summary = "获取维护统计信息")
    @GetMapping("/maintenance/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取维护统计信息")
    public ServerResponseEntity<Map<String, Object>> getMaintenanceStatistics(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Map<String, Object> statistics = assetService.getMaintenanceStatistics(startDate, endDate);
        return ServerResponseEntity.success(statistics);
    }

    // ==================== 资产盘点管理 ====================

    @Operation(summary = "分页查询盘点记录列表")
    @PostMapping("/inventory/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询盘点记录列表")
    public ServerResponseEntity<IPage<AssetInventory>> getInventoryList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "盘点批次号") @RequestParam(required = false) String inventoryBatchNo,
            @Parameter(description = "资产ID") @RequestParam(required = false) Long assetId,
            @Parameter(description = "资产名称") @RequestParam(required = false) String assetName,
            @Parameter(description = "盘点结果") @RequestParam(required = false) Integer inventoryResult,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<AssetInventory> page = new Page<>(current, size);
        AssetInventory inventory = new AssetInventory();
        inventory.setInventoryBatchNo(inventoryBatchNo);
        inventory.setAssetId(assetId);
        inventory.setAssetName(assetName);
        inventory.setInventoryResult(inventoryResult);
        inventory.setStatus(status);
        
        IPage<AssetInventory> result = assetService.getInventoryList(page, inventory);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询盘点记录")
    @GetMapping("/inventory/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询盘点记录")
    public ServerResponseEntity<AssetInventory> getInventoryById(@Parameter(description = "盘点记录ID") @PathVariable Long id) {
        AssetInventory inventory = assetService.getInventoryById(id);
        return ServerResponseEntity.success(inventory);
    }

    @Operation(summary = "保存盘点记录")
    @PostMapping("/inventory")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存盘点记录")
    public ServerResponseEntity<Boolean> saveInventory(@Validated @RequestBody AssetInventory inventory) {
        boolean success = assetService.saveInventory(inventory);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.SAVE_FAILED.getCode(), AssetErrorCode.SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新盘点记录")
    @PutMapping("/inventory")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新盘点记录")
    public ServerResponseEntity<Boolean> updateInventory(@Validated @RequestBody AssetInventory inventory) {
        boolean success = assetService.saveInventory(inventory);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.UPDATE_FAILED.getCode(), AssetErrorCode.UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除盘点记录")
    @DeleteMapping("/inventory/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除盘点记录")
    public ServerResponseEntity<Boolean> deleteInventory(@Parameter(description = "盘点记录ID") @PathVariable Long id) {
        boolean success = assetService.deleteInventory(id);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DELETE_FAILED.getCode(), AssetErrorCode.DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据批次号查询盘点记录")
    @GetMapping("/inventory/batch/{inventoryBatchNo}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据批次号查询盘点记录")
    public ServerResponseEntity<List<AssetInventory>> getInventoryByBatch(@Parameter(description = "盘点批次号") @PathVariable String inventoryBatchNo) {
        List<AssetInventory> inventories = assetService.getInventoryByBatch(inventoryBatchNo);
        return ServerResponseEntity.success(inventories);
    }

    @Operation(summary = "生成盘点批次号")
    @GetMapping("/inventory/generate-batch-no")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生成盘点批次号")
    public ServerResponseEntity<String> generateInventoryBatchNo() {
        String batchNo = assetService.generateInventoryBatchNo();
        return ServerResponseEntity.success(batchNo);
    }

    @Operation(summary = "获取盘点统计信息")
    @GetMapping("/inventory/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取盘点统计信息")
    public ServerResponseEntity<Map<String, Object>> getInventoryStatistics(@Parameter(description = "盘点批次号") @RequestParam String inventoryBatchNo) {
        Map<String, Object> statistics = assetService.getInventoryStatistics(inventoryBatchNo);
        return ServerResponseEntity.success(statistics);
    }

    // ==================== 资产折旧管理 ====================

    @Operation(summary = "分页查询折旧记录列表")
    @PostMapping("/depreciation/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询折旧记录列表")
    public ServerResponseEntity<IPage<AssetDepreciation>> getDepreciationList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "资产ID") @RequestParam(required = false) Long assetId,
            @Parameter(description = "折旧年月") @RequestParam(required = false) String depreciationYearMonth,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        PageParam<AssetDepreciation> page = new PageParam<>();
        page.setCurrent(current);
        page.setSize(size);
        AssetDepreciation depreciation = new AssetDepreciation();
        depreciation.setAssetId(assetId);
        depreciation.setDepreciationMonth(depreciationYearMonth);
        depreciation.setStatus(status);
        
        IPage<AssetDepreciation> result = assetService.getDepreciationList(depreciation, page);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询折旧记录")
    @GetMapping("/depreciation/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询折旧记录")
    public ServerResponseEntity<AssetDepreciation> getDepreciationById(@Parameter(description = "折旧记录ID") @PathVariable Long id) {
        AssetDepreciation depreciation = assetService.getDepreciationById(id);
        return ServerResponseEntity.success(depreciation);
    }

    @Operation(summary = "保存折旧记录")
    @PostMapping("/depreciation")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存折旧记录")
    public ServerResponseEntity<Boolean> addDepreciation(@Validated @RequestBody AssetDepreciation depreciation) {
        boolean success = assetService.addDepreciation(depreciation);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.SAVE_FAILED.getCode(), AssetErrorCode.SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新折旧记录")
    @PutMapping("/depreciation")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新折旧记录")
    public ServerResponseEntity<Boolean> updateDepreciation(@Validated @RequestBody AssetDepreciation depreciation) {
        boolean success = assetService.updateDepreciation(depreciation);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.UPDATE_FAILED.getCode(), AssetErrorCode.UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除折旧记录")
    @DeleteMapping("/depreciation/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除折旧记录")
    public ServerResponseEntity<Boolean> deleteDepreciation(@Parameter(description = "折旧记录ID") @PathVariable Long[] ids) {
        boolean success = assetService.deleteDepreciation(ids);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DELETE_FAILED.getCode(), AssetErrorCode.DELETE_FAILED.getMessage());
    }

    @Operation(summary = "批量计算折旧")
    @PostMapping("/depreciation/calculate-batch")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "批量计算折旧")
    public ServerResponseEntity<Boolean> calculateDepreciationBatch(
            @Parameter(description = "折旧年月") @RequestParam String depreciationYearMonth,
            @Parameter(description = "资产ID列表") @RequestParam(required = false) List<Long> assetIds) {
        boolean success = assetService.calculateDepreciationBatch(depreciationYearMonth, assetIds);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(AssetErrorCode.DEPRECIATION_BATCH_CALCULATE_FAILED.getCode(), AssetErrorCode.DEPRECIATION_BATCH_CALCULATE_FAILED.getMessage());
    }
}