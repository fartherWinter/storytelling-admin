package com.chennian.gateway;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author by chennian
 * @date 2025/5/21.
 */

@Component	//作用：讲从nacos中读取到的信息写成对象并注入到spring容器
@Data	//作用：在业务类注入该类即可调用其中的信息
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    private String dateformat;
}
