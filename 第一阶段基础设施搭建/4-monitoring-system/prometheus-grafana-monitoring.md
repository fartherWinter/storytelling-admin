# Prometheus + Grafana 监控体系方案

## 1. 监控体系架构设计

### 1.1 监控架构图
```
监控体系架构
├── 数据采集层 (Data Collection)
│   ├── Spring Boot Actuator (应用指标)
│   ├── Micrometer (指标规范)
│   ├── JVM Metrics (JVM指标)
│   └── Custom Metrics (自定义指标)
├── 数据存储层 (Data Storage)
│   ├── Prometheus (时序数据库)
│   └── AlertManager (告警管理)
├── 数据展示层 (Data Visualization)
│   ├── Grafana (可视化面板)
│   └── Prometheus UI (原生界面)
└── 告警通知层 (Alerting)
    ├── 邮件通知
    ├── 钉钉通知
    └── 短信通知
```

### 1.2 监控指标分类

#### 基础设施指标
- **系统指标**: CPU、内存、磁盘、网络
- **JVM指标**: 堆内存、GC、线程、类加载
- **数据库指标**: 连接池、查询性能、锁等待
- **缓存指标**: Redis连接、命中率、内存使用

#### 应用业务指标
- **接口指标**: QPS、响应时间、错误率
- **业务指标**: 订单量、用户活跃度、支付成功率
- **自定义指标**: 业务流程关键节点

#### 中间件指标
- **Gateway指标**: 路由转发、限流熔断
- **消息队列指标**: 消息积压、消费延迟
- **分布式锁指标**: 锁竞争、持有时间

## 2. Prometheus 配置

### 2.1 Prometheus 配置文件

#### prometheus.yml
```yaml
# Prometheus 主配置文件
global:
  scrape_interval: 15s
  evaluation_interval: 15s
  external_labels:
    cluster: 'storytelling-cluster'
    environment: 'production'

# 告警规则文件
rule_files:
  - "rules/*.yml"

# 告警管理器配置
alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

# 数据采集配置
scrape_configs:
  # Prometheus 自身监控
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
    metrics_path: /metrics
    scrape_interval: 30s

  # Gateway 服务监控
  - job_name: 'storytelling-gateway'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['gateway:8100']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
      - source_labels: [__address__]
        target_label: service
        replacement: 'storytelling-gateway'

  # Admin 服务监控
  - job_name: 'storytelling-admin'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['admin:8200']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
      - source_labels: [__address__]
        target_label: service
        replacement: 'storytelling-admin'

  # API 服务监控
  - job_name: 'storytelling-api'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['api:8300']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
      - source_labels: [__address__]
        target_label: service
        replacement: 'storytelling-api'

  # 微服务监控（为后续微服务拆分准备）
  - job_name: 'storytelling-user-service'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    consul_sd_configs:
      - server: 'consul:8500'
        services: ['storytelling-user-service']
    relabel_configs:
      - source_labels: [__meta_consul_service]
        target_label: service
      - source_labels: [__meta_consul_node]
        target_label: instance

  - job_name: 'storytelling-product-service'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    consul_sd_configs:
      - server: 'consul:8500'
        services: ['storytelling-product-service']
    relabel_configs:
      - source_labels: [__meta_consul_service]
        target_label: service
      - source_labels: [__meta_consul_node]
        target_label: instance

  - job_name: 'storytelling-order-service'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    consul_sd_configs:
      - server: 'consul:8500'
        services: ['storytelling-order-service']
    relabel_configs:
      - source_labels: [__meta_consul_service]
        target_label: service
      - source_labels: [__meta_consul_node]
        target_label: instance

  # MySQL 监控
  - job_name: 'mysql'
    static_configs:
      - targets: ['mysql-exporter:9104']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
        replacement: 'mysql-master'

  # Redis 监控
  - job_name: 'redis'
    static_configs:
      - targets: ['redis-exporter:9121']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
        replacement: 'redis-master'

  # Node Exporter (系统监控)
  - job_name: 'node-exporter'
    static_configs:
      - targets: ['node-exporter:9100']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance

  # Nacos 监控
  - job_name: 'nacos'
    metrics_path: '/nacos/actuator/prometheus'
    static_configs:
      - targets: ['nacos:8848']
    relabel_configs:
      - source_labels: [__address__]
        target_label: instance
        replacement: 'nacos-server'

# 存储配置
storage:
  tsdb:
    retention.time: 30d
    retention.size: 50GB
    path: /prometheus/data
```

