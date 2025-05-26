package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.FinancialTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 财务交易记录表 数据访问层
 * 
 * @author chennian
 * @date 2023/5/20
 */
@Mapper
public interface FinancialTransactionMapper extends BaseMapper<FinancialTransaction> {
    /**
     * 根据交易类型和日期范围统计交易金额
     * 
     * @param transactionType 交易类型
     * @param direction 交易方向
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易金额总和
     */
    default BigDecimal sumAmountByTypeAndDateRange(String transactionType, String direction, Date startDate, Date endDate) {
        // 默认实现使用BaseMapper的selectList方法，可以根据需要在XML中重写此方法以提高性能
        LambdaQueryWrapper<FinancialTransaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(transactionType), FinancialTransaction::getTransactionType, transactionType)
                  .eq(StringUtils.hasText(direction), FinancialTransaction::getDirection, direction)
                  .eq(FinancialTransaction::getStatus, "1")
                  .between(startDate != null && endDate != null, FinancialTransaction::getTransactionDate, startDate, endDate);
        
        List<FinancialTransaction> list = this.selectList(queryWrapper);
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return list.stream()
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}