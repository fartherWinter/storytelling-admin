package com.chennian.storytelling.common.combination.service.engine;

import com.chennian.storytelling.common.combination.service.logic.LogicFilter;
import com.chennian.storytelling.common.domain.model.aggregates.TreeRich;
import com.chennian.storytelling.common.domain.model.vo.EngineResult;
import com.chennian.storytelling.common.domain.model.vo.TreeNode;
import com.chennian.storytelling.common.domain.model.vo.TreeRoot;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 基础决策引擎功能
 *
 * @author by chennian
 * @date 2023/4/25 13:31
 */
@Slf4j
public abstract class EngineBase extends EngineConfig implements IEngine{
    @Override
    public abstract EngineResult process(Long treeId, String userId, TreeRich treeRich, Map<String, String> decisionMatter);

    protected TreeNode engineDecisionMaker(TreeRich treeRich, Long treeId, String userId, Map<String, String> decisionMatter) {
        TreeRoot treeRoot = treeRich.getTreeRoot();
        Map<Long, TreeNode> treeNodeMap = treeRich.getTreeNodeMap();
        // 规则树根ID
        Long rootNodeId = treeRoot.getTreeRootNodeId();
        TreeNode treeNodeInfo = treeNodeMap.get(rootNodeId);
        //节点类型[NodeType]；1子叶、2果实
        while (treeNodeInfo.getNodeType().equals(1)) {
            String ruleKey = treeNodeInfo.getRuleKey();
            LogicFilter logicFilter = logicFilterMap.get(ruleKey);
            String matterValue = logicFilter.matterValue(treeId, userId, decisionMatter);
            Long nextNode = logicFilter.filter(matterValue, treeNodeInfo.getTreeNodeLinkList());
            treeNodeInfo = treeNodeMap.get(nextNode);
            log.info("决策树引擎=>{} userId：{} treeId：{} treeNode：{} ruleKey：{} matterValue：{}", treeRoot.getTreeName(), userId, treeId, treeNodeInfo.getTreeNodeId(), ruleKey, matterValue);
        }
        return treeNodeInfo;
    }
}
