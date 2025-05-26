package com.chennian.storytelling.admin.controller;

import cn.hutool.core.lang.UUID;
import com.chennian.storytelling.common.constant.CacheConstants;
import com.chennian.storytelling.common.constant.Constants;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.Base64Util;
import com.google.code.kaptcha.Producer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author by chennian
 * @date 2025/5/8.
 */
@RestController
@Tag(name = "验证码")
@RequestMapping("/sys/login")
public class SysCaptchaController {
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final RedisCache redisCache;
    public SysCaptchaController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 生成验证码
     */
    @PostMapping("/captchaImage")
    @Operation(summary = "生成验证码")
    public ServerResponseEntity<Map<String, Object>> code(HttpServletResponse servletResponse) {
        String uuid = UUID.fastUUID().toString();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String capStr = null, code = null;
        BufferedImage image = null;
        //设置生成验证码类型
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        //转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return ServerResponseEntity.showFailMsg("验证码图片转换流信息写出错误" + e.getMessage());
        }
        HashMap<String, Object> captchaMap = new HashMap<String, Object>(16);
        captchaMap.put("uuid", uuid);
        captchaMap.put("img", Base64Util.encode(os.toByteArray()));
        return ServerResponseEntity.success(captchaMap);
    }

}
