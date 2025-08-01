package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.bean.bo.MallUserAutoLoginBo;
import com.chennian.storytelling.bean.bo.MallUserLoginBo;
import com.chennian.storytelling.bean.model.mall.MallUser;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.annotation.RateLimit;
import com.chennian.storytelling.common.annotation.ApiValidation;
import com.chennian.storytelling.common.annotation.AuditLog;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallUserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author by chennian
 * @date 2025/5/25.
 */

@RestController
@RequestMapping("/mall/login")
@Tag(name = "商城登录")
public class MallUserLoginController {
    private final MallUserLoginService mallUserLoginService;

    public MallUserLoginController(MallUserLoginService mallUserLoginService) {
        this.mallUserLoginService = mallUserLoginService;
    }

    @PostMapping("/getUserOpenid")
    @Operation(summary = "获取openid")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "获取openid")
    public ServerResponseEntity<String> getUserOpenid(@RequestBody MallUserLoginBo loginBo) {
        String openId = mallUserLoginService.getUserOpenid(loginBo);
        return ServerResponseEntity.success(openId);
    }

    @PostMapping("/decryptAesData")
    @Operation(summary = "解析用户数据")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "解析用户数据")
    public ServerResponseEntity<String> decryptAesData(@RequestBody MallUserLoginBo loginBo) {
        String msg = mallUserLoginService.decryptAesData(loginBo);
        return ServerResponseEntity.success(msg);
    }

    @PostMapping("/getMinAppToken")
    @Operation(summary = "获取token")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "获取token")
    public ServerResponseEntity<String> getMinAppToken() {
        String token = mallUserLoginService.getMinAppToken();
        return ServerResponseEntity.success(token);
    }

    @PostMapping("/userLogin")
    @Operation(summary = "微信登陆")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "微信登陆")
    @RateLimit(key = "mall:login:wechat:", count = 10, time = 60, limitType = RateLimit.LimitType.IP, message = "登录过于频繁，请稍后再试")
    @ApiValidation(validateSqlInjection = true, validateXss = true)
    @AuditLog(module = "商城登录", type = AuditLog.OperationType.LOGIN, description = "微信登录", recordParams = true)
    public ServerResponseEntity<MallUser> userLogin(@Valid @RequestBody MallUserLoginBo loginBo) {
        MallUser user = mallUserLoginService.userLogin(loginBo);
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/sendSms")
    @Operation(summary = "获取验证码")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "获取验证码")
    @RateLimit(key = "mall:sms:", count = 5, time = 60, limitType = RateLimit.LimitType.IP, message = "验证码发送过于频繁，请稍后再试")
    @ApiValidation(validateSqlInjection = true, validateXss = true)
    @AuditLog(module = "商城登录", type = AuditLog.OperationType.OTHER, description = "发送短信验证码", recordParams = true)
    public ServerResponseEntity<String> sendSms(@Valid @RequestBody MallUserLoginBo loginBo) {
        String smsCode = mallUserLoginService.sendSms(loginBo);
        return ServerResponseEntity.success(smsCode);
    }

    @PostMapping("/loginMobile")
    @Operation(summary = "手机号登陆")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "手机号登陆")
    @RateLimit(key = "mall:login:mobile:", count = 10, time = 60, limitType = RateLimit.LimitType.IP, message = "登录过于频繁，请稍后再试")
    @ApiValidation(validateSqlInjection = true, validateXss = true)
    @AuditLog(module = "商城登录", type = AuditLog.OperationType.LOGIN, description = "手机号登录", recordParams = true)
    public ServerResponseEntity<MallUser> loginMobile(@Valid @RequestBody MallUserLoginBo loginBo) {
        MallUser user = mallUserLoginService.loginMobile(loginBo);
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/auto/login")
    @Operation(summary = "自动登陆")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.GRANT, description = "自动登陆")
    public ServerResponseEntity<MallUser> autoLogin(@RequestBody MallUserAutoLoginBo loginBo) {
        MallUser user = mallUserLoginService.autoLogin(loginBo);
        return ServerResponseEntity.success(user);
    }
}
