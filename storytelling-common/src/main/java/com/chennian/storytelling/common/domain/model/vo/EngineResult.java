package com.chennian.storytelling.common.domain.model.vo;

import lombok.Data;

/**
 * 决策结果
 *
 * @author by chennian
 * @date 2023/4/25 11:36
 */
@Data
public class EngineResult {
    /**
     * 执行结果
     */
    private boolean isSuccess;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 规则树id
     */
    private Long treeId;
    /**
     * 果实节点id
     */
    private Long nodeId;
    /**
     * 果实节点值
     */
    private String nodeValue;

    public EngineResult() {
    }

    /**
     * 引擎成功标记
     */
    public EngineResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * 引擎初始化
     */
    public EngineResult(String userId, Long treeId, Long nodeId, String nodeValue) {
        this.isSuccess = true;
        this.userId = userId;
        this.treeId = treeId;
        this.nodeId = nodeId;
        this.nodeValue = nodeValue;
    }
}
