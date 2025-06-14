package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.manufacturing.ProductionLine;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.manufacturing.ProductionLineMapper;
import com.chennian.storytelling.service.ProductionLineService;
import com.chennian.storytelling.common.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 生产线管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class ProductionLineServiceImpl implements ProductionLineService {

    @Autowired
    private ProductionLineMapper productionLineMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    // 缓存过期时间（分钟）
    private static final int CACHE_EXPIRE_MINUTES = 30;
    
    // 缓存键前缀
    private static final String CAPACITY_ANALYSIS_CACHE_PREFIX = "production_line:capacity:";

    @Override
    public IPage<ProductionLine> getProductionLineList(PageParam<ProductionLine> pageParam, ProductionLine productionLine) {
        Page<ProductionLine> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<ProductionLine> queryWrapper = new QueryWrapper<>();
        
        if (productionLine != null) {
            if (StringUtils.hasText(productionLine.getLineCode())) {
                queryWrapper.like("line_code", productionLine.getLineCode());
            }
            if (StringUtils.hasText(productionLine.getLineName())) {
                queryWrapper.like("line_name", productionLine.getLineName());
            }
            if (productionLine.getWorkshopId() != null) {
                queryWrapper.eq("workshop_id", productionLine.getWorkshopId());
            }
            if (productionLine.getLineStatus() != null) {
                queryWrapper.eq("line_status", productionLine.getLineStatus());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return productionLineMapper.selectPage(page, queryWrapper);
    }

    @Override
    public ProductionLine getProductionLineById(Long id) {
        return productionLineMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProductionLine(ProductionLine productionLine) {
        productionLine.setCreateTime(DateTime.now());
        // 初始状态：停机
        productionLine.setLineStatus(0);
        productionLineMapper.insert(productionLine);
    }

    @Override
    @Transactional
    public void updateProductionLine(ProductionLine productionLine) {
        productionLine.setUpdateTime(DateTime.now());
        productionLineMapper.updateById(productionLine);
    }

    @Override
    @Transactional
    public void deleteProductionLine(Long[] ids) {
        productionLineMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void changeProductionLineStatus(Long lineId, Integer status, String reason) {
        ProductionLine line = new ProductionLine();
        line.setLineId(lineId);
        line.setLineStatus(status);
        line.setUpdateTime(DateTime.now());
        productionLineMapper.updateById(line);
        log.info("生产线状态变更，生产线ID：{}，新状态：{}，变更原因：{}", lineId, status, reason);
    }

    @Override
    @Transactional
    public void deleteProductionLine(Long id) {
        productionLineMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void changeProductionLineStatus(Long id, String status) {
        ProductionLine line = new ProductionLine();
        line.setLineId(id);
        // 根据状态字符串转换为对应的状态码
        Integer statusCode = convertStatusToCode(status);
        line.setLineStatus(statusCode);
        line.setUpdateTime(DateTime.now());
        productionLineMapper.updateById(line);
        log.info("生产线状态变更，生产线ID：{}，新状态：{}", id, status);
    }

    @Override
    public Map<String, Object> getProductionLineCapacityAnalysis(Long lineId, String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建缓存键
            String cacheKey = CAPACITY_ANALYSIS_CACHE_PREFIX + lineId + ":" + startDate + ":" + endDate;
            
            // 先从Redis缓存获取
            Map<String, Object> cachedResult = redisCache.getCacheObject(cacheKey);
            if (cachedResult != null) {
                log.info("从Redis缓存获取生产线产能分析数据，缓存键：{}", cacheKey);
                return cachedResult;
            }
            
            // 获取生产线基本信息
            ProductionLine line = productionLineMapper.selectById(lineId);
            if (line == null) {
                throw new RuntimeException("生产线不存在");
            }
            
            result.put("lineId", lineId);
            result.put("lineName", line.getLineName());
            result.put("analysisStartDate", startDate);
            result.put("analysisEndDate", endDate);
            
            // 从数据库获取产能利用率数据
            Map<String, Object> capacityData = productionLineMapper.selectCapacityUtilizationData(startDate, endDate, lineId);
            
            if (capacityData != null && !capacityData.isEmpty()) {
                // 使用数据库中的真实数据
                Map<String, Object> capacityUtilization = new HashMap<>();
                capacityUtilization.put("plannedCapacity", capacityData.getOrDefault("plannedCapacity", 1000));
                capacityUtilization.put("actualOutput", capacityData.getOrDefault("actualOutput", 850));
                capacityUtilization.put("utilizationRate", capacityData.getOrDefault("utilizationRate", 85.0));
                capacityUtilization.put("efficiency", capacityData.getOrDefault("efficiency", 92.3));
                result.put("capacityUtilization", capacityUtilization);
                
                // 时间段分析数据
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> timeAnalysis = (List<Map<String, Object>>) capacityData.get("timeAnalysis");
                if (timeAnalysis != null && !timeAnalysis.isEmpty()) {
                    result.put("timeAnalysis", timeAnalysis);
                } else {
                    // 如果没有时间段数据，生成默认数据
                    result.put("timeAnalysis", generateDefaultTimeAnalysis());
                }
                
                // 瓶颈分析数据
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> bottlenecks = (List<Map<String, Object>>) capacityData.get("bottlenecks");
                if (bottlenecks != null && !bottlenecks.isEmpty()) {
                    result.put("bottlenecks", bottlenecks);
                } else {
                    // 如果没有瓶颈数据，生成默认数据
                    result.put("bottlenecks", generateDefaultBottlenecks());
                }
            } else {
                // 如果数据库中没有数据，使用默认值
                Map<String, Object> capacityUtilization = new HashMap<>();
                capacityUtilization.put("plannedCapacity", 1000);
                capacityUtilization.put("actualOutput", 850);
                capacityUtilization.put("utilizationRate", 85.0);
                capacityUtilization.put("efficiency", 92.3);
                result.put("capacityUtilization", capacityUtilization);
                
                result.put("timeAnalysis", generateDefaultTimeAnalysis());
                result.put("bottlenecks", generateDefaultBottlenecks());
            }
            
            // 将结果缓存到Redis
            redisCache.setCacheObject(cacheKey, result, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            log.info("生产线产能分析完成，生产线ID：{}", lineId);
            
        } catch (Exception e) {
            log.error("生产线产能分析失败，生产线ID：{}", lineId, e);
            result.put("error", "分析失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 生成默认时间段分析数据
     */
    private List<Map<String, Object>> generateDefaultTimeAnalysis() {
        List<Map<String, Object>> timeAnalysis = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", LocalDateTime.now().minusDays(7-i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dayData.put("output", 120 + new Random().nextInt(50));
            dayData.put("efficiency", 85 + new Random().nextInt(15));
            timeAnalysis.add(dayData);
        }
        return timeAnalysis;
    }
    
    /**
     * 生成默认瓶颈分析数据
     */
    private List<Map<String, Object>> generateDefaultBottlenecks() {
        List<Map<String, Object>> bottlenecks = new ArrayList<>();
        Map<String, Object> bottleneck1 = new HashMap<>();
        bottleneck1.put("process", "装配工序");
        bottleneck1.put("impact", "中等");
        bottleneck1.put("suggestion", "增加操作员或优化工艺");
        bottlenecks.add(bottleneck1);
        return bottlenecks;
    }
    
    /**
     * 状态字符串转换为状态码
     */
    private Integer convertStatusToCode(String status) {
        switch (status.toLowerCase()) {
            case "running":
            case "运行中":
                return 1;
            case "maintenance":
            case "维护中":
                return 2;
            case "fault":
            case "故障":
                return 3;
            case "stopped":
            case "停机":
            default:
                return 0;
        }
    }
}