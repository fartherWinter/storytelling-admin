package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.asset.AssetCategory;
import com.chennian.storytelling.bean.model.asset.AssetDepreciation;
import com.chennian.storytelling.bean.model.asset.AssetInfo;
import com.chennian.storytelling.bean.model.asset.AssetInventory;
import com.chennian.storytelling.bean.model.asset.AssetMaintenance;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface AssetService {

    // ==================== 资产管理 ====================
    
    /**
     * 分页查询资产列表
     */
    IPage<AssetInfo> getAssetList(Page<AssetInfo> page, AssetInfo asset);
    
    /**
     * 根据ID查询资产信息
     */
    AssetInfo getAssetById(Long id);
    
    /**
     * 保存资产信息
     */
    boolean saveAsset(AssetInfo asset);
    
    /**
     * 删除资产
     */
    boolean deleteAsset(Long id);
    
    /**
     * 根据类别查询资产列表
     */
    List<AssetInfo> getAssetsByCategory(Long categoryId);
    
    /**
     * 根据部门查询资产列表
     */
    List<AssetInfo> getAssetsByDepartment(Long departmentId);
    
    /**
     * 查询即将到期的资产
     */
    List<AssetInfo> getExpiringAssets(Integer months);
    
    /**
     * 获取资产统计信息
     */
    Map<String, Object> getAssetStatistics();
    
    /**
     * 计算资产折旧
     */
    boolean calculateDepreciation();
    
    // ==================== 资产分类管理 ====================
    
    /**
     * 查询资产分类列表
     */
    List<AssetCategory> getCategoryList(AssetCategory category);
    
    /**
     * 根据ID查询资产分类信息
     */
    AssetCategory getCategoryById(Long id);
    
    /**
     * 保存资产分类
     */
    boolean saveCategory(AssetCategory category);
    
    /**
     * 删除资产分类
     */
    boolean deleteCategory(Long id);
    
    /**
     * 获取资产分类树结构
     */
    List<AssetCategory> getCategoryTree();
    
    // ==================== 资产维护管理 ====================
    
    /**
     * 分页查询资产维护记录列表
     */
    IPage<AssetMaintenance> getMaintenanceList(Page<AssetMaintenance> page, AssetMaintenance maintenance);
    
    /**
     * 根据ID查询资产维护记录信息
     */
    AssetMaintenance getMaintenanceById(Long id);
    
    /**
     * 保存资产维护记录
     */
    boolean saveMaintenance(AssetMaintenance maintenance);
    
    /**
     * 删除资产维护记录
     */
    boolean deleteMaintenance(Long id);
    
    /**
     * 根据资产ID查询维护记录
     */
    List<AssetMaintenance> getMaintenanceByAsset(Long assetId);
    
    /**
     * 查询即将到期的维护
     */
    List<AssetMaintenance> getUpcomingMaintenance(Integer days);
    
    /**
     * 获取维护统计信息
     */
    Map<String, Object> getMaintenanceStatistics(Date startDate, Date endDate);
    
    // ==================== 资产盘点管理 ====================
    
    /**
     * 分页查询资产盘点记录列表
     */
    IPage<AssetInventory> getInventoryList(Page<AssetInventory> page, AssetInventory inventory);
    
    /**
     * 根据ID查询资产盘点记录信息
     */
    AssetInventory getInventoryById(Long id);
    
    /**
     * 保存资产盘点记录
     */
    boolean saveInventory(AssetInventory inventory);
    
    /**
     * 删除资产盘点记录
     */
    boolean deleteInventory(Long id);
    
    /**
     * 根据盘点批次查询盘点记录
     */
    List<AssetInventory> getInventoryByBatch(String inventoryBatchNo);
    
    /**
     * 生成盘点批次号
     */
    String generateInventoryBatchNo();
    
    /**
     * 获取盘点统计信息
     */
    Map<String, Object> getInventoryStatistics(String inventoryBatchNo);
    
    // ==================== 资产折旧管理 ====================
    
    /**
     * 分页查询资产折旧记录列表
     */
    IPage<AssetDepreciation> getDepreciationList(AssetDepreciation depreciation, PageParam<AssetDepreciation> page);
    
    /**
     * 根据ID查询资产折旧记录信息
     */
    AssetDepreciation getDepreciationById(Long id);
    
    /**
     * 新增资产折旧记录
     */
    boolean addDepreciation(AssetDepreciation depreciation);
    
    /**
     * 修改资产折旧记录信息
     */
    boolean updateDepreciation(AssetDepreciation depreciation);
    
    /**
     * 删除资产折旧记录
     */
    boolean deleteDepreciation(Long[] ids);
    
    /**
     * 批量计算资产折旧
     */
    boolean calculateDepreciationBatch(String depreciationYearMonth, List<Long> assetIds);
}