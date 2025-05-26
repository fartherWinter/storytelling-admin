package com.chennian.storytelling.common.combination.service.engine;

import com.chennian.storytelling.common.domain.model.aggregates.TreeRich;
import com.chennian.storytelling.common.domain.model.vo.EngineResult;

import java.util.Map;

/**
 * 决策引擎接口
 *
 * @author by chennian
 * @date 2023/4/25 11:55
 */
public interface IEngine {
    EngineResult process(final Long treeId, final String userId, TreeRich treeRich, final Map<String, String> decisionMatter);
}
