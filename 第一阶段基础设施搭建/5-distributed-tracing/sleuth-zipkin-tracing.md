# Sleuth + Zipkin 分布式链路追踪方案

## 1. 链路追踪架构设计

### 1.1 追踪架构图
```
分布式链路追踪架构
├── 数据采集层 (Data Collection)
│   ├── Spring Cloud Sleuth (自动埋点)
│   ├── Custom Spans (自定义埋点)
│   ├── HTTP Headers (链路传递)
│   └── Async Context (异步上下文)
├── 数据传输层 (Data Transport)
│   ├── HTTP Reporter (HTTP传输)
│   ├── Kafka Reporter (消息队列传输)
│   └── RabbitMQ Reporter (消息队列传输)
├── 数据存储层 (Data Storage)
│   ├── Zipkin Server (链路数据存储)
│   ├── Elasticsearch (持久化存储)
│   └── MySQL (元数据存储)
└── 数据展示层 (Data Visualization)
    ├── Zipkin UI (链路查询界面)
    ├── Grafana (链路监控面板)
    └── Custom Dashboard (自定义面板)
```

### 1.2 追踪数据模型

#### Trace 结构
```
Trace (链路)
├── TraceId (链路ID)
├── SpanId (跨度ID)
├── ParentSpanId (父跨度ID)
├── ServiceName (服务名称)
├── OperationName (操作名称)
├── StartTime (开始时间)
├── Duration (持续时间)
├── Tags (标签)
│   ├── http.method
│   ├── http.url
│   ├── http.status_code
│   ├── db.statement
│   └── custom.business_id
└── Logs (日志)
    ├── Timestamp
    ├── Level
    └── Message
```

## 2. Zipkin Server 配置

### 2.1 Zipkin Server 部署

#### docker-compose-zipkin.yml
```yaml
version: '3.8'

services:
  # Zipkin Server
  zipkin:
    image: openzipkin/zipkin:latest
    container_name: storytelling-zipkin
    ports:
      - "9411:9411"
    environment:
      # 存储配置
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=elasticsearch:9200
      - ES_INDEX=zipkin
      - ES_INDEX_REPLICAS=0
      - ES_INDEX_SHARDS=1
      
      # 内存配置
      - JAVA_OPTS=-Xms512m -Xmx1024m
      
      # 采样配置
      - ZIPKIN_STORAGE_THROTTLE_ENABLED=true
      - ZIPKIN_STORAGE_THROTTLE_MIN_CONCURRENCY=10
      - ZIPKIN_STORAGE_THROTTLE_MAX_CONCURRENCY=200
      
      # UI配置
      - ZIPKIN_UI_LOGS_URL=http://kibana:5601
      - ZIPKIN_UI_DEPENDENCY_LOW_ERROR_RATE=0.1
      - ZIPKIN_UI_DEPENDENCY_HIGH_ERROR_RATE=0.5
    volumes:
      - zipkin-data:/zipkin
    networks:
      - tracing
    restart: unless-stopped
    depends_on:
      - elasticsearch

  # Elasticsearch (Zipkin存储)
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.17.0
    container_name: storytelling-elasticsearch
    ports:
      - "9200:9200"
      - "9300:9300"
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx1024m"
      - xpack.security.enabled=false
      - xpack.monitoring.enabled=false
      - xpack.watcher.enabled=false
      - xpack.ml.enabled=false
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data
    networks:
      - tracing
    restart: unless-stopped

  # Kibana (日志查询)
  kibana:
    image: docker.elastic.co/kibana/kibana:7.17.0
    container_name: storytelling-kibana
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
      - SERVER_NAME=storytelling-kibana
      - SERVER_HOST=0.0.0.0
    networks:
      - tracing
    restart: unless-stopped
    depends_on:
      - elasticsearch

  # Zipkin Dependencies (依赖关系分析)
  zipkin-dependencies:
    image: openzipkin/zipkin-dependencies:latest
    container_name: storytelling-zipkin-dependencies
    environment:
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=elasticsearch:9200
      - ES_INDEX=zipkin
    networks:
      - tracing
    restart: "no"
    depends_on:
      - elasticsearch
    # 每天凌晨2点运行依赖关系分析
    command: >
      sh -c "while true; do
        sleep 86400;
        java -jar zipkin-dependencies.jar;
      done"

volumes:
  zipkin-data:
  elasticsearch-data:

networks:
  tracing:
    driver: bridge
```

