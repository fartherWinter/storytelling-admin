# 第一阶段：基础设施搭建实施指南

## 1. 实施概览

### 1.1 实施目标
基于现有 Storytelling 项目，完善分布式微服务基础设施，为后续微服务拆分和系统扩展奠定坚实基础。

### 1.2 实施范围
- **服务注册中心**: 完善 Nacos 配置
- **API网关**: 优化 Gateway 路由规则
- **配置中心**: 统一配置管理
- **监控体系**: Prometheus + Grafana
- **链路追踪**: Sleuth + Zipkin

### 1.3 技术架构
```
基础设施架构
├── 服务治理层
│   ├── Nacos (服务注册发现 + 配置中心)
│   └── Gateway (API网关 + 路由转发)
├── 监控观测层
│   ├── Prometheus (指标收集)
│   ├── Grafana (可视化展示)
│   ├── Sleuth (链路追踪)
│   └── Zipkin (链路存储查询)
└── 基础设施层
    ├── MySQL (数据存储)
    ├── Redis (缓存)
    ├── Elasticsearch (日志存储)
    └── Docker (容器化部署)
```

## 2. 实施计划

### 2.1 实施阶段

#### 阶段一：Nacos 配置优化 (1-2天)
- **目标**: 完善服务注册发现和配置中心
- **产出**: 优化的 Nacos 配置、环境隔离、配置热更新
- **验收标准**: 服务注册正常、配置热更新生效

#### 阶段二：Gateway 路由优化 (2-3天)
- **目标**: 优化 API 网关路由规则和安全策略
- **产出**: 标准化路由配置、限流熔断、监控指标
- **验收标准**: 路由转发正常、限流熔断生效

#### 阶段三：统一配置管理 (1-2天)
- **目标**: 建立统一的配置管理体系
- **产出**: 配置分层架构、配置工具类、安全增强
- **验收标准**: 配置统一管理、安全访问控制

#### 阶段四：监控体系搭建 (3-4天)
- **目标**: 建立完整的监控体系
- **产出**: Prometheus + Grafana 监控、告警规则
- **验收标准**: 监控指标正常、告警及时准确

#### 阶段五：链路追踪集成 (2-3天)
- **目标**: 集成分布式链路追踪
- **产出**: Sleuth + Zipkin 链路追踪、性能分析
- **验收标准**: 链路追踪完整、性能影响可控

### 2.2 实施时间线
```
第1周:
├── Day 1-2: Nacos 配置优化
├── Day 3-5: Gateway 路由优化
└── Day 6-7: 统一配置管理

第2周:
├── Day 1-4: 监控体系搭建
├── Day 5-7: 链路追踪集成
└── 整体测试验证
```

## 3. 详细实施步骤

### 3.1 环境准备

#### 3.1.1 基础环境检查
```bash
# 检查 Docker 环境
docker --version
docker-compose --version

# 检查 Java 环境
java -version
mvn --version

# 检查网络连通性
ping nacos-server
ping mysql-server
ping redis-server
```

#### 3.1.2 创建工作目录
```bash
# 创建基础设施配置目录
mkdir -p storytelling/infrastructure
cd storytelling/infrastructure

# 创建各组件配置目录
mkdir -p nacos/{config,data}
mkdir -p gateway/config
mkdir -p prometheus/{config,rules,data}
mkdir -p grafana/{config,dashboards,data}
mkdir -p zipkin/{config,data}
mkdir -p elasticsearch/data
```

### 3.2 Nacos 配置优化实施

#### 3.2.1 部署 Nacos 集群
```yaml
# docker-compose-nacos.yml
version: '3.8'
services:
  nacos:
    image: nacos/nacos-server:v2.3.0
    container_name: storytelling-nacos
    ports:
      - "8848:8848"
      - "9848:9848"
    environment:
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_USER=nacos
      - MYSQL_SERVICE_PASSWORD=nacos123
      - NACOS_AUTH_ENABLE=true
      - NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789
      - NACOS_AUTH_IDENTITY_KEY=serverIdentity
      - NACOS_AUTH_IDENTITY_VALUE=security
    volumes:
      - ./nacos/data:/home/nacos/data
      - ./nacos/logs:/home/nacos/logs
    networks:
      - storytelling
    restart: unless-stopped
    depends_on:
      - mysql
```

