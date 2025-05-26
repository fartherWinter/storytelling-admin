package com.chennian.storytelling.bean.bo;

import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.utils.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
@Schema(description = "用户搜索实体")
public class SysUserSearchBo {
    @Schema(description = "用户名称")
    private String userName;
    @Schema(description = "手机号码")
    private String phoneNumber;
    @Schema(description = "用户状态")
    private String status;
    @Schema(description = "开始时间")
    private String startTime;
    @Schema(description = "结束")
    private String endTime;
    private PageParam<SysUser> pageParam;
}
