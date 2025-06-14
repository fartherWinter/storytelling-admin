package com.chennian.storytelling.bean.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HRM薪资统计数据VO对象
 * @author storytelling
 * @date 2024-01-01
 */
public class HrmSalaryStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 薪资记录总数
     */
    private Long totalSalaryRecords;

    /**
     * 薪资总额
     */
    private BigDecimal totalSalaryAmount;

    /**
     * 平均薪资
     */
    private BigDecimal averageSalary;

    /**
     * 最高薪资
     */
    private BigDecimal maxSalary;

    /**
     * 最低薪资
     */
    private BigDecimal minSalary;

    /**
     * 基本工资总额
     */
    private BigDecimal totalBaseSalary;

    /**
     * 绩效工资总额
     */
    private BigDecimal totalPerformanceSalary;

    /**
     * 津贴补助总额
     */
    private BigDecimal totalAllowance;

    /**
     * 加班费总额
     */
    private BigDecimal totalOvertimePay;

    /**
     * 扣款总额
     */
    private BigDecimal totalDeduction;

    /**
     * 部门薪资统计
     */
    private List<Map<String, Object>> departmentSalaryStats;

    /**
     * 职位薪资统计
     */
    private List<Map<String, Object>> positionSalaryStats;

    /**
     * 月度薪资趋势
     */
    private List<Map<String, Object>> monthlySalaryTrend;

    /**
     * 薪资分布区间
     */
    private List<Map<String, Object>> salaryDistribution;

    public Long getTotalSalaryRecords() {
        return totalSalaryRecords;
    }

    public void setTotalSalaryRecords(Long totalSalaryRecords) {
        this.totalSalaryRecords = totalSalaryRecords;
    }

    public BigDecimal getTotalSalaryAmount() {
        return totalSalaryAmount;
    }

    public void setTotalSalaryAmount(BigDecimal totalSalaryAmount) {
        this.totalSalaryAmount = totalSalaryAmount;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(BigDecimal averageSalary) {
        this.averageSalary = averageSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getTotalBaseSalary() {
        return totalBaseSalary;
    }

    public void setTotalBaseSalary(BigDecimal totalBaseSalary) {
        this.totalBaseSalary = totalBaseSalary;
    }

    public BigDecimal getTotalPerformanceSalary() {
        return totalPerformanceSalary;
    }

    public void setTotalPerformanceSalary(BigDecimal totalPerformanceSalary) {
        this.totalPerformanceSalary = totalPerformanceSalary;
    }

    public BigDecimal getTotalAllowance() {
        return totalAllowance;
    }

    public void setTotalAllowance(BigDecimal totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    public BigDecimal getTotalOvertimePay() {
        return totalOvertimePay;
    }

    public void setTotalOvertimePay(BigDecimal totalOvertimePay) {
        this.totalOvertimePay = totalOvertimePay;
    }

    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public List<Map<String, Object>> getDepartmentSalaryStats() {
        return departmentSalaryStats;
    }

    public void setDepartmentSalaryStats(List<Map<String, Object>> departmentSalaryStats) {
        this.departmentSalaryStats = departmentSalaryStats;
    }

    public List<Map<String, Object>> getPositionSalaryStats() {
        return positionSalaryStats;
    }

    public void setPositionSalaryStats(List<Map<String, Object>> positionSalaryStats) {
        this.positionSalaryStats = positionSalaryStats;
    }

    public List<Map<String, Object>> getMonthlySalaryTrend() {
        return monthlySalaryTrend;
    }

    public void setMonthlySalaryTrend(List<Map<String, Object>> monthlySalaryTrend) {
        this.monthlySalaryTrend = monthlySalaryTrend;
    }

    public List<Map<String, Object>> getSalaryDistribution() {
        return salaryDistribution;
    }

    public void setSalaryDistribution(List<Map<String, Object>> salaryDistribution) {
        this.salaryDistribution = salaryDistribution;
    }

    @Override
    public String toString() {
        return "HrmSalaryStatisticsVO{" +
                "totalSalaryRecords=" + totalSalaryRecords +
                ", totalSalaryAmount=" + totalSalaryAmount +
                ", averageSalary=" + averageSalary +
                ", maxSalary=" + maxSalary +
                ", minSalary=" + minSalary +
                ", totalBaseSalary=" + totalBaseSalary +
                ", totalPerformanceSalary=" + totalPerformanceSalary +
                ", totalAllowance=" + totalAllowance +
                ", totalOvertimePay=" + totalOvertimePay +
                ", totalDeduction=" + totalDeduction +
                ", departmentSalaryStats=" + departmentSalaryStats +
                ", positionSalaryStats=" + positionSalaryStats +
                ", monthlySalaryTrend=" + monthlySalaryTrend +
                ", salaryDistribution=" + salaryDistribution +
                '}';
    }
}