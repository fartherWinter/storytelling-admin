package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Customer;
import com.chennian.storytelling.bean.vo.CustomerFollowupVO;
import com.chennian.storytelling.bean.vo.CustomerStatisticsVO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/customer")
@Tag(name = "客户管理")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 查询客户列表
     */
    @PostMapping("/page")
    @Operation(summary = "客户列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "客户列表")
    public ServerResponseEntity<IPage<Customer>> page(Customer customer, PageParam<Customer> page) {
        IPage<Customer> customerPage = customerService.findByPage(page, customer);
        return ServerResponseEntity.success(customerPage);
    }

    /**
     * 获取客户详细信息
     */
    @PostMapping("/info/{customerId}")
    @Operation(summary = "客户详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "客户详情")
    public ServerResponseEntity<Customer> info(@PathVariable("customerId") Long customerId) {
        Customer customer = customerService.selectCustomerById(customerId);
        return ServerResponseEntity.success(customer);
    }

    /**
     * 新增客户
     */
    @PostMapping("/add")
    @Operation(summary = "新增客户")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增客户")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody Customer customer) {
        return ServerResponseEntity.success(customerService.insertCustomer(customer));
    }

    /**
     * 修改客户
     */
    @PostMapping("/update")
    @Operation(summary = "修改客户")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改客户")
    public ServerResponseEntity<Integer> update(@RequestBody Customer customer) {
        return ServerResponseEntity.success(customerService.updateCustomer(customer));
    }

    /**
     * 删除客户
     */
    @PostMapping("/remove/{customerIds}")
    @Operation(summary = "删除客户")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除客户")
    public ServerResponseEntity<Integer> remove(@PathVariable("customerIds") Long[] customerIds) {
        return ServerResponseEntity.success(customerService.deleteCustomerByIds(customerIds));
    }

    /**
     * 客户状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "客户状态修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "客户状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody Customer customer) {
        return ServerResponseEntity.success(customerService.updateCustomerStatus(customer));
    }

    /**
     * 客户统计分析
     */
    @PostMapping("/statistics")
    @Operation(summary = "客户统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "客户统计")
    public ServerResponseEntity<CustomerStatisticsVO> statistics() {
        CustomerStatisticsVO statistics = customerService.getCustomerStatistics();
        return ServerResponseEntity.success(statistics);
    }

    /**
     * 客户跟进记录
     */
    @PostMapping("/followup/{customerId}")
    @Operation(summary = "客户跟进记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "客户跟进记录")
    public ServerResponseEntity<List<CustomerFollowupVO>> followupRecords(@PathVariable("customerId") Long customerId) {
        List<CustomerFollowupVO> records = customerService.getFollowupRecords(customerId);
        return ServerResponseEntity.success(records);
    }

    /**
     * 添加客户跟进记录
     */
    @PostMapping("/followup/add")
    @Operation(summary = "添加跟进记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "添加跟进记录")
    public ServerResponseEntity<Integer> addFollowup(@RequestBody Map<String, Object> followup) {
        // 此处需要实现客户跟进服务
         return ServerResponseEntity.success(customerService.addFollowupRecord(followup));
    }
}