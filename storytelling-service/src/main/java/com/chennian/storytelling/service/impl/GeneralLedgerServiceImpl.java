package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.AccountChart;
import com.chennian.storytelling.bean.model.finance.Voucher;
import com.chennian.storytelling.bean.model.finance.VoucherEntry;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.AccountChartService;
import com.chennian.storytelling.service.GeneralLedgerService;
import com.chennian.storytelling.service.VoucherEntryService;
import com.chennian.storytelling.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 总账管理服务实现类
 * 作为门面模式的具体实现，整合会计科目管理、凭证管理和凭证分录管理功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class GeneralLedgerServiceImpl implements GeneralLedgerService {

    @Autowired
    private AccountChartService accountChartService;
    
    @Autowired
    private VoucherService voucherService;
    
    @Autowired
    private VoucherEntryService voucherEntryService;
    
    /**
     * 会计科目管理相关方法实现
     */
    @Override
    public IPage<AccountChart> findAccountChartByPage(PageParam<AccountChart> page, AccountChart accountChart) {
        return accountChartService.findAccountChartByPage(page, accountChart);
    }

    @Override
    public AccountChart selectAccountChartById(Long accountId) {
        return accountChartService.selectAccountChartById(accountId);
    }

    @Override
    public int insertAccountChart(AccountChart accountChart) {
        return accountChartService.insertAccountChart(accountChart);
    }

    @Override
    public int updateAccountChart(AccountChart accountChart) {
        return accountChartService.updateAccountChart(accountChart);
    }

    @Override
    public int deleteAccountChartByIds(Long[] accountIds) {
        return accountChartService.deleteAccountChartByIds(accountIds);
    }

    @Override
    public List<AccountChart> selectAccountChartTree() {
        return accountChartService.selectAccountChartTree();
    }

    /**
     * 凭证管理相关方法实现
     */
    @Override
    public IPage<Voucher> findVoucherByPage(PageParam<Voucher> page, Voucher voucher) {
        return voucherService.findVoucherByPage(page, voucher);
    }

    @Override
    public Voucher selectVoucherById(Long voucherId) {
        return voucherService.selectVoucherById(voucherId);
    }

    @Override
    public int insertVoucher(Voucher voucher) {
        return voucherService.insertVoucher(voucher);
    }

    @Override
    public int updateVoucher(Voucher voucher) {
        return voucherService.updateVoucher(voucher);
    }

    @Override
    public int deleteVoucherByIds(Long[] voucherIds) {
        return voucherService.deleteVoucherByIds(voucherIds);
    }

    @Override
    public int approveVoucher(Long voucherId, String reviewer) {
        return voucherService.approveVoucher(voucherId, reviewer);
    }

    @Override
    public int postVoucher(Long voucherId, String bookkeeper) {
        return voucherService.postVoucher(voucherId, bookkeeper);
    }

    @Override
    public int invalidateVoucher(Long voucherId) {
        return voucherService.invalidateVoucher(voucherId);
    }

    /**
     * 凭证分录管理相关方法实现
     */
    @Override
    public List<VoucherEntry> selectVoucherEntryByVoucherId(Long voucherId) {
        return voucherEntryService.selectVoucherEntryByVoucherId(voucherId);
    }

    @Override
    public int insertVoucherEntry(VoucherEntry voucherEntry) {
        return voucherEntryService.insertVoucherEntry(voucherEntry);
    }

    @Override
    public int updateVoucherEntry(VoucherEntry voucherEntry) {
        return voucherEntryService.updateVoucherEntry(voucherEntry);
    }

    @Override
    public int deleteVoucherEntryByIds(Long[] entryIds) {
        return voucherEntryService.deleteVoucherEntryByIds(entryIds);
    }
}