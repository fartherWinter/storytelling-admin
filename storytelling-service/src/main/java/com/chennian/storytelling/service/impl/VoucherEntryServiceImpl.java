package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.bean.model.finance.VoucherEntry;
import com.chennian.storytelling.service.VoucherEntryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 凭证分录管理服务实现类
 * @author chen
 * @date 2023/6/15
 */
@Service
public class VoucherEntryServiceImpl implements VoucherEntryService {

    @Override
    public List<VoucherEntry> selectVoucherEntryByVoucherId(Long voucherId) {
        // 实现根据凭证ID查询凭证分录逻辑
        return null;
    }

    @Override
    public int insertVoucherEntry(VoucherEntry voucherEntry) {
        // 实现新增凭证分录逻辑
        return 0;
    }

    @Override
    public int updateVoucherEntry(VoucherEntry voucherEntry) {
        // 实现修改凭证分录逻辑
        return 0;
    }

    @Override
    public int deleteVoucherEntryByIds(Long[] entryIds) {
        // 实现批量删除凭证分录逻辑
        return 0;
    }
}