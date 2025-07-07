package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商城购物车Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallCartMapper extends BaseMapper<MallCart> {
    
}