package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallFreightRule;
import com.chennian.storytelling.bean.vo.MallFreightRuleVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 物流费用规则Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallFreightRuleMapper extends BaseMapper<MallFreightRule> {

    /**
     * 查询指定区域和配送方式的有效运费规则
     *
     * @param areaCode 区域编码
     * @param deliveryMethodId 配送方式ID
     * @param currentTime 当前时间
     * @return 运费规则列表
     */
    List<MallFreightRuleVO> selectValidRules(
            @Param("areaCode") String areaCode,
            @Param("deliveryMethodId") Long deliveryMethodId,
            @Param("currentTime") LocalDateTime currentTime
    );

    /**
     * 批量插入运费规则
     *
     * @param rules 运费规则列表
     * @return 插入数量
     */
    int batchInsert(@Param("rules") List<MallFreightRule> rules);

    /**
     * 更新规则状态
     *
     * @param ruleIds 规则ID列表
     * @param status 状态
     * @param operator 操作人
     * @return 更新数量
     */
    int updateRuleStatus(
            @Param("ruleIds") List<Long> ruleIds,
            @Param("status") Integer status,
            @Param("operator") String operator
    );

    /**
     * 查询规则变更历史
     *
     * @param ruleId 规则ID
     * @return 变更历史
     */
    List<MallFreightRuleVO.RuleChangeHistory> selectRuleHistory(@Param("ruleId") Long ruleId);

    /**
     * 获取区域运费统计信息
     *
     * @param areaCode 区域编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计信息
     */
    Map<String, Object> selectFreightStats(
            @Param("areaCode") String areaCode,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 查询特定条件下的最优规则
     *
     * @param params 查询参数
     * @return 最优规则
     */
    MallFreightRuleVO selectOptimalRule(@Param("params") Map<String, Object> params);

    /**
     * 复制规则到新区域
     *
     * @param sourceRuleId 源规则ID
     * @param targetAreaCode 目标区域编码
     * @param operator 操作人
     * @return 新规则ID
     */
    Long copyRuleToArea(
            @Param("sourceRuleId") Long sourceRuleId,
            @Param("targetAreaCode") String targetAreaCode,
            @Param("operator") String operator
    );

    /**
     * 检查规则冲突
     *
     * @param rule 待检查的规则
     * @return 冲突的规则列表
     */
    List<MallFreightRule> checkRuleConflicts(@Param("rule") MallFreightRule rule);

    /**
     * 获取规则计算示例
     *
     * @param ruleId 规则ID
     * @return 计算示例列表
     */
    List<MallFreightRuleVO.FreightCalculationExample> selectCalculationExamples(@Param("ruleId") Long ruleId);

    /**
     * 更新规则优先级
     *
     * @param ruleId 规则ID
     * @param priority 新优先级
     * @param operator 操作人
     * @return 更新结果
     */
    int updateRulePriority(
            @Param("ruleId") Long ruleId,
            @Param("priority") Integer priority,
            @Param("operator") String operator
    );

    /**
     * 获取规则覆盖的区域列表
     *
     * @param ruleId 规则ID
     * @return 区域列表
     */
    List<Map<String, Object>> selectRuleCoverageAreas(@Param("ruleId") Long ruleId);
} 