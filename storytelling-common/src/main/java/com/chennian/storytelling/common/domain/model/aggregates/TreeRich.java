package com.chennian.storytelling.common.domain.model.aggregates;

import com.chennian.storytelling.common.domain.model.vo.TreeNode;
import com.chennian.storytelling.common.domain.model.vo.TreeRoot;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 规则树聚合
 *
 * @author by chennian
 * @date 2023/4/25 11:35
 */
@Data
@AllArgsConstructor
public class TreeRich {
    /**
     * 树根信息
     */
    private TreeRoot treeRoot;
    /**
     * 树节点ID -> 子节点
     */
    private Map<Long, TreeNode> treeNodeMap;

}
