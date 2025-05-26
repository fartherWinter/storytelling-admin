package com.chennian.storytelling.common.domain.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 规则树节点信息
 *
 * @author by chennian
 * @date 2023/4/25 11:36
 */
@Data
public class TreeNode {
    /**
     * 规则树ID
     */
    private Long treeId;
    /**
     * 规则树节点ID
     */
    private Long treeNodeId;
    /**
     * 节点类型；1子叶、2果实
     */
    private Integer nodeType;
    /**
     * 节点值[nodeType=2]；果实值
     */
    private String nodeValue;
    /**
     * 规则Key
     */
    private String ruleKey;
    /**
     * 规则描述
     */
    private String ruleDesc;
    /**
     * 节点链路
     */
    private List<TreeNodeLink> treeNodeLinkList;
}
