# 工作流接口文档

## 基础信息

- **API 版本**: v1
- **基础路径**: `/api/v1/workflow`
- **内容类型**: `application/json`
- **认证方式**: Bearer Token

## 1. 工作流基础操作 (WorkflowController)

### 1.1 部署流程定义

**接口地址**: `POST /api/v1/workflow/deploy`

**请求参数**:
```json
{
  "name": "流程名称",
  "category": "流程分类",
  "resourceName": "资源文件名",
  "deploymentFile": "部署文件内容(base64)"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "流程部署成功",
  "deploymentId": "deployment-123",
  "processName": "请假流程"
}
```

### 1.2 启动流程实例

**接口地址**: `POST /api/v1/workflow/start`

**请求参数**:
- `processKey` (string, required): 流程定义Key
- `businessKey` (string, required): 业务Key
- `variables` (object, optional): 流程变量

**响应示例**:
```json
{
  "success": true,
  "message": "流程启动成功",
  "processInstanceId": "process-instance-123",
  "processDefinitionId": "process-def-123",
  "businessKey": "business-key-123"
}
```

### 1.3 获取待办任务

**接口地址**: `GET /api/v1/workflow/tasks/todo`

**请求参数**:
- `assignee` (string, required): 任务处理人

**响应示例**:
```json
[
  {
    "id": "task-123",
    "name": "审批任务",
    "assignee": "user123",
    "createTime": "2024-01-01T10:00:00",
    "processInstanceId": "process-instance-123",
    "processDefinitionKey": "leave_process"
  }
]
```

### 1.4 完成任务

**接口地址**: `POST /api/v1/workflow/tasks/{taskId}/complete`

**请求参数**:
```json
{
  "variables": {
    "approved": true,
    "comment": "同意申请"
  }
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "任务完成成功"
}
```

## 2. 工作流定义管理 (WorkflowDefinitionController)

### 2.1 获取所有流程定义

**接口地址**: `GET /workflow/definitions`

**响应示例**:
```json
[
  {
    "id": "leave_process:1:123",
    "key": "leave_process",
    "name": "请假流程",
    "version": 1,
    "category": "HR",
    "deploymentId": "deployment-123"
  }
]
```

### 2.2 分页查询流程定义

**接口地址**: `GET /workflow/definitions/page`

**请求参数**:
- `page` (int, default: 0): 页码
- `size` (int, default: 20): 页大小
- `category` (string, optional): 分类过滤
- `key` (string, optional): Key过滤
- `name` (string, optional): 名称过滤

**响应示例**:
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "total": 100,
  "totalPages": 5
}
```

### 2.3 获取流程定义详情

**接口地址**: `GET /workflow/definitions/{definitionId}`

**响应示例**:
```json
{
  "definition": {
    "id": "leave_process:1:123",
    "key": "leave_process",
    "name": "请假流程"
  },
  "xml": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>...",
  "statistics": {
    "totalInstances": 50,
    "activeInstances": 10,
    "completedInstances": 40
  }
}
```

## 3. 工作流设计器 (WorkflowDesignerController)

### 3.1 获取设计器模型集合

**接口地址**: `GET /workflow/designer/editor/stencilset`

**响应示例**:
```json
{
  "namespace": "http://b3mn.org/stencilset/bpmn2.0#"
}
```

### 3.2 获取模型编辑器数据

**接口地址**: `GET /workflow/designer/model/{modelId}/json`

**响应示例**:
```json
{
  "id": "canvas",
  "resourceId": "canvas",
  "stencilset": {
    "namespace": "http://b3mn.org/stencilset/bpmn2.0#"
  },
  "childShapes": [...]
}
```

### 3.3 保存模型数据

**接口地址**: `POST /workflow/designer/model/{modelId}/save`

**请求参数**:
```json
{
  "name": "模型名称",
  "description": "模型描述",
  "json_xml": "模型JSON数据",
  "svg_xml": "模型SVG数据"
}
```

## 4. 工作流模型管理 (WorkflowModelController)

### 4.1 创建工作流模型

**接口地址**: `POST /workflow/model`

**请求参数**:
```json
{
  "name": "模型名称",
  "key": "model_key",
  "description": "模型描述",
  "category": "模型分类"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "model-id-123"
}
```

### 4.2 更新工作流模型

**接口地址**: `PUT /workflow/model/{modelId}`

**请求参数**:
```json
{
  "name": "更新后的模型名称",
  "description": "更新后的模型描述",
  "category": "更新后的分类"
}
```

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": true
}
```