### 2.2 Zipkin 高可用配置

#### zipkin-ha.yml
```yaml
version: '3.8'

services:
  # Zipkin Server 1
  zipkin-1:
    image: openzipkin/zipkin:latest
    container_name: storytelling-zipkin-1
    ports:
      - "9411:9411"
    environment:
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=elasticsearch-1:9200,elasticsearch-2:9200,elasticsearch-3:9200
      - JAVA_OPTS=-Xms512m -Xmx1024m
    networks:
      - tracing
    restart: unless-stopped

  # Zipkin Server 2
  zipkin-2:
    image: openzipkin/zipkin:latest
    container_name: storytelling-zipkin-2
    ports:
      - "9412:9411"
    environment:
      - STORAGE_TYPE=elasticsearch
      - ES_HOSTS=elasticsearch-1:9200,elasticsearch-2:9200,elasticsearch-3:9200
      - JAVA_OPTS=-Xms512m -Xmx1024m
    networks:
      - tracing
    restart: unless-stopped

  # Nginx 负载均衡
  zipkin-lb:
    image: nginx:alpine
    container_name: storytelling-zipkin-lb
    ports:
      - "9410:80"
    volumes:
      - ./nginx/zipkin.conf:/etc/nginx/conf.d/default.conf
    networks:
      - tracing
    restart: unless-stopped
    depends_on:
      - zipkin-1
      - zipkin-2

networks:
  tracing:
    driver: bridge
```

#### nginx/zipkin.conf
```nginx
upstream zipkin_backend {
    server zipkin-1:9411 weight=1 max_fails=3 fail_timeout=30s;
    server zipkin-2:9411 weight=1 max_fails=3 fail_timeout=30s;
}

server {
    listen 80;
    server_name localhost;

    location / {
        proxy_pass http://zipkin_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 健康检查
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        proxy_connect_timeout 5s;
        proxy_send_timeout 10s;
        proxy_read_timeout 10s;
    }
    
    # 健康检查端点
    location /health {
        access_log off;
        return 200 "healthy\n";
        add_header Content-Type text/plain;
    }
}
```

## 3. Spring Boot 应用链路追踪配置

### 3.1 依赖配置

#### pom.xml 添加依赖
```xml
<!-- 链路追踪相关依赖 -->
<dependencies>
    <!-- Spring Cloud Sleuth -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
    
    <!-- Zipkin 客户端 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    </dependency>
    
    <!-- Brave (链路追踪核心) -->
    <dependency>
        <groupId>io.zipkin.brave</groupId>
        <artifactId>brave</artifactId>
    </dependency>
    
    <!-- Zipkin Reporter -->
    <dependency>
        <groupId>io.zipkin.reporter2</groupId>
        <artifactId>zipkin-reporter-brave</artifactId>
    </dependency>
    
    <!-- HTTP Reporter -->
    <dependency>
        <groupId>io.zipkin.reporter2</groupId>
        <artifactId>zipkin-sender-okhttp3</artifactId>
    </dependency>
    
    <!-- Kafka Reporter (可选) -->
    <dependency>
        <groupId>io.zipkin.reporter2</groupId>
        <artifactId>zipkin-sender-kafka</artifactId>
    </dependency>
</dependencies>
```

### 3.2 链路追踪配置

