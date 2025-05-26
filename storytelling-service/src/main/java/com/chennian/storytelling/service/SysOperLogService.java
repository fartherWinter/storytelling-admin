package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author chen
* @createDate 2025-05-06 20:07:34
*/
public interface SysOperLogService extends IService<SysOperLog> {

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(SysOperLog operLog);
}
