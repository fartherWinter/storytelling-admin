package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.AccountChart;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;

/**
 * 会计科目管理服务接口
 * @author chen
 * @date 2023/6/15
 */
public interface AccountChartService {
    
    /**
     * 分页查询会计科目
     * @param page 分页参数
     * @param accountChart 查询条件
     * @return 会计科目分页数据
     */
    IPage<AccountChart> findAccountChartByPage(PageParam<AccountChart> page, AccountChart accountChart);
    
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
     * 查询会计科目树结构
     * @return 会计科目树结构列表
     */
    List<AccountChart> selectAccountChartTree();
}