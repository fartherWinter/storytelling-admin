package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.FinancialTransaction;
import com.chennian.storytelling.common.utils.DateUtils;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.FinancialTransactionMapper;
import com.chennian.storytelling.service.SysFinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 财务管理服务实现类
 * 
 * @author chennian
 * @date 2023/5/20
 */
@Service
public class SysFinancialServiceImpl extends ServiceImpl<FinancialTransactionMapper, FinancialTransaction> implements SysFinancialService {

    @Autowired
    private FinancialTransactionMapper financialTransactionMapper;

    /**
     * 分页查询财务交易记录
     *
     * @param page 分页参数
     * @param transaction 查询条件
     * @return 交易记录分页数据
     */
    @Override
    public IPage<FinancialTransaction> findByPage(PageParam<FinancialTransaction> page, FinancialTransaction transaction) {
        LambdaQueryWrapper<FinancialTransaction> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        if (transaction != null) {
            // 按交易类型查询
            if (StringUtils.hasText(transaction.getTransactionType())) {
                queryWrapper.eq(FinancialTransaction::getTransactionType, transaction.getTransactionType());
            }
            
            // 按交易方向查询
            if (StringUtils.hasText(transaction.getDirection())) {
                queryWrapper.eq(FinancialTransaction::getDirection, transaction.getDirection());
            }
            
            // 按交易状态查询
            if (StringUtils.hasText(transaction.getStatus())) {
                queryWrapper.eq(FinancialTransaction::getStatus, transaction.getStatus());
            }
            
            // 按交易日期范围查询
            if (transaction.getTransactionDate() != null) {
                queryWrapper.ge(FinancialTransaction::getTransactionDate, transaction.getTransactionDate());
            }
            
            // 按交易编号模糊查询
            if (StringUtils.hasText(transaction.getTransactionNo())) {
                queryWrapper.like(FinancialTransaction::getTransactionNo, transaction.getTransactionNo());
            }
        }
        
        // 默认按交易日期降序排序
        queryWrapper.orderByDesc(FinancialTransaction::getTransactionDate);
        
        // 执行分页查询
        return financialTransactionMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
    }

    /**
     * 根据ID查询交易记录
     *
     * @param transactionId 交易ID
     * @return 交易记录信息
     */
    @Override
    public FinancialTransaction selectTransactionById(Long transactionId) {
        return financialTransactionMapper.selectById(transactionId);
    }

