package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.Voucher;
import com.chennian.storytelling.common.utils.PageParam;

/**
 * 凭证管理服务接口
 * @author chen
 * @date 2023/6/15
 */
public interface VoucherService {
    
    /**
     * 分页查询凭证
     * @param page 分页参数
     * @param voucher 查询条件
     * @return 凭证分页数据
     */
    IPage<Voucher> findVoucherByPage(PageParam<Voucher> page, Voucher voucher);
    
    /**
     * 根据ID查询凭证
     * @param voucherId 凭证ID
     * @return 凭证信息
     */
    Voucher selectVoucherById(Long voucherId);
    
    /**
     * 新增凭证
     * @param voucher 凭证信息
     * @return 结果
     */
    int insertVoucher(Voucher voucher);
    
    /**
     * 修改凭证
     * @param voucher 凭证信息
     * @return 结果
     */
    int updateVoucher(Voucher voucher);
    
    /**
     * 批量删除凭证
     * @param voucherIds 需要删除的凭证ID数组
     * @return 结果
     */
    int deleteVoucherByIds(Long[] voucherIds);
    
    /**
     * 审核凭证
     * @param voucherId 凭证ID
     * @param reviewer 审核人
     * @return 结果
     */
    int approveVoucher(Long voucherId, String reviewer);
    
    /**
     * 过账凭证
     * @param voucherId 凭证ID
     * @param bookkeeper 记账人
     * @return 结果
     */
    int postVoucher(Long voucherId, String bookkeeper);
    
    /**
     * 作废凭证
     * @param voucherId 凭证ID
     * @return 结果
     */
    int invalidateVoucher(Long voucherId);
}