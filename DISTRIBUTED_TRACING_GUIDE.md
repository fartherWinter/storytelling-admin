# 分布式链路追踪系统配置指南

## 概述

本项目已完成分布式链路追踪系统的集成，使用 Spring Cloud Sleuth + Zipkin 实现微服务间的链路追踪。

## 架构组件

### 1. 链路追踪组件
- **Spring Cloud Sleuth**: 自动生成和传播链路追踪信息
- **Zipkin**: 链路数据收集和可视化展示
- **Brave**: 高性能的分布式追踪库
- **Elasticsearch**: Zipkin 数据存储后端

### 2. 监控组件
- **Prometheus**: 指标收集和存储
- **Grafana**: 监控数据可视化
- **Kibana**: 日志分析和查询

## 已配置的模块

### 微服务模块
所有微服务模块已添加链路追踪依赖：

1. **gateway** - 网关服务
2. **storytelling-api** - API 服务
3. **storytelling-admin** - 管理后台服务
4. **storytelling-service** - 业务服务层
5. **storytelling-dao** - 数据访问层
6. **storytelling-common** - 公共组件
7. **storytelling-report** - 报表服务
8. **storytelling-security-admin** - 安全模块-管理端
9. **storytelling-security-api** - 安全模块-API端
10. **storytelling-security-common** - 安全模块-公共组件

### 配置文件
每个服务的 `application.yml` 已添加链路追踪配置：

```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0  # 100% 采样率
    web:
      client:
        enabled: true
    http:
      enabled: true
    jdbc:
      enabled: true
    redis:
      enabled: true
    mongodb:
      enabled: true  # 仅 API 服务
  zipkin:
    base-url: http://localhost:9411
    sender:
      type: web
```

## 部署和启动

### 1. 启动基础设施
```bash
# 启动链路追踪基础设施
.\scripts\deploy-tracing.bat
```

### 2. 验证系统
```bash
# 验证链路追踪系统
.\scripts\verify-tracing.bat
```

### 3. 访问地址
- **Zipkin UI**: http://localhost:9411
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

## 功能特性

### 1. 自动链路追踪
- HTTP 请求自动追踪
- 数据库操作追踪
- Redis 操作追踪
- MongoDB 操作追踪（API 服务）
- 微服务间调用追踪

### 2. 业务链路追踪
- Controller 层方法追踪
- Service 层方法追踪
- DAO 层方法追踪
- 自动记录请求信息和执行时间
- 异常信息追踪

### 3. 性能监控
- 请求响应时间统计
- 错误率监控
- 服务依赖关系图
- 实时性能指标

### 4. 告警机制
- 服务可用性告警
- 高错误率告警
- 慢请求告警
- 资源使用率告警

## 使用指南

### 1. 查看链路追踪
1. 访问 Zipkin UI: http://localhost:9411
2. 选择服务名称和时间范围
3. 点击 "Find Traces" 查看链路
4. 点击具体链路查看详细信息

### 2. 监控指标
1. 访问 Grafana: http://localhost:3000
2. 使用 admin/admin 登录
3. 查看预配置的监控面板
4. 自定义监控图表

### 3. 日志分析
1. 访问 Kibana: http://localhost:5601
2. 配置索引模式 `zipkin*`
3. 查询和分析链路数据
4. 创建自定义仪表板

## 配置说明

### 1. 采样率配置
```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0  # 生产环境建议 0.1 (10%)
```

### 2. Zipkin 配置
```yaml
spring:
  zipkin:
    base-url: http://zipkin-server:9411  # 生产环境地址
    sender:
      type: web  # 或 rabbit, kafka
```

### 3. 自定义标签
```java
@Autowired
Tracer tracer;

public void businessMethod() {
    Span span = tracer.nextSpan().name("business-operation");
    try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
        span.tag("business.type", "important");
        span.start();
        // 业务逻辑
    } finally {
        span.end();
    }
}
```

## 故障排查

### 1. 链路数据缺失
- 检查服务配置是否正确
- 确认 Zipkin 服务是否正常运行
- 检查网络连接
- 查看应用日志

### 2. 性能问题
- 降低采样率
- 检查 Elasticsearch 性能
- 优化 Zipkin 配置
- 监控资源使用情况

### 3. 存储问题
- 检查 Elasticsearch 磁盘空间
- 配置数据保留策略
- 监控索引大小
- 定期清理历史数据

## 最佳实践

### 1. 生产环境配置
- 采样率设置为 10% 或更低
- 使用消息队列发送链路数据
- 配置数据保留策略
- 启用安全认证

### 2. 性能优化
- 合理设置采样率
- 使用异步发送
- 配置连接池
- 监控资源使用

### 3. 安全考虑
- 不记录敏感信息
- 配置访问控制
- 使用 HTTPS
- 定期更新组件

## 扩展功能

### 1. 自定义追踪
- 添加业务标签
- 自定义 Span 名称
- 记录业务指标
- 集成业务日志

### 2. 告警集成
- 集成钉钉/企业微信
- 邮件告警
- 短信告警
- 自定义告警规则

### 3. 数据分析
- 性能趋势分析
- 错误模式识别
- 容量规划
- 业务指标关联

## 维护指南

### 1. 日常维护
- 监控系统状态
- 检查磁盘空间
- 更新组件版本
- 备份配置文件

### 2. 数据管理
- 定期清理历史数据
- 监控存储使用
- 优化查询性能
- 配置索引策略

### 3. 升级指南
- 备份现有配置
- 测试新版本兼容性
- 逐步升级组件
- 验证功能正常

---

**注意**: 本指南基于当前项目配置，实际使用时请根据具体环境调整相关配置。