    /**
     * 新增交易记录
     *
     * @param transaction 交易记录信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTransaction(FinancialTransaction transaction) {
        // 生成交易编号
        if (!StringUtils.hasText(transaction.getTransactionNo())) {
            String dateStr = DateUtils.currentDateStr();
            transaction.setTransactionNo("FT" + dateStr + System.currentTimeMillis() % 10000);
        }
        
        // 设置创建时间
        if (transaction.getCreateTime() == null) {
            transaction.setCreateTime(new Date());
        }
        
        return financialTransactionMapper.insert(transaction);
    }

    /**
     * 修改交易记录
     *
     * @param transaction 交易记录信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTransaction(FinancialTransaction transaction) {
        // 设置更新时间
        transaction.setUpdateTime(new Date());
        return financialTransactionMapper.updateById(transaction);
    }

    /**
     * 批量删除交易记录
     *
     * @param transactionIds 需要删除的交易ID数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTransactionByIds(Long[] transactionIds) {
        return financialTransactionMapper.deleteBatchIds(Arrays.asList(transactionIds));
    }

    /**
     * 获取收入支出统计报表
     *
     * @param dateRange 日期范围
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getIncomeExpenseReport(String dateRange) {
        Map<String, Object> result = new HashMap<>();
        
        // 解析日期范围
        Date[] dates = parseDateRange(dateRange);
        Date startDate = dates[0];
        Date endDate = dates[1];
        
        // 查询收入总额
        LambdaQueryWrapper<FinancialTransaction> incomeQuery = new LambdaQueryWrapper<>();
        incomeQuery.eq(FinancialTransaction::getDirection, "1")
                  .eq(FinancialTransaction::getStatus, "1")
                  .between(FinancialTransaction::getTransactionDate, startDate, endDate);
        List<FinancialTransaction> incomeList = financialTransactionMapper.selectList(incomeQuery);
        double totalIncome = incomeList.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
        
        // 查询支出总额
        LambdaQueryWrapper<FinancialTransaction> expenseQuery = new LambdaQueryWrapper<>();
        expenseQuery.eq(FinancialTransaction::getDirection, "2")
                   .eq(FinancialTransaction::getStatus, "1")
                   .between(FinancialTransaction::getTransactionDate, startDate, endDate);
        List<FinancialTransaction> expenseList = financialTransactionMapper.selectList(expenseQuery);
        double totalExpense = expenseList.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
        
        // 按交易类型统计收入
        Map<String, Double> incomeByType = new HashMap<>();
        incomeList.forEach(t -> {
            String type = t.getTransactionType();
            incomeByType.put(type, incomeByType.getOrDefault(type, 0.0) + t.getAmount().doubleValue());
        });
        
        // 按交易类型统计支出
        Map<String, Double> expenseByType = new HashMap<>();
        expenseList.forEach(t -> {
            String type = t.getTransactionType();
            expenseByType.put(type, expenseByType.getOrDefault(type, 0.0) + t.getAmount().doubleValue());
        });
        
        // 组装结果
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("netProfit", totalIncome - totalExpense);
        result.put("incomeByType", incomeByType);
        result.put("expenseByType", expenseByType);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        
        return result;
    }

    /**
     * 获取利润分析报表
     *
     * @param dateRange 日期范围
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getProfitReport(String dateRange) {
        Map<String, Object> result = new HashMap<>();
        
        // 解析日期范围
        Date[] dates = parseDateRange(dateRange);
        Date startDate = dates[0];
        Date endDate = dates[1];
        
        // 查询收入总额
        LambdaQueryWrapper<FinancialTransaction> incomeQuery = new LambdaQueryWrapper<>();
        incomeQuery.eq(FinancialTransaction::getDirection, "1")
                  .eq(FinancialTransaction::getStatus, "1")
                  .between(FinancialTransaction::getTransactionDate, startDate, endDate);
        List<FinancialTransaction> incomeList = financialTransactionMapper.selectList(incomeQuery);
        double totalIncome = incomeList.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
        
        // 查询支出总额
        LambdaQueryWrapper<FinancialTransaction> expenseQuery = new LambdaQueryWrapper<>();
        expenseQuery.eq(FinancialTransaction::getDirection, "2")
                   .eq(FinancialTransaction::getStatus, "1")
                   .between(FinancialTransaction::getTransactionDate, startDate, endDate);
        List<FinancialTransaction> expenseList = financialTransactionMapper.selectList(expenseQuery);
        double totalExpense = expenseList.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum();
        
        // 计算毛利润
        double grossProfit = totalIncome - totalExpense;
        
        // 计算利润率
        double profitMargin = totalIncome > 0 ? (grossProfit / totalIncome) * 100 : 0;
        
        // 组装结果
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("grossProfit", grossProfit);
        result.put("profitMargin", profitMargin);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        
        return result;
    }

    /**
     * 获取现金流量表
     *
     * @param dateRange 日期范围
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getCashFlowReport(String dateRange) {
        Map<String, Object> result = new HashMap<>();
        
        // 解析日期范围
        Date[] dates = parseDateRange(dateRange);
        Date startDate = dates[0];
        Date endDate = dates[1];
        
        // 查询所有交易记录
        LambdaQueryWrapper<FinancialTransaction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FinancialTransaction::getStatus, "1")
                   .between(FinancialTransaction::getTransactionDate, startDate, endDate)
                   .orderByAsc(FinancialTransaction::getTransactionDate);
        List<FinancialTransaction> transactions = financialTransactionMapper.selectList(queryWrapper);
        
        // 计算现金流入
        double cashInflow = transactions.stream()
                .filter(t -> "1".equals(t.getDirection()))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();
        
        // 计算现金流出
        double cashOutflow = transactions.stream()
                .filter(t -> "2".equals(t.getDirection()))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();
        
        // 计算净现金流
        double netCashFlow = cashInflow - cashOutflow;
        
        // 按账户统计现金流
        Map<String, Double> cashFlowByAccount = new HashMap<>();
        transactions.forEach(t -> {
            String account = t.getAccount();
            double amount = t.getAmount().doubleValue();
            if ("1".equals(t.getDirection())) {
                cashFlowByAccount.put(account, cashFlowByAccount.getOrDefault(account, 0.0) + amount);
            } else {
                cashFlowByAccount.put(account, cashFlowByAccount.getOrDefault(account, 0.0) - amount);
            }
        });
        
        // 组装结果
        result.put("cashInflow", cashInflow);
        result.put("cashOutflow", cashOutflow);
        result.put("netCashFlow", netCashFlow);
        result.put("cashFlowByAccount", cashFlowByAccount);
        result.put("transactions", transactions);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        
        return result;
    }

    /**
     * 获取应收账款列表
     *
     * @return 应收账款数据
     */
    @Override
    public List<Map<String, Object>> getReceivableList() {
        // 这里应该查询应收账款表，但由于没有实际的应收账款表，暂时返回模拟数据
        List<Map<String, Object>> receivables = new ArrayList<>();
        
        // 模拟数据
        Map<String, Object> receivable1 = new HashMap<>();
        receivable1.put("receivableId", 1L);
        receivable1.put("receivableNo", "AR20230501001");
        receivable1.put("customerName", "北京科技有限公司");
        receivable1.put("amount", 15000.00);
        receivable1.put("paidAmount", 5000.00);
        receivable1.put("remainingAmount", 10000.00);
        receivable1.put("dueDate", new Date());
        receivable1.put("status", "1"); // 部分收款
        receivables.add(receivable1);
        
        Map<String, Object> receivable2 = new HashMap<>();
        receivable2.put("receivableId", 2L);
        receivable2.put("receivableNo", "AR20230502001");
        receivable2.put("customerName", "上海贸易有限公司");
        receivable2.put("amount", 8000.00);
        receivable2.put("paidAmount", 0.00);
        receivable2.put("remainingAmount", 8000.00);
        receivable2.put("dueDate", new Date());
        receivable2.put("status", "0"); // 未收款
        receivables.add(receivable2);
        
        return receivables;
    }

