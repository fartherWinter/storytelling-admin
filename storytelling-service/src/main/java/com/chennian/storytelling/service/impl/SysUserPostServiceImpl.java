package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.SysUserPost;
import com.chennian.storytelling.service.SysUserPostService;
import com.chennian.storytelling.dao.SysUserPostMapper;
import org.springframework.stereotype.Service;

/**
* @author chen
* @description 针对表【sys_user_post(用户与岗位关联表)】的数据库操作Service实现
* @createDate 2025-05-06 19:12:42
*/
@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost>
    implements SysUserPostService{

}




