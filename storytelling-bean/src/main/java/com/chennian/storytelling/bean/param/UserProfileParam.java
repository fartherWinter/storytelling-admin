package com.chennian.storytelling.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@Data
@Schema(description = "用户信息")
public class UserProfileParam {
    @Schema(description = "用户名称")
    private String userName;
    @Schema(description = "手机号码")
    private String phoneNumber;
    @Schema(description = "邮箱")
    private String email;
}