    /**
     * 获取应付账款列表
     *
     * @return 应付账款数据
     */
    @Override
    public List<Map<String, Object>> getPayableList() {
        // 这里应该查询应付账款表，但由于没有实际的应付账款表，暂时返回模拟数据
        List<Map<String, Object>> payables = new ArrayList<>();
        
        // 模拟数据
        Map<String, Object> payable1 = new HashMap<>();
        payable1.put("payableId", 1L);
        payable1.put("payableNo", "AP20230501001");
        payable1.put("supplierName", "广州原料供应商");
        payable1.put("amount", 12000.00);
        payable1.put("paidAmount", 6000.00);
        payable1.put("remainingAmount", 6000.00);
        payable1.put("dueDate", new Date());
        payable1.put("status", "1"); // 部分付款
        payables.add(payable1);
        
        Map<String, Object> payable2 = new HashMap<>();
        payable2.put("payableId", 2L);
        payable2.put("payableNo", "AP20230502001");
        payable2.put("supplierName", "深圳设备供应商");
        payable2.put("amount", 20000.00);
        payable2.put("paidAmount", 20000.00);
        payable2.put("remainingAmount", 0.00);
        payable2.put("dueDate", new Date());
        payable2.put("status", "2"); // 已付款
        payables.add(payable2);
        
        return payables;
    }
    
    /**
     * 解析日期范围字符串
     * 
     * @param dateRange 日期范围字符串，格式如：2023-01-01,2023-12-31
     * @return 开始日期和结束日期数组
     */
    private Date[] parseDateRange(String dateRange) {
        Date startDate;
        Date endDate;
        
        if (StringUtils.hasText(dateRange) && dateRange.contains(",")) {
            String[] dates = dateRange.split(",");
            startDate = DateUtils.parseDate(dates[0]);
            endDate = DateUtils.parseDate(dates[1]);
        } else {
            // 默认查询当前月
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startDate = calendar.getTime();
            
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.SECOND, -1);
            endDate = calendar.getTime();
        }
        
        return new Date[]{startDate, endDate};
    }
}
