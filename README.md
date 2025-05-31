# Storytelling 企业级管理系统

## 项目简介

Storytelling 是一个基于 Spring Boot 和微服务架构的企业级管理系统，集成了ERP、商城、工作流、报表等多个业务模块。系统采用前后端分离架构，支持分布式部署，具备高可用、高并发、易扩展的特点。

## 🏗️ 系统架构

### 技术栈

**后端技术栈：**
- **框架**: Spring Boot 2.7.18, Spring Cloud Alibaba
- **数据库**: MySQL 8.0, MongoDB
- **分库分表**: Apache ShardingSphere 5.4.1
- **缓存**: Redis (Redisson)
- **消息队列**: RocketMQ
- **工作流引擎**: Flowable
- **权限认证**: Sa-Token
- **服务注册与发现**: Nacos
- **服务熔断**: Sentinel
- **网关**: Spring Cloud Gateway
- **任务调度**: XXL-JOB
- **文档**: Knife4j (Swagger)
- **对象存储**: 腾讯云COS
- **支付**: 微信支付、支付宝

**前端技术栈：**
- **框架**: Vue 3
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **国际化**: Vue I18n
- **图表**: ECharts
- **HTTP客户端**: Axios

### 模块架构

```
storytelling/
├── gateway/                    # 网关服务
├── storytelling-admin/         # 后台管理服务
├── storytelling-api/           # 前台API服务
├── storytelling-bean/          # 实体类模块
├── storytelling-common/        # 公共工具模块
├── storytelling-service/       # 业务服务模块
├── storytelling-security/      # 安全认证模块
├── storytelling-report/        # 报表服务模块
└── storytelling-ui/           # 前端项目
```

## 🚀 核心功能

### 1. 系统管理
- **用户管理**: 用户信息维护、角色分配、权限控制
- **角色管理**: 角色定义、权限分配、数据权限
- **菜单管理**: 系统菜单配置、权限控制
- **部门管理**: 组织架构管理、层级关系
- **岗位管理**: 岗位信息维护、职责定义
- **日志管理**: 操作日志、登录日志、系统日志

### 2. ERP管理
- **客户管理**: 客户信息维护、客户关系管理、客户分析
- **供应商管理**: 供应商信息、合作关系、供应商评估
- **产品管理**: 产品信息、规格管理、价格体系
- **库存管理**: 库存监控、出入库管理、库存预警
- **销售订单**: 订单管理、订单跟踪、销售分析
- **采购订单**: 采购计划、订单执行、供应商协同
- **财务管理**: 凭证管理、成本核算、税务管理
- **供应链协同**: 供应链计划、绩效分析、事件管理

### 3. 商城系统
- **用户管理**: 商城用户注册、登录、信息管理
- **商品管理**: 商品展示、分类管理、库存同步
- **订单管理**: 订单处理、支付集成、物流跟踪
- **分库分表**: 基于用户ID和订单ID的水平分片
- **微信集成**: 微信小程序、公众号、支付

### 4. 工作流引擎
- **流程设计**: 可视化流程设计器、BPMN 2.0支持
- **流程部署**: 流程定义管理、版本控制
- **任务管理**: 待办任务、已办任务、任务委派
- **流程监控**: 流程实例跟踪、性能分析
- **业务集成**: 销售订单审批、采购审批等业务流程

### 5. 报表中心
- **报表模板**: 模板设计、参数配置
- **报表生成**: 动态数据查询、多格式导出
- **数据可视化**: 图表展示、仪表板
- **定时报表**: 自动生成、邮件推送

### 6. 会议系统
- **会议室管理**: 房间创建、权限控制
- **会议预约**: 会议安排、参会人员管理
- **在线会议**: 音视频通话、屏幕共享

## 🛠️ 技术特性

### 分布式架构
- **微服务**: 基于Spring Cloud的微服务架构
- **服务注册**: Nacos服务注册与发现
- **配置中心**: Nacos配置管理
- **服务网关**: Spring Cloud Gateway统一入口
- **负载均衡**: Ribbon客户端负载均衡
- **服务熔断**: Sentinel流量控制与熔断

### 数据存储
- **关系型数据库**: MySQL主从复制、读写分离
- **文档数据库**: MongoDB存储非结构化数据
- **分库分表**: ShardingSphere实现水平分片
- **缓存**: Redis分布式缓存
- **对象存储**: 腾讯云COS文件存储

### 安全认证
- **权限框架**: Sa-Token轻量级权限认证
- **JWT**: 无状态token认证
- **RBAC**: 基于角色的访问控制
- **数据权限**: 细粒度数据权限控制
- **接口加密**: 敏感数据传输加密

### 监控运维
- **链路追踪**: 分布式链路跟踪
- **性能监控**: 应用性能监控
- **日志收集**: 统一日志收集与分析
- **健康检查**: 服务健康状态监控

## 📦 快速开始

### 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **MongoDB**: 4.4+
- **Node.js**: 16+
- **Nacos**: 2.2+

### 安装部署

#### 1. 克隆项目
```bash
git clone https://github.com/your-repo/storytelling.git
cd storytelling
```

#### 2. 数据库初始化
```bash
# 执行SQL脚本
mysql -u root -p < sql/storytelling.sql
```

#### 3. 配置文件
```yaml
# 修改 storytelling-api/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/storytelling
    username: your_username
    password: your_password
  
  data:
    mongodb:
      host: localhost
      port: 27017
      database: storytelling
```
