# Gateway 路由规则优化方案

## 1. 当前Gateway分析

### 现有配置状态
- ✅ 已集成 Spring Cloud Gateway
- ✅ 已配置基础路由规则
- ✅ 已集成 Sentinel 限流
- ✅ 已配置 CORS 跨域

### 需要优化的点
1. **路由规则标准化**
2. **负载均衡策略优化**
3. **限流熔断增强**
4. **安全认证统一**
5. **监控指标完善**

## 2. Gateway 路由优化实施

### 2.1 路由规则标准化

#### 优化后的Gateway配置
```yaml
spring:
  cloud:
    gateway:
      # 服务发现配置
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
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
      
      # 路由配置
      routes:
        # ==================== 管理后台路由 ====================
        - id: storytelling-admin-route
          uri: lb://storytelling-admin
          predicates:
            - Path=/admin/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 50
                redis-rate-limiter.burstCapacity: 100
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: admin-circuit-breaker
                fallbackUri: forward:/fallback/admin
            - name: Retry
              args:
                retries: 3
                statuses: BAD_GATEWAY,GATEWAY_TIMEOUT
                methods: GET
                backoff:
                  firstBackoff: 50ms
                  maxBackoff: 500ms
          metadata:
            response-timeout: 30000
            connect-timeout: 10000
        
        # ==================== API服务路由 ====================
        - id: storytelling-api-route
          uri: lb://storytelling-api
          predicates:
            - Path=/app/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: api-circuit-breaker
                fallbackUri: forward:/fallback/api
            - name: Retry
              args:
                retries: 2
                statuses: BAD_GATEWAY,GATEWAY_TIMEOUT
                methods: GET
          metadata:
            response-timeout: 20000
            connect-timeout: 5000
        
        # ==================== 用户服务路由（微服务准备）====================
        - id: storytelling-user-service-route
          uri: lb://storytelling-user-service
          predicates:
            - Path=/user/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 200
                redis-rate-limiter.burstCapacity: 400
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: user-service-circuit-breaker
                fallbackUri: forward:/fallback/user
          metadata:
            response-timeout: 15000
            connect-timeout: 3000
        
        # ==================== 商品服务路由（微服务准备）====================
        - id: storytelling-product-service-route
          uri: lb://storytelling-product-service
          predicates:
            - Path=/product/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 150
                redis-rate-limiter.burstCapacity: 300
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@ipKeyResolver}"
            - name: CircuitBreaker
              args:
                name: product-service-circuit-breaker
                fallbackUri: forward:/fallback/product
          metadata:
            response-timeout: 10000
            connect-timeout: 3000
        
        # ==================== 订单服务路由（微服务准备）====================
        - id: storytelling-order-service-route
          uri: lb://storytelling-order-service
          predicates:
            - Path=/order/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 80
                redis-rate-limiter.burstCapacity: 160
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: order-service-circuit-breaker
                fallbackUri: forward:/fallback/order
          metadata:
            response-timeout: 25000
            connect-timeout: 5000
        
        # ==================== 库存服务路由（微服务准备）====================
        - id: storytelling-inventory-service-route
          uri: lb://storytelling-inventory-service
          predicates:
            - Path=/inventory/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 120
                redis-rate-limiter.burstCapacity: 240
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@ipKeyResolver}"
            - name: CircuitBreaker
              args:
                name: inventory-service-circuit-breaker
                fallbackUri: forward:/fallback/inventory
          metadata:
            response-timeout: 8000
            connect-timeout: 2000
        
        # ==================== 支付服务路由（微服务准备）====================
        - id: storytelling-payment-service-route
          uri: lb://storytelling-payment-service
          predicates:
            - Path=/payment/**
            - Method=GET,POST,PUT,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 60
                redis-rate-limiter.burstCapacity: 120
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
            - name: CircuitBreaker
              args:
                name: payment-service-circuit-breaker
                fallbackUri: forward:/fallback/payment
          metadata:
            response-timeout: 30000
            connect-timeout: 5000
        
        # ==================== 搜索服务路由（微服务准备）====================
        - id: storytelling-search-service-route
          uri: lb://storytelling-search-service
          predicates:
            - Path=/search/**
            - Method=GET,POST
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 300
                redis-rate-limiter.burstCapacity: 600
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@ipKeyResolver}"
            - name: CircuitBreaker
              args:
                name: search-service-circuit-breaker
                fallbackUri: forward:/fallback/search
          metadata:
            response-timeout: 5000
            connect-timeout: 2000
        
        # ==================== 文件服务路由 ====================
        - id: storytelling-file-service-route
          uri: lb://storytelling-file-service
          predicates:
            - Path=/file/**
            - Method=GET,POST,DELETE
          filters:
            - name: StripPrefix
              args:
                parts: 1
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 50
                redis-rate-limiter.burstCapacity: 100
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@ipKeyResolver}"
          metadata:
            response-timeout: 60000
            connect-timeout: 10000
      
      # 全局过滤器
      default-filters:
        - DedupeResponseHeader=X-Real-IP, RETAIN_UNIQUE
        - AddRequestHeader=X-Gateway-Version, 1.0.0
        - AddRequestHeader=X-Request-Time, #{T(System).currentTimeMillis()}
        - AddResponseHeader=X-Response-Time, #{T(System).currentTimeMillis()}
        - name: RequestSize
          args:
            maxSize: 10MB
      
      # CORS 配置优化
      globalcors:
        add-to-simple-url-handler-mapping: true
        cors-configurations:
          '[/**]':
            allowed-origin-patterns: 
              - "http://localhost:3000"
              - "http://localhost:8080"
              - "http://localhost:8081"
              - "https://*.storytelling.com"
              - "https://*.storytelling.cn"
            allowed-methods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
              - PATCH
              - HEAD
            allowed-headers: 
              - "*"
            exposed-headers:
              - Authorization
              - Content-Disposition
              - X-Total-Count
              - X-Request-Id
              - X-Response-Time
            allow-credentials: true
            max-age: 3600

# 负载均衡配置
spring:
  cloud:
    loadbalancer:
      ribbon:
        enabled: false
      cache:
        enabled: true
        ttl: 35s
        capacity: 256
      health-check:
        initial-delay: 0s
        interval: 25s
        path:
          default: /actuator/health
```

