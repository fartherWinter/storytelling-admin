package com.chennian.storytelling.bean.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Data
@Schema(description = "设置用户信息")
public class UserInfoParam {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户手机")
    private String mobile;
    @Schema(description = "用户昵称")
    private String nickName;
    @Schema(description = "用户性别")
    private String gender;
    @Schema(description = "用户支付密码")
    private String paymentPassword;
    @Schema(description = "用户头像")
    private String headUrl;
}