#### 3.2.2 配置 Nacos 数据库
```sql
-- 创建 Nacos 数据库
CREATE DATABASE nacos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建 Nacos 用户
CREATE USER 'nacos'@'%' IDENTIFIED BY 'nacos123';
GRANT ALL PRIVILEGES ON nacos.* TO 'nacos'@'%';
FLUSH PRIVILEGES;

-- 导入 Nacos 表结构
-- (从 nacos-server 获取 mysql-schema.sql)
```

#### 3.2.3 配置应用连接 Nacos
```yaml
# application-nacos.yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:storytelling-dev}
        group: ${NACOS_GROUP:DEFAULT_GROUP}
        username: ${NACOS_USERNAME:nacos}
        password: ${NACOS_PASSWORD:nacos}
        metadata:
          version: ${project.version:1.0.0}
          zone: ${DEPLOY_ZONE:default}
      config:
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
        namespace: ${NACOS_NAMESPACE:storytelling-dev}
        group: ${NACOS_GROUP:DEFAULT_GROUP}
        username: ${NACOS_USERNAME:nacos}
        password: ${NACOS_PASSWORD:nacos}
        file-extension: yml
        shared-configs:
          - data-id: common-config.yml
            group: COMMON_GROUP
            refresh: true
          - data-id: datasource-config.yml
            group: COMMON_GROUP
            refresh: true
```

### 3.3 Gateway 路由优化实施

#### 3.3.1 更新 Gateway 配置
```yaml
# gateway/application-gateway.yml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        # 管理后台路由
        - id: storytelling-admin-route
          uri: lb://storytelling-admin
          predicates:
            - Path=/admin/**
          filters:
            - StripPrefix=1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: admin-circuit-breaker
                fallbackUri: forward:/fallback/admin
        
        # API 服务路由
        - id: storytelling-api-route
          uri: lb://storytelling-api
          predicates:
            - Path=/api/**
          filters:
            - StripPrefix=1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 200
                redis-rate-limiter.burstCapacity: 400
                key-resolver: "#{@ipKeyResolver}"
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY,GATEWAY_TIMEOUT
                methods: GET,POST
```

#### 3.3.2 添加自定义过滤器
```java
// RequestLoggingFilter.java
@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString();
        
        log.info("Gateway Request [{}]: {} {}", 
                requestId, request.getMethod(), request.getURI());
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    long duration = System.currentTimeMillis() - startTime;
                    ServerHttpResponse response = exchange.getResponse();
                    
                    log.info("Gateway Response [{}]: {} - {}ms", 
                            requestId, response.getStatusCode(), duration);
                });
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}
```

### 3.4 监控体系搭建实施

#### 3.4.1 部署 Prometheus
```bash
# 启动 Prometheus
docker-compose -f docker-compose-monitoring.yml up -d prometheus

# 验证 Prometheus
curl http://localhost:9090/api/v1/status/config
```

#### 3.4.2 部署 Grafana
```bash
# 启动 Grafana
docker-compose -f docker-compose-monitoring.yml up -d grafana

# 访问 Grafana
# http://localhost:3000 (admin/admin123)
```

#### 3.4.3 配置应用监控
```yaml
# application-monitoring.yml
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
    tags:
      application: ${spring.application.name}
      environment: ${spring.profiles.active}
```

### 3.5 链路追踪集成实施

#### 3.5.1 部署 Zipkin
```bash
# 启动 Zipkin
docker-compose -f docker-compose-zipkin.yml up -d zipkin

# 验证 Zipkin
curl http://localhost:9411/health
```

#### 3.5.2 配置应用链路追踪
```yaml
# application-tracing.yml
spring:
  sleuth:
    enabled: true
    sampler:
      probability: 1.0
    zipkin:
      base-url: http://zipkin:9411
      sender:
        type: web
```

## 4. 验证测试

### 4.1 功能验证脚本

