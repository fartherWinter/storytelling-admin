package com.chennian.storytelling.common.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 腾讯云存储
 *
 * @author by chennian
 * @date 2023/3/18 16:39
 */
@Data
@Component
public class Tencent {
    private String accessKey;

    private String secretKey;

    private String bucket;

    private String resourcesUrl;
}
