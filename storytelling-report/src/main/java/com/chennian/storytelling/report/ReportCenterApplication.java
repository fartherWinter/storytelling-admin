package com.chennian.storytelling.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 报表中心微服务应用
 * 提供报表生成、导出和数据可视化功能
 *
 * @author chennian
 */
@SpringBootApplication(scanBasePackages = {"com.chennian.storytelling"})
@MapperScan({"com.chennian.storytelling.report.mapper"})
public class ReportCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportCenterApplication.class, args);
    }
}