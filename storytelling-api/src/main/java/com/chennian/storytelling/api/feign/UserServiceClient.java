package com.chennian.storytelling.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.bo.MallSearchRecordsSearchBo;
import com.chennian.storytelling.bean.dto.MallUserDto;
import com.chennian.storytelling.bean.model.mall.MallSearchRecords;
import com.chennian.storytelling.bean.param.UserInfoParam;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务Feign客户端
 * 调用 storytelling-user-service 微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@FeignClient(name = "storytelling-user-service", path = "/user")
public interface UserServiceClient {

    /**
     * 查看用户信息
     */
    @PostMapping("/info")
    ServerResponseEntity<MallUserDto> info(@RequestBody UserInfoParam userInfoParam);

    /**
     * 查看用户搜索记录
     */
    @PostMapping("/SearchRecords/page")
    ServerResponseEntity<IPage<MallSearchRecords>> getSearchRecordsPage(@RequestBody MallSearchRecordsSearchBo searchBo);

    /**
     * 新增用户搜索记录
     */
    @PostMapping("/getSearchRecords/add")
    ServerResponseEntity<Void> getSearchRecordsAdd(@RequestBody MallSearchRecords mallSearchRecords);

    /**
     * 删除用户搜索记录
     */
    @PostMapping("/getSearchRecords/remove")
    ServerResponseEntity<Void> getSearchRecordsRemove(@RequestBody MallSearchRecordsSearchBo searchBo);

    /**
     * 清空用户搜索记录
     */
    @PostMapping("/getSearchRecords/empty")
    ServerResponseEntity<Void> getSearchRecordsEmpty(@RequestBody MallSearchRecordsSearchBo searchBo);
}