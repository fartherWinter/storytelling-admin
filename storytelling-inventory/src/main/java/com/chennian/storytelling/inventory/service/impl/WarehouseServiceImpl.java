package com.chennian.storytelling.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.common.util.SecurityUtils;
import com.chennian.storytelling.inventory.entity.Warehouse;
import com.chennian.storytelling.inventory.mapper.WarehouseMapper;
import com.chennian.storytelling.inventory.service.InventoryLogService;
import com.chennian.storytelling.inventory.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final InventoryLogService inventoryLogService;

    @Override
    public Warehouse getWarehouseById(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        return warehouseMapper.selectById(warehouseId);
    }

    @Override
    public Warehouse getWarehouseByCode(String warehouseCode) {
        if (!StringUtils.hasText(warehouseCode)) {
            throw new BusinessException("仓库编码不能为空");
        }
        return warehouseMapper.selectWarehouseByCode(warehouseCode);
    }

    @Override
    public IPage<Warehouse> getWarehousePage(Page<Warehouse> page, String warehouseCode, String warehouseName,
                                           Integer warehouseType, String region, Integer status,
                                           Boolean isDefault, Boolean supportAutoAllocation) {
        return warehouseMapper.selectWarehousePage(page, warehouseCode, warehouseName, warehouseType,
                region, status, isDefault, supportAutoAllocation);
    }

    @Override
    public List<Warehouse> getAllAvailableWarehouses() {
        return warehouseMapper.selectAllAvailableWarehouses();
    }

    @Override
    public List<Warehouse> getWarehouseListByType(Integer warehouseType) {
        if (warehouseType == null) {
            throw new BusinessException("仓库类型不能为空");
        }
        return warehouseMapper.selectWarehouseListByType(warehouseType);
    }

    @Override
    public List<Warehouse> getWarehouseListByRegion(String region) {
        if (!StringUtils.hasText(region)) {
            throw new BusinessException("所属区域不能为空");
        }
        return warehouseMapper.selectWarehouseListByRegion(region);
    }

    @Override
    public Warehouse getDefaultWarehouse() {
        return warehouseMapper.selectDefaultWarehouse();
    }

    @Override
    public List<Warehouse> getAutoAllocationWarehouses() {
        return warehouseMapper.selectAutoAllocationWarehouses();
    }

    @Override
    public List<Warehouse> getChildWarehouses(Long parentWarehouseId) {
        if (parentWarehouseId == null) {
            throw new BusinessException("父仓库ID不能为空");
        }
        return warehouseMapper.selectChildWarehouses(parentWarehouseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new BusinessException("仓库信息不能为空");
        }
        
        // 校验必填字段
        validateWarehouse(warehouse);
        
        // 检查仓库编码是否已存在
        if (checkWarehouseCodeExists(warehouse.getWarehouseCode())) {
            throw new BusinessException("仓库编码已存在");
        }
        
        // 检查仓库名称是否已存在
        if (checkWarehouseNameExists(warehouse.getWarehouseName())) {
            throw new BusinessException("仓库名称已存在");
        }
        
        // 设置创建信息
        warehouse.setCreateTime(LocalDateTime.now());
        warehouse.setCreateBy(SecurityUtils.getCurrentUserId());
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(SecurityUtils.getCurrentUserId());
        warehouse.setDeleted(false);
        
        // 设置排序值
        if (warehouse.getSortOrder() == null) {
            Integer maxSortOrder = warehouseMapper.getMaxSortOrder();
            warehouse.setSortOrder(maxSortOrder != null ? maxSortOrder + 1 : 1);
        }
        
        // 生成仓库路径
        if (warehouse.getParentId() != null) {
            String parentPath = warehouseMapper.getWarehousePath(warehouse.getParentId());
            warehouse.setWarehousePath(parentPath + "," + warehouse.getId());
        } else {
            warehouse.setWarehousePath(String.valueOf(warehouse.getId()));
        }
        
        boolean result = warehouseMapper.insert(warehouse) > 0;
        
        if (result) {
            // 更新仓库路径
            updateWarehousePath(warehouse);
            
            // 如果设置为默认仓库，需要清除其他默认仓库
            if (warehouse.getIsDefault() != null && warehouse.getIsDefault()) {
                clearOtherDefaultWarehouses(warehouse.getId());
            }
            
            // 记录操作日志
            inventoryLogService.recordInventoryLog(null, 11, "创建仓库",
                    null, warehouse.toString(), 1, null, null, null, 0L, "创建仓库：" + warehouse.getWarehouseName());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWarehouse(Warehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new BusinessException("仓库信息或ID不能为空");
        }
        
        // 获取原仓库信息
        Warehouse oldWarehouse = warehouseMapper.selectById(warehouse.getId());
        if (oldWarehouse == null) {
            throw new BusinessException("仓库记录不存在");
        }
        
        // 校验必填字段
        validateWarehouse(warehouse);
        
        // 检查仓库编码是否重复（排除自己）
        if (!oldWarehouse.getWarehouseCode().equals(warehouse.getWarehouseCode()) && 
            checkWarehouseCodeExists(warehouse.getWarehouseCode())) {
            throw new BusinessException("仓库编码已存在");
        }
        
        // 检查仓库名称是否重复（排除自己）
        if (!oldWarehouse.getWarehouseName().equals(warehouse.getWarehouseName()) && 
            checkWarehouseNameExists(warehouse.getWarehouseName())) {
            throw new BusinessException("仓库名称已存在");
        }
        
        // 设置更新信息
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = warehouseMapper.updateById(warehouse) > 0;
        
        if (result) {
            // 如果设置为默认仓库，需要清除其他默认仓库
            if (warehouse.getIsDefault() != null && warehouse.getIsDefault() && 
                (oldWarehouse.getIsDefault() == null || !oldWarehouse.getIsDefault())) {
                clearOtherDefaultWarehouses(warehouse.getId());
            }
            
            // 如果父仓库发生变化，需要更新仓库路径
            if (!Objects.equals(oldWarehouse.getParentId(), warehouse.getParentId())) {
                updateWarehousePath(warehouse);
                updateChildWarehousePaths(warehouse.getId());
            }
            
            // 记录操作日志
            inventoryLogService.recordInventoryLog(null, 12, "更新仓库",
                    oldWarehouse.toString(), warehouse.toString(), 1, null, null, null, 0L, "更新仓库：" + warehouse.getWarehouseName());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null) {
            throw new BusinessException("仓库记录不存在");
        }
        
        // 检查是否有库存
        if (checkWarehouseHasInventory(warehouseId)) {
            throw new BusinessException("仓库中还有库存，不能删除");
        }
        
        // 检查是否有子仓库
        if (checkHasChildWarehouses(warehouseId)) {
            throw new BusinessException("仓库下还有子仓库，不能删除");
        }
        
        // 逻辑删除
        warehouse.setDeleted(true);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = warehouseMapper.updateById(warehouse) > 0;
        
        if (result) {
            // 记录操作日志
            inventoryLogService.recordInventoryLog(null, 13, "删除仓库",
                    warehouse.toString(), null, 1, null, null, null, 0L, "删除仓库：" + warehouse.getWarehouseName());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteWarehouse(List<Long> warehouseIds) {
        if (CollectionUtils.isEmpty(warehouseIds)) {
            throw new BusinessException("仓库ID列表不能为空");
        }
        
        for (Long warehouseId : warehouseIds) {
            deleteWarehouse(warehouseId);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableWarehouse(Long warehouseId) {
        return updateWarehouseStatus(warehouseId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableWarehouse(Long warehouseId) {
        return updateWarehouseStatus(warehouseId, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateWarehouseStatus(List<Long> warehouseIds, Integer status) {
        if (CollectionUtils.isEmpty(warehouseIds) || status == null) {
            throw new BusinessException("仓库ID列表和状态不能为空");
        }
        
        return warehouseMapper.batchUpdateWarehouseStatus(warehouseIds, status, SecurityUtils.getCurrentUserId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        
        // 清除其他默认仓库
        clearOtherDefaultWarehouses(warehouseId);
        
        // 设置当前仓库为默认
        return warehouseMapper.setDefaultWarehouse(warehouseId, SecurityUtils.getCurrentUserId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearDefaultWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        
        return warehouseMapper.clearDefaultWarehouse(warehouseId, SecurityUtils.getCurrentUserId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWarehouseSortOrder(Long warehouseId, Integer sortOrder) {
        if (warehouseId == null || sortOrder == null) {
            throw new BusinessException("仓库ID和排序值不能为空");
        }
        
        return warehouseMapper.updateWarehouseSortOrder(warehouseId, sortOrder, SecurityUtils.getCurrentUserId()) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveWarehouse(Long warehouseId, Long newParentId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null) {
            throw new BusinessException("仓库记录不存在");
        }
        
        // 检查是否移动到自己的子仓库下
        if (newParentId != null && isChildWarehouse(warehouseId, newParentId)) {
            throw new BusinessException("不能移动到自己的子仓库下");
        }
        
        warehouse.setParentId(newParentId);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = warehouseMapper.updateById(warehouse) > 0;
        
        if (result) {
            // 更新仓库路径
            updateWarehousePath(warehouse);
            updateChildWarehousePaths(warehouseId);
        }
        
        return result;
    }

    @Override
    public boolean checkWarehouseCodeExists(String warehouseCode) {
        if (!StringUtils.hasText(warehouseCode)) {
            return false;
        }
        return warehouseMapper.checkWarehouseCodeExists(warehouseCode) > 0;
    }

    @Override
    public boolean checkWarehouseNameExists(String warehouseName) {
        if (!StringUtils.hasText(warehouseName)) {
            return false;
        }
        return warehouseMapper.checkWarehouseNameExists(warehouseName) > 0;
    }

    @Override
    public boolean checkWarehouseHasInventory(Long warehouseId) {
        if (warehouseId == null) {
            return false;
        }
        return warehouseMapper.checkWarehouseHasInventory(warehouseId) > 0;
    }

    @Override
    public boolean checkHasChildWarehouses(Long warehouseId) {
        if (warehouseId == null) {
            return false;
        }
        return warehouseMapper.checkHasChildWarehouses(warehouseId) > 0;
    }

    @Override
    public Integer getWarehouseInventoryCount(Long warehouseId) {
        if (warehouseId == null) {
            return 0;
        }
        Integer count = warehouseMapper.getWarehouseInventoryCount(warehouseId);
        return count != null ? count : 0;
    }

    @Override
    public BigDecimal getWarehouseTotalStock(Long warehouseId) {
        if (warehouseId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalStock = warehouseMapper.getWarehouseTotalStock(warehouseId);
        return totalStock != null ? totalStock : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getWarehouseCapacityUsage(Long warehouseId) {
        if (warehouseId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal usage = warehouseMapper.getWarehouseCapacityUsage(warehouseId);
        return usage != null ? usage : BigDecimal.ZERO;
    }

    @Override
    public String getWarehousePath(Long warehouseId) {
        if (warehouseId == null) {
            return "";
        }
        String path = warehouseMapper.getWarehousePath(warehouseId);
        return path != null ? path : "";
    }

    @Override
    public List<Warehouse> buildWarehouseTree(List<Warehouse> warehouseList) {
        if (CollectionUtils.isEmpty(warehouseList)) {
            return new ArrayList<>();
        }
        
        // 构建仓库映射
        Map<Long, Warehouse> warehouseMap = warehouseList.stream()
                .collect(Collectors.toMap(Warehouse::getId, warehouse -> warehouse));
        
        List<Warehouse> rootWarehouses = new ArrayList<>();
        
        for (Warehouse warehouse : warehouseList) {
            if (warehouse.getParentId() == null) {
                // 根仓库
                rootWarehouses.add(warehouse);
            } else {
                // 子仓库
                Warehouse parent = warehouseMap.get(warehouse.getParentId());
                if (parent != null) {
                    if (parent.getChildWarehouses() == null) {
                        parent.setChildWarehouses(new ArrayList<>());
                    }
                    parent.getChildWarehouses().add(warehouse);
                }
            }
        }
        
        return rootWarehouses;
    }

    @Override
    public String generateWarehousePath(Long warehouseId, Long parentId) {
        if (warehouseId == null) {
            return "";
        }
        
        if (parentId == null) {
            return String.valueOf(warehouseId);
        }
        
        String parentPath = getWarehousePath(parentId);
        return parentPath + "," + warehouseId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChildWarehousePaths(Long parentWarehouseId) {
        if (parentWarehouseId == null) {
            return false;
        }
        
        List<Warehouse> childWarehouses = getChildWarehouses(parentWarehouseId);
        if (CollectionUtils.isEmpty(childWarehouses)) {
            return true;
        }
        
        for (Warehouse childWarehouse : childWarehouses) {
            updateWarehousePath(childWarehouse);
            updateChildWarehousePaths(childWarehouse.getId());
        }
        
        return true;
    }

    @Override
    public Map<String, Object> getWarehouseStatistics() {
        return warehouseMapper.getWarehouseStatistics();
    }

    @Override
    public Warehouse autoAllocateWarehouse(String region, Integer warehouseType, BigDecimal requiredCapacity) {
        List<Warehouse> availableWarehouses = warehouseMapper.selectAutoAllocationWarehouses();
        
        if (CollectionUtils.isEmpty(availableWarehouses)) {
            return null;
        }
        
        // 过滤条件
        List<Warehouse> filteredWarehouses = availableWarehouses.stream()
                .filter(warehouse -> {
                    // 区域匹配
                    if (StringUtils.hasText(region) && !region.equals(warehouse.getRegion())) {
                        return false;
                    }
                    // 类型匹配
                    if (warehouseType != null && !warehouseType.equals(warehouse.getWarehouseType())) {
                        return false;
                    }
                    // 容量检查
                    if (requiredCapacity != null && !checkWarehouseCapacitySufficient(warehouse.getId(), requiredCapacity)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(filteredWarehouses)) {
            return null;
        }
        
        // 按容量使用率排序，选择使用率最低的仓库
        return filteredWarehouses.stream()
                .min(Comparator.comparing(warehouse -> getWarehouseCapacityUsage(warehouse.getId())))
                .orElse(null);
    }

    @Override
    public Map<String, Object> getWarehouseCapacityInfo(Long warehouseId) {
        if (warehouseId == null) {
            return new HashMap<>();
        }
        
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> capacityInfo = new HashMap<>();
        capacityInfo.put("warehouseId", warehouseId);
        capacityInfo.put("warehouseName", warehouse.getWarehouseName());
        capacityInfo.put("totalCapacity", warehouse.getCapacity());
        
        BigDecimal usedCapacity = getWarehouseCapacityUsage(warehouseId);
        capacityInfo.put("usedCapacity", usedCapacity);
        
        BigDecimal availableCapacity = warehouse.getCapacity().subtract(usedCapacity);
        capacityInfo.put("availableCapacity", availableCapacity);
        
        BigDecimal usageRate = BigDecimal.ZERO;
        if (warehouse.getCapacity().compareTo(BigDecimal.ZERO) > 0) {
            usageRate = usedCapacity.divide(warehouse.getCapacity(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        capacityInfo.put("usageRate", usageRate);
        
        return capacityInfo;
    }

    @Override
    public boolean checkWarehouseCapacitySufficient(Long warehouseId, BigDecimal requiredCapacity) {
        if (warehouseId == null || requiredCapacity == null) {
            return false;
        }
        
        Map<String, Object> capacityInfo = getWarehouseCapacityInfo(warehouseId);
        BigDecimal availableCapacity = (BigDecimal) capacityInfo.get("availableCapacity");
        
        return availableCapacity != null && availableCapacity.compareTo(requiredCapacity) >= 0;
    }

    /**
     * 校验仓库信息
     */
    private void validateWarehouse(Warehouse warehouse) {
        if (!StringUtils.hasText(warehouse.getWarehouseCode())) {
            throw new BusinessException("仓库编码不能为空");
        }
        if (!StringUtils.hasText(warehouse.getWarehouseName())) {
            throw new BusinessException("仓库名称不能为空");
        }
        if (warehouse.getWarehouseType() == null) {
            throw new BusinessException("仓库类型不能为空");
        }
        if (warehouse.getArea() != null && warehouse.getArea().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("仓库面积不能小于0");
        }
        if (warehouse.getCapacity() != null && warehouse.getCapacity().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("仓库容量不能小于0");
        }
    }

    /**
     * 更新仓库状态
     */
    private boolean updateWarehouseStatus(Long warehouseId, Integer status) {
        if (warehouseId == null || status == null) {
            throw new BusinessException("仓库ID和状态不能为空");
        }
        
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null) {
            throw new BusinessException("仓库记录不存在");
        }
        
        warehouse.setStatus(status);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setUpdateBy(SecurityUtils.getCurrentUserId());
        
        boolean result = warehouseMapper.updateById(warehouse) > 0;
        
        if (result) {
            String operation = status == 1 ? "启用仓库" : "禁用仓库";
            // 记录操作日志
            inventoryLogService.recordInventoryLog(null, 14, operation,
                    String.valueOf(warehouse.getStatus() == 1 ? 0 : 1), String.valueOf(status),
                    1, null, null, null, 0L, operation + "：" + warehouse.getWarehouseName());
        }
        
        return result;
    }

    /**
     * 清除其他默认仓库
     */
    private void clearOtherDefaultWarehouses(Long excludeWarehouseId) {
        warehouseMapper.clearOtherDefaultWarehouses(excludeWarehouseId, SecurityUtils.getCurrentUserId());
    }

    /**
     * 更新仓库路径
     */
    private void updateWarehousePath(Warehouse warehouse) {
        String newPath = generateWarehousePath(warehouse.getId(), warehouse.getParentId());
        warehouse.setWarehousePath(newPath);
        warehouseMapper.updateWarehousePath(warehouse.getId(), newPath, SecurityUtils.getCurrentUserId());
    }

    /**
     * 检查是否为子仓库
     */
    private boolean isChildWarehouse(Long parentWarehouseId, Long childWarehouseId) {
        if (parentWarehouseId == null || childWarehouseId == null) {
            return false;
        }
        
        String childPath = getWarehousePath(childWarehouseId);
        return StringUtils.hasText(childPath) && childPath.contains(String.valueOf(parentWarehouseId));
    }
}