package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.manufacturing.Equipment;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.manufacturing.EquipmentMapper;
import com.chennian.storytelling.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 设备管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public IPage<Equipment> getEquipmentList(PageParam<Equipment> pageParam, Equipment equipment) {
        Page<Equipment> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        
        if (equipment != null) {
            if (StringUtils.hasText(equipment.getEquipmentCode())) {
                queryWrapper.like("equipment_code", equipment.getEquipmentCode());
            }
            if (StringUtils.hasText(equipment.getEquipmentName())) {
                queryWrapper.like("equipment_name", equipment.getEquipmentName());
            }
            if (equipment.getEquipmentType() != null) {
                queryWrapper.eq("equipment_type", equipment.getEquipmentType());
            }
            if (equipment.getEquipmentStatus() != null) {
                queryWrapper.eq("equipment_status", equipment.getEquipmentStatus());
            }
            if (equipment.getLineId() != null) {
                queryWrapper.eq("production_line_id", equipment.getLineId());
            }
        }
        
        queryWrapper.orderByDesc("create_time");
        return equipmentMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Equipment getEquipmentById(Long id) {
        return equipmentMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addEquipment(Equipment equipment) {
        equipment.setCreateTime(DateTime.now());
        // 初始状态：停机
        equipment.setEquipmentStatus(0);
        equipmentMapper.insert(equipment);
    }

    @Override
    @Transactional
    public void updateEquipment(Equipment equipment) {
        equipment.setUpdateTime(DateTime.now());
        equipmentMapper.updateById(equipment);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long[] ids) {
        equipmentMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void changeEquipmentStatus(Long equipmentId, Integer status, String reason) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setEquipmentStatus(status);
        equipment.setUpdateTime(DateTime.now());
        equipmentMapper.updateById(equipment);
        log.info("设备状态变更，设备ID：{}，新状态：{}，变更原因：{}", equipmentId, status, reason);
    }

    @Override
    @Transactional
    public void deleteEquipment(Long id) {
        equipmentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void changeEquipmentStatus(Long id, String status) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(id);
        // 根据状态字符串转换为对应的状态码
        Integer statusCode = convertStatusToCode(status);
        equipment.setEquipmentStatus(statusCode);
        equipment.setUpdateTime(DateTime.now());
        equipmentMapper.updateById(equipment);
        log.info("设备状态变更，设备ID：{}，新状态：{}", id, status);
    }

    @Override
    @Transactional
    public void recordEquipmentMaintenance(Long equipmentId, String maintenanceType, String description, Double cost) {
        // 记录设备维护信息
        log.info("设备维护记录，设备ID：{}，维护类型：{}，描述：{}，费用：{}", equipmentId, maintenanceType, description, cost);
        
        // 更新设备状态为维护中
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentId);
        equipment.setEquipmentStatus(2); // 维护中
        equipment.setUpdateTime(DateTime.now());
        equipmentMapper.updateById(equipment);
    }

    @Override
    public Map<String, Object> getEquipmentEfficiencyAnalysis(Long equipmentId, String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取设备基本信息
            Equipment equipment = equipmentMapper.selectById(equipmentId);
            if (equipment == null) {
                throw new RuntimeException("设备不存在");
            }
            
            // 模拟设备效率分析数据
            result.put("equipmentId", equipmentId);
            result.put("equipmentName", equipment.getEquipmentName());
            result.put("analysisStartDate", startDate);
            result.put("analysisEndDate", endDate);
            
            // OEE分析（Overall Equipment Effectiveness）
            Map<String, Object> oeeAnalysis = new HashMap<>();
            oeeAnalysis.put("availability", 92.5); // 可用性
            oeeAnalysis.put("performance", 88.3);  // 性能效率
            oeeAnalysis.put("quality", 95.2);      // 质量率
            oeeAnalysis.put("oee", 77.8);          // 综合设备效率
            result.put("oeeAnalysis", oeeAnalysis);
            
            // 运行时间分析
            Map<String, Object> runtimeAnalysis = new HashMap<>();
            runtimeAnalysis.put("totalTime", 168.0);     // 总时间（小时）
            runtimeAnalysis.put("runningTime", 155.4);   // 运行时间
            runtimeAnalysis.put("downTime", 12.6);       // 停机时间
            runtimeAnalysis.put("maintenanceTime", 8.2); // 维护时间
            result.put("runtimeAnalysis", runtimeAnalysis);
            
            // 故障分析
            List<Map<String, Object>> faultAnalysis = new ArrayList<>();
            Map<String, Object> fault1 = new HashMap<>();
            fault1.put("faultType", "机械故障");
            fault1.put("frequency", 2);
            fault1.put("totalDownTime", 4.5);
            faultAnalysis.add(fault1);
            result.put("faultAnalysis", faultAnalysis);
            
            // 效率趋势
            List<Map<String, Object>> efficiencyTrend = new ArrayList<>();
            for (int i = 1; i <= 7; i++) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", LocalDateTime.now().minusDays(7-i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dayData.put("efficiency", 75 + new Random().nextInt(20));
                dayData.put("output", 80 + new Random().nextInt(30));
                efficiencyTrend.add(dayData);
            }
            result.put("efficiencyTrend", efficiencyTrend);
            
            log.info("设备效率分析完成，设备ID：{}", equipmentId);
            
        } catch (Exception e) {
            log.error("设备效率分析失败，设备ID：{}", equipmentId, e);
            result.put("error", "分析失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional
    public void recordEquipmentMaintenance(Long id, String description) {
        // 记录设备维护信息（简化版）
        log.info("设备维护记录，设备ID：{}，描述：{}", id, description);
        
        // 更新设备状态为维护中
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(id);
        // 维护中
        equipment.setEquipmentStatus(2);
        equipment.setUpdateTime(DateTime.now());
        equipmentMapper.updateById(equipment);
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