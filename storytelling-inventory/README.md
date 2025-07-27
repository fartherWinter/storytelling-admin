# 库存管理模块 (Storytelling Inventory)

## 模块简介

库存管理模块是故事叙述系统的核心业务模块之一，负责管理商品库存、仓库信息、库存变动记录和操作日志等功能。该模块提供了完整的库存管理解决方案，支持多仓库管理、库存预警、库存锁定、预占库存、库存调拨等高级功能。

## 主要功能

### 🏪 仓库管理
- 多层级仓库结构支持
- 仓库类型管理（实体仓库、虚拟仓库、中转仓库、退货仓库）
- 仓库容量管理和使用率监控
- 仓库自动分配功能
- 仓库状态管理

### 📦 库存管理
- 商品库存信息管理
- 库存数量跟踪（总数量、可用数量、锁定数量、预占数量）
- 库存状态管理
- SKU编码管理
- 成本价管理

### 📊 库存操作
- 库存入库/出库操作
- 库存锁定/解锁功能
- 库存预占/释放功能
- 库存调拨操作
- 库存盘点功能

### ⚠️ 库存预警
- 低库存预警
- 缺货预警
- 库存阈值管理
- 自动预警通知

### 📝 记录管理
- 库存变动记录跟踪
- 操作日志记录
- 审计追踪
- 数据统计分析

## 技术架构

### 技术栈
- **框架**: Spring Boot 2.7.x
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus
- **缓存**: Redis
- **消息队列**: RabbitMQ
- **服务发现**: Nacos
- **分布式锁**: Redisson
- **API文档**: SpringDoc OpenAPI
- **监控**: Spring Boot Actuator

### 模块结构
```
storytelling-inventory/
├── src/main/java/com/chennian/storytelling/inventory/
│   ├── config/          # 配置类
│   ├── constant/        # 常量定义
│   ├── controller/      # REST控制器
│   ├── entity/          # 实体类
│   ├── exception/       # 异常类
│   ├── mapper/          # MyBatis映射器
│   ├── service/         # 服务接口
│   ├── service/impl/    # 服务实现
│   └── util/           # 工具类
├── src/main/resources/
│   ├── db/migration/    # 数据库迁移脚本
│   ├── mapper/          # MyBatis XML映射文件
│   └── application.yml  # 应用配置
└── pom.xml             # Maven配置
```

## 数据库设计

### 核心表结构

#### 仓库表 (warehouse)
- 支持多层级仓库结构
- 仓库类型、容量、状态管理
- 联系人信息和地址信息

#### 库存表 (inventory)
- 商品与仓库的库存关联
- 多种数量类型跟踪
- 库存阈值和预警设置

#### 库存变动记录表 (inventory_record)
- 记录所有库存变动操作
- 支持多种变动类型
- 操作人和时间跟踪

#### 库存操作日志表 (inventory_log)
- 详细的操作日志记录
- 请求参数和响应结果
- 性能监控数据

## API接口

### 库存管理接口
- `GET /inventory/{id}` - 根据ID查询库存
- `GET /inventory/product/{productId}/warehouse/{warehouseId}` - 查询指定商品和仓库的库存
- `GET /inventory/sku/{skuCode}` - 根据SKU编码查询库存
- `GET /inventory/list` - 分页查询库存列表
- `POST /inventory` - 创建库存
- `PUT /inventory/{id}` - 更新库存
- `DELETE /inventory/{id}` - 删除库存
- `POST /inventory/stock-in` - 库存入库
- `POST /inventory/stock-out` - 库存出库
- `POST /inventory/lock` - 锁定库存
- `POST /inventory/unlock` - 解锁库存
- `POST /inventory/reserve` - 预占库存
- `POST /inventory/release` - 释放预占库存
- `POST /inventory/transfer` - 库存调拨
- `POST /inventory/stocktaking` - 库存盘点

### 仓库管理接口
- `GET /warehouse/{id}` - 根据ID查询仓库
- `GET /warehouse/code/{code}` - 根据编码查询仓库
- `GET /warehouse/list` - 分页查询仓库列表
- `GET /warehouse/tree` - 获取仓库树结构
- `POST /warehouse` - 创建仓库
- `PUT /warehouse/{id}` - 更新仓库
- `DELETE /warehouse/{id}` - 删除仓库
- `PUT /warehouse/{id}/status` - 更新仓库状态
- `PUT /warehouse/{id}/default` - 设置默认仓库