### 2.2 告警规则配置

#### rules/application-rules.yml
```yaml
# 应用层告警规则
groups:
  - name: application.rules
    rules:
      # 应用服务可用性告警
      - alert: ServiceDown
        expr: up == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Service {{ $labels.service }} is down"
          description: "Service {{ $labels.service }} on instance {{ $labels.instance }} has been down for more than 1 minute."

      # 应用响应时间告警
      - alert: HighResponseTime
        expr: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High response time on {{ $labels.service }}"
          description: "95th percentile response time is {{ $value }}s on service {{ $labels.service }}"

      # 应用错误率告警
      - alert: HighErrorRate
        expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) / rate(http_server_requests_seconds_count[5m]) > 0.05
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High error rate on {{ $labels.service }}"
          description: "Error rate is {{ $value | humanizePercentage }} on service {{ $labels.service }}"

      # JVM 内存使用告警
      - alert: HighJVMMemoryUsage
        expr: jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} > 0.8
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High JVM memory usage on {{ $labels.service }}"
          description: "JVM heap memory usage is {{ $value | humanizePercentage }} on service {{ $labels.service }}"

      # GC 频率告警
      - alert: HighGCRate
        expr: rate(jvm_gc_collection_seconds_count[5m]) > 1
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High GC rate on {{ $labels.service }}"
          description: "GC rate is {{ $value }} per second on service {{ $labels.service }}"

      # 数据库连接池告警
      - alert: HighDBConnectionUsage
        expr: hikaricp_connections_active / hikaricp_connections_max > 0.8
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High database connection usage on {{ $labels.service }}"
          description: "Database connection usage is {{ $value | humanizePercentage }} on service {{ $labels.service }}"
```

#### rules/business-rules.yml
```yaml
# 业务层告警规则
groups:
  - name: business.rules
    rules:
      # 订单创建失败率告警
      - alert: HighOrderFailureRate
        expr: rate(business_order_created_total{status="failed"}[5m]) / rate(business_order_created_total[5m]) > 0.1
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High order failure rate"
          description: "Order failure rate is {{ $value | humanizePercentage }} in the last 5 minutes"

      # 支付失败率告警
      - alert: HighPaymentFailureRate
        expr: rate(business_payment_processed_total{status="failed"}[5m]) / rate(business_payment_processed_total[5m]) > 0.05
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "High payment failure rate"
          description: "Payment failure rate is {{ $value | humanizePercentage }} in the last 5 minutes"

      # 库存不足告警
      - alert: LowInventory
        expr: business_inventory_quantity < 10
        for: 1m
        labels:
          severity: warning
        annotations:
          summary: "Low inventory for product {{ $labels.product_id }}"
          description: "Product {{ $labels.product_id }} has only {{ $value }} items left in inventory"

      # 用户注册异常告警
      - alert: HighUserRegistrationFailure
        expr: rate(business_user_registration_total{status="failed"}[10m]) > 5
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High user registration failure rate"
          description: "User registration failure rate is {{ $value }} per second in the last 10 minutes"
```

