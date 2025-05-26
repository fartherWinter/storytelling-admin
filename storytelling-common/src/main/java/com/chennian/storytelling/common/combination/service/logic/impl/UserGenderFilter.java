package com.chennian.storytelling.common.combination.service.logic.impl;

import com.chennian.storytelling.common.combination.service.logic.BaseLogic;

import java.util.Map;

/**
 * @author by chennian
 * @date 2023/4/25 13:35
 */
public class UserGenderFilter extends BaseLogic {
    @Override
    public String matterValue(Long treeId, String userId, Map<String, String> decisionMatter) {
        return decisionMatter.get("gender");
    }
}
