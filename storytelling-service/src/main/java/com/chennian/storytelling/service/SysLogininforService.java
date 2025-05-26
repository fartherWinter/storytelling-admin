package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.SysLogininfor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author chen

* @createDate 2025-05-06 20:05:19
*/

public interface SysLogininforService extends IService<SysLogininfor> {

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(SysLogininfor logininfor);

}