#### rules/infrastructure-rules.yml
```yaml
# 基础设施告警规则
groups:
  - name: infrastructure.rules
    rules:
      # CPU 使用率告警
      - alert: HighCPUUsage
        expr: 100 - (avg by(instance) (rate(node_cpu_seconds_total{mode="idle"}[5m])) * 100) > 80
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High CPU usage on {{ $labels.instance }}"
          description: "CPU usage is {{ $value }}% on instance {{ $labels.instance }}"

      # 内存使用率告警
      - alert: HighMemoryUsage
        expr: (node_memory_MemTotal_bytes - node_memory_MemAvailable_bytes) / node_memory_MemTotal_bytes > 0.8
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High memory usage on {{ $labels.instance }}"
          description: "Memory usage is {{ $value | humanizePercentage }} on instance {{ $labels.instance }}"

      # 磁盘使用率告警
      - alert: HighDiskUsage
        expr: (node_filesystem_size_bytes - node_filesystem_free_bytes) / node_filesystem_size_bytes > 0.8
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High disk usage on {{ $labels.instance }}"
          description: "Disk usage is {{ $value | humanizePercentage }} on instance {{ $labels.instance }} mount {{ $labels.mountpoint }}"

      # Redis 连接数告警
      - alert: HighRedisConnections
        expr: redis_connected_clients / redis_config_maxclients > 0.8
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High Redis connections"
          description: "Redis connection usage is {{ $value | humanizePercentage }}"

      # MySQL 连接数告警
      - alert: HighMySQLConnections
        expr: mysql_global_status_threads_connected / mysql_global_variables_max_connections > 0.8
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High MySQL connections"
          description: "MySQL connection usage is {{ $value | humanizePercentage }}"
```

### 2.3 AlertManager 配置

#### alertmanager.yml
```yaml
# AlertManager 配置文件
global:
  smtp_smarthost: 'smtp.qq.com:587'
  smtp_from: 'storytelling-alert@qq.com'
  smtp_auth_username: 'storytelling-alert@qq.com'
  smtp_auth_password: 'your-smtp-password'

# 路由配置
route:
  group_by: ['alertname', 'cluster', 'service']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'default'
  routes:
    # 严重告警立即通知
    - match:
        severity: critical
      receiver: 'critical-alerts'
      group_wait: 0s
      repeat_interval: 5m
    
    # 业务告警
    - match:
        alertname: ~".*Business.*|.*Order.*|.*Payment.*"
      receiver: 'business-alerts'
      group_interval: 5m
    
    # 基础设施告警
    - match:
        alertname: ~".*CPU.*|.*Memory.*|.*Disk.*"
      receiver: 'infrastructure-alerts'
      group_interval: 10m

# 接收器配置
receivers:
  # 默认接收器
  - name: 'default'
    email_configs:
      - to: 'admin@storytelling.com'
        subject: '[Storytelling] Alert: {{ .GroupLabels.alertname }}'
        body: |
          {{ range .Alerts }}
          Alert: {{ .Annotations.summary }}
          Description: {{ .Annotations.description }}
          Labels: {{ range .Labels.SortedPairs }}{{ .Name }}={{ .Value }} {{ end }}
          {{ end }}

  # 严重告警接收器
  - name: 'critical-alerts'
    email_configs:
      - to: 'admin@storytelling.com,ops@storytelling.com'
        subject: '[CRITICAL] Storytelling Alert: {{ .GroupLabels.alertname }}'
        body: |
          🚨 CRITICAL ALERT 🚨
          
          {{ range .Alerts }}
          Alert: {{ .Annotations.summary }}
          Description: {{ .Annotations.description }}
          Severity: {{ .Labels.severity }}
          Service: {{ .Labels.service }}
          Instance: {{ .Labels.instance }}
          Time: {{ .StartsAt.Format "2006-01-02 15:04:05" }}
          {{ end }}
    webhook_configs:
      # 钉钉通知
      - url: 'https://oapi.dingtalk.com/robot/send?access_token=your-dingtalk-token'
        send_resolved: true
        http_config:
          proxy_url: ''
        title: 'Storytelling Critical Alert'
        text: |
          {{ range .Alerts }}
          **{{ .Annotations.summary }}**
          
          > 描述: {{ .Annotations.description }}
          > 服务: {{ .Labels.service }}
          > 实例: {{ .Labels.instance }}
          > 时间: {{ .StartsAt.Format "2006-01-02 15:04:05" }}
          {{ end }}

  # 业务告警接收器
  - name: 'business-alerts'
    email_configs:
      - to: 'business@storytelling.com'
        subject: '[Business] Storytelling Alert: {{ .GroupLabels.alertname }}'
        body: |
          📊 Business Alert
          
          {{ range .Alerts }}
          Alert: {{ .Annotations.summary }}
          Description: {{ .Annotations.description }}
          Service: {{ .Labels.service }}
          Time: {{ .StartsAt.Format "2006-01-02 15:04:05" }}
          {{ end }}

  # 基础设施告警接收器
  - name: 'infrastructure-alerts'
    email_configs:
      - to: 'ops@storytelling.com'
        subject: '[Infrastructure] Storytelling Alert: {{ .GroupLabels.alertname }}'
        body: |
          🔧 Infrastructure Alert
          
          {{ range .Alerts }}
          Alert: {{ .Annotations.summary }}
          Description: {{ .Annotations.description }}
          Instance: {{ .Labels.instance }}
          Time: {{ .StartsAt.Format "2006-01-02 15:04:05" }}
          {{ end }}

# 抑制规则
inhibit_rules:
  # 当服务下线时，抑制其他相关告警
  - source_match:
      alertname: 'ServiceDown'
    target_match_re:
      alertname: '.*'
    equal: ['service', 'instance']
```

