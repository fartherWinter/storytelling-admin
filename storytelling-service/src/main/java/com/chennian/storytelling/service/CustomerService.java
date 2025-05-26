package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.Customer;
import com.chennian.storytelling.bean.vo.CustomerFollowupVO;
import com.chennian.storytelling.bean.vo.CustomerStatisticsVO;
import com.chennian.storytelling.bean.vo.MobileCustomerVO;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 客户服务接口
 * @author chennian
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 分页查询客户列表
     * @param page 分页参数
     * @param customer 查询条件
     * @return 客户分页数据
     */
    IPage<Customer> findByPage(PageParam<Customer> page, Customer customer);

    /**
     * 根据ID查询客户
     * @param customerId 客户ID
     * @return 客户信息
     */
    Customer selectCustomerById(Long customerId);

    /**
     * 新增客户
     * @param customer 客户信息
     * @return 结果
     */
    int insertCustomer(Customer customer);

    /**
     * 修改客户
     * @param customer 客户信息
     * @return 结果
     */
    int updateCustomer(Customer customer);

    /**
     * 批量删除客户
     * @param customerIds 需要删除的客户ID数组
     * @return 结果
     */
    int deleteCustomerByIds(Long[] customerIds);

    /**
     * 更新客户状态
     * @param customer 客户信息
     * @return 结果
     */
    int updateCustomerStatus(Customer customer);
    
    /**
     * 获取客户统计数据
     * @return 统计数据
     */
    CustomerStatisticsVO getCustomerStatistics();
    
    /**
     * 获取客户分布数据
     * @return 客户分布数据
     */
    Map<String, Object> getCustomerDistribution();
    
    /**
     * 获取客户跟进记录
     * @param customerId 客户ID
     * @return 跟进记录列表
     */
    List<CustomerFollowupVO> getFollowupRecords(Long customerId);
    
    /**
     * 添加客户跟进记录
     * @param followup 跟进信息
     * @return 结果
     */
    int addFollowupRecord(Map<String, Object> followup);
    
    /**
     * 根据条件查询客户列表
     * @param customer 查询条件
     * @return 客户列表
     */
    List<Customer> selectCustomerList(Customer customer);
    
    /**
     * 获取移动端客户列表
     * @param params 查询参数，包含keyword、page、size
     * @return 移动端客户列表
     */
    List<MobileCustomerVO> getMobileCustomerList(Map<String, Object> params);
    
    /**
     * 获取移动端客户详情
     * @param customerId 客户ID
     * @return 移动端客户详情
     */
    MobileCustomerVO getMobileCustomerDetail(Long customerId);
    
    /**
     * 移动端快速添加客户
     * @param customerVO 客户信息
     * @return 客户ID
     */
    Long quickAddCustomer(MobileCustomerVO customerVO);
    
    /**
     * 获取最近的客户活动记录
     * @param limit 获取记录的数量限制
     * @return 客户活动记录列表
     */
    List<Map<String, Object>> getRecentActivities(int limit);

    Integer getActiveCustomerCount();

    Object getCustomerOrderStatus(Long customerId);

    Object getProductRecommendations(Long customerId);

    Object getCollaborationMessages(Long customerId);

    void sendCollaborationMessage(Long customerId, Map<String, Object> message);
}