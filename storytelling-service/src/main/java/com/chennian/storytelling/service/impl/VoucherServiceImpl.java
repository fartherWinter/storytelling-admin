package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.Voucher;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.VoucherService;
import org.springframework.stereotype.Service;

/**
 * 凭证管理服务实现类
 * @author chen
 * @date 2023/6/15
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Override
    public IPage<Voucher> findVoucherByPage(PageParam<Voucher> page, Voucher voucher) {
        // 实现凭证分页查询逻辑
        return null;
    }

    @Override
    public Voucher selectVoucherById(Long voucherId) {
        // 实现根据ID查询凭证逻辑
        return null;
    }

    @Override
    public int insertVoucher(Voucher voucher) {
        // 实现新增凭证逻辑
        return 0;
    }

    @Override
    public int updateVoucher(Voucher voucher) {
        // 实现修改凭证逻辑
        return 0;
    }

    @Override
    public int deleteVoucherByIds(Long[] voucherIds) {
        // 实现批量删除凭证逻辑
        return 0;
    }

    @Override
    public int approveVoucher(Long voucherId, String reviewer) {
        // 实现审核凭证逻辑
        return 0;
    }

    @Override
    public int postVoucher(Long voucherId, String bookkeeper) {
        // 实现过账凭证逻辑
        return 0;
    }

    @Override
    public int invalidateVoucher(Long voucherId) {
        // 实现作废凭证逻辑
        return 0;
    }
}