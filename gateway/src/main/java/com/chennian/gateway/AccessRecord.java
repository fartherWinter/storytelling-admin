package com.chennian.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

/**
 * @author chen
 * @date 2023/6/17 11:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRecord {
    private MultiValueMap<String, String> formData;
    private HttpHeaders headers;
    private String remoteAddress;
    private String targetUri;
    private String method;
    private String scheme;
    private String path;
    private String body;
}
