package com.chennian.storytelling.bean.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
@Schema(description = "岗位搜索实体")
public class SysPostSearchBo {
    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "岗位状态")
    private String status;
}
