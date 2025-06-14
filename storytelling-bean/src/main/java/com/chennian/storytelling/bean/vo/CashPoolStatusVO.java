package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 资金池状态VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class CashPoolStatusVO {
    /**
     * 资金池基本信息
     */
    private BasicInfo basicInfo;
    
    /**
     * 资金池余额信息
     */
    private BalanceInfo balanceInfo;
    
    /**
     * 子账户信息
     */
    private Map<String, SubAccount> subAccounts;
    
    /**
     * 近期交易记录
     */
    private RecentTransactions recentTransactions;
    
    /**
     * 资金池基本信息
     */
    @Data
    public static class BasicInfo {
        /**
         * 资金池ID
         */
        private String poolId;
        
        /**
         * 资金池名称
         */
        private String poolName;
        
        /**
         * 资金池币种
         */
        private String currency;
        
        /**
         * 创建日期
         */
        private Date createDate;
        
        /**
         * 状态
         */
        private String status;
    }
    
    /**
     * 资金池余额信息
     */
    @Data
    public static class BalanceInfo {
        /**
         * 总余额
         */
        private Double totalBalance;
        
        /**
         * 可用余额
         */
        private Double availableBalance;
        
        /**
         * 冻结金额
         */
        private Double frozenAmount;
        
        /**
         * 透支额度
         */
        private Double overdraftLimit;
        
        /**
         * 已用透支
         */
        private Double usedOverdraft;
    }
    
    /**
     * 子账户信息
     */
    @Data
    public static class SubAccount {
        /**
         * 子账户ID
         */
        private String subAccountId;
        
        /**
         * 子账户名称
         */
        private String subAccountName;
        
        /**
         * 余额
         */
        private Double balance;
        
        /**
         * 限额
         */
        private Double limit;
    }
    
    /**
     * 近期交易记录
     */
    @Data
    public static class RecentTransactions {
        /**
         * 交易总数
         */
        private Integer totalTransactions;
        
        /**
         * 交易总额
         */
        private Double totalAmount;
        
        /**
         * 最大单笔交易
         */
        private Double maxTransaction;
        
        /**
         * 最近交易日期
         */
        private Date latestTransactionDate;
    }
}