package com.chennian.storytelling.common.combination.service.engine;

import com.chennian.storytelling.common.combination.service.logic.LogicFilter;

import java.util.Map;

/**
 * 决策节点配置
 *
 * @author by chennian
 * @date 2023/4/25 13:29
 */
public class EngineConfig {
    static Map<String, LogicFilter> logicFilterMap;

//    static {
//        logicFilterMap = new ConcurrentHashMap<>();
//        logicFilterMap.put("userAge", new UserAgeFilter());
//        logicFilterMap.put("userGender", new UserGenderFilter());
//    }

    public Map<String, LogicFilter> getLogicFilterMap() {
        return logicFilterMap;
    }

    public void setLogicFilterMap(Map<String, LogicFilter> logicFilterMap) {
        EngineConfig.logicFilterMap = logicFilterMap;
    }

}
