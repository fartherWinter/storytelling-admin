package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.FinancialTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT COALESCE(SUM(amount), 0) FROM financial_transaction " +
            "WHERE status = '1' " +
            "AND (#{transactionType} IS NULL OR transaction_type = #{transactionType}) " +
            "AND (#{direction} IS NULL OR direction = #{direction}) " +
            "AND (#{startDate} IS NULL OR transaction_date >= #{startDate}) " +
            "AND (#{endDate} IS NULL OR transaction_date <= #{endDate})")
    BigDecimal sumAmountByTypeAndDateRange(@Param("transactionType") String transactionType,
                                         @Param("direction") String direction,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);

    /**
     * 批量插入交易记录
     * 
     * @param transactions 交易记录列表
     * @return 插入成功的记录数
     */
    int batchInsert(@Param("list") List<FinancialTransaction> transactions);
    
    /**
     * 批量更新交易记录
     * 
     * @param transactions 交易记录列表
     * @return 更新成功的记录数
     */
    int batchUpdate(@Param("list") List<FinancialTransaction> transactions);
}