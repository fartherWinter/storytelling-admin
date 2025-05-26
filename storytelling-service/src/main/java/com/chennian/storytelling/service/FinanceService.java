package com.chennian.storytelling.service;

/**
 * 财务管理服务接口（门面模式）
 * 整合各个专门的财务服务接口，提供统一的访问入口
 * 已按照单一职责原则拆分为多个专门的服务接口：
 * - GeneralLedgerService：总账管理（会计科目、凭证管理）
 * - AccountsReceivableService：应收账款管理
 * - AccountsPayableService：应付账款管理
 * - AssetManagementService：资产管理
 * - CostAccountingService：成本会计
 * - CashManagementService：现金与资金管理
 * - BudgetService：预算与管控
 * - TaxService：税务管理
 * - FinancialReportService：财务报表
 * - FinancialAnalysisService：财务分析与决策支持
 * 
 * @author chen
 * @date 2023/6/15
 */
public interface FinanceService extends GeneralLedgerService, AccountsReceivableService, AccountsPayableService,
        AssetManagementService, CostAccountingService, CashManagementService, BudgetService,
        TaxService, FinancialReportService, FinancialAnalysisService {
    // 作为门面接口，继承所有专门服务接口的方法，无需额外定义方法
}