## 3. Spring Boot 应用监控配置

### 3.1 应用监控依赖

#### pom.xml 添加依赖
```xml
<!-- 监控相关依赖 -->
<dependencies>
    <!-- Spring Boot Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    
    <!-- Micrometer Prometheus -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
    
    <!-- Micrometer Core -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-core</artifactId>
    </dependency>
</dependencies>
```

### 3.2 应用监控配置

#### application-monitoring.yml
```yaml
# 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,env,configprops,beans,mappings
      base-path: /actuator
      cors:
        allowed-origins: "*"
        allowed-methods: GET,POST
  endpoint:
    health:
      show-details: always
      show-components: always
    metrics:
      enabled: true
    prometheus:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
        step: 60s
        descriptions: true
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.5,0.9,0.95,0.99
    tags:
      application: ${spring.application.name}
      environment: ${spring.profiles.active:dev}
      version: ${project.version:1.0.0}
  health:
    redis:
      enabled: true
    db:
      enabled: true
    diskspace:
      enabled: true
    ping:
      enabled: true

# 自定义健康检查
health:
  custom:
    business:
      enabled: true
    external-api:
      enabled: true
```

### 3.3 自定义指标配置

#### 业务指标收集器
```java
@Component
@Slf4j
public class BusinessMetricsCollector {
    
    private final MeterRegistry meterRegistry;
    private final Counter orderCreatedCounter;
    private final Counter paymentProcessedCounter;
    private final Timer orderProcessingTimer;
    private final Gauge inventoryGauge;
    
    public BusinessMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // 订单创建计数器
        this.orderCreatedCounter = Counter.builder("business_order_created_total")
                .description("Total number of orders created")
                .tag("application", "storytelling")
                .register(meterRegistry);
        
        // 支付处理计数器
        this.paymentProcessedCounter = Counter.builder("business_payment_processed_total")
                .description("Total number of payments processed")
                .tag("application", "storytelling")
                .register(meterRegistry);
        
        // 订单处理时间
        this.orderProcessingTimer = Timer.builder("business_order_processing_duration")
                .description("Order processing duration")
                .tag("application", "storytelling")
                .register(meterRegistry);
        
        // 库存数量
        this.inventoryGauge = Gauge.builder("business_inventory_quantity")
                .description("Current inventory quantity")
                .tag("application", "storytelling")
                .register(meterRegistry, this, BusinessMetricsCollector::getCurrentInventory);
    }
    
    /**
     * 记录订单创建
     */
    public void recordOrderCreated(String status, String orderType) {
        orderCreatedCounter.increment(
                Tags.of(
                        "status", status,
                        "order_type", orderType
                )
        );
    }
    
    /**
     * 记录支付处理
     */
    public void recordPaymentProcessed(String status, String paymentMethod, double amount) {
        paymentProcessedCounter.increment(
                Tags.of(
                        "status", status,
                        "payment_method", paymentMethod
                )
        );
        
        // 记录支付金额
        meterRegistry.counter("business_payment_amount_total",
                        "status", status,
                        "payment_method", paymentMethod)
                .increment(amount);
    }
    
    /**
     * 记录订单处理时间
     */
    public Timer.Sample startOrderProcessing() {
        return Timer.start(meterRegistry);
    }
    
    public void recordOrderProcessingTime(Timer.Sample sample, String orderType, String status) {
        sample.stop(Timer.builder("business_order_processing_duration")
                .tag("order_type", orderType)
                .tag("status", status)
                .register(meterRegistry));
    }
    
    /**
     * 获取当前库存总量
     */
    private double getCurrentInventory() {
        // 这里应该从数据库或缓存获取实际库存数据
        // 为了示例，返回一个模拟值
        return 1000.0;
    }
    
    /**
     * 记录用户注册
     */
    public void recordUserRegistration(String status, String source) {
        meterRegistry.counter("business_user_registration_total",
                        "status", status,
                        "source", source)
                .increment();
    }
    
    /**
     * 记录API调用
     */
    public void recordApiCall(String endpoint, String method, String status, long duration) {
        meterRegistry.timer("business_api_call_duration",
                        "endpoint", endpoint,
                        "method", method,
                        "status", status)
                .record(duration, TimeUnit.MILLISECONDS);
    }
}
```

