# Storytelling 企业级管理系统

## 项目简介

Storytelling 是一个基于 Spring Boot 和微服务架构的企业级管理系统，集成了ERP、商城、工作流、报表等多个业务模块。系统采用前后端分离架构，支持分布式部署，具备高可用、高并发、易扩展的特点。

## 🏗️ 系统架构

### 技术栈

**后端技术栈：**
- **框架**: Spring Boot 3.2.12, Spring Cloud Alibaba
- **数据库**: MySQL 8.0, MongoDB
- **分库分表**: Apache ShardingSphere 5.4.1
- **缓存**: Redis (Redisson)
- **消息队列**: RabbitMQ
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
- **通知公告**: 系统通知、公告发布、消息推送

### 2. ERP管理
- **客户管理**: 客户信息维护、客户关系管理、客户分析
- **供应商管理**: 供应商信息、合作关系、供应商评估
- **产品管理**: 产品信息、规格管理、价格体系
- **库存管理**: 库存监控、出入库管理、库存预警
- **销售订单**: 订单管理、订单跟踪、销售分析
- **采购订单**: 采购计划、订单执行、供应商协同
- **供应链协同**: 供应链计划、绩效分析、事件管理、里程碑管理

### 3. 财务管理
- **会计科目**: 科目设置、科目体系、科目管理
- **应收应付**: 应收账款管理、应付账款管理、账龄分析
- **财务报表**: 资产负债表、利润表、现金流量表
- **财务分析**: 财务比率分析、趋势分析、财务仪表盘
- **成本会计**: 成本中心管理、产品成本分析、ABC成本法
- **资金管理**: 资金池管理、外汇风险管理、投资组合分析
- **财务交易**: 交易记录、凭证管理、审计跟踪

### 4. 制造管理
- **BOM管理**: 物料清单、BOM模板、物料结构
- **生产计划**: 生产排程、产能规划、计划执行
- **生产订单**: 工单管理、生产跟踪、进度监控
- **生产线管理**: 产线配置、产线监控、效率分析
- **设备管理**: 设备档案、设备维护、设备状态
- **工单管理**: 工单创建、工单执行、工单完成

### 5. 商城系统
- **用户管理**: 商城用户注册、登录、信息管理
- **商品管理**: 商品展示、分类管理、库存同步
- **订单管理**: 订单处理、子订单管理、物流跟踪
- **购物车**: 购物车管理、商品收藏、批量操作
- **优惠券**: 优惠券发放、使用管理、营销活动
- **搜索记录**: 用户搜索行为、热门搜索、搜索优化
- **分库分表**: 基于用户ID和订单ID的水平分片

### 6. 人力资源管理
- **员工管理**: 员工档案、入职离职、员工信息维护
- **部门管理**: 部门设置、组织架构、部门职责
- **岗位管理**: 岗位设置、岗位职责、岗位要求
- **考勤管理**: 考勤记录、请假管理、加班管理
- **薪资管理**: 薪资计算、薪资发放、薪资报表

### 7. 资产管理
- **资产信息**: 资产档案、资产分类、资产状态
- **资产折旧**: 折旧计算、折旧方法、折旧报表
- **资产盘点**: 盘点计划、盘点执行、盘点结果
- **资产维护**: 维护计划、维护记录、维护成本

### 8. 项目管理
- **项目信息**: 项目创建、项目档案、项目状态
- **任务管理**: 任务分配、任务跟踪、任务完成
- **里程碑**: 里程碑设置、进度跟踪、关键节点
- **资源管理**: 资源分配、资源利用率、资源冲突
- **风险管理**: 风险识别、风险评估、风险应对

### 9. 质量管理
- **质量标准**: 质量标准制定、标准维护、标准执行
- **质量检验**: 检验计划、检验执行、检验报告
- **质量问题**: 问题记录、问题分析、问题处理
- **质量改进**: 改进计划、改进措施、改进效果

