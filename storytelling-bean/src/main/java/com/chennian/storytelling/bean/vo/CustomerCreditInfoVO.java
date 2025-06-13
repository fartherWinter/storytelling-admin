package com.chennian.storytelling.bean.vo;

import java.util.List;

/**
 * 客户信用信息VO
 * @author chen
 * @date 2023/6/15
 */
public class CustomerCreditInfoVO {
    
    /**
     * 客户名称
     */
    private String customerName;
    
    /**
     * 客户编号
     */
    private String customerCode;
    
    /**
     * 应收总额
     */
    private Double totalAmount;
    
    /**
     * 已收金额
     */
    private Double receivedAmount;
    
    /**
     * 信用额度
     */
    private Double creditLimit;
    
    /**
     * 剩余信用额度
     */
    private Double remainingCreditLimit;
    
    /**
     * 信用评级
     */
    private String creditRating;
    
    /**
     * 付款条件
     */
    private String paymentTerms;
    
    /**
     * 最近收款记录
     */
    private List<CollectionRecord> recentCollections;
    
    /**
     * 待收发票
     */
    private List<PendingInvoice> pendingInvoices;
    
    /**
     * 收款记录
     */
    public static class CollectionRecord {
        /**
         * 日期
         */
        private String date;
        
        /**
         * 金额
         */
        private Double amount;
        
        /**
         * 发票号
         */
        private String invoiceNo;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }
    }
    
    /**
     * 待收发票
     */
    public static class PendingInvoice {
        /**
         * 发票号
         */
        private String invoiceNo;
        
        /**
         * 日期
         */
        private String date;
        
        /**
         * 金额
         */
        private Double amount;
        
        /**
         * 到期日
         */
        private String dueDate;

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(Double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getRemainingCreditLimit() {
        return remainingCreditLimit;
    }

    public void setRemainingCreditLimit(Double remainingCreditLimit) {
        this.remainingCreditLimit = remainingCreditLimit;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public List<CollectionRecord> getRecentCollections() {
        return recentCollections;
    }

    public void setRecentCollections(List<CollectionRecord> recentCollections) {
        this.recentCollections = recentCollections;
    }

    public List<PendingInvoice> getPendingInvoices() {
        return pendingInvoices;
    }

    public void setPendingInvoices(List<PendingInvoice> pendingInvoices) {
        this.pendingInvoices = pendingInvoices;
    }
}