#### application-tracing.yml
```yaml
# 链路追踪配置
spring:
  sleuth:
    # 基础配置
    enabled: true
    trace-id128: true  # 使用128位TraceId
    supports-join: true  # 支持Span合并
    
    # 采样配置
    sampler:
      probability: 1.0  # 采样率 (开发环境100%，生产环境建议0.1)
      rate: 1000  # 每秒最大采样数
    
    # Zipkin配置
    zipkin:
      enabled: true
      base-url: http://zipkin:9411  # Zipkin服务地址
      sender:
        type: web  # 发送方式: web/kafka/rabbit
      compression:
        enabled: true  # 启用压缩
      discovery-client-enabled: false  # 禁用服务发现
      
    # HTTP追踪配置
    web:
      enabled: true
      client:
        enabled: true
        skip-pattern: "/actuator.*|/health.*|/info.*"  # 跳过健康检查等端点
      servlet:
        enabled: true
        
    # 异步追踪配置
    async:
      enabled: true
      configurer:
        enabled: true
        
    # 数据库追踪配置
    jdbc:
      enabled: true
      datasource-proxy:
        enabled: true
        query:
          enable-logging: true
          log-level: debug
          
    # Redis追踪配置
    redis:
      enabled: true
      
    # 消息队列追踪配置
    messaging:
      enabled: true
      kafka:
        enabled: true
      rabbit:
        enabled: true
        
    # 自定义标签
    baggage:
      correlation-enabled: true
      correlation-fields:
        - user-id
        - tenant-id
        - business-id
      remote-fields:
        - user-id
        - tenant-id
        - business-id
        
    # 日志集成
    log:
      slf4j:
        enabled: true
        name-skip-pattern: "^org\.springframework\.cloud\.sleuth.*"
        
# Zipkin Reporter配置
zipkin:
  reporter:
    http:
      endpoint: http://zipkin:9411/api/v2/spans
      connection-timeout: 10000
      read-timeout: 60000
      compression-enabled: true
    kafka:
      bootstrap-servers: kafka:9092
      topic: zipkin
      
# 自定义追踪配置
tracing:
  custom:
    # 业务追踪开关
    business-tracing-enabled: true
    # 性能追踪开关
    performance-tracing-enabled: true
    # 错误追踪开关
    error-tracing-enabled: true
    # 慢查询阈值(毫秒)
    slow-query-threshold: 1000
    # 慢接口阈值(毫秒)
    slow-api-threshold: 2000
```

### 3.3 自定义链路追踪配置

#### TracingConfiguration.java
```java
@Configuration
@EnableConfigurationProperties(TracingProperties.class)
@Slf4j
public class TracingConfiguration {
    
    @Autowired
    private TracingProperties tracingProperties;
    
    /**
     * 自定义采样器
     */
    @Bean
    public ProbabilityBasedSampler customSampler() {
        return new ProbabilityBasedSampler(tracingProperties.getSampleRate());
    }
    
    /**
     * 自定义Span命名策略
     */
    @Bean
    public SpanNamer customSpanNamer() {
        return new SpanNamer() {
            @Override
            public String name(HttpRequest httpRequest, String defaultName) {
                String uri = httpRequest.getURI();
                String method = httpRequest.getMethod();
                
                // 自定义命名规则
                if (uri.startsWith("/api/")) {
                    return "API-" + method + "-" + extractApiPath(uri);
                } else if (uri.startsWith("/admin/")) {
                    return "ADMIN-" + method + "-" + extractAdminPath(uri);
                }
                
                return defaultName;
            }
            
            private String extractApiPath(String uri) {
                // 提取API路径的业务含义
                if (uri.contains("/user/")) return "USER";
                if (uri.contains("/order/")) return "ORDER";
                if (uri.contains("/product/")) return "PRODUCT";
                return "UNKNOWN";
            }
            
            private String extractAdminPath(String uri) {
                // 提取管理端路径的业务含义
                if (uri.contains("/system/")) return "SYSTEM";
                if (uri.contains("/monitor/")) return "MONITOR";
                return "UNKNOWN";
            }
        };
    }
    
    /**
     * 自定义Span标签增强器
     */
    @Bean
    public SpanCustomizer customSpanCustomizer() {
        return span -> {
            // 添加应用信息
            span.tag("application.name", "storytelling")
                .tag("application.version", getClass().getPackage().getImplementationVersion())
                .tag("environment", System.getProperty("spring.profiles.active", "dev"));
                
            // 添加主机信息
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                span.tag("host.name", localhost.getHostName())
                    .tag("host.address", localhost.getHostAddress());
            } catch (Exception e) {
                log.warn("Failed to get host information", e);
            }
        };
    }
    
    /**
     * HTTP客户端追踪配置
     */
    @Bean
    public HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.newBuilder(tracing)
                .clientRequestSampler(HttpRequestSampler.TRACE_ID)
                .serverRequestSampler(HttpRequestSampler.TRACE_ID)
                .clientParser(new CustomHttpClientParser())
                .serverParser(new CustomHttpServerParser())
                .build();
    }
    
    /**
     * 数据库追踪配置
     */
    @Bean
    public P6SpyOptions p6SpyOptions() {
        return new P6SpyOptions() {
            @Override
            public void load(Properties properties) {
                properties.setProperty("driverlist", "com.mysql.cj.jdbc.Driver");
                properties.setProperty("appender", "com.p6spy.engine.spy.appender.Slf4JLogger");
                properties.setProperty("logMessageFormat", "com.p6spy.engine.spy.appender.CustomLineFormat");
                properties.setProperty("customLogMessageFormat", 
                    "%(currentTime) | %(executionTime) | %(category) | %(effectiveSql)");
                properties.setProperty("filter", "true");
                properties.setProperty("exclude", "select 1");
            }
        };
    }
}
```

