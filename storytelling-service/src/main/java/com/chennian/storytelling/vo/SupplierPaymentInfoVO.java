package com.chennian.storytelling.vo;

import java.util.List;

/**
 * 供应商付款信息VO
 * @author chen
 * @date 2023/6/15
 */
public class SupplierPaymentInfoVO {
    
    /** 供应商名称 */
    private String supplierName;
    
    /** 供应商编号 */
    private String supplierCode;
    
    /** 应付总额 */
    private Double totalAmount;
    
    /** 已付金额 */
    private Double paidAmount;
    
    /** 付款条件 */
    private String paymentTerms;
    
    /** 信用额度 */
    private Double creditLimit;
    
    /** 剩余信用额度 */
    private Double remainingCreditLimit;
    
    /** 最近付款记录 */
    private List<PaymentRecord> recentPayments;
    
    /** 待付发票 */
    private List<PendingInvoice> pendingInvoices;
    
    /**
     * 付款记录内部类
     */
    public static class PaymentRecord {
        /** 日期 */
        private String date;
        
        /** 金额 */
        private Double amount;
        
        /** 发票号 */
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
     * 待付发票内部类
     */
    public static class PendingInvoice {
        /** 发票号 */
        private String invoiceNo;
        
        /** 日期 */
        private String date;
        
        /** 金额 */
        private Double amount;
        
        /** 到期日 */
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
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

    public List<PaymentRecord> getRecentPayments() {
        return recentPayments;
    }

    public void setRecentPayments(List<PaymentRecord> recentPayments) {
        this.recentPayments = recentPayments;
    }

    public List<PendingInvoice> getPendingInvoices() {
        return pendingInvoices;
    }

    public void setPendingInvoices(List<PendingInvoice> pendingInvoices) {
        this.pendingInvoices = pendingInvoices;
    }
}