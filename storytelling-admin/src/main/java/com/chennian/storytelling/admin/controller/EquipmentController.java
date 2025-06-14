package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.Equipment;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 设备管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/equipment")
@Tag(name = "设备管理")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * 查询设备列表
     */
    @PostMapping("/page")
    @Operation(summary = "设备列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "设备列表")
    public ServerResponseEntity<IPage<Equipment>> equipmentPage(Equipment equipment, PageParam<Equipment> page) {
        IPage<Equipment> equipmentPage = equipmentService.getEquipmentList(page, equipment);
        return ServerResponseEntity.success(equipmentPage);
    }

    /**
     * 获取设备详细信息
     */
    @PostMapping("/info/{equipmentId}")
    @Operation(summary = "设备详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "设备详情")
    public ServerResponseEntity<Equipment> equipmentInfo(@PathVariable("equipmentId") Long equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        return ServerResponseEntity.success(equipment);
    }

    /**
     * 新增设备
     */
    @PostMapping("/add")
    @Operation(summary = "新增设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增设备")
    public ServerResponseEntity<Void> addEquipment(@Validated @RequestBody Equipment equipment) {
        equipmentService.addEquipment(equipment);
        return ServerResponseEntity.success();
    }

    /**
     * 修改设备
     */
    @PostMapping("/edit")
    @Operation(summary = "修改设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改设备")
    public ServerResponseEntity<Void> editEquipment(@Validated @RequestBody Equipment equipment) {
        equipmentService.updateEquipment(equipment);
        return ServerResponseEntity.success();
    }

    /**
     * 删除设备
     */
    @PostMapping("/remove/{equipmentId}")
    @Operation(summary = "删除设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除设备")
    public ServerResponseEntity<Void> removeEquipment(@PathVariable Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ServerResponseEntity.success();
    }

    /**
     * 启动设备
     */
    @PostMapping("/start")
    @Operation(summary = "启动设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "启动设备")
    public ServerResponseEntity<Void> startEquipment(@RequestParam Long equipmentId) {
        equipmentService.changeEquipmentStatus(equipmentId, "RUNNING");
        return ServerResponseEntity.success();
    }

    /**
     * 停止设备
     */
    @PostMapping("/stop")
    @Operation(summary = "停止设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "停止设备")
    public ServerResponseEntity<Void> stopEquipment(@RequestParam Long equipmentId) {
        equipmentService.changeEquipmentStatus(equipmentId, "STOPPED");
        return ServerResponseEntity.success();
    }

    /**
     * 维护设备
     */
    @PostMapping("/maintain")
    @Operation(summary = "维护设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "维护设备")
    public ServerResponseEntity<Void> maintainEquipment(@RequestParam Long equipmentId, @RequestParam String description) {
        equipmentService.recordEquipmentMaintenance(equipmentId, description);
        return ServerResponseEntity.success();
    }

    /**
     * 校准设备
     */
    @PostMapping("/calibrate")
    @Operation(summary = "校准设备")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "校准设备")
    public ServerResponseEntity<Void> calibrateEquipment(@RequestParam Long equipmentId) {
        equipmentService.changeEquipmentStatus(equipmentId, "CALIBRATING");
        return ServerResponseEntity.success();
    }
}