package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Supplier;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/supplier")
@Tag(name = "供应商管理")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 查询供应商列表
     */
    @PostMapping("/page")
    @Operation(summary = "供应商列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "供应商列表")
    public ServerResponseEntity<IPage<Supplier>> page(Supplier supplier, PageParam<Supplier> page) {
        IPage<Supplier> supplierPage = supplierService.findByPage(page, supplier);
        return ServerResponseEntity.success(supplierPage);
    }

    /**
     * 获取供应商详细信息
     */
    @PostMapping("/info/{supplierId}")
    @Operation(summary = "供应商详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "供应商详情")
    public ServerResponseEntity<Supplier> info(@PathVariable("supplierId") Long supplierId) {
        Supplier supplier = supplierService.selectSupplierById(supplierId);
        return ServerResponseEntity.success(supplier);
    }

    /**
     * 新增供应商
     */
    @PostMapping("/add")
    @Operation(summary = "新增供应商")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增供应商")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody Supplier supplier) {
        return ServerResponseEntity.success(supplierService.insertSupplier(supplier));
    }

    /**
     * 修改供应商
     */
    @PostMapping("/update")
    @Operation(summary = "修改供应商")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改供应商")
    public ServerResponseEntity<Integer> update(@RequestBody Supplier supplier) {
        return ServerResponseEntity.success(supplierService.updateSupplier(supplier));
    }

    /**
     * 删除供应商
     */
    @PostMapping("/remove/{supplierIds}")
    @Operation(summary = "删除供应商")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除供应商")
    public ServerResponseEntity<Integer> remove(@PathVariable("supplierIds") Long[] supplierIds) {
        return ServerResponseEntity.success(supplierService.deleteSupplierByIds(supplierIds));
    }

    /**
     * 供应商状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "供应商状态修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "供应商状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody Supplier supplier) {
        return ServerResponseEntity.success(supplierService.updateSupplierStatus(supplier));
    }

    /**
     * 供应商评估记录
     */
    @PostMapping("/evaluation/{supplierId}")
    @Operation(summary = "供应商评估记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "供应商评估记录")
    public ServerResponseEntity<List<Map<String, Object>>> evaluationRecords(@PathVariable("supplierId") Long supplierId) {
        List<Map<String, Object>> records = supplierService.getEvaluationRecords(supplierId);
        return ServerResponseEntity.success(records);
    }

    /**
     * 添加供应商评估记录
     */
    @PostMapping("/evaluation/add")
    @Operation(summary = "添加评估记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "添加评估记录")
    public ServerResponseEntity<Integer> addEvaluation(@RequestBody Map<String, Object> evaluation) {
        return ServerResponseEntity.success(supplierService.addEvaluationRecord(evaluation));
    }

    /**
     * 供应商合作历史
     */
    @PostMapping("/history/{supplierId}")
    @Operation(summary = "供应商合作历史")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "供应商合作历史")
    public ServerResponseEntity<List<Map<String, Object>>> cooperationHistory(@PathVariable("supplierId") Long supplierId) {
        List<Map<String, Object>> history = supplierService.getCooperationHistory(supplierId);
        return ServerResponseEntity.success(history);
    }
}