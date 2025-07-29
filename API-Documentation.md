# Storytelling ERP 系统 API 接口文档

## 项目概述

Storytelling ERP 是一个基于 Spring Boot + Vue3 的企业资源规划系统，集成了系统管理、ERP、财务、制造、商城、人力资源、资产、项目、质量、工作流、报表等多个模块。

## 技术栈

### 后端技术
- Spring Boot 3.x
- MyBatis Plus
- Flowable 工作流引擎
- Sa-Token 权限认证
- Swagger/OpenAPI 3.0
- MySQL 数据库
- Redis 缓存
- 腾讯云对象存储

### 前端技术
- Vue 3.x
- Element Plus
- Vue Router 4.x
- Pinia 状态管理
- Axios HTTP 客户端
- ECharts 图表库
- Vue I18n 国际化

## API 基础信息

### 基础URL
```
http://localhost:8100
```

### 认证方式
使用 Sa-Token 进行身份认证，请求头需要携带：
```
satoken: {token_value}
```

### 响应格式
```json
{
  "code": "200",
  "msg": "操作成功",
  "data": {}
}
```

## 系统管理模块

### 用户管理 API

#### 1. 用户分页查询
- **接口地址**: `POST /system/user/page`
- **接口描述**: 分页查询用户列表
- **请求参数**:
```json
{
  "userName": "用户名",
  "nickName": "昵称",
  "email": "邮箱",
  "phoneNumber": "手机号",
  "sex": "性别(0男1女2未知)",
  "status": "状态(0正常1停用)",
  "deptId": "部门ID",
  "pageNum": 1,
  "pageSize": 10
}
```
- **响应数据**:
```json
{
  "code": "200",
  "msg": "查询成功",
  "data": {
    "records": [
      {
        "userId": 1,
        "userName": "admin",
        "nickName": "管理员",
        "email": "admin@example.com",
        "phoneNumber": "13800138000",
        "sex": "0",
        "status": "0",
        "createTime": "2023-01-01 00:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

#### 2. 获取用户详情
- **接口地址**: `POST /system/user/info/{userId}`
- **接口描述**: 根据用户ID获取用户详细信息
- **路径参数**: `userId` - 用户ID

#### 3. 新增用户
- **接口地址**: `POST /system/user/add`
- **接口描述**: 新增用户
- **请求参数**:
```json
{
  "userName": "test",
  "nickName": "测试用户",
  "password": "123456",
  "email": "test@example.com",
  "phoneNumber": "13800138001",
  "sex": "0",
  "deptId": 1,
  "roleIds": [1, 2],
  "postIds": [1]
}
```

#### 4. 修改用户
- **接口地址**: `POST /system/user/update`
- **接口描述**: 修改用户信息

#### 5. 删除用户
- **接口地址**: `POST /system/user/remove/{userIds}`
- **接口描述**: 批量删除用户
- **路径参数**: `userIds` - 用户ID数组，用逗号分隔

#### 6. 用户状态修改
- **接口地址**: `POST /system/user/changeStatus`
- **接口描述**: 修改用户状态（启用/停用）

### 菜单管理 API

#### 1. 获取菜单列表
- **接口地址**: `POST /system/menu/list`
- **接口描述**: 获取菜单树形列表

#### 2. 获取菜单详情
- **接口地址**: `POST /system/menu/info/{menuId}`
- **接口描述**: 根据菜单ID获取菜单详细信息

#### 3. 新增菜单
- **接口地址**: `POST /system/menu/add`
- **接口描述**: 新增菜单

#### 4. 修改菜单
- **接口地址**: `POST /system/menu/update`
- **接口描述**: 修改菜单信息

#### 5. 删除菜单
- **接口地址**: `POST /system/menu/remove/{menuId}`
- **接口描述**: 删除菜单

## ERP 模块

### 客户管理 API

#### 1. 客户分页查询
- **接口地址**: `POST /erp/customer/page`
- **接口描述**: 分页查询客户列表
- **请求参数**:
```json
{
  "customerName": "客户名称",
  "customerType": "客户类型",
  "status": "状态",
  "pageNum": 1,
  "pageSize": 10
}
```

#### 2. 获取客户详情
- **接口地址**: `POST /erp/customer/info/{customerId}`
- **接口描述**: 根据客户ID获取客户详细信息

#### 3. 新增客户
- **接口地址**: `POST /erp/customer/add`
- **接口描述**: 新增客户

#### 4. 修改客户
- **接口地址**: `POST /erp/customer/update`
- **接口描述**: 修改客户信息

#### 5. 删除客户
- **接口地址**: `POST /erp/customer/remove/{customerIds}`
- **接口描述**: 批量删除客户

#### 6. 客户状态修改
- **接口地址**: `POST /erp/customer/changeStatus`
- **接口描述**: 修改客户状态

### 产品管理 API

#### 1. 产品分页查询
- **接口地址**: `POST /erp/product/page`
- **接口描述**: 分页查询产品列表

#### 2. 获取产品详情
- **接口地址**: `POST /erp/product/info/{productId}`
- **接口描述**: 根据产品ID获取产品详细信息

#### 3. 新增产品
- **接口地址**: `POST /erp/product/add`
- **接口描述**: 新增产品

#### 4. 修改产品
- **接口地址**: `POST /erp/product/update`
- **接口描述**: 修改产品信息

#### 5. 删除产品
- **接口地址**: `POST /erp/product/remove/{productIds}`
- **接口描述**: 批量删除产品

#### 6. 产品状态修改
- **接口地址**: `POST /erp/product/changeStatus`
- **接口描述**: 修改产品状态

### 库存管理 API

#### 1. 库存分页查询
- **接口地址**: `POST /erp/inventory/page`
- **接口描述**: 分页查询库存列表

#### 2. 获取库存详情
- **接口地址**: `POST /erp/inventory/info/{inventoryId}`
- **接口描述**: 根据库存ID获取库存详细信息

#### 3. 库存调整
- **接口地址**: `POST /erp/inventory/adjust`
- **接口描述**: 库存数量调整

#### 4. 库存盘点
- **接口地址**: `POST /erp/inventory/stocktake`
- **接口描述**: 批量库存盘点

#### 5. 库存预警列表
- **接口地址**: `POST /erp/inventory/warning/list`
- **接口描述**: 获取库存预警列表

#### 6. 库存调拨
- **接口地址**: `POST /erp/inventory/transfer`
- **接口描述**: 库存调拨操作

## 工作流模块

### 工作流基础操作 API

#### 1. 部署流程定义
- **接口地址**: `POST /workflow/deploy`
- **接口描述**: 上传并部署新的流程定义
- **请求参数**:
```json
{
  "name": "流程名称",
  "category": "流程分类",
  "resourceName": "资源名称",
  "deploymentFile": "流程定义文件内容(Base64)"
}
```
- **响应数据**:
```json
{
  "success": true,
  "message": "流程部署成功",
  "deploymentId": "deployment-id",
  "processName": "流程名称"
}
```

#### 2. 启动流程实例
- **接口地址**: `POST /workflow/start`
- **接口描述**: 根据流程定义启动新的流程实例
- **请求参数**:
```json
{
  "processKey": "流程定义Key",
  "businessKey": "业务Key",
  "variables": {
    "key1": "value1",
    "key2": "value2"
  }
}
```

### 工作流任务管理 API

#### 1. 高级任务查询
- **接口地址**: `GET /workflow/tasks/search`
- **接口描述**: 高级任务查询
- **请求参数**:
```
assignee: 任务分配人
processDefinitionKey: 流程定义Key
businessKey: 业务Key
taskName: 任务名称
startTime: 开始时间
endTime: 结束时间
page: 页码
size: 每页大小
```

#### 2. 获取任务详情
- **接口地址**: `GET /workflow/tasks/{taskId}/detail`
- **接口描述**: 获取任务详细信息

#### 3. 任务提醒设置
- **接口地址**: `POST /workflow/tasks/{taskId}/reminder`
- **接口描述**: 设置任务提醒

### 工作流配置 API

#### 1. 获取工作流配置
- **接口地址**: `GET /workflow/config`
- **接口描述**: 获取工作流配置信息

#### 2. 更新工作流配置
- **接口地址**: `PUT /workflow/config`
- **接口描述**: 更新工作流配置

#### 3. 获取流程定义列表
- **接口地址**: `GET /workflow/definitions`
- **接口描述**: 获取已部署的流程定义列表

#### 4. 流程监控
- **接口地址**: `GET /workflow/monitor`
- **接口描述**: 获取流程监控数据

## 报表模块

### 报表管理 API

#### 1. 创建报告
- **接口地址**: `POST /report`
- **接口描述**: 创建报告（日报/周报）
- **请求参数**:
```json
{
  "type": "daily", // daily: 日报, weekly: 周报
  "title": "报告标题",
  "content": "报告内容",
  "reportDate": "2023-12-01",
  "userId": 1
}
```

#### 2. 更新报告
- **接口地址**: `PUT /report/{id}`
- **接口描述**: 更新报告信息

#### 3. 删除报告
- **接口地址**: `DELETE /report/{id}`
- **接口描述**: 删除报告

#### 4. 获取报告详情
- **接口地址**: `GET /report/{id}`
- **接口描述**: 根据ID获取报告详情

#### 5. 获取用户报告列表
- **接口地址**: `GET /report/user/{userId}`
- **接口描述**: 根据用户ID和类型获取报告列表
- **请求参数**: `type` - 报告类型（daily/weekly）

#### 6. 获取指定日期日报
- **接口地址**: `GET /report/user/{userId}/date/{reportDate}`
- **接口描述**: 获取指定用户在指定日期的日报

#### 7. 获取指定周的周报
- **接口地址**: `GET /report/user/{userId}/week/{weekStartDate}`
- **接口描述**: 获取指定用户在指定周的周报

#### 8. 提交报告并发起审批
- **接口地址**: `POST /report/submit`
- **接口描述**: 提交报告并发起工作流审批

#### 9. 审批报告
- **接口地址**: `POST /report/approve/{id}`
- **接口描述**: 领导审批报告
- **请求参数**:
```
approve: true/false (通过/拒绝)
comment: 审批意见
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未登录 |
| 402 | Token无效 |
| 403 | 没有权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 数据字典

### 用户状态
- 0: 正常
- 1: 停用

### 性别
- 0: 男
- 1: 女
- 2: 未知

### 菜单类型
- M: 目录
- C: 菜单
- F: 按钮

### 报告类型
- daily: 日报
- weekly: 周报

## 开发注意事项

1. **认证**: 所有API请求都需要在请求头中携带有效的satoken
2. **分页**: 分页查询统一使用POST方法，参数包含pageNum和pageSize
3. **批量操作**: 批量删除等操作使用数组形式的ID参数
4. **文件上传**: 文件上传使用Base64编码格式
5. **日期格式**: 日期统一使用ISO格式（yyyy-MM-dd HH:mm:ss）
6. **响应格式**: 所有接口响应都遵循统一的格式规范

## 更新日志

### v1.0.0 (2025-05-01)
- 初始版本发布
- 完成系统管理、ERP、工作流、报表等核心模块API
- 支持用户权限管理和工作流审批
- 集成腾讯云存储和Redis缓存

---

**文档维护**: 开发团队  
**最后更新**: 2023-12-01  
**版本**: v1.0.0