#### 4.1.1 服务注册验证
```bash
#!/bin/bash
# verify-nacos.sh

echo "验证 Nacos 服务注册..."

# 检查 Nacos 健康状态
nacos_health=$(curl -s http://localhost:8848/nacos/v1/console/health/readiness)
if [[ $nacos_health == *"UP"* ]]; then
    echo "✓ Nacos 服务正常"
else
    echo "✗ Nacos 服务异常"
    exit 1
fi

# 检查服务注册
services=$(curl -s "http://localhost:8848/nacos/v1/ns/catalog/services")
if [[ $services == *"storytelling"* ]]; then
    echo "✓ 服务注册正常"
else
    echo "✗ 服务注册异常"
    exit 1
fi

echo "Nacos 验证完成"
```

#### 4.1.2 Gateway 路由验证
```bash
#!/bin/bash
# verify-gateway.sh

echo "验证 Gateway 路由..."

# 检查 Gateway 健康状态
gateway_health=$(curl -s http://localhost:8100/actuator/health)
if [[ $gateway_health == *"UP"* ]]; then
    echo "✓ Gateway 服务正常"
else
    echo "✗ Gateway 服务异常"
    exit 1
fi

# 测试路由转发
admin_response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8100/admin/health)
if [[ $admin_response == "200" ]]; then
    echo "✓ Admin 路由正常"
else
    echo "✗ Admin 路由异常: $admin_response"
fi

api_response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8100/api/health)
if [[ $api_response == "200" ]]; then
    echo "✓ API 路由正常"
else
    echo "✗ API 路由异常: $api_response"
fi

echo "Gateway 验证完成"
```

#### 4.1.3 监控验证
```bash
#!/bin/bash
# verify-monitoring.sh

echo "验证监控体系..."

# 检查 Prometheus
prometheus_health=$(curl -s http://localhost:9090/-/healthy)
if [[ $prometheus_health == "Prometheus is Healthy." ]]; then
    echo "✓ Prometheus 正常"
else
    echo "✗ Prometheus 异常"
fi

# 检查 Grafana
grafana_health=$(curl -s http://localhost:3000/api/health)
if [[ $grafana_health == *"ok"* ]]; then
    echo "✓ Grafana 正常"
else
    echo "✗ Grafana 异常"
fi

# 检查指标收集
metrics=$(curl -s "http://localhost:9090/api/v1/query?query=up")
if [[ $metrics == *"success"* ]]; then
    echo "✓ 指标收集正常"
else
    echo "✗ 指标收集异常"
fi

echo "监控验证完成"
```

#### 4.1.4 链路追踪验证
```bash
#!/bin/bash
# verify-tracing.sh

echo "验证链路追踪..."

# 检查 Zipkin
zipkin_health=$(curl -s http://localhost:9411/health)
if [[ $zipkin_health == *"UP"* ]]; then
    echo "✓ Zipkin 正常"
else
    echo "✗ Zipkin 异常"
fi

# 生成测试请求
echo "生成测试链路..."
curl -s http://localhost:8100/api/test/trace > /dev/null
sleep 2

# 检查链路数据
traces=$(curl -s "http://localhost:9411/api/v2/traces?limit=1")
if [[ $traces != "[]" ]]; then
    echo "✓ 链路追踪正常"
else
    echo "✗ 链路追踪异常"
fi

echo "链路追踪验证完成"
```

### 4.2 性能验证

#### 4.2.1 压力测试脚本
```bash
#!/bin/bash
# performance-test.sh

echo "开始性能测试..."

# 安装 Apache Bench (如果未安装)
which ab > /dev/null || {
    echo "请安装 Apache Bench: apt-get install apache2-utils"
    exit 1
}

# Gateway 性能测试
echo "测试 Gateway 性能..."
ab -n 1000 -c 10 http://localhost:8100/api/health

# 直连服务性能测试
echo "测试直连服务性能..."
ab -n 1000 -c 10 http://localhost:8300/health

echo "性能测试完成"
```

### 4.3 集成验证

