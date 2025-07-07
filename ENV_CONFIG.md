# 环境变量配置说明

本文档说明了项目中使用的环境变量配置，用于提高应用的安全性和灵活性。

## 数据库配置

### MySQL 数据库
- `DB_URL`: 数据库连接URL
  - 默认值: `jdbc:mysql://localhost:3306/storytelling?allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true`
- `DB_USERNAME`: 数据库用户名
  - 默认值: `root`
- `DB_PASSWORD`: 数据库密码
  - 默认值: `12345678`
  - **生产环境必须修改**

## Redis 配置

- `REDIS_HOST`: Redis服务器地址
  - 默认值: `localhost`
- `REDIS_PORT`: Redis端口
  - 默认值: `6379`
- `REDIS_DATABASE`: Redis数据库索引
  - 默认值: `0`
- `REDIS_PASSWORD`: Redis密码
  - 默认值: 空
  - **生产环境建议设置密码**

## RabbitMQ 配置

- `RABBITMQ_HOST`: RabbitMQ服务器地址
  - 默认值: `localhost`
- `RABBITMQ_PORT`: RabbitMQ端口
  - 默认值: `5672`
- `RABBITMQ_USERNAME`: RabbitMQ用户名
  - 默认值: `guest`
- `RABBITMQ_PASSWORD`: RabbitMQ密码
  - 默认值: `guest`
  - **生产环境必须修改**
- `RABBITMQ_VIRTUAL_HOST`: RabbitMQ虚拟主机
  - 默认值: `/`

## 安全配置

- `JWT_SECRET_KEY`: JWT签名密钥
  - 默认值: `default-secret-key-change-in-production`
  - **生产环境必须使用强密钥**
  - 建议使用至少32位的随机字符串

## 环境变量设置方法

### 1. 系统环境变量
在操作系统中设置环境变量：

**Windows:**
```cmd
set JWT_SECRET_KEY=your-strong-secret-key
set DB_PASSWORD=your-database-password
```

**Linux/Mac:**
```bash
export JWT_SECRET_KEY=your-strong-secret-key
export DB_PASSWORD=your-database-password
```

### 2. IDE 配置
在 IntelliJ IDEA 中：
1. 打开 Run/Debug Configurations
2. 在 Environment variables 中添加所需变量

### 3. Docker 配置
在 docker-compose.yml 中：
```yaml
services:
  app:
    environment:
      - JWT_SECRET_KEY=your-strong-secret-key
      - DB_PASSWORD=your-database-password
```

### 4. Kubernetes 配置
使用 ConfigMap 和 Secret：
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
data:
  JWT_SECRET_KEY: <base64-encoded-secret>
  DB_PASSWORD: <base64-encoded-password>
```

## 安全建议

1. **生产环境必须修改所有默认密码**
2. **JWT密钥应使用强随机字符串**
3. **敏感信息不要提交到版本控制系统**
4. **定期轮换密钥和密码**
5. **使用专门的密钥管理服务（如 HashiCorp Vault）**

## 验证配置

启动应用后，可以通过以下方式验证配置是否生效：

1. 检查应用日志，确认连接成功
2. 访问健康检查端点
3. 测试各个功能模块

如果遇到配置问题，请检查：
- 环境变量是否正确设置
- 服务是否正常运行
- 网络连接是否正常