#### TracingProperties.java
```java
@ConfigurationProperties(prefix = "tracing.custom")
@Data
public class TracingProperties {
    
    /**
     * 业务追踪开关
     */
    private boolean businessTracingEnabled = true;
    
    /**
     * 性能追踪开关
     */
    private boolean performanceTracingEnabled = true;
    
    /**
     * 错误追踪开关
     */
    private boolean errorTracingEnabled = true;
    
    /**
     * 采样率
     */
    private float sampleRate = 1.0f;
    
    /**
     * 慢查询阈值(毫秒)
     */
    private long slowQueryThreshold = 1000;
    
    /**
     * 慢接口阈值(毫秒)
     */
    private long slowApiThreshold = 2000;
    
    /**
     * 跳过追踪的URL模式
     */
    private List<String> skipPatterns = Arrays.asList(
        "/actuator.*",
        "/health.*",
        "/info.*",
        "/metrics.*"
    );
    
    /**
     * 自定义标签
     */
    private Map<String, String> customTags = new HashMap<>();
}
```

### 3.4 自定义追踪组件

#### BusinessTracingAspect.java
```java
@Aspect
@Component
@Slf4j
public class BusinessTracingAspect {
    
    private final Tracer tracer;
    private final TracingProperties tracingProperties;
    
    public BusinessTracingAspect(Tracer tracer, TracingProperties tracingProperties) {
        this.tracer = tracer;
        this.tracingProperties = tracingProperties;
    }
    
    /**
     * 业务方法追踪
     */
    @Around("@annotation(businessTrace)")
    public Object traceBusinessMethod(ProceedingJoinPoint joinPoint, BusinessTrace businessTrace) throws Throwable {
        if (!tracingProperties.isBusinessTracingEnabled()) {
            return joinPoint.proceed();
        }
        
        String operationName = businessTrace.value().isEmpty() ? 
            joinPoint.getSignature().getName() : businessTrace.value();
            
        Span span = tracer.nextSpan()
                .name("business." + operationName)
                .tag("business.operation", operationName)
                .tag("business.class", joinPoint.getTarget().getClass().getSimpleName())
                .tag("business.method", joinPoint.getSignature().getName())
                .start();
                
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // 添加业务参数
            addBusinessParameters(span, joinPoint, businessTrace);
            
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            // 记录执行时间
            span.tag("business.duration", String.valueOf(duration));
            
            // 检查是否为慢操作
            if (duration > tracingProperties.getSlowApiThreshold()) {
                span.tag("business.slow", "true");
                log.warn("Slow business operation detected: {} took {}ms", operationName, duration);
            }
            
            // 添加业务结果
            addBusinessResult(span, result, businessTrace);
            
            return result;
            
        } catch (Exception e) {
            span.tag("error", true)
                .tag("error.message", e.getMessage())
                .tag("error.class", e.getClass().getSimpleName());
            throw e;
        } finally {
            span.end();
        }
    }
    
    /**
     * 数据库操作追踪
     */
    @Around("execution(* com.chennian.storytelling.dao..*.*(..))")
    public Object traceDatabaseOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        Span span = tracer.nextSpan()
                .name("db." + methodName)
                .tag("db.operation", methodName)
                .tag("db.mapper", className)
                .tag("component", "mybatis")
                .start();
                
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            span.tag("db.duration", String.valueOf(duration));
            
            // 检查慢查询
            if (duration > tracingProperties.getSlowQueryThreshold()) {
                span.tag("db.slow", "true");
                log.warn("Slow database query detected: {}.{} took {}ms", className, methodName, duration);
            }
            
            return result;
            
        } catch (Exception e) {
            span.tag("error", true)
                .tag("error.message", e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
    
    /**
     * 缓存操作追踪
     */
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) || " +
            "@annotation(org.springframework.cache.annotation.CachePut) || " +
            "@annotation(org.springframework.cache.annotation.CacheEvict)")
    public Object traceCacheOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        Span span = tracer.nextSpan()
                .name("cache." + methodName)
                .tag("cache.operation", methodName)
                .tag("cache.class", className)
                .tag("component", "redis")
                .start();
                
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return joinPoint.proceed();
        } catch (Exception e) {
            span.tag("error", true)
                .tag("error.message", e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
    
    private void addBusinessParameters(Span span, ProceedingJoinPoint joinPoint, BusinessTrace businessTrace) {
        if (!businessTrace.includeParameters()) {
            return;
        }
        
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        
        for (int i = 0; i < args.length && i < paramNames.length; i++) {
            if (args[i] != null) {
                String paramValue = args[i].toString();
                // 限制参数长度，避免过长的追踪数据
                if (paramValue.length() > 100) {
                    paramValue = paramValue.substring(0, 100) + "...";
                }
                span.tag("business.param." + paramNames[i], paramValue);
            }
        }
    }
    
    private void addBusinessResult(Span span, Object result, BusinessTrace businessTrace) {
        if (!businessTrace.includeResult() || result == null) {
            return;
        }
        
        String resultValue = result.toString();
        if (resultValue.length() > 100) {
            resultValue = resultValue.substring(0, 100) + "...";
        }
        span.tag("business.result", resultValue);
    }
}
```