### 记录查询接口
- `GET /inventory-record/list` - 查询库存变动记录
- `GET /inventory-log/list` - 查询库存操作日志
- `GET /inventory/stats` - 获取库存统计信息
- `GET /warehouse/stats` - 获取仓库统计信息

## 配置说明

### 应用配置 (application.yml)
```yaml
inventory:
  stock:
    low-threshold: 10          # 默认低库存阈值
    warning-threshold: 20      # 默认库存预警阈值
    lock-timeout: 30          # 库存锁定超时时间（分钟）
    reserve-timeout: 60       # 预占库存超时时间（分钟）
  warehouse:
    default-capacity: 10000   # 默认仓库容量
    capacity-warning-threshold: 80  # 容量预警阈值（百分比）
```

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/storytelling_inventory
    username: root
    password: 123456
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 2
```

## 部署指南

### 环境要求
- JDK 8+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- Nacos 2.0+

### 部署步骤

1. **数据库初始化**
   ```sql
   # 执行数据库初始化脚本
   mysql -u root -p < src/main/resources/db/migration/V1__init_inventory_tables.sql
   ```

2. **配置文件修改**
   ```bash
   # 修改application.yml中的数据库连接信息
   # 修改Redis连接信息
   # 修改Nacos服务地址
   ```

3. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

4. **启动服务**
   ```bash
   java -jar target/storytelling-inventory-1.0.0.jar
   ```

5. **验证部署**
   ```bash
   # 健康检查
   curl http://localhost:8083/inventory/actuator/health
   
   # API文档
   http://localhost:8083/inventory/swagger-ui.html
   ```

### Docker部署

```dockerfile
FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY target/storytelling-inventory-1.0.0.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# 构建镜像
docker build -t storytelling-inventory:1.0.0 .

# 运行容器
docker run -d -p 8083:8083 --name inventory-service storytelling-inventory:1.0.0
```

## 监控和运维

### 健康检查
- 健康检查端点: `/actuator/health`
- 应用信息端点: `/actuator/info`
- 指标监控端点: `/actuator/metrics`

### 日志管理
- 日志文件位置: `logs/storytelling-inventory.log`
- 日志级别配置: `logging.level.com.chennian.storytelling.inventory`

### 性能监控
- 数据库连接池监控: `/druid/index.html`
- Redis连接监控
- 接口响应时间监控

## 开发指南

### 本地开发环境搭建

1. **克隆代码**
   ```bash
   git clone <repository-url>
   cd storytelling/storytelling-inventory
   ```

2. **启动依赖服务**
   ```bash
   # 启动MySQL
   docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0
   
   # 启动Redis
   docker run -d --name redis -p 6379:6379 redis:6.0
   
   # 启动RabbitMQ
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8-management
   ```

3. **运行应用**
   ```bash
   mvn spring-boot:run
   ```

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一异常处理
- 完善的注释和文档

### 测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn jacoco:report
```

## 常见问题

### Q: 库存数量不一致怎么办？
A: 可以使用库存盘点功能进行库存校正，系统会记录盘点差异并生成调整记录。

### Q: 如何处理并发库存操作？
A: 系统使用分布式锁和数据库事务保证并发安全，同时提供库存锁定功能避免超卖。

### Q: 库存预警如何配置？
A: 可以在库存表中设置低库存阈值和预警阈值，系统会定时检查并发送预警通知。

### Q: 如何查看库存变动历史？
A: 系统完整记录了所有库存变动，可以通过库存变动记录接口查询详细历史。

## 联系方式

- 开发者: chennian
- 邮箱: chennian@example.com
- 项目地址: <repository-url>

## 版本历史

### v1.0.0 (2024-01-01)
- 初始版本发布
- 基础库存管理功能
- 仓库管理功能
- 库存变动记录
- 操作日志记录
- API接口文档

---

**注意**: 本文档会随着项目的发展持续更新，请关注最新版本。