package com.chennian.storytelling.service.mall;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.bo.MallSearchRecordsSearchBo;
import com.chennian.storytelling.bean.dto.MallUserDto;
import com.chennian.storytelling.bean.model.mall.MallSearchRecords;
import com.chennian.storytelling.bean.model.mall.MallUser;
import com.chennian.storytelling.bean.param.UserInfoParam;

import java.math.BigDecimal;

/**
 * @author chen
 * @date 2025/05/20
 */
public interface MallUserService extends IService<MallUser> {
    /**
     * 获取用户信息数据
     *
     * @return
     */
    MallUserDto info(UserInfoParam userId);



    /**
     * 搜索记录查询
     *
     * @return
     */
    IPage<MallSearchRecords> getSearchRecordsPage(MallSearchRecordsSearchBo searchBo);

    /**
     * 新增用户搜索记录
     *
     */
    void getSearchRecordsAdd(MallSearchRecords mallSearchRecords);

    /**
     * 删除用户搜索记录
     *
     */
    void getSearchRecordsRemove(MallSearchRecordsSearchBo searchBo);

    /**
     * 清空用户搜索记录
     *
     */
    void getSearchRecordsEmpty(MallSearchRecordsSearchBo searchBo);

}