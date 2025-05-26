package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.bo.MallSearchRecordsSearchBo;
import com.chennian.storytelling.bean.dto.MallUserDto;
import com.chennian.storytelling.bean.model.mall.MallSearchRecords;
import com.chennian.storytelling.bean.param.UserInfoParam;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@RestController
@RequestMapping("/mall/user")
@Tag(name = "用户信息")
public class MallUserController {

    private final MallUserService mallUserService;

    public MallUserController(MallUserService mallUserService) {
        this.mallUserService = mallUserService;
    }

    /**
     * 查看用户接口
     */
    @PostMapping("/info")
    @Operation(summary = "查看用户信息", description = "根据用户ID（userId）获取用户信息")
    public ServerResponseEntity<MallUserDto> info(@RequestBody UserInfoParam userInfoParam) {
        MallUserDto userDto = mallUserService.info(userInfoParam);
        return ServerResponseEntity.success(userDto);
    }


    /**
     * 查看用户搜索记录
     */
    @PostMapping("/SearchRecords/page")
    @Operation(summary = "查看用户搜索记录")
    public ServerResponseEntity<IPage<MallSearchRecords>> getSearchRecordsPage(@RequestBody MallSearchRecordsSearchBo searchBo) {
        IPage<MallSearchRecords> mallSearchRecordsIPage = mallUserService.getSearchRecordsPage(searchBo);
        return ServerResponseEntity.success(mallSearchRecordsIPage);
    }
    /**
     * 新增用户搜索记录
     */
    @PostMapping("/getSearchRecords/add")
    @Operation(summary = "新增用户搜索记录")
    public ServerResponseEntity<Void> getSearchRecordsAdd(@RequestBody MallSearchRecords mallSearchRecords) {
        mallUserService.getSearchRecordsAdd(mallSearchRecords);
        return ServerResponseEntity.success();
    }

    /**
     * 删除用户搜索记录
     */
    @PostMapping("/getSearchRecords/remove")
    @Operation(summary = "删除用户搜索记录")
    public ServerResponseEntity<Void> getSearchRecordsRemove(@RequestBody MallSearchRecordsSearchBo searchBo) {
        mallUserService.getSearchRecordsRemove(searchBo);
        return ServerResponseEntity.success();
    }

    /**
     * 清空用户搜索记录
     */
    @PostMapping("/getSearchRecords/empty")
    @Operation(summary = "清空用户搜索记录")
    public ServerResponseEntity<Void> getSearchRecordsEmpty(@RequestBody MallSearchRecordsSearchBo searchBo) {
        mallUserService.getSearchRecordsEmpty(searchBo);
        return ServerResponseEntity.success();
    }
    
}
