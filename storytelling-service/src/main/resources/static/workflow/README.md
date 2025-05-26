# 可配置工作流模块使用说明

## 功能介绍

本模块基于Flowable工作流引擎，实现了一个可在前端页面直接创建和配置的工作流系统，无需修改后端代码即可定义新的业务流程。主要功能包括：

1. **工作流模型管理**：创建、编辑、删除和查询工作流模型
2. **流程设计器**：通过拖拽方式可视化设计流程
3. **流程部署**：将设计好的流程模型部署为可执行的流程定义
4. **流程实例管理**：启动流程实例、查询流程状态、终止流程等
5. **任务管理**：查询待办任务、完成任务、审批或拒绝任务等

## 使用方法

### 1. 访问工作流模型管理页面

打开浏览器，访问以下地址：

```
http://localhost:端口号/workflow/index.html
```

### 2. 创建新的工作流模型

1. 点击页面右上角的"创建新模型"按钮
2. 填写模型基本信息：
   - 模型名称：流程的显示名称
   - 模型标识：流程的唯一标识符，只能包含字母、数字和下划线
   - 模型分类：用于对流程进行分类管理
   - 描述：流程的详细说明
3. 点击"创建"按钮完成创建

### 3. 设计流程

1. 在模型列表中，点击对应模型的"设计"按钮
2. 在流程设计器页面，通过拖拽方式添加流程节点和连线
3. 配置每个节点的属性，如名称、处理人、表单等
4. 点击"保存"按钮保存设计

### 4. 部署流程

1. 在流程设计器页面，点击"部署"按钮
2. 或在模型列表页面，点击对应模型的"部署"按钮

### 5. 启动流程实例

通过调用API接口启动流程实例：

```
POST /workflow/start?processKey={流程标识}&businessKey={业务键}&businessType={业务类型}&title={流程标题}
```

### 6. 处理任务

通过调用API接口查询和处理任务：

```
GET /workflow/tasks?assignee={处理人}
```

```
POST /workflow/tasks/{taskId}/complete
```

## API接口说明

### 工作流模型管理接口

| 接口路径 | 请求方式 | 说明 |
| --- | --- | --- |
| /workflow/model | GET | 获取模型列表 |
| /workflow/model | POST | 创建模型 |
| /workflow/model/{modelId} | GET | 获取模型详情 |
| /workflow/model/{modelId} | PUT | 更新模型 |
| /workflow/model/{modelId} | DELETE | 删除模型 |
| /workflow/model/{modelId}/deploy | POST | 部署模型 |
| /workflow/model/{modelId}/xml | GET | 获取模型XML |
| /workflow/model/{modelId}/xml | POST | 保存模型XML |
| /workflow/model/{modelId}/export-xml | GET | 导出模型XML |
| /workflow/model/{modelId}/export-png | GET | 导出模型PNG |

### 流程设计器接口

| 接口路径 | 请求方式 | 说明 |
| --- | --- | --- |
| /workflow/designer/editor/stencilset | GET | 获取模型集合 |
| /workflow/designer/model/{modelId}/json | GET | 获取模型JSON |
| /workflow/designer/model/{modelId}/save | POST | 保存模型 |
| /workflow/designer/model/{modelId}/convert-to-xml | POST | 转换为XML |
| /workflow/designer/model/{modelId}/deploy | POST | 部署模型 |

### 流程定义接口

| 接口路径 | 请求方式 | 说明 |
| --- | --- | --- |
| /workflow/process-definitions | GET | 获取流程定义列表 |
| /workflow/process-definitions/{processDefinitionId} | GET | 获取流程定义详情 |
| /workflow/process-definitions/{processDefinitionId}/resource | GET | 获取流程定义资源 |

### 流程实例接口

| 接口路径 | 请求方式 | 说明 |
| --- | --- | --- |
| /workflow/start | POST | 启动流程实例 |
| /workflow/process/{processInstanceId}/tasks | GET | 获取流程任务 |
| /workflow/process/{processInstanceId}/diagram | GET | 获取流程图 |
| /workflow/process/{processInstanceId}/variables/{variableName} | GET | 获取流程变量 |
| /workflow/process/{processInstanceId}/variables/{variableName} | POST | 设置流程变量 |
| /workflow/process/{processInstanceId} | DELETE | 终止流程实例 |

### 任务接口

| 接口路径 | 请求方式 | 说明 |
| --- | --- | --- |
| /workflow/tasks | GET | 获取任务列表 |
| /workflow/tasks/{taskId}/complete | POST | 完成任务 |
| /workflow/tasks/{taskId}/approve | POST | 审批通过 |
| /workflow/tasks/{taskId}/reject | POST | 拒绝任务 |

## 注意事项

1. 流程设计时，确保每个用户任务都指定了处理人或候选人组
2. 流程部署后不可修改，如需修改请创建新版本
3. 启动流程实例时，业务键(businessKey)应当是唯一的，建议使用业务表的主键
4. 可以通过流程变量传递业务数据，但不建议传递大量数据

## 常见问题

1. **Q: 如何在流程中使用表单？**  
   A: 可以在用户任务节点的属性中配置表单键，然后在前端根据表单键加载对应的表单。

2. **Q: 如何设置任务的处理人？**  
   A: 在用户任务节点的属性中设置assignee(处理人)或candidateUsers(候选人)。

3. **Q: 如何实现条件分支？**  
   A: 在连线的属性中设置条件表达式，如${approved == true}。

4. **Q: 如何与业务系统集成？**  
   A: 通过businessKey关联业务数据，启动流程时传入业务主键，完成任务时可以更新业务状态。