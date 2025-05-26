package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.finance.AccountChart;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.AccountChartMapper;
import com.chennian.storytelling.service.AccountChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会计科目管理服务实现类
 * @author chen
 * @date 2023/6/15
 */
@Service
public class AccountChartServiceImpl implements AccountChartService {

    @Autowired
    private AccountChartMapper accountChartMapper;

    @Override
    public IPage<AccountChart> findAccountChartByPage(PageParam<AccountChart> page, AccountChart accountChart) {
        return accountChartMapper.selectAccountChartPage(page, accountChart);
    }

    @Override
    public AccountChart selectAccountChartById(Long accountId) {
        return accountChartMapper.selectAccountChartById(accountId);
    }

    @Override
    public int insertAccountChart(AccountChart accountChart) {
        return accountChartMapper.insertAccountChart(accountChart);
    }

    @Override
    public int updateAccountChart(AccountChart accountChart) {
        return accountChartMapper.updateAccountChart(accountChart);
    }

    @Override
    public int deleteAccountChartByIds(Long[] accountIds) {
        return accountChartMapper.deleteAccountChartByIds(accountIds);
    }

    @Override
    public List<AccountChart> selectAccountChartTree() {
        // 获取所有会计科目列表
        List<AccountChart> allAccountCharts = accountChartMapper.selectAccountChartList();
        
        // 构建树形结构
        // 1. 找出所有顶级节点（parentId为null或0的节点）
        List<AccountChart> rootNodes = allAccountCharts.stream()
                .filter(chart -> chart.getParentId() == null || chart.getParentId() == 0)
                .collect(Collectors.toList());
        
        // 2. 递归构建子树
        for (AccountChart rootNode : rootNodes) {
            buildChildrenTree(rootNode, allAccountCharts);
        }
        
        return rootNodes;
    }
    
    /**
     * 递归构建会计科目树结构
     * @param parent 父节点
     * @param allAccountCharts 所有会计科目列表
     */
    private void buildChildrenTree(AccountChart parent, List<AccountChart> allAccountCharts) {
        // 查找当前节点的所有子节点
        List<AccountChart> children = allAccountCharts.stream()
                .filter(chart -> parent.getAccountId().equals(chart.getParentId()))
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            // 设置子节点列表
            parent.setChildren(children);
            // 递归处理每个子节点
            for (AccountChart child : children) {
                buildChildrenTree(child, allAccountCharts);
            }
        } else {
            // 没有子节点，设置空列表
            parent.setChildren(new ArrayList<>());
        }
    }
}