package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.AccountChart;
import com.chennian.storytelling.common.utils.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会计科目Mapper接口
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface AccountChartMapper extends BaseMapper<AccountChart> {
    
    /**
     * 分页查询会计科目
     * @param page 分页参数
     * @param accountChart 查询条件
     * @return 会计科目分页数据
     */
    IPage<AccountChart> selectAccountChartPage(PageParam<AccountChart> page, @Param("accountChart") AccountChart accountChart);
    
    /**
     * 根据ID查询会计科目
     * @param accountId 会计科目ID
     * @return 会计科目信息
     */
    AccountChart selectAccountChartById(Long accountId);
    
    /**
     * 新增会计科目
     * @param accountChart 会计科目信息
     * @return 结果
     */
    int insertAccountChart(AccountChart accountChart);
    
    /**
     * 修改会计科目
     * @param accountChart 会计科目信息
     * @return 结果
     */
    int updateAccountChart(AccountChart accountChart);
    
    /**
     * 批量删除会计科目
     * @param accountIds 需要删除的会计科目ID数组
     * @return 结果
     */
    int deleteAccountChartByIds(Long[] accountIds);
    
    /**
     * 查询所有会计科目
     * @return 会计科目列表
     */
    List<AccountChart> selectAccountChartList();
    
    /**
     * 查询子会计科目
     * @param parentId 父级会计科目ID
     * @return 子会计科目列表
     */
    List<AccountChart> selectChildrenAccountChart(@Param("parentId") Long parentId);
}