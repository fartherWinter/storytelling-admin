package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商城商品Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductMapper extends BaseMapper<MallProduct> {
    
}