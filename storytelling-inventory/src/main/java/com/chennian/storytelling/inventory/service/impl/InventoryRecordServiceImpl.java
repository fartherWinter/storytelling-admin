package com.chennian.storytelling.inventory.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.common.util.SecurityUtils;
import com.chennian.storytelling.inventory.entity.InventoryRecord;
import com.chennian.storytelling.inventory.mapper.InventoryRecordMapper;
import com.chennian.storytelling.inventory.service.InventoryLogService;
import com.chennian.storytelling.inventory.service.InventoryRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存变动记录服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryRecordServiceImpl extends ServiceImpl<InventoryRecordMapper, InventoryRecord> implements InventoryRecordService {

    private final InventoryRecordMapper inventoryRecordMapper;
    private final InventoryLogService inventoryLogService;

    @Override
    public InventoryRecord getInventoryRecordById(Long recordId) {
        if (recordId == null) {
            throw new BusinessException("记录ID不能为空");
        }
        return inventoryRecordMapper.selectById(recordId);
    }

    @Override
    public IPage<InventoryRecord> getInventoryRecordPage(Page<InventoryRecord> page, Long inventoryId, Long productId,
                                                       Long warehouseId, String skuCode, Integer changeType,
                                                       String changeReason, String businessNo, Integer businessType,
                                                       Long operatorId, String operatorName, LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordMapper.selectInventoryRecordPage(page, inventoryId, productId, warehouseId, skuCode,
                changeType, changeReason, businessNo, businessType, operatorId, operatorName, startTime, endTime);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByInventoryId(Long inventoryId) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByInventoryId(inventoryId);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByProductId(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByProductId(productId);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByWarehouseId(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByWarehouseId(warehouseId);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByBusinessNo(String businessNo) {
        if (!StringUtils.hasText(businessNo)) {
            throw new BusinessException("业务单号不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByBusinessNo(businessNo);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByChangeType(Integer changeType, LocalDateTime startTime, LocalDateTime endTime) {
        if (changeType == null) {
            throw new BusinessException("变动类型不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByChangeType(changeType, startTime, endTime);
    }

    @Override
    public List<InventoryRecord> getInventoryRecordListByOperator(Long operatorId, LocalDateTime startTime, LocalDateTime endTime) {
        if (operatorId == null) {
            throw new BusinessException("操作人ID不能为空");
        }
        return inventoryRecordMapper.selectInventoryRecordListByOperator(operatorId, startTime, endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventoryRecord(InventoryRecord inventoryRecord) {
        if (inventoryRecord == null) {
            throw new BusinessException("库存变动记录不能为空");
        }
        
        // 校验必填字段
        validateInventoryRecord(inventoryRecord);
        
        // 设置创建信息
        inventoryRecord.setCreateTime(LocalDateTime.now());
        inventoryRecord.setCreateBy(SecurityUtils.getCurrentUserId());
        
        // 如果操作人信息为空，使用当前用户
        if (inventoryRecord.getOperatorId() == null) {
            inventoryRecord.setOperatorId(SecurityUtils.getCurrentUserId());
        }
        if (!StringUtils.hasText(inventoryRecord.getOperatorName())) {
            inventoryRecord.setOperatorName(SecurityUtils.getCurrentUsername());
        }
        if (inventoryRecord.getOperationTime() == null) {
            inventoryRecord.setOperationTime(LocalDateTime.now());
        }
        
        boolean result = inventoryRecordMapper.insert(inventoryRecord) > 0;
        
        if (result) {
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventoryRecord.getInventoryId(), 21, "创建库存变动记录",
                    null, inventoryRecord.toString(), 1, null, inventoryRecord.getBusinessNo(),
                    inventoryRecord.getBusinessType(), 0L, "创建库存变动记录");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateInventoryRecord(List<InventoryRecord> inventoryRecordList) {
        if (CollectionUtils.isEmpty(inventoryRecordList)) {
            throw new BusinessException("库存变动记录列表不能为空");
        }
        
        for (InventoryRecord inventoryRecord : inventoryRecordList) {
            createInventoryRecord(inventoryRecord);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventoryRecord(Long recordId) {
        if (recordId == null) {
            throw new BusinessException("记录ID不能为空");
        }
        
        InventoryRecord inventoryRecord = inventoryRecordMapper.selectById(recordId);
        if (inventoryRecord == null) {
            throw new BusinessException("库存变动记录不存在");
        }
        
        boolean result = inventoryRecordMapper.deleteById(recordId) > 0;
        
        if (result) {
            // 记录操作日志
            inventoryLogService.recordInventoryLog(inventoryRecord.getInventoryId(), 22, "删除库存变动记录",
                    inventoryRecord.toString(), null, 1, null, inventoryRecord.getBusinessNo(),
                    inventoryRecord.getBusinessType(), 0L, "删除库存变动记录");
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInventoryRecord(List<Long> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            throw new BusinessException("记录ID列表不能为空");
        }
        
        return inventoryRecordMapper.batchDeleteInventoryRecord(recordIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventoryRecordByTimeRange(LocalDateTime endTime) {
        if (endTime == null) {
            throw new BusinessException("结束时间不能为空");
        }
        
        return inventoryRecordMapper.deleteInventoryRecordByTimeRange(endTime) > 0;
    }

    @Override
    public Map<String, Object> getInventoryChangeStatistics(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        return inventoryRecordMapper.getInventoryChangeStatistics(inventoryId, startTime, endTime);
    }

    @Override
    public Map<String, Object> getProductChangeStatistics(Long productId, LocalDateTime startTime, LocalDateTime endTime) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        return inventoryRecordMapper.getProductChangeStatistics(productId, startTime, endTime);
    }

    @Override
    public Map<String, Object> getWarehouseChangeStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        return inventoryRecordMapper.getWarehouseChangeStatistics(warehouseId, startTime, endTime);
    }

    @Override
    public Map<String, Object> getInStockStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordMapper.getInStockStatistics(startTime, endTime);
    }

    @Override
    public Map<String, Object> getOutStockStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordMapper.getOutStockStatistics(startTime, endTime);
    }

    @Override
    public Map<String, Object> getTransferStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordMapper.getTransferStatistics(startTime, endTime);
    }

    @Override
    public Map<String, Object> getCheckStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordMapper.getCheckStatistics(startTime, endTime);
    }

    @Override
    public BigDecimal getInventoryTurnoverRate(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        BigDecimal turnoverRate = inventoryRecordMapper.getInventoryTurnoverRate(inventoryId, startTime, endTime);
        return turnoverRate != null ? turnoverRate : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getProductTurnoverRate(Long productId, LocalDateTime startTime, LocalDateTime endTime) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        BigDecimal turnoverRate = inventoryRecordMapper.getProductTurnoverRate(productId, startTime, endTime);
        return turnoverRate != null ? turnoverRate : BigDecimal.ZERO;
    }

    @Override
    public String exportInventoryRecord(Long inventoryId, Integer changeType, String businessNo,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        // 构建导出文件名
        String fileName = "inventory_record_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        // 这里应该实现具体的导出逻辑，比如使用 EasyExcel 等工具
        // 为了示例，这里只返回文件名
        log.info("导出库存变动记录，文件名：{}", fileName);
        
        // 记录操作日志
        inventoryLogService.recordInventoryLog(inventoryId, 23, "导出库存变动记录",
                null, null, 1, null, businessNo, null, 0L, "导出库存变动记录到文件：" + fileName);
        
        return fileName;
    }

    @Override
    public Map<String, Object> generateInventoryChangeReport(LocalDateTime startTime, LocalDateTime endTime, Integer reportType) {
        if (startTime == null || endTime == null) {
            throw new BusinessException("开始时间和结束时间不能为空");
        }
        if (reportType == null) {
            throw new BusinessException("报表类型不能为空");
        }
        
        Map<String, Object> report = new HashMap<>();
        report.put("reportType", reportType);
        report.put("startTime", startTime);
        report.put("endTime", endTime);
        report.put("generateTime", LocalDateTime.now());
        
        switch (reportType) {
            case 1: // 变动统计报表
                report.put("title", "库存变动统计报表");
                report.put("inStockStatistics", getInStockStatistics(startTime, endTime));
                report.put("outStockStatistics", getOutStockStatistics(startTime, endTime));
                report.put("transferStatistics", getTransferStatistics(startTime, endTime));
                report.put("checkStatistics", getCheckStatistics(startTime, endTime));
                break;
            case 2: // 周转率报表
                report.put("title", "库存周转率报表");
                report.put("turnoverStatistics", inventoryRecordMapper.getTurnoverStatistics(startTime, endTime));
                break;
            case 3: // 变动趋势报表
                report.put("title", "库存变动趋势报表");
                report.put("trendStatistics", inventoryRecordMapper.getChangeTrendStatistics(startTime, endTime));
                break;
            case 4: // 异常变动报表
                report.put("title", "库存异常变动报表");
                report.put("abnormalStatistics", inventoryRecordMapper.getAbnormalChangeStatistics(startTime, endTime));
                break;
            default:
                throw new BusinessException("不支持的报表类型：" + reportType);
        }
        
        // 记录操作日志
        inventoryLogService.recordInventoryLog(null, 24, "生成库存变动报表",
                null, report.toString(), 1, null, null, null, 0L, "生成库存变动报表，类型：" + reportType);
        
        return report;
    }

    /**
     * 校验库存变动记录
     */
    private void validateInventoryRecord(InventoryRecord inventoryRecord) {
        if (inventoryRecord.getInventoryId() == null) {
            throw new BusinessException("库存ID不能为空");
        }
        if (inventoryRecord.getProductId() == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (inventoryRecord.getWarehouseId() == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        if (!StringUtils.hasText(inventoryRecord.getSkuCode())) {
            throw new BusinessException("SKU编码不能为空");
        }
        if (inventoryRecord.getChangeType() == null) {
            throw new BusinessException("变动类型不能为空");
        }
        if (inventoryRecord.getChangeQuantity() == null) {
            throw new BusinessException("变动数量不能为空");
        }
        if (inventoryRecord.getBeforeStock() == null || inventoryRecord.getBeforeStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("变动前库存数量不能为空且不能小于0");
        }
        if (inventoryRecord.getAfterStock() == null || inventoryRecord.getAfterStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("变动后库存数量不能为空且不能小于0");
        }
        if (inventoryRecord.getCostPrice() != null && inventoryRecord.getCostPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("成本价不能小于0");
        }
    }
}