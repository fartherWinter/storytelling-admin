package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.finance.VoucherEntry;

import java.util.List;

/**
 * 凭证分录管理服务接口
 * @author chen
 * @date 2023/6/15
 */
public interface VoucherEntryService {
    
    /**
     * 根据凭证ID查询凭证分录
     * @param voucherId 凭证ID
     * @return 凭证分录列表
     */
    List<VoucherEntry> selectVoucherEntryByVoucherId(Long voucherId);
    
    /**
     * 新增凭证分录
     * @param voucherEntry 凭证分录信息
     * @return 结果
     */
    int insertVoucherEntry(VoucherEntry voucherEntry);
    
    /**
     * 修改凭证分录
     * @param voucherEntry 凭证分录信息
     * @return 结果
     */
    int updateVoucherEntry(VoucherEntry voucherEntry);
    
    /**
     * 批量删除凭证分录
     * @param entryIds 需要删除的凭证分录ID数组
     * @return 结果
     */
    int deleteVoucherEntryByIds(Long[] entryIds);
}