### 2.2 自定义过滤器实现

#### 请求日志过滤器
```java
@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString();
        
        // 添加请求ID到Header
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-Request-Id", requestId)
                .build();
        
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
        
        long startTime = System.currentTimeMillis();
        
        log.info("Gateway Request - ID: {}, Method: {}, URI: {}, RemoteAddr: {}", 
                requestId, request.getMethod(), request.getURI(), 
                request.getRemoteAddress());
        
        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            ServerHttpResponse response = exchange.getResponse();
            
            log.info("Gateway Response - ID: {}, Status: {}, Duration: {}ms", 
                    requestId, response.getStatusCode(), (endTime - startTime));
        }));
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}
```

#### 认证过滤器
```java
@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    
    private final List<String> skipAuthPaths = Arrays.asList(
            "/admin/login",
            "/app/login",
            "/app/register",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 跳过不需要认证的路径
        if (skipAuthPaths.stream().anyMatch(skipPath -> 
                new AntPathMatcher().match(skipPath, path))) {
            return chain.filter(exchange);
        }
        
        // 获取Token
        String token = request.getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst("token");
        }
        
        if (StringUtils.isEmpty(token)) {
            return unauthorized(exchange);
        }
        
        try {
            // 验证Token（使用Sa-Token）
            StpUtil.checkLogin(token);
            
            // 添加用户信息到Header
            String userId = StpUtil.getLoginIdAsString();
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Token", token)
                    .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
            
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return unauthorized(exchange);
        }
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        
        String body = "{\"code\":401,\"message\":\"Unauthorized\",\"data\":null}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
}
```

### 2.3 限流Key解析器

#### 用户限流Key解析器
```java
@Component("userKeyResolver")
public class UserKeyResolver implements KeyResolver {
    
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-User-Id"))
                .switchIfEmpty(Mono.just("anonymous"))
                .map(userId -> "user:" + userId);
    }
}
```

#### IP限流Key解析器
```java
@Component("ipKeyResolver")
public class IpKeyResolver implements KeyResolver {
    
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(getClientIp(exchange.getRequest()))
                .map(ip -> "ip:" + ip);
    }
    
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }
}
```

### 2.4 熔断降级处理

