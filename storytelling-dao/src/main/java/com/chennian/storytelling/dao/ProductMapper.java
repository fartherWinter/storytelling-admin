package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品数据访问层
 * @author chennian
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    /**
     * 更新产品状态
     * 
     * @param productId 产品ID
     * @param status 状态
     * @return 结果
     */
    int updateStatus(@Param("productId") Long productId, @Param("status") String status);
}