### 4.3 获取模型列表

**接口地址**: `GET /workflow/model`

**请求参数**:
- `page` (int, default: 1): 页码
- `size` (int, default: 10): 页大小
- `name` (string, optional): 名称过滤
- `category` (string, optional): 分类过滤

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [...],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

### 4.4 删除模型

**接口地址**: `DELETE /workflow/model/{modelId}`

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": true
}
```

## 5. 工作流任务管理 (WorkflowTaskController)

### 5.1 高级任务查询

**接口地址**: `GET /workflow/tasks/search`

**请求参数**:
- `assignee` (string, optional): 任务处理人
- `processDefinitionKey` (string, optional): 流程定义Key
- `businessKey` (string, optional): 业务Key
- `taskName` (string, optional): 任务名称
- `startTime` (datetime, optional): 开始时间
- `endTime` (datetime, optional): 结束时间
- `page` (int, default: 0): 页码
- `size` (int, default: 20): 页大小

**响应示例**:
```json
{
  "tasks": [...],
  "total": 50,
  "page": 0,
  "size": 20
}
```

### 5.2 获取任务详情

**接口地址**: `GET /workflow/tasks/{taskId}/detail`

**响应示例**:
```json
{
  "taskId": "task-123",
  "taskInfo": {
    "name": "审批任务",
    "assignee": "user123",
    "createTime": "2024-01-01T10:00:00"
  },
  "processVariables": {
    "leaveType": "annual",
    "days": 3
  },
  "comments": [...]
}
```

### 5.3 设置任务提醒

**接口地址**: `POST /workflow/tasks/{taskId}/reminder`

**请求参数**:
- `reminderTime` (datetime, required): 提醒时间
- `message` (string, optional): 提醒消息

## 6. 工作流流程管理 (WorkflowProcessController)

### 6.1 高级流程实例查询

**接口地址**: `GET /workflow/processes/search`

**请求参数**:
- `processDefinitionKey` (string, optional): 流程定义Key
- `businessKey` (string, optional): 业务Key
- `startUserId` (string, optional): 启动用户ID
- `status` (string, optional): 状态
- `startTime` (datetime, optional): 开始时间
- `endTime` (datetime, optional): 结束时间
- `page` (int, default: 0): 页码
- `size` (int, default: 20): 页大小

**响应示例**:
```json
{
  "page": 0,
  "size": 20,
  "total": 0,
  "processes": []
}
```

### 6.2 启动流程实例（增强版）

**接口地址**: `POST /workflow/processes/start`

**请求参数**:
- `processKey` (string, required): 流程Key
- `businessKey` (string, required): 业务Key
- `businessType` (string, required): 业务类型
- `title` (string, required): 流程标题
- `startUserId` (string, optional): 启动用户ID
- `variables` (object, optional): 流程变量

**响应示例**:
```json
{
  "success": true,
  "message": "流程启动成功",
  "processInstanceId": "process-instance-123",
  "processDefinitionId": "process-def-123",
  "businessKey": "business-key-123"
}
```

### 6.3 获取流程实例详情

**接口地址**: `GET /workflow/processes/{processInstanceId}/detail`

**响应示例**:
```json
{
  "processInstanceId": "process-instance-123",
  "processDefinitionKey": "leave_process",
  "businessKey": "business-key-123",
  "startTime": "2024-01-01T10:00:00",
  "status": "active",
  "variables": {...},
  "tasks": [...],
  "history": [...]
}
```

## 7. 工作流表单管理 (WorkflowFormController)

### 7.1 获取所有表单模板

**接口地址**: `GET /workflow/forms`

**响应示例**:
```json
[
  {
    "id": "leave_request_form",
    "name": "请假申请表",
    "description": "员工请假申请表单",
    "processKey": "leave_process",
    "version": 1,
    "status": "active"
  }
]
```

### 7.2 获取表单模板详情

**接口地址**: `GET /workflow/forms/{formId}`

**响应示例**:
```json
{
  "id": "leave_request_form",
  "name": "请假申请表",
  "description": "员工请假申请表单",
  "processKey": "leave_process",
  "version": 1,
  "status": "active",
  "fields": [
    {
      "id": "leaveType",
      "name": "请假类型",
      "type": "select",
      "required": true,
      "options": [
        {"value": "annual", "label": "年假"},
        {"value": "sick", "label": "病假"},
        {"value": "personal", "label": "事假"}
      ]
    },
    {
      "id": "startDate",
      "name": "开始日期",
      "type": "date",
      "required": true
    }
  ]
}
```

## 8. 工作流监控管理 (WorkflowMonitorController)

### 8.1 获取工作流仪表板数据

**接口地址**: `GET /workflow/monitor/dashboard`

**请求参数**:
- `startTime` (datetime, optional): 开始时间
- `endTime` (datetime, optional): 结束时间

**响应示例**:
```json
{
  "statistics": {
    "totalProcesses": 1000,
    "activeProcesses": 150,
    "completedProcesses": 850,
    "totalTasks": 2000,
    "pendingTasks": 300,
    "completedTasks": 1700
  }
}
```

### 8.2 获取详细统计报告

**接口地址**: `GET /workflow/monitor/report`

**请求参数**:
- `startTime` (datetime, optional): 开始时间
- `endTime` (datetime, optional): 结束时间
- `groupBy` (string, default: "day"): 分组方式

**响应示例**:
```json
{
  "totalProcesses": 1000,
  "activeProcesses": 150,
  "completedProcesses": 850,
  "totalTasks": 2000,
  "pendingTasks": 300,
  "completedTasks": 1700,
  "averageProcessDuration": 86400000,
  "averageTaskDuration": 3600000
}
```

### 8.3 获取流程性能分析

**接口地址**: `GET /workflow/monitor/performance`

**请求参数**:
- `processDefinitionKey` (string, optional): 流程定义Key
- `startTime` (datetime, optional): 开始时间
- `endTime` (datetime, optional): 结束时间

**响应示例**:
```json
{
  "basicStats": {
    "totalProcesses": 1000,
    "activeProcesses": 150,
    "completedProcesses": 850
  },
  "performanceMetrics": {
    "averageProcessDuration": 86400000,
    "averageTaskDuration": 3600000,
    "bottleneckTasks": [...],
    "exceptionProcesses": [...]
  }
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 数据类型说明

### ProcessDefinitionDTO
```json
{
  "id": "string",
  "key": "string",
  "name": "string",
  "version": "integer",
  "category": "string",
  "deploymentId": "string",
  "resourceName": "string",
  "deploymentFile": "string"
}
```

### WorkflowModelDTO
```json
{
  "id": "string",
  "name": "string",
  "key": "string",
  "description": "string",
  "category": "string",
  "version": "integer",
  "createTime": "datetime",
  "updateTime": "datetime"
}
```

### TaskDTO
```json
{
  "id": "string",
  "name": "string",
  "assignee": "string",
  "createTime": "datetime",
  "dueDate": "datetime",
  "processInstanceId": "string",
  "processDefinitionKey": "string",
  "businessKey": "string",
  "variables": "object"
}
```

### WorkflowStatisticsDTO
```json
{
  "totalProcesses": "long",
  "activeProcesses": "long",
  "completedProcesses": "long",
  "totalTasks": "long",
  "pendingTasks": "long",
  "completedTasks": "long",
  "averageProcessDuration": "long",
  "averageTaskDuration": "long"
}
```