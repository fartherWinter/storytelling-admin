package com.chennian.storytelling.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.asset.AssetCategory;
import com.chennian.storytelling.bean.model.asset.AssetDepreciation;
import com.chennian.storytelling.bean.model.asset.AssetInfo;
import com.chennian.storytelling.bean.model.asset.AssetInventory;
import com.chennian.storytelling.bean.model.asset.AssetMaintenance;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.asset.*;
import com.chennian.storytelling.service.AssetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetInfoMapper assetInfoMapper;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private AssetMaintenanceMapper assetMaintenanceMapper;

    @Autowired
    private AssetInventoryMapper assetInventoryMapper;

    @Autowired
    private AssetDepreciationMapper assetDepreciationMapper;

    // ==================== 资产信息管理 ====================

    @Override
    public IPage<AssetInfo> getAssetList(Page<AssetInfo> page, AssetInfo asset) {
        LambdaQueryWrapper<AssetInfo> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(asset.getAssetName())) {
            wrapper.like(AssetInfo::getAssetName, asset.getAssetName());
        }
        if (StringUtils.hasText(asset.getAssetNo())) {
            wrapper.like(AssetInfo::getAssetNo, asset.getAssetNo());
        }
        if (asset.getCategoryId() != null) {
            wrapper.eq(AssetInfo::getCategoryId, asset.getCategoryId());
        }
        if (asset.getDepartmentId() != null) {
            wrapper.eq(AssetInfo::getDepartmentId, asset.getDepartmentId());
        }
        if (asset.getStatus() != null) {
            wrapper.eq(AssetInfo::getStatus, asset.getStatus());
        }
        
        wrapper.orderByDesc(AssetInfo::getCreateTime);
        
        IPage<AssetInfo> result = assetInfoMapper.selectPage(page, wrapper);
        
        // 填充类别名称
        result.getRecords().forEach(assetInfo -> {
            if (assetInfo.getCategoryId() != null) {
                AssetCategory category = assetCategoryMapper.selectById(assetInfo.getCategoryId());
                if (category != null) {
                    assetInfo.setCategoryName(category.getCategoryName());
                }
            }
        });
        
        return result;
    }

    @Override
    public AssetInfo getAssetById(Long id) {
        AssetInfo asset = assetInfoMapper.selectById(id);
        if (asset != null && asset.getCategoryId() != null) {
            AssetCategory category = assetCategoryMapper.selectById(asset.getCategoryId());
            if (category != null) {
                asset.setCategoryName(category.getCategoryName());
            }
        }
        return asset;
    }

    @Override
    @Transactional
    public boolean saveAsset(AssetInfo asset) {
        // 检查资产编号是否重复
        if (StringUtils.hasText(asset.getAssetNo())) {
            AssetInfo existing = assetInfoMapper.selectByAssetNo(asset.getAssetNo());
            if (existing != null && !existing.getId().equals(asset.getId())) {
                throw new RuntimeException("资产编号已存在");
            }
        }
        
        // 计算当前价值（购买价格 - 累计折旧）
        if (asset.getPurchasePrice() != null && asset.getAccumulatedDepreciation() != null) {
            asset.setCurrentValue(asset.getPurchasePrice().subtract(asset.getAccumulatedDepreciation()));
        }
        
        if (asset.getId() == null) {
            asset.setCreateTime(new Date());
            return assetInfoMapper.insert(asset) > 0;
        } else {
            asset.setUpdateTime(new Date());
            return assetInfoMapper.updateById(asset) > 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteAsset(Long id) {
        return assetInfoMapper.deleteById(id) > 0;
    }

    @Override
    public List<AssetInfo> getAssetsByCategory(Long categoryId) {
        return assetInfoMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<AssetInfo> getAssetsByDepartment(Long departmentId) {
        return assetInfoMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<AssetInfo> getExpiringAssets(Integer months) {
        return assetInfoMapper.selectExpiringAssets(months);
    }

    @Override
    public Map<String, Object> getAssetStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总资产数
        Long totalCount = assetInfoMapper.selectCount(null);
        result.put("totalCount", totalCount);
        
        // 正常使用资产数
        Long normalCount = assetInfoMapper.selectCount(new LambdaQueryWrapper<AssetInfo>().eq(AssetInfo::getStatus, 1));
        result.put("normalCount", normalCount);
        
        // 闲置资产数
        Long idleCount = assetInfoMapper.selectCount(new LambdaQueryWrapper<AssetInfo>().eq(AssetInfo::getStatus, 2));
        result.put("idleCount", idleCount);
        
        // 维修中资产数
        Long repairCount = assetInfoMapper.selectCount(new LambdaQueryWrapper<AssetInfo>().eq(AssetInfo::getStatus, 3));
        result.put("repairCount", repairCount);
        
        // 按类别统计
        List<Map<String, Object>> categoryStats = assetInfoMapper.selectAssetStatistics();
        result.put("categoryStatistics", categoryStats);
        
        // 价值统计
        List<Map<String, Object>> valueStats = assetInfoMapper.selectAssetValueStatistics();
        result.put("valueStatistics", valueStats);
        
        return result;
    }

    @Override
    @Transactional
    public boolean calculateDepreciation() {
        List<AssetInfo> assets = assetInfoMapper.selectAssetsForDepreciation();
        
        for (AssetInfo asset : assets) {
            // 计算折旧（这里使用直线法示例）
            if (asset.getDepreciationMethod() == 1 && asset.getUsefulLife() != null && asset.getUsefulLife() > 0) {
                BigDecimal depreciableAmount = asset.getPurchasePrice();
                if (asset.getResidualRate() != null) {
                    BigDecimal residualValue = asset.getPurchasePrice().multiply(asset.getResidualRate().divide(new BigDecimal(100)));
                    depreciableAmount = asset.getPurchasePrice().subtract(residualValue);
                }
                
                BigDecimal monthlyDepreciation = depreciableAmount.divide(new BigDecimal(asset.getUsefulLife() * 12), 2, BigDecimal.ROUND_HALF_UP);
                
                // 计算已使用月数
                Calendar purchaseCalendar = Calendar.getInstance();
                purchaseCalendar.setTime(asset.getPurchaseDate());
                Calendar currentCalendar = Calendar.getInstance();
                
                int usedMonths = (currentCalendar.get(Calendar.YEAR) - purchaseCalendar.get(Calendar.YEAR)) * 12 +
                               (currentCalendar.get(Calendar.MONTH) - purchaseCalendar.get(Calendar.MONTH));
                
                BigDecimal totalDepreciation = monthlyDepreciation.multiply(new BigDecimal(usedMonths));
                
                // 不能超过可折旧金额
                if (totalDepreciation.compareTo(depreciableAmount) > 0) {
                    totalDepreciation = depreciableAmount;
                }
                
                asset.setAccumulatedDepreciation(totalDepreciation);
                asset.setCurrentValue(asset.getPurchasePrice().subtract(totalDepreciation));
            }
        }
        
        return assetInfoMapper.batchUpdateDepreciation(assets) > 0;
    }

    // ==================== 资产类别管理 ====================

    @Override
    public List<AssetCategory> getCategoryList(AssetCategory category) {
        LambdaQueryWrapper<AssetCategory> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(category.getCategoryName())) {
            wrapper.like(AssetCategory::getCategoryName, category.getCategoryName());
        }
        if (StringUtils.hasText(category.getCategoryCode())) {
            wrapper.like(AssetCategory::getCategoryCode, category.getCategoryCode());
        }
        if (category.getParentId() != null) {
            wrapper.eq(AssetCategory::getParentId, category.getParentId());
        }
        if (category.getStatus() != null) {
            wrapper.eq(AssetCategory::getStatus, category.getStatus());
        }
        
        wrapper.orderByAsc(AssetCategory::getOrderNum);
        
        List<AssetCategory> list = assetCategoryMapper.selectList(wrapper);
        
        // 填充上级类别名称
        list.forEach(cat -> {
            if (cat.getParentId() != null && cat.getParentId() != 0) {
                AssetCategory parent = assetCategoryMapper.selectById(cat.getParentId());
                if (parent != null) {
                    cat.setParentName(parent.getCategoryName());
                }
            }
        });
        
        return list;
    }

    @Override
    public AssetCategory getCategoryById(Long id) {
        AssetCategory category = assetCategoryMapper.selectById(id);
        if (category != null && category.getParentId() != null && category.getParentId() != 0) {
            AssetCategory parent = assetCategoryMapper.selectById(category.getParentId());
            if (parent != null) {
                category.setParentName(parent.getCategoryName());
            }
        }
        return category;
    }

    @Override
    @Transactional
    public boolean saveCategory(AssetCategory category) {
        // 检查类别编码是否重复
        if (StringUtils.hasText(category.getCategoryCode())) {
            AssetCategory existing = assetCategoryMapper.selectByCategoryCode(category.getCategoryCode());
            if (existing != null && !existing.getId().equals(category.getId())) {
                throw new RuntimeException("类别编码已存在");
            }
        }
        
        if (category.getId() == null) {
            category.setCreateTime(new Date());
            return assetCategoryMapper.insert(category) > 0;
        } else {
            category.setUpdateTime(new Date());
            return assetCategoryMapper.updateById(category) > 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        // 检查是否有子类别
        List<AssetCategory> children = assetCategoryMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子类别，无法删除");
        }
        
        // 检查是否有资产使用该类别
        List<AssetInfo> assets = assetInfoMapper.selectByCategoryId(id);
        if (!assets.isEmpty()) {
            throw new RuntimeException("类别下存在资产，无法删除");
        }
        
        return assetCategoryMapper.deleteById(id) > 0;
    }

    @Override
    public List<AssetCategory> getCategoryTree() {
        return assetCategoryMapper.selectCategoryTree();
    }

    // ==================== 资产维护管理 ====================

    @Override
    public IPage<AssetMaintenance> getMaintenanceList(Page<AssetMaintenance> page, AssetMaintenance maintenance) {
        LambdaQueryWrapper<AssetMaintenance> wrapper = new LambdaQueryWrapper<>();
        
        if (maintenance.getAssetId() != null) {
            wrapper.eq(AssetMaintenance::getAssetId, maintenance.getAssetId());
        }
        if (StringUtils.hasText(maintenance.getAssetName())) {
            wrapper.like(AssetMaintenance::getAssetName, maintenance.getAssetName());
        }
        if (maintenance.getMaintenanceType() != null) {
            wrapper.eq(AssetMaintenance::getMaintenanceType, maintenance.getMaintenanceType());
        }
        if (maintenance.getStatus() != null) {
            wrapper.eq(AssetMaintenance::getStatus, maintenance.getStatus());
        }
        
        wrapper.orderByDesc(AssetMaintenance::getMaintenanceDate);
        
        return assetMaintenanceMapper.selectPage(page, wrapper);
    }

    @Override
    public AssetMaintenance getMaintenanceById(Long id) {
        return assetMaintenanceMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean saveMaintenance(AssetMaintenance maintenance) {
        if (maintenance.getId() == null) {
            maintenance.setCreateTime(new Date());
            return assetMaintenanceMapper.insert(maintenance) > 0;
        } else {
            maintenance.setUpdateTime(new Date());
            return assetMaintenanceMapper.updateById(maintenance) > 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteMaintenance(Long id) {
        return assetMaintenanceMapper.deleteById(id) > 0;
    }

    @Override
    public List<AssetMaintenance> getMaintenanceByAsset(Long assetId) {
        return assetMaintenanceMapper.selectByAssetId(assetId);
    }

    @Override
    public List<AssetMaintenance> getUpcomingMaintenance(Integer days) {
        return assetMaintenanceMapper.selectUpcomingMaintenance(days);
    }

    @Override
    public Map<String, Object> getMaintenanceStatistics(Date startDate, Date endDate) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> stats = assetMaintenanceMapper.selectMaintenanceStatistics(startDate, endDate);
        result.put("statistics", stats);
        
        List<Map<String, Object>> costStats = assetMaintenanceMapper.selectMaintenanceCostStatistics(startDate, endDate);
        result.put("costStatistics", costStats);
        
        return result;
    }

    // ==================== 资产盘点管理 ====================

    @Override
    public IPage<AssetInventory> getInventoryList(Page<AssetInventory> page, AssetInventory inventory) {
        LambdaQueryWrapper<AssetInventory> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(inventory.getInventoryBatchNo())) {
            wrapper.eq(AssetInventory::getInventoryBatchNo, inventory.getInventoryBatchNo());
        }
        if (inventory.getAssetId() != null) {
            wrapper.eq(AssetInventory::getAssetId, inventory.getAssetId());
        }
        if (StringUtils.hasText(inventory.getAssetName())) {
            wrapper.like(AssetInventory::getAssetName, inventory.getAssetName());
        }
        if (inventory.getInventoryResult() != null) {
            wrapper.eq(AssetInventory::getInventoryResult, inventory.getInventoryResult());
        }
        if (inventory.getStatus() != null) {
            wrapper.eq(AssetInventory::getStatus, inventory.getStatus());
        }
        
        wrapper.orderByDesc(AssetInventory::getInventoryDate);
        
        return assetInventoryMapper.selectPage(page, wrapper);
    }

    @Override
    public AssetInventory getInventoryById(Long id) {
        return assetInventoryMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean saveInventory(AssetInventory inventory) {
        // 计算盈亏数量和价值
        if (inventory.getBookQuantity() != null && inventory.getActualQuantity() != null) {
            inventory.setDifferenceQuantity(inventory.getActualQuantity() - inventory.getBookQuantity());
        }
        
        if (inventory.getBookValue() != null && inventory.getActualValue() != null) {
            inventory.setDifferenceValue(inventory.getActualValue().subtract(inventory.getBookValue()));
        }
        
        // 设置盘点结果
        if (inventory.getDifferenceQuantity() != null) {
            if (inventory.getDifferenceQuantity() > 0) {
                inventory.setInventoryResult(1); // 盘盈
            } else if (inventory.getDifferenceQuantity() < 0) {
                inventory.setInventoryResult(2); // 盘亏
            } else {
                inventory.setInventoryResult(3); // 账实相符
            }
        }
        
        if (inventory.getId() == null) {
            inventory.setCreateTime(new Date());
            return assetInventoryMapper.insert(inventory) > 0;
        } else {
            inventory.setUpdateTime(new Date());
            return assetInventoryMapper.updateById(inventory) > 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteInventory(Long id) {
        return assetInventoryMapper.deleteById(id) > 0;
    }

    @Override
    public List<AssetInventory> getInventoryByBatch(String inventoryBatchNo) {
        return assetInventoryMapper.selectByInventoryBatchNo(inventoryBatchNo);
    }

    @Override
    public String generateInventoryBatchNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
        String latestBatchNo = assetInventoryMapper.selectLatestInventoryBatchNo();
        
        if (latestBatchNo != null && latestBatchNo.startsWith("INV" + dateStr)) {
            // 提取序号并加1
            String seqStr = latestBatchNo.substring(11);
            int seq = Integer.parseInt(seqStr) + 1;
            return "INV" + dateStr + String.format("%03d", seq);
        } else {
            return "INV" + dateStr + "001";
        }
    }

    @Override
    public Map<String, Object> getInventoryStatistics(String inventoryBatchNo) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> stats = assetInventoryMapper.selectInventoryStatistics(inventoryBatchNo);
        result.put("statistics", stats);
        
        return result;
    }

    // ==================== 资产折旧管理 ====================

    @Override
    public IPage<AssetDepreciation> getDepreciationList(AssetDepreciation depreciation, PageParam<AssetDepreciation> page) {
        return assetDepreciationMapper.selectDepreciationPage(page, depreciation);

    }

    @Override
    public AssetDepreciation getDepreciationById(Long id) {
        return assetDepreciationMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean addDepreciation(AssetDepreciation depreciation) {
        try {
            // 参数校验
            if (depreciation == null || depreciation.getAssetId() == null || 
                !StringUtils.hasText(depreciation.getDepreciationMonth())) {
                throw new RuntimeException("参数不能为空");
            }
            
            // 检查是否已存在相同资产和月份的折旧记录
            AssetDepreciation existing = assetDepreciationMapper.selectByAssetIdAndMonth(
                depreciation.getAssetId(), depreciation.getDepreciationMonth());
            if (existing != null) {
                throw new RuntimeException("该资产在此月份已存在折旧记录");
            }
            
            depreciation.setCreateTime(new Date());
            int result = assetDepreciationMapper.insert(depreciation);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("新增失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateDepreciation(AssetDepreciation depreciation) {
        try {
            // 参数校验
            if (depreciation == null || depreciation.getId() == null) {
                throw new RuntimeException("参数不能为空");
            }
            
            depreciation.setUpdateTime(new Date());
            int result = assetDepreciationMapper.updateById(depreciation);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("修改失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteDepreciation(Long[] ids) {
        try {
            // 参数校验
            if (ids == null || ids.length == 0) {
                throw new RuntimeException("请选择要删除的记录");
            }
            
            int result = assetDepreciationMapper.deleteByIds(Arrays.asList(ids));
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean calculateDepreciationBatch(String depreciationMonth, List<Long> assetIds) {
        try {
            // 参数校验
            if (!StringUtils.hasText(depreciationMonth) || assetIds == null || assetIds.isEmpty()) {
                throw new RuntimeException("参数不能为空");
            }
            
            List<AssetDepreciation> depreciationList = new ArrayList<>();
            int skipCount = 0;
            
            // 获取需要计算折旧的资产信息
            for (Long assetId : assetIds) {
                if (assetId == null) {
                    skipCount++;
                    continue;
                }
                
                AssetInfo asset = assetInfoMapper.selectById(assetId);
                if (asset == null) {
                    skipCount++;
                    continue;
                }
                
                // 检查是否已存在折旧记录
                AssetDepreciation existing = assetDepreciationMapper.selectByAssetIdAndMonth(assetId, depreciationMonth);
                if (existing != null) {
                    skipCount++;
                    continue;
                }
                
                // 计算折旧
                AssetDepreciation depreciation = calculateDepreciation(asset, depreciationMonth);
                if (depreciation != null) {
                    depreciationList.add(depreciation);
                } else {
                    skipCount++;
                }
            }
            
            if (!depreciationList.isEmpty()) {
                int result = assetDepreciationMapper.batchInsert(depreciationList);
                return result > 0;
            } else {
                // 没有需要计算的资产也算成功
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("批量计算折旧失败：" + e.getMessage());
        }
    }

    /**
     * 计算单个资产的折旧
     */
    private AssetDepreciation calculateDepreciation(AssetInfo asset, String depreciationMonth) {
        // 参数校验
        if (asset == null || asset.getId() == null || asset.getPurchasePrice() == null ||
            asset.getUsefulLife() == null || !StringUtils.hasText(depreciationMonth)) {
            return null;
        }
        
        // 校验原值和使用年限必须大于0
        if (asset.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0 || asset.getUsefulLife() <= 0) {
            return null;
        }
        
        AssetDepreciation depreciation = new AssetDepreciation();
        depreciation.setAssetId(asset.getId());
        depreciation.setAssetCode(asset.getAssetNo());
        depreciation.setAssetName(asset.getAssetName());
        depreciation.setDepreciationMonth(depreciationMonth);
        depreciation.setOriginalValue(asset.getPurchasePrice());
        depreciation.setUsefulLife(asset.getUsefulLife());
        
        // 获取最新的累计折旧
        AssetDepreciation latest = assetDepreciationMapper.selectLatestByAssetId(asset.getId());
        BigDecimal accumulatedDepreciation = latest != null && latest.getAccumulatedDepreciation() != null 
            ? latest.getAccumulatedDepreciation() : BigDecimal.ZERO;
        
        // 计算残值（默认残值率5%）
        BigDecimal residualRate = asset.getResidualRate() != null ? asset.getResidualRate() : new BigDecimal("0.05");
        // 确保残值率在合理范围内（0-1之间）
        if (residualRate.compareTo(BigDecimal.ZERO) < 0 || residualRate.compareTo(BigDecimal.ONE) > 0) {
            residualRate = new BigDecimal("0.05");
        }
        BigDecimal residualValue = asset.getPurchasePrice().multiply(residualRate);
        
        // 计算月折旧额（直线法）
        BigDecimal depreciableAmount = asset.getPurchasePrice().subtract(residualValue);
        // 使用年限转换为月数
        BigDecimal usefulMonths = new BigDecimal(asset.getUsefulLife()).multiply(new BigDecimal("12"));
        BigDecimal monthlyDepreciation = depreciableAmount.divide(usefulMonths, 2, java.math.RoundingMode.HALF_UP);
        
        // 更新累计折旧
        BigDecimal newAccumulatedDepreciation = accumulatedDepreciation.add(monthlyDepreciation);
        
        // 确保累计折旧不超过可折旧金额
        if (newAccumulatedDepreciation.compareTo(depreciableAmount) > 0) {
            monthlyDepreciation = depreciableAmount.subtract(accumulatedDepreciation);
            newAccumulatedDepreciation = depreciableAmount;
        }
        
        // 如果已经完全折旧，则不再计算
        if (monthlyDepreciation.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        
        // 计算净值
        BigDecimal netValue = asset.getPurchasePrice().subtract(newAccumulatedDepreciation);
        // 确保净值不小于残值
        if (netValue.compareTo(residualValue) < 0) {
            netValue = residualValue;
        }
        
        depreciation.setDepreciationMethod(1); // 直线法
        depreciation.setCurrentDepreciation(monthlyDepreciation);
        depreciation.setAccumulatedDepreciation(newAccumulatedDepreciation);
        depreciation.setNetValue(netValue);
        depreciation.setResidualRate(residualRate);
        depreciation.setResidualValue(residualValue);
        
        // 计算折旧率（避免除零错误）
        BigDecimal depreciationRate = BigDecimal.ZERO;
        if (asset.getPurchasePrice().compareTo(BigDecimal.ZERO) > 0) {
            depreciationRate = monthlyDepreciation.divide(asset.getPurchasePrice(), 4, java.math.RoundingMode.HALF_UP);
        }
        depreciation.setDepreciationRate(depreciationRate);
        
        // 判断折旧状态 2-已完成，1-折旧中
        int status = newAccumulatedDepreciation.compareTo(depreciableAmount) >= 0 ? 2 : 1;
        depreciation.setStatus(status);
        depreciation.setCreateTime(new Date());
        
        return depreciation;
    }
}