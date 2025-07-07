package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.SupplyChainPlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应链协同计划Mapper接口
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Mapper
public interface SupplyChainPlanMapper extends BaseMapper<SupplyChainPlan> {
}