package com.chennian.storytelling.bean.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
public class SysRolePartitionBo {
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    /**
     * 分区组
     */
    @NotNull(message = "分区ID不能为空")
    private Long[] partitionIds;
}
