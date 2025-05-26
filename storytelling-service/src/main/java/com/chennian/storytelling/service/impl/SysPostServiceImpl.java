package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.SysPost;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.SysUserPostMapper;
import com.chennian.storytelling.service.SysPostService;
import com.chennian.storytelling.dao.SysPostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author chen
* @description 针对表【sys_post(岗位信息表)】的数据库操作Service实现
* @createDate 2025-05-06 19:12:12
*/
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost>
    implements SysPostService{

    private SysPostMapper postMapper;
    private SysUserPostMapper sysUserPostMapper;
    public SysPostServiceImpl(SysPostMapper postMapper, SysUserPostMapper sysUserPostMapper) {
        this.postMapper = postMapper;
        this.sysUserPostMapper = sysUserPostMapper;
    }
    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }
    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return postMapper.selectPostById(postId);
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return null;
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return
     */
    @Override
    public boolean checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return sysUserPostMapper.countUserPostById(postId);
    }

    @Override
    public int deletePostById(Long postId) {
        return 0;
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new StorytellingBindException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return postMapper.deletePostByIds(postIds);
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return
     */
    @Override
    public int insertPost(SysPost post) {
        return postMapper.insertPost(post);
    }

    @Override
    public int updatePost(SysPost post) {
        return postMapper.updatePost(post);
    }

    /**
     * 修改岗位状态
     */
    @Override
    public int changeStatus(SysPost post) {
        return postMapper.quickEnable(post.getPostId(), post.getStatus());
    }
}




