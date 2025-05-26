package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.CostAccountingMapper;
import com.chennian.storytelling.service.CostAccountingService;
import com.chennian.storytelling.vo.ActivityBasedCostAnalysisVO;
import com.chennian.storytelling.vo.CostCenterAnalysisVO;
import com.chennian.storytelling.vo.CostComposition;
import com.chennian.storytelling.vo.CostOptimization;
import com.chennian.storytelling.vo.CostSummary;
import com.chennian.storytelling.vo.CostTrend;
import com.chennian.storytelling.vo.ProductCostAnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成本会计服务实现类
 * 负责成本中心分析、产品成本分析等功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class CostAccountingServiceImpl implements CostAccountingService {
    
    @Autowired
    private CostAccountingMapper costAccountingMapper;

    /**
     * 获取成本中心分析
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心分析数据
     */
    @Override
    public CostCenterAnalysisVO getCostCenterAnalysis(String period, Long costCenterId) {
        CostCenterAnalysisVO costCenterAnalysis = new CostCenterAnalysisVO();
        
        // 获取成本中心基本信息
        Map<String, Object> basicInfoMap = costAccountingMapper.getCostCenterBasicInfo(period, costCenterId);
        CostCenterAnalysisVO.BasicInfo basicInfo = new CostCenterAnalysisVO.BasicInfo();
        basicInfo.setCostCenterId(costCenterId);
        basicInfo.setCostCenterCode((String) basicInfoMap.get("costCenterCode"));
        basicInfo.setCostCenterName((String) basicInfoMap.get("costCenterName"));
        basicInfo.setDepartmentId((Long) basicInfoMap.get("departmentId"));
        basicInfo.setDepartmentName((String) basicInfoMap.get("departmentName"));
        basicInfo.setManagerId((Long) basicInfoMap.get("managerId"));
        basicInfo.setManagerName((String) basicInfoMap.get("managerName"));
        basicInfo.setCreateDate((Date) basicInfoMap.get("createDate"));
        basicInfo.setStatus((String) basicInfoMap.get("status"));
        costCenterAnalysis.setBasicInfo(basicInfo);
        
        // 获取成本汇总
        Map<String, Object> costSummaryMap = costAccountingMapper.getCostCenterSummary(period, costCenterId);
        CostCenterAnalysisVO.CostSummary costSummary = new CostCenterAnalysisVO.CostSummary();
        costSummary.setTotalCost((Double) costSummaryMap.get("totalCost"));
        costSummary.setDirectCost((Double) costSummaryMap.get("directCost"));
        costSummary.setIndirectCost((Double) costSummaryMap.get("indirectCost"));
        costSummary.setLaborCost((Double) costSummaryMap.get("laborCost"));
        costSummary.setMaterialCost((Double) costSummaryMap.get("materialCost"));
        costSummary.setOverheadCost((Double) costSummaryMap.get("overheadCost"));
        costSummary.setOtherCost((Double) costSummaryMap.get("otherCost"));
        costCenterAnalysis.setCostSummary(costSummary);
        
        // 获取成本明细
        Map<String, Object> costDetailsMap = costAccountingMapper.getCostCenterDetails(period, costCenterId);
        CostCenterAnalysisVO.CostDetails costDetails = new CostCenterAnalysisVO.CostDetails();
        costDetails.setCostItemId((Long) costDetailsMap.get("costItemId"));
        costDetails.setCostItemName((String) costDetailsMap.get("costItemName"));
        costDetails.setCostType((String) costDetailsMap.get("costType"));
        costDetails.setCostCategory((String) costDetailsMap.get("costCategory"));
        costDetails.setAmount((Double) costDetailsMap.get("amount"));
        costDetails.setBudgetAmount((Double) costDetailsMap.get("budgetAmount"));
        costDetails.setVariance((Double) costDetailsMap.get("variance"));
        costDetails.setVariancePercentage((Double) costDetailsMap.get("variancePercentage"));
        costCenterAnalysis.setCostDetails(costDetails);
        
        // 获取成本分析
        Map<String, Object> costAnalysisMap = costAccountingMapper.getCostCenterAnalysis(period, costCenterId);
        CostCenterAnalysisVO.CostAnalysis costAnalysis = new CostCenterAnalysisVO.CostAnalysis();
        costAnalysis.setCostCenterId((Long) costAnalysisMap.get("costCenterId"));
        costAnalysis.setCostCenterName((String) costAnalysisMap.get("costCenterName"));
        costAnalysis.setTotalCost((Double) costAnalysisMap.get("totalCost"));
        costAnalysis.setPreviousPeriodCost((Double) costAnalysisMap.get("previousPeriodCost"));
        costAnalysis.setCostVariance((Double) costAnalysisMap.get("costVariance"));
        costAnalysis.setCostVariancePercentage((Double) costAnalysisMap.get("costVariancePercentage"));
        costAnalysis.setYtdCost((Double) costAnalysisMap.get("ytdCost"));
        costAnalysis.setAnnualBudget((Double) costAnalysisMap.get("annualBudget"));
        costAnalysis.setBudgetUtilizationPercentage((Double) costAnalysisMap.get("budgetUtilizationPercentage"));
        costAnalysis.setCostPerUnit((Double) costAnalysisMap.get("costPerUnit"));
        costAnalysis.setEfficiencyIndex((Double) costAnalysisMap.get("efficiencyIndex"));
        costAnalysis.setOtherExpenses((Double) costAnalysisMap.get("otherExpenses"));
        costCenterAnalysis.setCostAnalysis(costAnalysis);
        
        return costCenterAnalysis;
    }

    /**
     * 获取产品成本分析
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本分析数据
     */
    @Override
    public ProductCostAnalysisVO getProductCostAnalysis(String period, Long productId) {
        ProductCostAnalysisVO productCostAnalysis = new ProductCostAnalysisVO();
        
        // 获取产品基本信息
        Map<String, Object> basicInfoMap = costAccountingMapper.getProductBasicInfo(period, productId);
        productCostAnalysis.setProductId(productId);
        productCostAnalysis.setProductName((String) basicInfoMap.get("productName"));
        productCostAnalysis.setProductCategory((String) basicInfoMap.get("productCategory"));
        productCostAnalysis.setProductCode((String) basicInfoMap.get("productCode"));
        productCostAnalysis.setUnitOfMeasure((String) basicInfoMap.get("unitOfMeasure"));
        productCostAnalysis.setStatus((String) basicInfoMap.get("status"));
        
        // 获取成本汇总
        Map<String, Object> costSummaryMap = costAccountingMapper.getProductCostSummary(period, productId);
        CostSummary costSummary = new CostSummary();
        costSummary.setStandardCost(new java.math.BigDecimal(String.valueOf(costSummaryMap.get("standardCost"))));
        costSummary.setActualCost(new java.math.BigDecimal(String.valueOf(costSummaryMap.get("actualCost"))));
        costSummary.setCostVariance(new java.math.BigDecimal(String.valueOf(costSummaryMap.get("costVariance"))));
        costSummary.setCostVariancePercentage(Double.valueOf(String.valueOf(costSummaryMap.get("costVarianceRate"))));
        productCostAnalysis.setCostSummary(costSummary);
        
        // 获取成本构成
        Map<String, Object> costCompositionMap = costAccountingMapper.getProductCostComposition(period, productId);
        List<CostComposition> costCompositionList = new java.util.ArrayList<>();
        
        // 直接材料
        CostComposition directMaterialComp = new CostComposition();
        directMaterialComp.setCostType("直接材料");
        directMaterialComp.setAmount((Double) costCompositionMap.get("directMaterial"));
        directMaterialComp.setPercentage((Double) costCompositionMap.get("directMaterialPercentage"));
        costCompositionList.add(directMaterialComp);
        
        // 直接人工
        CostComposition directLaborComp = new CostComposition();
        directLaborComp.setCostType("直接人工");
        directLaborComp.setAmount((Double) costCompositionMap.get("directLabor"));
        directLaborComp.setPercentage((Double) costCompositionMap.get("directLaborPercentage"));
        costCompositionList.add(directLaborComp);
        
        // 制造费用
        CostComposition manufacturingComp = new CostComposition();
        manufacturingComp.setCostType("制造费用");
        manufacturingComp.setAmount((Double) costCompositionMap.get("manufacturingOverhead"));
        manufacturingComp.setPercentage((Double) costCompositionMap.get("manufacturingOverheadPercentage"));
        costCompositionList.add(manufacturingComp);
        
        // 其他费用
        CostComposition otherComp = new CostComposition();
        otherComp.setCostType("其他费用");
        otherComp.setAmount((Double) costCompositionMap.get("otherExpenses"));
        otherComp.setPercentage((Double) costCompositionMap.get("otherExpensesPercentage"));
        costCompositionList.add(otherComp);
        
        productCostAnalysis.setCostComposition(costCompositionList);
        
        // 获取成本趋势
        Map<String, Object> costTrendMap = costAccountingMapper.getProductCostTrend(period, productId);
        List<CostTrend> costTrendList = new java.util.ArrayList<>();
        
        // 当前期间成本趋势
        CostTrend currentTrend = new CostTrend();
        currentTrend.setPeriod(period);
        currentTrend.setTotalCost((Double) costTrendMap.get("currentCost"));
        currentTrend.setLastMonthCost((Double) costTrendMap.get("lastMonthCost"));
        currentTrend.setMonthOnMonthChange((String) costTrendMap.get("monthOnMonthChange"));
        currentTrend.setLastYearCost((Double) costTrendMap.get("lastYearCost"));
        currentTrend.setYearOnYearChange((String) costTrendMap.get("yearOnYearChange"));
        costTrendList.add(currentTrend);
        
        productCostAnalysis.setCostTrends(costTrendList);
        
        // 获取成本优化建议
        Map<String, Object> costOptimizationMap = costAccountingMapper.getProductCostOptimization(period, productId);
        List<CostOptimization> costOptimizationList = new java.util.ArrayList<>();
        
        // 材料成本优化
        CostOptimization materialOpt = new CostOptimization();
        materialOpt.setArea("材料成本");
        materialOpt.setSuggestion((String) costOptimizationMap.get("materialCostOptimization"));
        materialOpt.setPotentialSaving((Double) costOptimizationMap.get("materialSavingPotential"));
        materialOpt.setPriority(1);
        costOptimizationList.add(materialOpt);
        
        // 工艺优化
        CostOptimization processOpt = new CostOptimization();
        processOpt.setArea("工艺流程");
        processOpt.setSuggestion((String) costOptimizationMap.get("processOptimization"));
        processOpt.setPotentialSaving((Double) costOptimizationMap.get("processSavingPotential"));
        processOpt.setPriority(2);
        costOptimizationList.add(processOpt);
        
        // 批量生产
        CostOptimization batchOpt = new CostOptimization();
        batchOpt.setArea("批量生产");
        batchOpt.setSuggestion((String) costOptimizationMap.get("batchProduction"));
        batchOpt.setPotentialSaving((Double) costOptimizationMap.get("batchSavingPotential"));
        batchOpt.setPriority(3);
        costOptimizationList.add(batchOpt);
        
        productCostAnalysis.setCostOptimizations(costOptimizationList);
        
        return productCostAnalysis;
    }

    /**
     * 获取基于活动的成本分析
     * @param period 期间
     * @return 基于活动的成本分析数据
     */
    @Override
    public ActivityBasedCostAnalysisVO getActivityBasedCostAnalysis(String period) {
        ActivityBasedCostAnalysisVO abcAnalysis = new ActivityBasedCostAnalysisVO();
        
        // 获取活动成本池
        List<ActivityBasedCostAnalysisVO.ActivityCostPool> activityCostPoolList = new ArrayList<>();
        Map<String, Double> activityCostPoolMap = costAccountingMapper.getActivityCostPool(period);
        
        for (Map.Entry<String, Double> entry : activityCostPoolMap.entrySet()) {
            ActivityBasedCostAnalysisVO.ActivityCostPool pool = new ActivityBasedCostAnalysisVO.ActivityCostPool();
            String[] keyParts = entry.getKey().split("_");
            pool.setActivityId(Long.valueOf(keyParts[0]));
            pool.setActivityName(keyParts[1]);
            pool.setCostAmount(entry.getValue());
            activityCostPoolList.add(pool);
        }
        abcAnalysis.setActivityCostPool(activityCostPoolList);
        
        // 获取成本动因
        List<ActivityBasedCostAnalysisVO.CostDriver> costDriversList = new ArrayList<>();
        Map<String, Integer> costDriversMap = costAccountingMapper.getCostDrivers(period);
        
        for (Map.Entry<String, Integer> entry : costDriversMap.entrySet()) {
            ActivityBasedCostAnalysisVO.CostDriver driver = new ActivityBasedCostAnalysisVO.CostDriver();
            String[] keyParts = entry.getKey().split("_");
            driver.setDriverId(Long.valueOf(keyParts[0]));
            driver.setDriverName(keyParts[1]);
            driver.setDriverUnit(keyParts.length > 2 ? keyParts[2] : "单位");
            driver.setDriverQuantity(entry.getValue());
            costDriversList.add(driver);
        }
        abcAnalysis.setCostDrivers(costDriversList);
        
        // 获取单位成本动因率
        List<ActivityBasedCostAnalysisVO.UnitCostDriverRate> unitRateList = new ArrayList<>();
        Map<String, Double> unitRateMap = costAccountingMapper.getUnitCostDriverRate(period);
        
        for (Map.Entry<String, Double> entry : unitRateMap.entrySet()) {
            ActivityBasedCostAnalysisVO.UnitCostDriverRate rate = new ActivityBasedCostAnalysisVO.UnitCostDriverRate();
            String[] keyParts = entry.getKey().split("_");
            rate.setActivityId(Long.valueOf(keyParts[0]));
            rate.setActivityName(keyParts[1]);
            rate.setDriverId(Long.valueOf(keyParts[2]));
            rate.setDriverName(keyParts[3]);
            rate.setRate(entry.getValue());
            unitRateList.add(rate);
        }
        abcAnalysis.setUnitCostDriverRate(unitRateList);
        
        // 获取产品ABC成本分析
        Map<String, Map<String, Object>> productABCMap = costAccountingMapper.getProductABC(period);
        Map<String, ActivityBasedCostAnalysisVO.ProductABC> productABC = new HashMap<>();
        
        for (Map.Entry<String, Map<String, Object>> entry : productABCMap.entrySet()) {
            ActivityBasedCostAnalysisVO.ProductABC product = new ActivityBasedCostAnalysisVO.ProductABC();
            Map<String, Object> productData = entry.getValue();
            
            product.setProductName((String) productData.get("productName"));
            product.setTraditionalCost((Double) productData.get("traditionalCost"));
            product.setAbcCost((Double) productData.get("abcCost"));
            product.setCostVariance((Double) productData.get("costVariance"));
            product.setVarianceRate((String) productData.get("varianceRate"));
            
            productABC.put(entry.getKey(), product);
        }
        abcAnalysis.setProductABC(productABC);
        
        // 获取活动价值分析
        List<ActivityBasedCostAnalysisVO.ActivityValueAnalysis> valueAnalysisList = new ArrayList<>();
        Map<String, Object> activityValueAnalysisMap = costAccountingMapper.getActivityValueAnalysis(period);
        
        // 处理活动价值分析数据
        for (Map.Entry<String, Object> entry : activityValueAnalysisMap.entrySet()) {
            if (entry.getKey().startsWith("activity_")) {
                Map<String, Object> valueData = (Map<String, Object>) entry.getValue();
                ActivityBasedCostAnalysisVO.ActivityValueAnalysis analysis = new ActivityBasedCostAnalysisVO.ActivityValueAnalysis();
                
                analysis.setActivityId((Long) valueData.get("activityId"));
                analysis.setActivityName((String) valueData.get("activityName"));
                analysis.setCostAmount((Double) valueData.get("costAmount"));
                analysis.setValueAddedPercentage((Double) valueData.get("valueAddedPercentage"));
                analysis.setNonValueAddedPercentage((Double) valueData.get("nonValueAddedPercentage"));
                analysis.setValueClassification((String) valueData.get("valueClassification"));
                analysis.setImprovementPotential((String) valueData.get("improvementPotential"));
                
                valueAnalysisList.add(analysis);
            }
        }
        abcAnalysis.setActivityValueAnalysis(valueAnalysisList);
        
        return abcAnalysis;
    }
}