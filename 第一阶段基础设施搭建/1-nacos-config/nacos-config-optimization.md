# Nacos 配置中心优化方案

## 1. 当前配置分析

### 现有配置状态
- ✅ 已集成 Nacos Discovery 和 Config
- ✅ Gateway 已配置 Nacos 服务发现
- ✅ 基础的配置文件结构已建立

### 需要优化的点
1. **配置文件管理规范化**
2. **多环境配置隔离**
3. **配置热更新机制**
4. **配置安全性增强**

## 2. Nacos 配置优化实施

### 2.1 配置命名规范

```yaml
# 配置文件命名规范
# 格式：${spring.application.name}-${profile}.${file-extension}
# 示例：
storytelling-gateway-dev.yaml    # Gateway开发环境配置
storytelling-gateway-prod.yaml   # Gateway生产环境配置
storytelling-api-dev.yaml        # API服务开发环境配置
storytelling-api-prod.yaml       # API服务生产环境配置
storytelling-admin-dev.yaml      # Admin服务开发环境配置
storytelling-admin-prod.yaml     # Admin服务生产环境配置

# 共享配置
storytelling-common.yaml          # 公共配置
storytelling-datasource.yaml      # 数据源配置
storytelling-redis.yaml           # Redis配置
storytelling-security.yaml        # 安全配置
```

### 2.2 Namespace 环境隔离

```yaml
# 环境隔离配置
namespaces:
  dev: "storytelling-dev"      # 开发环境
  test: "storytelling-test"    # 测试环境
  prod: "storytelling-prod"    # 生产环境
  
# Group 分组策略
groups:
  DEFAULT_GROUP: "默认分组"
  GATEWAY_GROUP: "网关配置组"
  SERVICE_GROUP: "服务配置组"
  DATABASE_GROUP: "数据库配置组"
  CACHE_GROUP: "缓存配置组"
```

### 2.3 配置文件结构优化

#### 公共配置 (storytelling-common.yaml)
```yaml
# 公共配置
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    default-property-inclusion: non_null
  
  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
      enabled: true

# MyBatis-Plus 公共配置
mybatis-plus:
  mapper-locations: classpath*:/mapper/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: AUTO
      field-strategy: NOT_NULL
      table-underline: true
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    jdbc-type-for-null: 'null'

# Sa-Token 公共配置
sa-token:
  token-name: token
  timeout: 86400
  token-style: jwt
  jwt-secret-key: ${SA_TOKEN_SECRET:chennian-storytelling-2024}
  is-share: true
  client-ip-header: X-Real-IP
  check-id-token: false
  token-session-check-login: false
  token-prefix: ""
```

#### 数据源配置 (storytelling-datasource.yaml)
```yaml
# 数据源配置
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${DB_HOST:127.0.0.1}:${DB_PORT:13306}/${DB_NAME:storytelling}?sslMode=DISABLED&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 300000
      connection-timeout: 30000
      max-lifetime: 1800000
      auto-commit: true
      connection-test-query: SELECT 1
      pool-name: HikariCP-${spring.application.name}
      
# ShardingSphere 配置（为后续分库分表准备）
spring:
  shardingsphere:
    datasource:
      names: master,slave
      master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://${DB_MASTER_HOST:127.0.0.1}:${DB_MASTER_PORT:13306}/${DB_NAME:storytelling}?sslMode=DISABLED&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
        username: ${DB_MASTER_USERNAME:root}
        password: ${DB_MASTER_PASSWORD:123456}
      slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://${DB_SLAVE_HOST:127.0.0.1}:${DB_SLAVE_PORT:13307}/${DB_NAME:storytelling}?sslMode=DISABLED&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
        username: ${DB_SLAVE_USERNAME:root}
        password: ${DB_SLAVE_PASSWORD:123456}
    rules:
      readwrite-splitting:
        data-sources:
          readwrite_ds:
            static-strategy:
              write-data-source-name: master
              read-data-source-names:
                - slave
    props:
      sql-show: true
```

#### Redis 配置 (storytelling-redis.yaml)
```yaml
# Redis 配置
spring:
  data:
    redis:
      host: ${REDIS_HOST:127.0.0.1}
      port: ${REDIS_PORT:16379}
      password: ${REDIS_PASSWORD:}
      database: ${REDIS_DATABASE:0}
      timeout: 10000ms
      lettuce:
        pool:
          max-active: 20
          max-wait: -1ms
          max-idle: 10
          min-idle: 5
        shutdown-timeout: 100ms
      
# Redisson 配置
spring:
  redisson:
    config: |
      singleServerConfig:
        address: "redis://${REDIS_HOST:127.0.0.1}:${REDIS_PORT:16379}"
        password: ${REDIS_PASSWORD:}
        database: ${REDIS_DATABASE:0}
        connectionMinimumIdleSize: 5
        connectionPoolSize: 20
        idleConnectionTimeout: 10000
        connectTimeout: 10000
        timeout: 3000
        retryAttempts: 3
        retryInterval: 1500
```

### 2.4 Gateway 配置优化