#### BusinessTrace.java
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessTrace {
    
    /**
     * 操作名称
     */
    String value() default "";
    
    /**
     * 是否包含参数
     */
    boolean includeParameters() default false;
    
    /**
     * 是否包含返回结果
     */
    boolean includeResult() default false;
    
    /**
     * 业务类型
     */
    String businessType() default "";
    
    /**
     * 自定义标签
     */
    String[] tags() default {};
}
```

### 3.5 链路上下文传递

#### TracingContextHolder.java
```java
@Component
@Slf4j
public class TracingContextHolder {
    
    private final Tracer tracer;
    
    public TracingContextHolder(Tracer tracer) {
        this.tracer = tracer;
    }
    
    /**
     * 获取当前TraceId
     */
    public String getCurrentTraceId() {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().traceId();
        }
        return null;
    }
    
    /**
     * 获取当前SpanId
     */
    public String getCurrentSpanId() {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().spanId();
        }
        return null;
    }
    
    /**
     * 设置业务标签
     */
    public void setBusinessTag(String key, String value) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("business." + key, value);
        }
    }
    
    /**
     * 设置用户信息
     */
    public void setUserInfo(String userId, String userName) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("user.id", userId)
                      .tag("user.name", userName);
        }
        
        // 设置Baggage，用于跨服务传递
        BaggageField userIdField = BaggageField.create("user-id");
        userIdField.updateValue(userId);
    }
    
    /**
     * 设置租户信息
     */
    public void setTenantInfo(String tenantId) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("tenant.id", tenantId);
        }
        
        BaggageField tenantIdField = BaggageField.create("tenant-id");
        tenantIdField.updateValue(tenantId);
    }
    
    /**
     * 创建子Span
     */
    public Span createChildSpan(String operationName) {
        return tracer.nextSpan()
                .name(operationName)
                .start();
    }
    
    /**
     * 异步执行时传递追踪上下文
     */
    public <T> CompletableFuture<T> traceAsync(Supplier<T> supplier) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan == null) {
            return CompletableFuture.supplyAsync(supplier);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try (Tracer.SpanInScope ws = tracer.withSpanInScope(currentSpan)) {
                return supplier.get();
            }
        });
    }
}
```

## 4. Gateway 链路追踪集成

### 4.1 Gateway 追踪配置

#### GatewayTracingConfiguration.java
```java
@Configuration
@Slf4j
public class GatewayTracingConfiguration {
    
