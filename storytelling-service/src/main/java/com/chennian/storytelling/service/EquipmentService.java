package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.Equipment;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 设备管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface EquipmentService {

    /**
     * 查询设备列表
     */
    IPage<Equipment> getEquipmentList(PageParam<Equipment> page, Equipment equipment);
    
    /**
     * 根据ID查询设备信息
     */
    Equipment getEquipmentById(Long id);
    
    /**
     * 新增设备
     */
    void addEquipment(Equipment equipment);
    
    /**
     * 修改设备信息
     */
    void updateEquipment(Equipment equipment);
    
    /**
     * 删除设备
     */
    void deleteEquipment(Long[] ids);
    
    /**
     * 设备状态变更
     */
    void changeEquipmentStatus(Long equipmentId, Integer status, String reason);

    /**
     * 删除设备（单个）
     */
    @Transactional
    void deleteEquipment(Long id);

    /**
     * 设备状态变更（简化版）
     */
    @Transactional
    void changeEquipmentStatus(Long id, String status);

    /**
     * 设备维护记录
     */
    void recordEquipmentMaintenance(Long equipmentId, String maintenanceType, String description, Double cost);
    
    /**
     * 设备效率分析
     */
    Map<String, Object> getEquipmentEfficiencyAnalysis(Long equipmentId, String startDate, String endDate);

    /**
     * 设备维护记录（简化版）
     */
    @Transactional
    void recordEquipmentMaintenance(Long id, String description);
}