### 10. 工作流引擎
- **流程设计**: 可视化流程设计器、BPMN 2.0支持
- **流程分类**: 流程分类管理、流程模板
- **任务管理**: 待办任务、已办任务、任务委派
- **流程实例**: 流程启动、流程跟踪、流程监控
- **权限管理**: 流程权限、角色权限、用户权限
- **通知模板**: 消息通知、邮件提醒、系统配置

### 11. 报表中心
- **报表模板**: 模板设计、参数配置
- **日报周报**: 日报生成、周报汇总、定期报告
- **数据可视化**: 图表展示、仪表板
- **定时报表**: 自动生成、邮件推送

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
- **配置安全**: 敏感信息分离存储，支持环境变量和外部配置文件
- **工作流API**: 统一的工作流API接口，支持版本控制和路径验证

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

**主配置文件**
```yaml
# 修改 storytelling-api/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/storytelling
    username: ${spring.datasource.username:}
    password: ${spring.datasource.password:}
  
  data:
    mongodb:
      host: localhost
      port: 27017
      database: storytelling
    
    redis:
      host: localhost
      port: 6379
      password: ${spring.data.redis.password:}
```

**敏感信息配置**
```yaml
# 创建 application-secrets.yml (不提交到版本控制)
spring:
  datasource:
    username: your_username
    password: your_password
  
  data:
    redis:
      password: your_redis_password
  
  mail:
    username: your_email@example.com
    password: your_email_password

# 腾讯云配置
file:
  tencent:
    secretId: your_secret_id
    secretKey: your_secret_key
    bucketName: your_bucket_name
```

#### 4. 启动服务
```bash
# 启动后端服务
mvn clean install
java -jar storytelling-api/target/storytelling-api.jar

# 启动前端服务
cd storytelling-ui
npm install
npm run dev
```

## 🔧 配置管理

### 配置文件结构
- `application.yml` - 主配置文件，包含通用配置
- `application-dev.yml` - 开发环境配置
- `application-prod.yml` - 生产环境配置
- `application-secrets.yml` - 敏感信息配置（不提交到版本控制）

### 安全配置最佳实践
1. **敏感信息分离**: 将数据库密码、API密钥等敏感信息存储在独立的配置文件中
2. **环境变量支持**: 支持通过环境变量覆盖配置项
3. **版本控制排除**: 确保敏感配置文件不被提交到版本控制系统
4. **配置加密**: 生产环境建议对敏感配置进行加密存储

### 工作流API配置
系统集成了统一的工作流API接口，支持以下特性：
- **版本控制**: API版本管理，向后兼容
- **路径验证**: 严格的路径参数验证，防止非法访问
- **权限控制**: 基于角色的API访问控制
- **审计日志**: 完整的API调用日志记录

```yaml
# 工作流API配置示例
workflow:
  api:
    version: v1
    base-path: /api/workflow
    path-validation:
      enabled: true
      task-id:
        pattern: '[a-zA-Z0-9_-]+'
      process-instance-id:
        pattern: '[a-zA-Z0-9_-]+'
      process-definition-id:
         pattern: '[a-zA-Z0-9_-]+'
```

## 📋 部署说明

### 开发环境部署
1. 确保所有依赖服务已启动（MySQL、Redis、MongoDB、Nacos）
2. 创建并配置 `application-secrets.yml` 文件
3. 执行数据库初始化脚本
4. 启动后端服务和前端服务

### 生产环境部署
1. **容器化部署**: 支持Docker容器化部署
2. **配置外部化**: 敏感配置通过环境变量或外部配置中心管理
3. **负载均衡**: 配置Nginx或其他负载均衡器
4. **监控告警**: 集成监控系统，设置关键指标告警
5. **备份策略**: 定期备份数据库和重要配置文件

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：
- 🐛 Bug修复
- ✨ 新功能开发
- 📝 文档改进
- 🎨 UI/UX优化
- 🔧 性能优化

### 贡献流程
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系我们

- **项目维护者**: [Your Name](mailto:your.email@example.com)
- **问题反馈**: [GitHub Issues](https://github.com/your-repo/storytelling/issues)
- **技术交流**: [项目讨论区](https://github.com/your-repo/storytelling/discussions)

---

**感谢您对 Storytelling 项目的关注和支持！** 🎉
