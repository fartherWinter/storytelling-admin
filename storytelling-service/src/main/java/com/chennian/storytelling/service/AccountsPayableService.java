package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.PayablesAgingVO;
import com.chennian.storytelling.bean.vo.PayablesSummaryVO;
import com.chennian.storytelling.bean.vo.SupplierPaymentInfoVO;
import java.util.List;
import java.util.Map;

/**
 * 应付账款管理服务接口
 * 负责应付账款相关功能
 * @author chen
 * @date 2023/6/15
 */
public interface AccountsPayableService {
    
    /**
     * 获取应付账款汇总信息
     * @return 应付账款汇总数据
     */
    PayablesSummaryVO getPayablesSummary();
    
    /**
     * 获取应付账款账龄分析
     * @return 应付账款账龄数据
     */
    List<PayablesAgingVO> getPayablesAging();
    
    /**
     * 获取供应商付款信息
     * @param supplierId 供应商ID
     * @return 供应商付款信息
     */
    SupplierPaymentInfoVO getSupplierPaymentInfo(Long supplierId);
    
    /**
     * 记录应付账款付款
     * @param supplierId 供应商ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 结果
     */
    int recordPayablePayment(Long supplierId, String invoiceNo, Double amount);
}