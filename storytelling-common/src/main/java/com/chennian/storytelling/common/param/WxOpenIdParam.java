package com.chennian.storytelling.common.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2023/4/21 9:19
 */
@Data
@Schema(description = "微信授权登录验证")
public class WxOpenIdParam {
    @Schema(description = "小程序 appId")
    private String appId;
    @Schema(description = "小程序 appSecret")
    private String sCode;
    @Schema(description = "登录时获取的 code，可通过wx.login获取")
    private String jsCode;
    @Schema(description = "授权类型，此处只需填写 authorization_code")
    private String grantType;
}
