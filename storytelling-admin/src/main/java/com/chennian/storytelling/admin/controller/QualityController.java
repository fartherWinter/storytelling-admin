package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.bean.model.quality.QualityImprovement;
import com.chennian.storytelling.bean.model.quality.QualityInspection;
import com.chennian.storytelling.bean.model.quality.QualityIssue;
import com.chennian.storytelling.bean.model.quality.QualityStandard;
import com.chennian.storytelling.bean.vo.QualityStatisticsVO;
import com.chennian.storytelling.bean.vo.QualityTrendVO;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.QualityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/quality")
@Tag(name = "质量管理")
public class QualityController {

    private final QualityService qualityService;

    public QualityController(QualityService qualityService) {
        this.qualityService = qualityService;
    }

    // ==================== 质量检验管理 ====================
    
    /**
     * 查询质量检验列表
     */
    @PostMapping("/inspection/page")
    @Operation(summary = "质量检验列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量检验列表")
    public ServerResponseEntity<IPage<QualityInspection>> inspectionPage(QualityInspection inspection, PageParam<QualityInspection> page) {
        IPage<QualityInspection> inspectionPage = qualityService.getInspectionList(inspection, page);
        return ServerResponseEntity.success(inspectionPage);
    }

    /**
     * 获取质量检验详细信息
     */
    @PostMapping("/inspection/info/{inspectionId}")
    @Operation(summary = "质量检验详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量检验详情")
    public ServerResponseEntity<QualityInspection> inspectionInfo(@PathVariable("inspectionId") Long inspectionId) {
        QualityInspection inspection = qualityService.getInspectionById(inspectionId);
        return ServerResponseEntity.success(inspection);
    }

    /**
     * 新增质量检验
     */
    @PostMapping("/inspection/add")
    @Operation(summary = "新增质量检验")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增质量检验")
    public ServerResponseEntity<Void> addInspection(@Validated @RequestBody QualityInspection inspection) {
        qualityService.addInspection(inspection);
        return ServerResponseEntity.success();
    }

    /**
     * 修改质量检验
     */
    @PostMapping("/inspection/edit")
    @Operation(summary = "修改质量检验")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改质量检验")
    public ServerResponseEntity<Void> editInspection(@Validated @RequestBody QualityInspection inspection) {
        qualityService.updateInspection(inspection);
        return ServerResponseEntity.success();
    }

    /**
     * 删除质量检验
     */
    @PostMapping("/inspection/remove/{inspectionIds}")
    @Operation(summary = "删除质量检验")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除质量检验")
    public ServerResponseEntity<Void> removeInspection(@PathVariable Long[] inspectionIds) {
        qualityService.deleteInspection(inspectionIds);
        return ServerResponseEntity.success();
    }