    /**
     * Gateway 追踪过滤器
     */
    @Bean
    public GlobalFilter tracingFilter(Tracer tracer) {
        return new TracingGlobalFilter(tracer);
    }
    
    /**
     * WebClient 追踪配置
     */
    @Bean
    public WebClient tracingWebClient(HttpTracing httpTracing) {
        return WebClient.builder()
                .filter(TracingExchangeFilterFunction.create(httpTracing))
                .build();
    }
}
```

#### TracingGlobalFilter.java
```java
@Component
@Slf4j
public class TracingGlobalFilter implements GlobalFilter, Ordered {
    
    private final Tracer tracer;
    
    public TracingGlobalFilter(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 创建Gateway Span
        Span gatewaySpan = tracer.nextSpan()
                .name("gateway." + request.getMethod().name())
                .tag("component", "spring-cloud-gateway")
                .tag("http.method", request.getMethod().name())
                .tag("http.url", request.getURI().toString())
                .tag("http.path", request.getPath().value())
                .start();
        
        // 添加请求头信息
        addRequestHeaders(gatewaySpan, request);
        
        // 添加路由信息
        addRouteInfo(gatewaySpan, exchange);
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(gatewaySpan)) {
            return chain.filter(exchange)
                    .doOnSuccess(aVoid -> {
                        // 添加响应信息
                        ServerHttpResponse response = exchange.getResponse();
                        gatewaySpan.tag("http.status_code", String.valueOf(response.getStatusCode().value()));
                        
                        if (response.getStatusCode().is5xxServerError()) {
                            gatewaySpan.tag("error", true);
                        }
                    })
                    .doOnError(throwable -> {
                        gatewaySpan.tag("error", true)
                                  .tag("error.message", throwable.getMessage())
                                  .tag("error.class", throwable.getClass().getSimpleName());
                    })
                    .doFinally(signalType -> gatewaySpan.end());
        }
    }
    
    private void addRequestHeaders(Span span, ServerHttpRequest request) {
        // 添加重要的请求头
        HttpHeaders headers = request.getHeaders();
        
        String userAgent = headers.getFirst("User-Agent");
        if (userAgent != null) {
            span.tag("http.user_agent", userAgent);
        }
        
        String clientIp = getClientIp(request);
        if (clientIp != null) {
            span.tag("http.client_ip", clientIp);
        }
        
        String authorization = headers.getFirst("Authorization");
        if (authorization != null) {
            span.tag("http.has_auth", "true");
        }
    }
    
    private void addRouteInfo(Span span, ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {
            span.tag("gateway.route.id", route.getId())
                .tag("gateway.route.uri", route.getUri().toString());
        }
        
        URI requestUrl = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        if (requestUrl != null) {
            span.tag("gateway.target.url", requestUrl.toString());
        }
    }
    
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
            request.getRemoteAddress().getAddress().getHostAddress() : null;
    }
    
    @Override
    public int getOrder() {
        return -1; // 最高优先级
    }
}
```

## 5. 链路追踪最佳实践

### 5.1 采样策略
```yaml
# 环境相关采样配置
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  sleuth:
    sampler:
      probability: ${TRACING_SAMPLE_RATE:1.0}  # 通过环境变量控制
      
---
# 开发环境
spring:
  profiles: dev
  sleuth:
    sampler:
      probability: 1.0  # 100%采样
      
---
# 测试环境
spring:
  profiles: test
  sleuth:
    sampler:
      probability: 0.5  # 50%采样
      
---
# 生产环境
spring:
  profiles: prod
  sleuth:
    sampler:
      probability: 0.1  # 10%采样
```

### 5.2 性能优化
```yaml
# 性能优化配置
spring:
  sleuth:
    zipkin:
      sender:
        type: kafka  # 使用Kafka异步发送，减少对应用性能的影响
      compression:
        enabled: true  # 启用压缩
    
    # 限制Span数据大小
    span:
      max-annotation-events: 50
      max-message-events: 50
      max-link-count: 50
      
    # 异步处理
    async:
      enabled: true
      configurer:
        enabled: true
        