#### 4.3.1 端到端测试
```bash
#!/bin/bash
# e2e-test.sh

echo "开始端到端测试..."

# 1. 用户注册
echo "测试用户注册..."
register_response=$(curl -s -X POST \
  http://localhost:8100/api/user/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com"
  }')

if [[ $register_response == *"success"* ]]; then
    echo "✓ 用户注册成功"
else
    echo "✗ 用户注册失败: $register_response"
fi

# 2. 用户登录
echo "测试用户登录..."
login_response=$(curl -s -X POST \
  http://localhost:8100/api/user/login \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "testuser",
    "password": "123456"
  }')

if [[ $login_response == *"token"* ]]; then
    echo "✓ 用户登录成功"
    token=$(echo $login_response | jq -r '.data.token')
else
    echo "✗ 用户登录失败: $login_response"
    exit 1
fi

# 3. 获取用户信息
echo "测试获取用户信息..."
user_info=$(curl -s -X GET \
  http://localhost:8100/api/user/info \
  -H "Authorization: Bearer $token")

if [[ $user_info == *"testuser"* ]]; then
    echo "✓ 获取用户信息成功"
else
    echo "✗ 获取用户信息失败: $user_info"
fi

echo "端到端测试完成"
```

## 5. 部署脚本

### 5.1 一键部署脚本
```bash
#!/bin/bash
# deploy-infrastructure.sh

set -e

echo "开始部署基础设施..."

# 检查环境
echo "检查环境依赖..."
command -v docker >/dev/null 2>&1 || { echo "请安装 Docker"; exit 1; }
command -v docker-compose >/dev/null 2>&1 || { echo "请安装 Docker Compose"; exit 1; }

# 创建网络
echo "创建 Docker 网络..."
docker network create storytelling 2>/dev/null || true

# 部署基础服务
echo "部署基础服务..."
docker-compose -f docker-compose-base.yml up -d

# 等待服务启动
echo "等待服务启动..."
sleep 30

# 部署 Nacos
echo "部署 Nacos..."
docker-compose -f docker-compose-nacos.yml up -d
sleep 20

# 部署监控
echo "部署监控服务..."
docker-compose -f docker-compose-monitoring.yml up -d
sleep 15

# 部署链路追踪
echo "部署链路追踪..."
docker-compose -f docker-compose-zipkin.yml up -d
sleep 15

# 验证部署
echo "验证部署结果..."
./verify-nacos.sh
./verify-monitoring.sh
./verify-tracing.sh

echo "基础设施部署完成！"
echo "访问地址:"
echo "  Nacos: http://localhost:8848/nacos (nacos/nacos)"
echo "  Grafana: http://localhost:3000 (admin/admin123)"
echo "  Prometheus: http://localhost:9090"
echo "  Zipkin: http://localhost:9411"
```

### 5.2 应用部署脚本
```bash
#!/bin/bash
# deploy-applications.sh

set -e

echo "开始部署应用服务..."

# 构建应用镜像
echo "构建应用镜像..."
mvn clean package -DskipTests
docker build -t storytelling-gateway:latest gateway/
docker build -t storytelling-admin:latest storytelling-admin/
docker build -t storytelling-api:latest storytelling-api/

# 部署应用
echo "部署应用服务..."
docker-compose -f docker-compose-apps.yml up -d

# 等待应用启动
echo "等待应用启动..."
sleep 60

# 验证应用
echo "验证应用部署..."
./verify-gateway.sh
./e2e-test.sh

echo "应用部署完成！"
echo "访问地址:"
echo "  Gateway: http://localhost:8100"
echo "  Admin: http://localhost:8200"
echo "  API: http://localhost:8300"
```

## 6. 运维指南

### 6.1 日常运维

#### 6.1.1 健康检查
```bash
#!/bin/bash
# health-check.sh

echo "执行健康检查..."

# 检查容器状态
echo "检查容器状态:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# 检查服务健康
echo "\n检查服务健康:"
services=("nacos:8848" "prometheus:9090" "grafana:3000" "zipkin:9411")
for service in "${services[@]}"; do
    IFS=':' read -r host port <<< "$service"
    if curl -s -f http://localhost:$port/health > /dev/null 2>&1; then
        echo "✓ $host 正常"
    else
        echo "✗ $host 异常"
    fi
done

# 检查磁盘空间
echo "\n检查磁盘空间:"
df -h | grep -E '(Filesystem|/dev/)'

# 检查内存使用
echo "\n检查内存使用:"
free -h
```

