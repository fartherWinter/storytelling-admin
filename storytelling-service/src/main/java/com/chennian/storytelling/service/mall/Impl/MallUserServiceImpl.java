package com.chennian.storytelling.service.mall.Impl;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.bo.MallSearchRecordsSearchBo;
import com.chennian.storytelling.bean.dto.MallUserDto;
import com.chennian.storytelling.bean.model.mall.MallSearchRecords;
import com.chennian.storytelling.bean.model.mall.MallUser;
import com.chennian.storytelling.bean.param.UserInfoParam;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.MallSearchRecordsMapper;
import com.chennian.storytelling.dao.MallUserMapper;
import com.chennian.storytelling.service.mall.MallUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Service
public class MallUserServiceImpl extends ServiceImpl<MallUserMapper, MallUser> implements MallUserService {
    private final MallSearchRecordsMapper mallSearchRecordsMapper;

    public MallUserServiceImpl(MallSearchRecordsMapper mallSearchRecordsMapper) {
        this.mallSearchRecordsMapper = mallSearchRecordsMapper;
    }

    /**
     * 获取用户详情数据
     *
     * @param userInfoParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallUserDto info(UserInfoParam userInfoParam) {
        MallUserDto userDto = new MallUserDto();
        return userDto;
    }


    /**
     * 搜索记录查询
     *
     * @param searchBo
     * @return
     */
    @Override
    public IPage<MallSearchRecords> getSearchRecordsPage(MallSearchRecordsSearchBo searchBo) {
        return mallSearchRecordsMapper.selectPage(searchBo.getPageParam(), new LambdaQueryWrapper<MallSearchRecords>()
                .eq(StringUtils.isNotBlank(searchBo.getMobile()), MallSearchRecords::getMobile, searchBo.getMobile())
                .orderByDesc(MallSearchRecords::getCreateTime)
        );
    }

    /**
     * 新增用户搜索记录
     *
     */
    @Override
    public void getSearchRecordsAdd(MallSearchRecords mallSearchRecords) {
        mallSearchRecords.setCreateTime(DateTime.now());
        try {
            mallSearchRecordsMapper.insert(mallSearchRecords);
        } catch (Exception e) {
            throw new StorytellingBindException("搜索记录插入失败");
        }
    }

    /**
     * 删除用户搜索记录
     *
     * @param searchBo
     */
    @Override
    public void getSearchRecordsRemove(MallSearchRecordsSearchBo searchBo) {
        mallSearchRecordsMapper.deleteById(searchBo.getId());
    }

    /**
     * 清空用户搜索记录
     *
     * @param searchBo
     */
    @Override
    public void getSearchRecordsEmpty(MallSearchRecordsSearchBo searchBo) {
        mallSearchRecordsMapper.delete(new LambdaQueryWrapper<MallSearchRecords>()
                .eq(MallSearchRecords::getMobile, searchBo.getMobile()));
    }
}