zipkin:
  reporter:
    kafka:
      bootstrap-servers: ${KAFKA_SERVERS:localhost:9092}
      topic: zipkin
      # 批量发送配置
      batch-size: 16384
      linger-ms: 5
      buffer-memory: 33554432
```

### 5.3 错误处理
```java
@Component
@Slf4j
public class TracingErrorHandler {
    
    private final Tracer tracer;
    
    public TracingErrorHandler(Tracer tracer) {
        this.tracer = tracer;
    }
    
    /**
     * 全局异常处理
     */
    @EventListener
    public void handleException(ApplicationEvent event) {
        if (event instanceof ExceptionEvent) {
            ExceptionEvent exceptionEvent = (ExceptionEvent) event;
            recordException(exceptionEvent.getException(), exceptionEvent.getContext());
        }
    }
    
    /**
     * 记录异常信息到当前Span
     */
    public void recordException(Throwable exception, Map<String, String> context) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("error", true)
                      .tag("error.class", exception.getClass().getSimpleName())
                      .tag("error.message", exception.getMessage());
                      
            // 添加堆栈信息（限制长度）
            String stackTrace = getStackTrace(exception);
            if (stackTrace.length() > 1000) {
                stackTrace = stackTrace.substring(0, 1000) + "...";
            }
            currentSpan.tag("error.stack", stackTrace);
            
            // 添加上下文信息
            if (context != null) {
                context.forEach((key, value) -> 
                    currentSpan.tag("error.context." + key, value));
            }
        }
    }
    
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
```

## 6. 监控和告警

### 6.1 Zipkin 监控指标
```yaml
# Prometheus 监控Zipkin
scrape_configs:
  - job_name: 'zipkin'
    static_configs:
      - targets: ['zipkin:9411']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 30s
```

### 6.2 链路追踪告警规则
```yaml
# 链路追踪告警规则
groups:
  - name: tracing.rules
    rules:
      # 追踪数据丢失告警
      - alert: TracingDataLoss
        expr: rate(zipkin_collector_spans_dropped_total[5m]) > 0
        for: 2m
        labels:
          severity: warning
        annotations:
          summary: "Tracing data is being dropped"
          description: "Zipkin is dropping spans at a rate of {{ $value }} per second"
          
      # 高错误率告警
      - alert: HighErrorRate
        expr: rate(zipkin_collector_spans_total{result="error"}[5m]) / rate(zipkin_collector_spans_total[5m]) > 0.1
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High error rate in traces"
          description: "Error rate in traces is {{ $value | humanizePercentage }}"
          
      # 慢请求告警
      - alert: SlowRequests
        expr: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Slow requests detected"
          description: "95th percentile response time is {{ $value }}s"
```

## 7. 验证方案

### 7.1 功能验证
- [ ] Zipkin UI 正常访问
- [ ] 链路数据正常收集
- [ ] 跨服务链路追踪正常
- [ ] 自定义Span正常创建
- [ ] 错误信息正常记录

### 7.2 性能验证
- [ ] 追踪对应用性能影响 < 5%
- [ ] Zipkin 查询响应时间 < 2s
- [ ] 数据存储空间合理

### 7.3 可用性验证
- [ ] Zipkin 高可用部署
- [ ] 数据备份恢复正常
- [ ] 链路数据不丢失

## 8. 故障排查指南

### 8.1 常见问题
```bash
# 1. 检查Zipkin服务状态
curl http://zipkin:9411/health

# 2. 检查应用追踪配置
curl http://app:8080/actuator/sleuth

# 3. 检查Elasticsearch存储
curl http://elasticsearch:9200/_cluster/health

# 4. 查看追踪日志
docker logs storytelling-zipkin
```

### 8.2 性能调优
```yaml
# JVM调优
ZIPKIN_JAVA_OPTS: >
  -Xms1g -Xmx2g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=100
  -XX:+UseStringDeduplication
  
# Elasticsearch调优
ES_JAVA_OPTS: >
  -Xms1g -Xmx1g
  -XX:+UseG1GC
```