#### 6.1.2 日志管理
```bash
#!/bin/bash
# log-management.sh

# 查看应用日志
view_logs() {
    local service=$1
    local lines=${2:-100}
    echo "查看 $service 最近 $lines 行日志:"
    docker logs --tail $lines $service
}

# 清理旧日志
clean_logs() {
    echo "清理 Docker 日志..."
    docker system prune -f
    
    echo "清理应用日志..."
    find ./logs -name "*.log" -mtime +7 -delete
}

# 导出日志
export_logs() {
    local date=$(date +%Y%m%d)
    local backup_dir="./logs/backup/$date"
    
    mkdir -p $backup_dir
    
    for service in storytelling-gateway storytelling-admin storytelling-api; do
        docker logs $service > "$backup_dir/${service}.log" 2>&1
    done
    
    echo "日志已导出到: $backup_dir"
}

case "$1" in
    view)
        view_logs $2 $3
        ;;
    clean)
        clean_logs
        ;;
    export)
        export_logs
        ;;
    *)
        echo "用法: $0 {view|clean|export} [service] [lines]"
        exit 1
        ;;
esac
```

### 6.2 故障排查

#### 6.2.1 常见问题排查
```bash
#!/bin/bash
# troubleshoot.sh

echo "开始故障排查..."

# 1. 检查网络连通性
echo "1. 检查网络连通性:"
services=("nacos" "mysql" "redis" "prometheus" "grafana" "zipkin")
for service in "${services[@]}"; do
    if docker exec storytelling-gateway ping -c 1 $service > /dev/null 2>&1; then
        echo "✓ $service 网络正常"
    else
        echo "✗ $service 网络异常"
    fi
done

# 2. 检查端口占用
echo "\n2. 检查端口占用:"
ports=(8100 8200 8300 8848 9090 3000 9411)
for port in "${ports[@]}"; do
    if netstat -tuln | grep :$port > /dev/null; then
        echo "✓ 端口 $port 已占用"
    else
        echo "✗ 端口 $port 未占用"
    fi
done

# 3. 检查资源使用
echo "\n3. 检查资源使用:"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"

# 4. 检查错误日志
echo "\n4. 检查错误日志:"
for service in storytelling-gateway storytelling-admin storytelling-api; do
    error_count=$(docker logs $service 2>&1 | grep -i error | wc -l)
    if [ $error_count -gt 0 ]; then
        echo "⚠ $service 有 $error_count 个错误"
        docker logs --tail 5 $service 2>&1 | grep -i error
    else
        echo "✓ $service 无错误日志"
    fi
done
```

## 7. 成功标准

### 7.1 功能标准
- [ ] Nacos 服务注册发现正常
- [ ] Gateway 路由转发正常
- [ ] 配置热更新生效
- [ ] 监控指标收集正常
- [ ] 链路追踪完整
- [ ] 告警规则生效

### 7.2 性能标准
- [ ] Gateway 响应时间 < 50ms
- [ ] 服务注册时间 < 5s
- [ ] 配置更新时间 < 10s
- [ ] 监控查询时间 < 2s
- [ ] 链路查询时间 < 3s

### 7.3 可用性标准
- [ ] 服务可用性 > 99.9%
- [ ] 监控数据完整性 > 99%
- [ ] 链路追踪覆盖率 > 95%
- [ ] 告警及时性 < 1min

## 8. 后续规划

### 8.1 短期优化 (1个月内)
- 完善监控告警规则
- 优化链路追踪性能
- 增加自动化运维脚本
- 建立故障处理流程

### 8.2 中期规划 (3个月内)
- 集成 ELK 日志系统
- 实现配置管理界面
- 建立服务治理规范
- 完善安全认证体系

### 8.3 长期规划 (6个月内)
- 实现多环境管理
- 建立 DevOps 流水线
- 实现自动扩缩容
- 完善灾备方案

---

**实施完成后，基础设施将为后续的微服务拆分和系统扩展提供强有力的支撑，确保系统的可观测性、可维护性和高可用性。**