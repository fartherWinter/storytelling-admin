package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户数据访问层
 * @author chennian
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    
}