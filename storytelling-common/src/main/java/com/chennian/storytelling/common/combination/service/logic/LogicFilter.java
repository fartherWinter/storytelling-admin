package com.chennian.storytelling.common.combination.service.logic;

import com.chennian.storytelling.common.domain.model.vo.TreeNodeLink;

import java.util.List;
import java.util.Map;

/**
 * 定义适配的通用接口，逻辑决策器，获取决策值
 * 用于提供决策能力的节点实现该接口
 *
 * @author by chennian
 * @date 2023/4/25 11:39
 */
public interface LogicFilter {
    /**
     * 逻辑决策器
     *
     * @param matterValue          决策值
     * @param treeNodeLineInfoList 决策节点
     * @return 下一个节点Id
     */
    Long filter(String matterValue, List<TreeNodeLink> treeNodeLineInfoList);

    /**
     * 获取决策值
     *
     * @param decisionMatter 决策物料
     * @return 决策值
     */
    String matterValue(Long treeId, String userId, Map<String, String> decisionMatter);
}