#### 自定义健康检查
```java
@Component
public class BusinessHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        
        try {
            // 检查数据库连接
            checkDatabase(builder);
            
            // 检查Redis连接
            checkRedis(builder);
            
            // 检查业务关键功能
            checkBusinessFunctions(builder);
            
            return builder.up().build();
            
        } catch (Exception e) {
            return builder.down(e).build();
        }
    }
    
    private void checkDatabase(Health.Builder builder) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(3)) {
                builder.withDetail("database", "UP");
            } else {
                builder.withDetail("database", "DOWN");
            }
        } catch (Exception e) {
            builder.withDetail("database", "DOWN - " + e.getMessage());
        }
    }
    
    private void checkRedis(Health.Builder builder) {
        try {
            redisTemplate.opsForValue().set("health:check", "ok", 10, TimeUnit.SECONDS);
            String result = (String) redisTemplate.opsForValue().get("health:check");
            if ("ok".equals(result)) {
                builder.withDetail("redis", "UP");
            } else {
                builder.withDetail("redis", "DOWN");
            }
        } catch (Exception e) {
            builder.withDetail("redis", "DOWN - " + e.getMessage());
        }
    }
    
    private void checkBusinessFunctions(Health.Builder builder) {
        // 检查关键业务功能
        builder.withDetail("order-service", "UP")
               .withDetail("payment-service", "UP")
               .withDetail("inventory-service", "UP");
    }
}
```

## 4. Grafana 仪表板配置

### 4.1 应用概览仪表板

#### storytelling-application-overview.json
```json
{
  "dashboard": {
    "id": null,
    "title": "Storytelling Application Overview",
    "tags": ["storytelling", "application", "overview"],
    "timezone": "browser",
    "panels": [
      {
        "id": 1,
        "title": "Service Status",
        "type": "stat",
        "targets": [
          {
            "expr": "up{job=~\"storytelling-.*\"}",
            "legendFormat": "{{service}}"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "color": {
              "mode": "thresholds"
            },
            "thresholds": {
              "steps": [
                {"color": "red", "value": 0},
                {"color": "green", "value": 1}
              ]
            }
          }
        }
      },
      {
        "id": 2,
        "title": "Request Rate (QPS)",
        "type": "graph",
        "targets": [
          {
            "expr": "sum(rate(http_server_requests_seconds_count[5m])) by (service)",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "id": 3,
        "title": "Response Time (95th percentile)",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (service, le))",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "id": 4,
        "title": "Error Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "sum(rate(http_server_requests_seconds_count{status=~\"5..\"}[5m])) by (service) / sum(rate(http_server_requests_seconds_count[5m])) by (service)",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "id": 5,
        "title": "JVM Memory Usage",
        "type": "graph",
        "targets": [
          {
            "expr": "jvm_memory_used_bytes{area=\"heap\"} / jvm_memory_max_bytes{area=\"heap\"}",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "id": 6,
        "title": "GC Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(jvm_gc_collection_seconds_count[5m])",
            "legendFormat": "{{service}} - {{gc}}"
          }
        ]
      }
    ],
    "time": {
      "from": "now-1h",
      "to": "now"
    },
    "refresh": "30s"
  }
}
```