#### 降级处理器
```java
@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {
    
    @RequestMapping("/admin")
    public ResponseEntity<Map<String, Object>> adminFallback() {
        log.warn("Admin service fallback triggered");
        return createFallbackResponse("管理服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/api")
    public ResponseEntity<Map<String, Object>> apiFallback() {
        log.warn("API service fallback triggered");
        return createFallbackResponse("API服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/user")
    public ResponseEntity<Map<String, Object>> userFallback() {
        log.warn("User service fallback triggered");
        return createFallbackResponse("用户服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/product")
    public ResponseEntity<Map<String, Object>> productFallback() {
        log.warn("Product service fallback triggered");
        return createFallbackResponse("商品服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/order")
    public ResponseEntity<Map<String, Object>> orderFallback() {
        log.warn("Order service fallback triggered");
        return createFallbackResponse("订单服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/inventory")
    public ResponseEntity<Map<String, Object>> inventoryFallback() {
        log.warn("Inventory service fallback triggered");
        return createFallbackResponse("库存服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/payment")
    public ResponseEntity<Map<String, Object>> paymentFallback() {
        log.warn("Payment service fallback triggered");
        return createFallbackResponse("支付服务暂时不可用，请稍后重试");
    }
    
    @RequestMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFallback() {
        log.warn("Search service fallback triggered");
        return createFallbackResponse("搜索服务暂时不可用，请稍后重试");
    }
    
    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", message);
        result.put("data", null);
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
    }
}
```

### 2.5 健康检查配置

#### 健康检查指标
```java
@Component
public class GatewayHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @Override
    public Health health() {
        try {
            List<String> services = discoveryClient.getServices();
            
            Health.Builder builder = Health.up()
                    .withDetail("gateway.version", "1.0.0")
                    .withDetail("services.count", services.size())
                    .withDetail("services.list", services);
            
            // 检查关键服务状态
            checkCriticalServices(builder, services);
            
            return builder.build();
            
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
    
    private void checkCriticalServices(Health.Builder builder, List<String> services) {
        String[] criticalServices = {"storytelling-admin", "storytelling-api"};
        
        for (String service : criticalServices) {
            boolean available = services.contains(service);
            builder.withDetail("service." + service, available ? "UP" : "DOWN");
        }
    }
}
```

## 3. 监控指标配置

### 3.1 Prometheus 指标
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,gateway
  endpoint:
    health:
      show-details: always
    gateway:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: storytelling-gateway
      environment: ${spring.profiles.active}
```

### 3.2 自定义指标
```java
@Component
@Slf4j
public class GatewayMetricsFilter implements GlobalFilter, Ordered {
    
    private final MeterRegistry meterRegistry;
    private final Counter requestCounter;
    private final Timer requestTimer;
    
    public GatewayMetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestCounter = Counter.builder("gateway_requests_total")
                .description("Total number of requests")
                .register(meterRegistry);
        this.requestTimer = Timer.builder("gateway_request_duration")
                .description("Request duration")
                .register(meterRegistry);
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Timer.Sample sample = Timer.start(meterRegistry);
        
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    requestCounter.increment(
                            Tags.of(
                                    "method", exchange.getRequest().getMethod().name(),
                                    "status", String.valueOf(exchange.getResponse().getStatusCode().value()),
                                    "path", exchange.getRequest().getPath().value()
                            )
                    );
                    sample.stop(requestTimer);
                })
                .doOnError(throwable -> {
                    requestCounter.increment(
                            Tags.of(
                                    "method", exchange.getRequest().getMethod().name(),
                                    "status", "error",
                                    "path", exchange.getRequest().getPath().value()
                            )
                    );
                    sample.stop(requestTimer);
                });
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
}
```

## 4. 部署配置

### 4.1 Docker 配置
```dockerfile
FROM openjdk:17-jre-slim

VOLUME /tmp

COPY target/storytelling-gateway-*.jar app.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 4.2 Kubernetes 配置
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: storytelling-gateway
  labels:
    app: storytelling-gateway
spec:
  replicas: 2
  selector:
    matchLabels:
      app: storytelling-gateway
  template:
    metadata:
      labels:
        app: storytelling-gateway
    spec:
      containers:
      - name: storytelling-gateway
        image: storytelling/gateway:latest
        ports:
        - containerPort: 8100
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: NACOS_SERVER
          value: "nacos-service:8848"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8100
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8100
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: storytelling-gateway-service
spec:
  selector:
    app: storytelling-gateway
  ports:
  - protocol: TCP
    port: 8100
    targetPort: 8100
  type: LoadBalancer
```

## 5. 验证方案

### 5.1 功能验证
- [ ] 路由转发正常
- [ ] 限流熔断生效
- [ ] 认证授权正常
- [ ] 负载均衡正常
- [ ] 健康检查正常

### 5.2 性能验证
- [ ] 网关延迟 < 10ms
- [ ] 吞吐量 > 1000 QPS
- [ ] 内存使用 < 1GB
- [ ] CPU使用 < 80%

### 5.3 稳定性验证
- [ ] 服务重启后路由恢复
- [ ] 后端服务异常时降级正常
- [ ] 高并发下网关稳定