package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 税务管理服务接口
 * 负责税务汇总、增值税申报、所得税准备等功能
 * @author chen
 * @date 2023/6/15
 */
public interface TaxService {
    
    /**
     * 获取税务汇总信息
     * @param period 期间
     * @return 税务汇总数据
     */
    Map<String, Object> getTaxSummary(String period);
    
    /**
     * 获取增值税申报数据
     * @param period 期间
     * @return 增值税申报数据
     */
    Map<String, Object> getVatDeclarationData(String period);
    
    /**
     * 获取所得税准备数据
     * @param period 期间
     * @return 所得税准备数据
     */
    Map<String, Object> getIncomeTaxProvisionData(String period);
}