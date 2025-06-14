package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.Bom;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.BomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * BOM管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/bom")
@Tag(name = "BOM管理")
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    /**
     * 查询BOM列表
     */
    @PostMapping("/page")
    @Operation(summary = "BOM列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "BOM列表")
    public ServerResponseEntity<IPage<Bom>> bomPage(Bom bom, PageParam<Bom> page) {
        IPage<Bom> bomPage = bomService.getBomList(page, bom);
        return ServerResponseEntity.success(bomPage);
    }

    /**
     * 获取BOM详细信息
     */
    @PostMapping("/info/{bomId}")
    @Operation(summary = "BOM详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "BOM详情")
    public ServerResponseEntity<Bom> bomInfo(@PathVariable("bomId") Long bomId) {
        Bom bom = bomService.getBomById(bomId);
        return ServerResponseEntity.success(bom);
    }

    /**
     * 新增BOM
     */
    @PostMapping("/add")
    @Operation(summary = "新增BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增BOM")
    public ServerResponseEntity<Void> addBom(@Validated @RequestBody Bom bom) {
        bomService.addBom(bom);
        return ServerResponseEntity.success();
    }

    /**
     * 修改BOM
     */
    @PostMapping("/edit")
    @Operation(summary = "修改BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改BOM")
    public ServerResponseEntity<Void> editBom(@Validated @RequestBody Bom bom) {
        bomService.updateBom(bom);
        return ServerResponseEntity.success();
    }

    /**
     * 删除BOM
     */
    @PostMapping("/remove/{bomId}")
    @Operation(summary = "删除BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除BOM")
    public ServerResponseEntity<Void> removeBom(@PathVariable Long bomId) {
        bomService.deleteBom(bomId);
        return ServerResponseEntity.success();
    }

    /**
     * 审核BOM
     */
    @PostMapping("/approve")
    @Operation(summary = "审核BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "审核BOM")
    public ServerResponseEntity<Void> approveBom(@RequestParam Long bomId, @RequestParam String status) {
        bomService.auditBom(bomId, status);
        return ServerResponseEntity.success();
    }

    /**
     * 激活BOM
     */
    @PostMapping("/activate")
    @Operation(summary = "激活BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "激活BOM")
    public ServerResponseEntity<Void> activateBom(@RequestParam Long bomId) {
        bomService.auditBom(bomId, "ACTIVE");
        return ServerResponseEntity.success();
    }

    /**
     * 停用BOM
     */
    @PostMapping("/deactivate")
    @Operation(summary = "停用BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "停用BOM")
    public ServerResponseEntity<Void> deactivateBom(@RequestParam Long bomId) {
        bomService.auditBom(bomId, "INACTIVE");
        return ServerResponseEntity.success();
    }

    /**
     * 复制BOM
     */
    @PostMapping("/copy")
    @Operation(summary = "复制BOM")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "复制BOM")
    public ServerResponseEntity<Void> copyBom(@RequestParam Long bomId, @RequestParam String version) {
        bomService.createBomVersion(bomId, version);
        return ServerResponseEntity.success();
    }
}