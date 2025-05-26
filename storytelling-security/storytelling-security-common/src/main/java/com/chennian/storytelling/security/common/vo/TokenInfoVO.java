package com.chennian.storytelling.security.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2023/3/19 19:54
 */
@Data
public class TokenInfoVO {
    @Schema(description = "accessToken" )
    private String accessToken;

    @Schema(description = "refreshToken" )
    private String refreshToken;

    @Schema(description = "在多少秒后过期" )
    private Integer expiresIn;
}
