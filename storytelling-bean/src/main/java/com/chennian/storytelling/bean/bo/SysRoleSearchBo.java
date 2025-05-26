package com.chennian.storytelling.bean.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
@Schema(description = "角色")
public class SysRoleSearchBo {
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "权限字符")
    private String roleKey;
    @Schema(description = "状态")
    private String status;
    @Schema(description = "开始时间")
    private String startTime;
    @Schema(description = "结束")
    private String endTime;
}
