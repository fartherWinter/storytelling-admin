package com.chennian.storytelling.common.domain.model.vo;

import lombok.Data;

/**
 * 树根信息
 *
 * @author by chennian
 * @date 2023/4/25 11:37
 */
@Data
public class TreeRoot {
    /**
     * 规则树ID
     */
    private Long treeId;
    /**
     * 规则树根ID
     */
    private Long treeRootNodeId;
    /**
     * 规则树名称
     */
    private String treeName;
}
