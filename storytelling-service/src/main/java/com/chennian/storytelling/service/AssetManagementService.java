package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 资产管理服务接口
 * 负责固定资产管理相关功能
 * @author chen
 * @date 2023/6/15
 */
public interface AssetManagementService {
    
    /**
     * 获取固定资产汇总信息
     * @return 固定资产汇总数据
     */
    Map<String, Object> getFixedAssetsSummary();
    
    /**
     * 计算资产折旧
     * @param period 期间
     * @return 结果
     */
    int calculateDepreciation(String period);
    
    /**
     * 记录资产处置
     * @param assetId 资产ID
     * @param disposalAmount 处置金额
     * @return 结果
     */
    int recordAssetDisposal(Long assetId, Double disposalAmount);
    
    /**
     * 记录资产转移
     * @param assetId 资产ID
     * @param fromDeptId 原部门ID
     * @param toDeptId 目标部门ID
     * @return 结果
     */
    int recordAssetTransfer(Long assetId, Long fromDeptId, Long toDeptId);
}