### 4.2 业务监控仪表板

#### storytelling-business-metrics.json
```json
{
  "dashboard": {
    "id": null,
    "title": "Storytelling Business Metrics",
    "tags": ["storytelling", "business", "metrics"],
    "panels": [
      {
        "id": 1,
        "title": "Order Creation Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(business_order_created_total[5m])",
            "legendFormat": "{{status}}"
          }
        ]
      },
      {
        "id": 2,
        "title": "Payment Success Rate",
        "type": "stat",
        "targets": [
          {
            "expr": "rate(business_payment_processed_total{status=\"success\"}[5m]) / rate(business_payment_processed_total[5m])",
            "legendFormat": "Success Rate"
          }
        ]
      },
      {
        "id": 3,
        "title": "User Registration Trend",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(business_user_registration_total[5m])",
            "legendFormat": "{{status}}"
          }
        ]
      },
      {
        "id": 4,
        "title": "Inventory Levels",
        "type": "graph",
        "targets": [
          {
            "expr": "business_inventory_quantity",
            "legendFormat": "{{product_id}}"
          }
        ]
      }
    ]
  }
}
```

### 4.3 基础设施监控仪表板

#### storytelling-infrastructure.json
```json
{
  "dashboard": {
    "id": null,
    "title": "Storytelling Infrastructure",
    "tags": ["storytelling", "infrastructure", "system"],
    "panels": [
      {
        "id": 1,
        "title": "CPU Usage",
        "type": "graph",
        "targets": [
          {
            "expr": "100 - (avg by(instance) (rate(node_cpu_seconds_total{mode=\"idle\"}[5m])) * 100)",
            "legendFormat": "{{instance}}"
          }
        ]
      },
      {
        "id": 2,
        "title": "Memory Usage",
        "type": "graph",
        "targets": [
          {
            "expr": "(node_memory_MemTotal_bytes - node_memory_MemAvailable_bytes) / node_memory_MemTotal_bytes * 100",
            "legendFormat": "{{instance}}"
          }
        ]
      },
      {
        "id": 3,
        "title": "Disk Usage",
        "type": "graph",
        "targets": [
          {
            "expr": "(node_filesystem_size_bytes - node_filesystem_free_bytes) / node_filesystem_size_bytes * 100",
            "legendFormat": "{{instance}} - {{mountpoint}}"
          }
        ]
      },
      {
        "id": 4,
        "title": "Network I/O",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(node_network_receive_bytes_total[5m])",
            "legendFormat": "{{instance}} - {{device}} - RX"
          },
          {
            "expr": "rate(node_network_transmit_bytes_total[5m])",
            "legendFormat": "{{instance}} - {{device}} - TX"
          }
        ]
      }
    ]
  }
}
```

## 5. Docker Compose 部署配置

### 5.1 监控服务部署

