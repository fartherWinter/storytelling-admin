package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.model.SysOperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author chen
*
* @createDate 2025-05-06 20:07:34
*
*/
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(SysOperLog operLog);
}




