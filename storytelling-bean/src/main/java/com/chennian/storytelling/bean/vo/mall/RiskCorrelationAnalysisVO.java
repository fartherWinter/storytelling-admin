package com.chennian.storytelling.bean.vo.mall;

/**
 * 风险关联分析VO
 * 
 * @author chennian
 * @date 2024-01-01
 */
public class RiskCorrelationAnalysisVO {
    
    /** 风险1ID */
    private Long risk1Id;
    
    /** 风险1名称 */
    private String risk1Name;
    
    /** 风险1类型 */
    private String risk1Type;
    
    /** 风险2ID */
    private Long risk2Id;
    
    /** 风险2名称 */
    private String risk2Name;
    
    /** 风险2类型 */
    private String risk2Type;
    
    /** 关联类型 */
    private String correlationType;
    
    /** 等级差异 */
    private Integer levelDifference;
    
    public RiskCorrelationAnalysisVO() {
    }
    
    public Long getRisk1Id() {
        return risk1Id;
    }
    
    public void setRisk1Id(Long risk1Id) {
        this.risk1Id = risk1Id;
    }
    
    public String getRisk1Name() {
        return risk1Name;
    }
    
    public void setRisk1Name(String risk1Name) {
        this.risk1Name = risk1Name;
    }
    
    public String getRisk1Type() {
        return risk1Type;
    }
    
    public void setRisk1Type(String risk1Type) {
        this.risk1Type = risk1Type;
    }
    
    public Long getRisk2Id() {
        return risk2Id;
    }
    
    public void setRisk2Id(Long risk2Id) {
        this.risk2Id = risk2Id;
    }
    
    public String getRisk2Name() {
        return risk2Name;
    }
    
    public void setRisk2Name(String risk2Name) {
        this.risk2Name = risk2Name;
    }
    
    public String getRisk2Type() {
        return risk2Type;
    }
    
    public void setRisk2Type(String risk2Type) {
        this.risk2Type = risk2Type;
    }
    
    public String getCorrelationType() {
        return correlationType;
    }
    
    public void setCorrelationType(String correlationType) {
        this.correlationType = correlationType;
    }
    
    public Integer getLevelDifference() {
        return levelDifference;
    }
    
    public void setLevelDifference(Integer levelDifference) {
        this.levelDifference = levelDifference;
    }
    
    @Override
    public String toString() {
        return "RiskCorrelationAnalysisVO{" +
                "risk1Id=" + risk1Id +
                ", risk1Name='" + risk1Name + '\'' +
                ", risk1Type='" + risk1Type + '\'' +
                ", risk2Id=" + risk2Id +
                ", risk2Name='" + risk2Name + '\'' +
                ", risk2Type='" + risk2Type + '\'' +
                ", correlationType='" + correlationType + '\'' +
                ", levelDifference=" + levelDifference +
                '}';
    }
}