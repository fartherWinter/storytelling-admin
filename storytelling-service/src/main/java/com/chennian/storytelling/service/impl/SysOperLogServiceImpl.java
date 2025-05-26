package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.SysOperLog;
import com.chennian.storytelling.service.SysOperLogService;
import com.chennian.storytelling.dao.SysOperLogMapper;
import org.springframework.stereotype.Service;

/**
* @author chen
* @createDate 2025-05-06 20:07:34
*/
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
    implements SysOperLogService{

    private final SysOperLogMapper operLogMapper;

    public SysOperLogServiceImpl(SysOperLogMapper operLogMapper) {
        this.operLogMapper = operLogMapper;
    }

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog) {
        operLogMapper.insertOperlog(operLog);
    }
}