    /**
     * 批量检验
     */
    @PostMapping("/inspection/batch")
    @Operation(summary = "批量检验")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "批量检验")
    public ServerResponseEntity<Void> batchInspection(@RequestBody List<QualityInspection> inspections) {
        qualityService.batchInspection(inspections);
        return ServerResponseEntity.success();
    }

    // ==================== 质量标准管理 ====================
    
    /**
     * 查询质量标准列表
     */
    @PostMapping("/standard/page")
    @Operation(summary = "质量标准列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量标准列表")
    public ServerResponseEntity<IPage<QualityStandard>> standardPage(QualityStandard standard, PageParam<QualityStandard> page) {
        IPage<QualityStandard> standardPage = qualityService.getStandardList(standard, page);
        return ServerResponseEntity.success(standardPage);
    }

    /**
     * 获取质量标准详细信息
     */
    @PostMapping("/standard/info/{standardId}")
    @Operation(summary = "质量标准详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量标准详情")
    public ServerResponseEntity<QualityStandard> standardInfo(@PathVariable("standardId") Long standardId) {
        QualityStandard standard = qualityService.getStandardById(standardId);
        return ServerResponseEntity.success(standard);
    }

    /**
     * 新增质量标准
     */
    @PostMapping("/standard/add")
    @Operation(summary = "新增质量标准")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增质量标准")
    public ServerResponseEntity<Void> addStandard(@Validated @RequestBody QualityStandard standard) {
        qualityService.addStandard(standard);
        return ServerResponseEntity.success();
    }

    /**
     * 修改质量标准
     */
    @PostMapping("/standard/edit")
    @Operation(summary = "修改质量标准")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改质量标准")
    public ServerResponseEntity<Void> editStandard(@Validated @RequestBody QualityStandard standard) {
        qualityService.updateStandard(standard);
        return ServerResponseEntity.success();
    }

    /**
     * 删除质量标准
     */
    @PostMapping("/standard/remove/{standardIds}")
    @Operation(summary = "删除质量标准")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除质量标准")
    public ServerResponseEntity<Void> removeStandard(@PathVariable Long[] standardIds) {
        qualityService.deleteStandard(standardIds);
        return ServerResponseEntity.success();
    }

    /**
     * 根据产品获取质量标准
     */
    @GetMapping("/standard/product/{productId}")
    @Operation(summary = "产品质量标准")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品质量标准")
    public ServerResponseEntity<List<QualityStandard>> getStandardByProduct(@PathVariable Long productId) {
        List<QualityStandard> standards = qualityService.getStandardsByProductId(productId);
        return ServerResponseEntity.success(standards);
    }

    // ==================== 质量问题管理 ====================
    
    /**
     * 查询质量问题列表
     */
    @PostMapping("/issue/page")
    @Operation(summary = "质量问题列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量问题列表")
    public ServerResponseEntity<IPage<QualityIssue>> issuePage(QualityIssue issue, PageParam<QualityIssue> page) {
        IPage<QualityIssue> issuePage = qualityService.getIssueList(page, issue);
        return ServerResponseEntity.success(issuePage);
    }

    /**
     * 获取质量问题详细信息
     */
    @PostMapping("/issue/info/{issueId}")
    @Operation(summary = "质量问题详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量问题详情")
    public ServerResponseEntity<QualityIssue> issueInfo(@PathVariable("issueId") Long issueId) {
        QualityIssue issue = qualityService.getIssueById(issueId);
        return ServerResponseEntity.success(issue);
    }

    /**
     * 新增质量问题
     */
    @PostMapping("/issue/add")
    @Operation(summary = "新增质量问题")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增质量问题")
    public ServerResponseEntity<Void> addIssue(@Validated @RequestBody QualityIssue issue) {
        qualityService.addIssue(issue);
        return ServerResponseEntity.success();
    }

    /**
     * 修改质量问题
     */
    @PostMapping("/issue/edit")
    @Operation(summary = "修改质量问题")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改质量问题")
    public ServerResponseEntity<Void> editIssue(@Validated @RequestBody QualityIssue issue) {
        qualityService.updateIssue(issue);
        return ServerResponseEntity.success();
    }

    /**
     * 删除质量问题
     */
    @PostMapping("/issue/remove/{issueIds}")
    @Operation(summary = "删除质量问题")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除质量问题")
    public ServerResponseEntity<Void> removeIssue(@PathVariable Long[] issueIds) {
        qualityService.deleteIssue(issueIds);
        return ServerResponseEntity.success();
    }

    /**
     * 质量问题处理
     */
    @PostMapping("/issue/handle")
    @Operation(summary = "质量问题处理")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "质量问题处理")
    public ServerResponseEntity<Void> handleIssue(@RequestParam Long issueId, 
                                                 @RequestParam String handleResult,
                                                 @RequestParam(required = false) String remark) {
        qualityService.assignIssue(issueId, handleResult, remark);
        return ServerResponseEntity.success();
    }

    // ==================== 质量改进管理 ====================
    
    /**
     * 查询质量改进列表
     */
    @PostMapping("/improvement/page")
    @Operation(summary = "质量改进列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量改进列表")
    public ServerResponseEntity<IPage<QualityImprovement>> improvementPage(QualityImprovement improvement, PageParam<QualityImprovement> page) {
        IPage<QualityImprovement> improvementPage = qualityService.getImprovementList(page, improvement);
        return ServerResponseEntity.success(improvementPage);
    }

    /**
     * 新增质量改进
     */
    @PostMapping("/improvement/add")
    @Operation(summary = "新增质量改进")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增质量改进")
    public ServerResponseEntity<Void> addImprovement(@Validated @RequestBody QualityImprovement improvement) {
        qualityService.addImprovement(improvement);
        return ServerResponseEntity.success();
    }

    /**
     * 修改质量改进
     */
    @PostMapping("/improvement/edit")
    @Operation(summary = "修改质量改进")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改质量改进")
    public ServerResponseEntity<Void> editImprovement(@Validated @RequestBody QualityImprovement improvement) {
        qualityService.updateImprovement(improvement);
        return ServerResponseEntity.success();
    }

    /**
     * 质量改进执行
     */
    @PostMapping("/improvement/execute")
    @Operation(summary = "质量改进执行")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "质量改进执行")
    public ServerResponseEntity<Void> executeImprovement(@RequestParam Long improvementId) {
        qualityService.executeImprovement(improvementId);
        return ServerResponseEntity.success();
    }

    /**
     * 质量改进执行
     */
    @PostMapping("/improvement/evaluate")
    @Operation(summary = "质量改进执行")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "质量改进执行")
    public ServerResponseEntity<Void> executeImprovement(@RequestParam Long improvementId,
                                                         @RequestParam String executeResult,
                                                         @RequestParam(required = false) String remark) {
        qualityService.evaluateImprovement(improvementId, executeResult, remark);
        return ServerResponseEntity.success();
    }

    // ==================== 质量统计分析 ====================
    
    /**
     * 质量统计
     */
    @GetMapping("/statistics")
    @Operation(summary = "质量统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量统计")
    public ServerResponseEntity<QualityStatisticsVO> statistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long productId) {
        QualityStatisticsVO statistics = qualityService.getQualityStatistics(startDate, endDate, productId);
        return ServerResponseEntity.success(statistics);
    }

    /**
     * 质量趋势分析
     */
    @GetMapping("/trend")
    @Operation(summary = "质量趋势分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量趋势分析")
    public ServerResponseEntity<QualityTrendVO> trend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String analysisType) {
        QualityTrendVO trend = qualityService.getQualityTrendAnalysis(startDate, endDate, analysisType);
        return ServerResponseEntity.success(trend);
    }

    /**
     * 质量合格率分析
     */
    @GetMapping("/pass-rate")
    @Operation(summary = "质量合格率分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量合格率分析")
    public ServerResponseEntity<Map<String, Object>> passRateAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long supplierId) {
        Map<String, Object> result = qualityService.getPassRateAnalysis(startDate, endDate, productId, supplierId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 导出质量报表
     */
    @GetMapping("/export")
    @Operation(summary = "导出质量报表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.EXPORT, operatorType = OperatorType.MANAGE, description = "导出质量报表")
    public ServerResponseEntity<Map<String, Object>> exportQuality(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam String reportType) {
        Map<String, Object> result = qualityService.exportQualityReport(startDate, endDate, productId, reportType);
        return ServerResponseEntity.success(result);
    }
}