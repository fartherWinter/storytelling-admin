package com.chennian.storytelling.vo;

/**
 * 应付账款账龄分析VO
 * @author chen
 * @date 2023/6/15
 */
public class PayablesAgingVO {
    
    /** 供应商名称 */
    private String supplierName;
    
    /** 供应商编号 */
    private String supplierCode;
    
    /** 应付总额 */
    private Double totalAmount;
    
    /** 30天以内金额 */
    private Double within30Days;
    
    /** 31-60天金额 */
    private Double days31To60;
    
    /** 61-90天金额 */
    private Double days61To90;
    
    /** 90天以上金额 */
    private Double over90Days;
    
    /** 联系人 */
    private String contactPerson;
    
    /** 联系电话 */
    private String contactPhone;

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

    public Double getWithin30Days() {
        return within30Days;
    }

    public void setWithin30Days(Double within30Days) {
        this.within30Days = within30Days;
    }

    public Double getDays31To60() {
        return days31To60;
    }

    public void setDays31To60(Double days31To60) {
        this.days31To60 = days31To60;
    }

    public Double getDays61To90() {
        return days61To90;
    }

    public void setDays61To90(Double days61To90) {
        this.days61To90 = days61To90;
    }

    public Double getOver90Days() {
        return over90Days;
    }

    public void setOver90Days(Double over90Days) {
        this.over90Days = over90Days;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}