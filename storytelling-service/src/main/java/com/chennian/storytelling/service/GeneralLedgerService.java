package com.chennian.storytelling.service;

/**
 * 总账管理服务接口
 * 作为门面接口，整合会计科目管理、凭证管理和凭证分录管理功能
 * @author chen
 * @date 2023/6/15
 */
public interface GeneralLedgerService extends AccountChartService, VoucherService, VoucherEntryService {
    // 作为门面接口，继承了AccountChartService、VoucherService和VoucherEntryService的所有方法
}