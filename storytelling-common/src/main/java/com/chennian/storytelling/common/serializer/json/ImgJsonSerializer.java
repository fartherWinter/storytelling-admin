package com.chennian.storytelling.common.serializer.json;


import cn.hutool.core.util.StrUtil;
import com.chennian.storytelling.common.bean.Tencent;
import com.chennian.storytelling.common.utils.ImgUploadUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * 图片Json信息序列化
 *
 * @author chennian
 * @date 2023/3/14 14:31
 */
@Component
public class ImgJsonSerializer extends JsonSerializer<String> {
    @Autowired
    private Tencent tencent;
    @Autowired
    private ImgUploadUtil imgUploadUtil;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StrUtil.isBlank(value)) {
            gen.writeString(StrUtil.EMPTY);
            return;
        }
        String[] imgs = value.split(StrUtil.COMMA);
        StringBuilder sb = new StringBuilder();
        String resourceUrl = "";
        if (Objects.equals(imgUploadUtil.getUploadType(), 2)) {
            resourceUrl = tencent.getResourcesUrl();
        } else if (Objects.equals(imgUploadUtil.getUploadType(), 1)) {
            resourceUrl = imgUploadUtil.getResourceUrl();
        }
        for (String img : imgs) {
            sb.append(resourceUrl).append(img).append(StrUtil.COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);
        gen.writeString(sb.toString());
    }
}
