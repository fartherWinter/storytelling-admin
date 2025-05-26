package com.chennian.storytelling.admin.controller;


import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.FinancialTransaction;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SysFinancialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/finance")
@Tag(name = "财务管理")
public class FinancialController {

    private final SysFinancialService financialService;
    public FinancialController(SysFinancialService financialService) {
        this.financialService = financialService;
    }


    /**
     * 查询财务交易记录列表
     */
    @PostMapping("/transaction/page")
    @Operation(summary = "交易记录列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "交易记录列表")
    public ServerResponseEntity<IPage<FinancialTransaction>> page(FinancialTransaction transaction, PageParam<FinancialTransaction> page) {
        IPage<FinancialTransaction> transactionPage = financialService.findByPage(page, transaction);
        return ServerResponseEntity.success(transactionPage);
    }

    /**
     * 获取交易记录详细信息
     */
    @PostMapping("/transaction/info/{transactionId}")
    @Operation(summary = "交易记录详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "交易记录详情")
    public ServerResponseEntity<FinancialTransaction> info(@PathVariable("transactionId") Long transactionId) {
        FinancialTransaction transaction = financialService.selectTransactionById(transactionId);
        return ServerResponseEntity.success(transaction);
    }

    /**
     * 新增交易记录
     */
    @PostMapping("/transaction/add")
    @Operation(summary = "新增交易记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增交易记录")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody FinancialTransaction transaction) {
        return ServerResponseEntity.success(financialService.insertTransaction(transaction));
    }

    /**
     * 修改交易记录
     */
    @PostMapping("/transaction/update")
    @Operation(summary = "修改交易记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改交易记录")
    public ServerResponseEntity<Integer> update(@RequestBody FinancialTransaction transaction) {
        // 此处需要实现财务交易服务
         return ServerResponseEntity.success(financialService.updateTransaction(transaction));
//        return ServerResponseEntity.success(1);
    }

    /**
     * 删除交易记录
     */
    @PostMapping("/transaction/remove/{transactionIds}")
    @Operation(summary = "删除交易记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除交易记录")
    public ServerResponseEntity<Integer> remove(@PathVariable("transactionIds") Long[] transactionIds) {
        // 此处需要实现财务交易服务
         return ServerResponseEntity.success(financialService.deleteTransactionByIds(transactionIds));
//        return ServerResponseEntity.success(1);
    }

    /**
     * 财务报表 - 收入支出统计
     */
    @PostMapping("/report/income-expense")
    @Operation(summary = "收入支出统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "收入支出统计")
    public ServerResponseEntity<Map<String, Object>> incomeExpenseReport(@RequestParam(required = false) String dateRange) {
        Map<String, Object> reportData = financialService.getIncomeExpenseReport(dateRange);
        return ServerResponseEntity.success(reportData);
    }

    /**
     * 财务报表 - 利润分析
     */
    @PostMapping("/report/profit")
    @Operation(summary = "利润分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "利润分析")
    public ServerResponseEntity<Map<String, Object>> profitReport(@RequestParam(required = false) String dateRange) {
        Map<String, Object> reportData = financialService.getProfitReport(dateRange);
        return ServerResponseEntity.success(reportData);
    }

    /**
     * 财务报表 - 现金流量表
     */
    @PostMapping("/report/cash-flow")
    @Operation(summary = "现金流量表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "现金流量表")
    public ServerResponseEntity<Map<String, Object>> cashFlowReport(@RequestParam(required = false) String dateRange) {
        Map<String, Object> reportData = financialService.getCashFlowReport(dateRange);
        return ServerResponseEntity.success(reportData);
    }

    /**
     * 应收账款管理
     */
    @PostMapping("/receivable/list")
    @Operation(summary = "应收账款列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "应收账款列表")
    public ServerResponseEntity<List<Map<String, Object>>> receivableList() {
        List<Map<String, Object>> receivables = financialService.getReceivableList();
        return ServerResponseEntity.success(receivables);
    }

    /**
     * 应付账款管理
     */
    @PostMapping("/payable/list")
    @Operation(summary = "应付账款列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "应付账款列表")
    public ServerResponseEntity<List<Map<String, Object>>> payableList() {
        List<Map<String, Object>> payables = financialService.getPayableList();
        return ServerResponseEntity.success(payables);
    }
}