#### storytelling-gateway-dev.yaml
```yaml
server:
  port: 8100
  servlet:
    context-path: /api

spring:
  application:
    name: storytelling-gateway
  main:
    web-application-type: reactive
  
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER:127.0.0.1:8848}
        username: ${NACOS_USERNAME:nacos}
        password: ${NACOS_PASSWORD:nacos}
        namespace: storytelling-dev
        group: GATEWAY_GROUP
        metadata:
          version: 1.0.0
          zone: dev
      config:
        server-addr: ${NACOS_SERVER:127.0.0.1:8848}
        username: ${NACOS_USERNAME:nacos}
        password: ${NACOS_PASSWORD:nacos}
        namespace: storytelling-dev
        group: GATEWAY_GROUP
        file-extension: yaml
        shared-configs:
          - data-id: storytelling-common.yaml
            group: DEFAULT_GROUP
            refresh: true
          - data-id: storytelling-redis.yaml
            group: CACHE_GROUP
            refresh: true
    
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
          predicates:
            - name: Path
              args:
                pattern: "'/api/' + serviceId + '/**'"
          filters:
            - name: StripPrefix
              args:
                parts: 2
      
      # 路由配置
      routes:
        # 管理后台路由
        - id: storytelling-admin
          uri: lb://storytelling-admin
          predicates:
            - Path=/admin/**
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
                redis-rate-limiter.requestedTokens: 1
        
        # API服务路由
        - id: storytelling-api
          uri: lb://storytelling-api
          predicates:
            - Path=/app/**
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 200
                redis-rate-limiter.burstCapacity: 400
                redis-rate-limiter.requestedTokens: 1
        
        # 用户服务路由（为微服务拆分准备）
        - id: storytelling-user-service
          uri: lb://storytelling-user-service
          predicates:
            - Path=/user/**
          filters:
            - name: StripPrefix
              args:
                parts: 1
        
        # 商品服务路由（为微服务拆分准备）
        - id: storytelling-product-service
          uri: lb://storytelling-product-service
          predicates:
            - Path=/product/**
          filters:
            - name: StripPrefix
              args:
                parts: 1
        
        # 订单服务路由（为微服务拆分准备）
        - id: storytelling-order-service
          uri: lb://storytelling-order-service
          predicates:
            - Path=/order/**
          filters:
            - name: StripPrefix
              args:
                parts: 1
      
      # 全局过滤器
      default-filters:
        - DedupeResponseHeader=X-Real-IP, RETAIN_UNIQUE
        - AddRequestHeader=X-Gateway-Version, 1.0.0
        - AddResponseHeader=X-Response-Time, #{T(System).currentTimeMillis()}
      
      # CORS 配置
      globalcors:
        add-to-simple-url-handler-mapping: true
        cors-configurations:
          '[/**]':
            allowed-origin-patterns: 
              - "http://localhost:3000"
              - "http://localhost:8080"
              - "https://*.storytelling.com"
            allowed-methods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
              - PATCH
            allowed-headers: "*"
            exposed-headers:
              - Authorization
              - Content-Disposition
              - X-Total-Count
            allow-credentials: true
            max-age: 3600

# 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true

# 日志配置
logging:
  config: classpath:logback/logback-dev.xml
  level:
    com.chennian.gateway: DEBUG
    org.springframework.cloud.gateway: DEBUG
    reactor.netty: INFO
```

### 2.5 配置热更新实现

#### 配置监听器
```java
@Component
@Slf4j
public class NacosConfigListener {
    
    @NacosConfigListener(dataId = "storytelling-gateway-dev.yaml", groupId = "GATEWAY_GROUP")
    public void onGatewayConfigChange(String configInfo) {
        log.info("Gateway配置发生变更: {}", configInfo);
        // 处理配置变更逻辑
    }
    
    @NacosConfigListener(dataId = "storytelling-common.yaml", groupId = "DEFAULT_GROUP")
    public void onCommonConfigChange(String configInfo) {
        log.info("公共配置发生变更: {}", configInfo);
        // 处理配置变更逻辑
    }
}
```

### 2.6 配置安全性增强

#### 敏感信息加密
```yaml
# 使用 Nacos 配置加密
spring:
  cloud:
    nacos:
      config:
        # 启用配置加密
        encode-data-key: storytelling-encrypt-key
        
# 敏感配置示例
datasource:
  password: ENC(encrypted_password_here)
  
redis:
  password: ENC(encrypted_redis_password_here)
  
sa-token:
  jwt-secret-key: ENC(encrypted_jwt_secret_here)
```

## 3. 配置管理最佳实践

### 3.1 配置版本管理
- 使用 Git 管理配置文件模板
- 建立配置变更审批流程
- 配置回滚机制

### 3.2 配置监控
- 配置变更日志记录
- 配置生效状态监控
- 异常配置告警

### 3.3 配置备份
- 定期备份重要配置
- 多环境配置同步
- 灾难恢复预案

## 4. 实施步骤

1. **第一步**：创建 Namespace 和 Group
2. **第二步**：迁移现有配置到 Nacos
3. **第三步**：优化配置文件结构
4. **第四步**：实现配置热更新
5. **第五步**：配置安全性增强
6. **第六步**：建立配置管理规范

## 5. 验证方案

### 5.1 功能验证
- [ ] 配置热更新生效
- [ ] 多环境隔离正常
- [ ] 服务发现正常
- [ ] 配置加密解密正常

### 5.2 性能验证
- [ ] 配置拉取延迟 < 100ms
- [ ] 配置变更推送延迟 < 1s
- [ ] 服务启动时间无明显增加

### 5.3 稳定性验证
- [ ] Nacos 服务重启后配置恢复
- [ ] 网络异常时配置缓存生效
- [ ] 配置错误时服务降级正常