#### docker-compose-monitoring.yml
```yaml
version: '3.8'

services:
  # Prometheus
  prometheus:
    image: prom/prometheus:latest
    container_name: storytelling-prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
      - ./prometheus/rules:/etc/prometheus/rules
      - prometheus-data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--storage.tsdb.retention.time=30d'
      - '--web.enable-lifecycle'
    networks:
      - monitoring
    restart: unless-stopped

  # AlertManager
  alertmanager:
    image: prom/alertmanager:latest
    container_name: storytelling-alertmanager
    ports:
      - "9093:9093"
    volumes:
      - ./alertmanager/alertmanager.yml:/etc/alertmanager/alertmanager.yml
      - alertmanager-data:/alertmanager
    command:
      - '--config.file=/etc/alertmanager/alertmanager.yml'
      - '--storage.path=/alertmanager'
    networks:
      - monitoring
    restart: unless-stopped

  # Grafana
  grafana:
    image: grafana/grafana:latest
    container_name: storytelling-grafana
    ports:
      - "3000:3000"
    volumes:
      - grafana-data:/var/lib/grafana
      - ./grafana/provisioning:/etc/grafana/provisioning
      - ./grafana/dashboards:/var/lib/grafana/dashboards
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin123
      - GF_USERS_ALLOW_SIGN_UP=false
      - GF_INSTALL_PLUGINS=grafana-piechart-panel
    networks:
      - monitoring
    restart: unless-stopped

  # Node Exporter
  node-exporter:
    image: prom/node-exporter:latest
    container_name: storytelling-node-exporter
    ports:
      - "9100:9100"
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    command:
      - '--path.procfs=/host/proc'
      - '--path.rootfs=/rootfs'
      - '--path.sysfs=/host/sys'
      - '--collector.filesystem.mount-points-exclude=^/(sys|proc|dev|host|etc)($$|/)'
    networks:
      - monitoring
    restart: unless-stopped

  # MySQL Exporter
  mysql-exporter:
    image: prom/mysqld-exporter:latest
    container_name: storytelling-mysql-exporter
    ports:
      - "9104:9104"
    environment:
      - DATA_SOURCE_NAME=exporter:password@(mysql:3306)/
    networks:
      - monitoring
    restart: unless-stopped
    depends_on:
      - mysql

  # Redis Exporter
  redis-exporter:
    image: oliver006/redis_exporter:latest
    container_name: storytelling-redis-exporter
    ports:
      - "9121:9121"
    environment:
      - REDIS_ADDR=redis://redis:6379
    networks:
      - monitoring
    restart: unless-stopped
    depends_on:
      - redis

volumes:
  prometheus-data:
  alertmanager-data:
  grafana-data:

networks:
  monitoring:
    driver: bridge
```

## 6. 监控最佳实践

### 6.1 指标命名规范
```yaml
# 指标命名规范
业务指标:
  - business_{domain}_{action}_{unit}
  - 示例: business_order_created_total

应用指标:
  - application_{component}_{metric}_{unit}
  - 示例: application_cache_hit_ratio

基础设施指标:
  - infrastructure_{resource}_{metric}_{unit}
  - 示例: infrastructure_cpu_usage_percent

自定义指标:
  - custom_{service}_{metric}_{unit}
  - 示例: custom_gateway_request_duration_seconds
```

### 6.2 告警策略
```yaml
告警级别:
  - Critical: 影响核心业务功能
  - Warning: 需要关注但不影响业务
  - Info: 信息性告警

告警频率:
  - Critical: 立即通知，5分钟重复
  - Warning: 10分钟延迟，1小时重复
  - Info: 30分钟延迟，4小时重复

告警渠道:
  - Critical: 邮件 + 短信 + 钉钉
  - Warning: 邮件 + 钉钉
  - Info: 邮件
```

## 7. 验证方案

### 7.1 功能验证
- [ ] Prometheus 数据采集正常
- [ ] Grafana 仪表板显示正常
- [ ] 告警规则触发正常
- [ ] 通知渠道工作正常

### 7.2 性能验证
- [ ] 监控系统资源占用 < 5%
- [ ] 指标查询响应时间 < 1s
- [ ] 数据存储空间合理

### 7.3 可用性验证
- [ ] 监控系统高可用
- [ ] 数据备份恢复正常
- [ ] 告警不丢失不重复