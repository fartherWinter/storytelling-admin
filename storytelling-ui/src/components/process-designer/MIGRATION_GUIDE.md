# 流程设计器迁移指南

本指南将帮助您从现有的多个流程设计器迁移到新的统一流程设计器 `ProcessDesignerUnified.vue`。

## 📋 迁移概述

### 原有组件
- `ProcessDesigner.vue` - 主要流程设计器
- `ProcessDesignerDemo.vue` - 演示版本
- `ProcessDesignerSimple.vue` - 简化版本
- `CustomWorkflow.vue` - 自定义工作流
- `FormDesigner.vue` - 表单设计器

### 新统一组件
- `ProcessDesignerUnified.vue` - 统一流程设计器
- `ProcessNodeProperties.vue` - 节点属性组件
- `ProcessConnectionProperties.vue` - 连线属性组件
- `useProcessDesignerUnified.js` - 统一组合式函数

## 🔄 迁移步骤

### 1. 更新路由配置

**原有路由：**
```javascript
// router/index.js
{
  path: '/workflow/designer',
  component: () => import('@/views/workflow/ProcessDesigner.vue')
},
{
  path: '/workflow/designer-demo',
  component: () => import('@/views/ProcessDesignerDemo.vue')
},
{
  path: '/workflow/designer-simple',
  component: () => import('@/components/process-designer/ProcessDesignerSimple.vue')
}
```

**新路由：**
```javascript
// router/index.js
{
  path: '/workflow/designer',
  component: () => import('@/components/process-designer/ProcessDesignerUnified.vue')
}
```

### 2. 更新组件引用

**原有引用：**
```vue
<template>
  <ProcessDesigner 
    :initial-data="processData"
    @save="handleSave"
  />
</template>

<script>
import ProcessDesigner from '@/views/workflow/ProcessDesigner.vue'
</script>
```

**新引用：**
```vue
<template>
  <ProcessDesignerUnified 
    :initial-data="processData"
    :enabled-node-types="enabledTypes"
    :readonly="false"
    @save="handleSave"
    @preview="handlePreview"
    @deploy="handleDeploy"
    @change="handleChange"
  />
</template>

<script>
import ProcessDesignerUnified from '@/components/process-designer/ProcessDesignerUnified.vue'
</script>
```

### 3. 更新属性配置

**新组件支持的属性：**
```javascript
const props = {
  // 初始数据
  initialData: {
    type: Object,
    default: () => ({
      name: '',
      nodes: [],
      connections: []
    })
  },
  
  // 启用的节点类型
  enabledNodeTypes: {
    type: Array,
    default: () => [
      'start', 'end', 'userTask', 'serviceTask', 'scriptTask',
      'exclusiveGateway', 'parallelGateway', 'timerEvent', 'messageEvent'
    ]
  },
  
  // 是否只读
  readonly: {
    type: Boolean,
    default: false
  },
  
  // 工具栏配置
  toolbarConfig: {
    type: Object,
    default: () => ({
      showSave: true,
      showPreview: true,
      showExport: true,
      showDeploy: true,
      showImport: true,
      showValidate: true
    })
  }
}
```

### 4. 更新事件处理

**新组件支持的事件：**
```javascript
// 保存事件
const handleSave = (processData) => {
  console.log('保存流程:', processData)
  // processData 包含: { name, nodes, connections, version, createdAt }
}

// 预览事件
const handlePreview = (processData) => {
  console.log('预览流程:', processData)
}

// 部署事件
const handleDeploy = (processData) => {
  console.log('部署流程:', processData)
}

// 变更事件
const handleChange = () => {
  console.log('流程已变更')
}

// 画布点击事件
const handleCanvasClick = () => {
  console.log('画布被点击')
}
```

## 🆕 新功能特性

### 1. 统一的状态管理
- 使用 Pinia store 进行状态管理
- 支持撤销/重做操作
- 历史记录管理

### 2. 增强的属性配置
- 独立的节点属性组件
- 独立的连线属性组件
- 支持更多节点类型和属性

### 3. 改进的用户体验
- 现代化的 UI 设计
- 响应式布局
- 更好的拖拽体验
- 网格对齐功能

### 4. 强大的导入导出
- JSON 格式导入导出
- BPMN 格式支持
- 图片导出功能

### 5. 流程验证
- 自动流程验证
- 错误提示和修复建议
- 完整性检查

## 🔧 自定义配置

### 1. 自定义节点类型

```javascript
// 在 useProcessDesignerUnified.js 中添加新节点类型
const customNodeTypes = {
  customTask: {
    name: '自定义任务',
    icon: 'Custom',
    color: '#ff6b6b',
    category: 'tasks',
    defaultProps: {
      name: '自定义任务',
      assignee: '',
      formKey: ''
    }
  }
}
```

### 2. 自定义主题

```css
/* 在组件中覆盖 CSS 变量 */
.process-designer {
  --primary-color: #your-color;
  --secondary-color: #your-secondary-color;
  --background-color: #your-background;
}
```

### 3. 自定义工具栏

```vue
<ProcessDesignerUnified
  :toolbar-config="{
    showSave: true,
    showPreview: false,
    showExport: true,
    showDeploy: false,
    showImport: true,
    showValidate: true
  }"
/>
```

## 📝 数据格式变更

### 节点数据格式

**原格式：**
```javascript
{
  id: 'node1',
  type: 'userTask',
  name: '用户任务',
  x: 100,
  y: 100
}
```

**新格式：**
```javascript
{
  id: 'node1',
  type: 'userTask',
  name: '用户任务',
  x: 100,
  y: 100,
  width: 120,
  height: 80,
  properties: {
    assignee: '',
    candidateGroups: '',
    formKey: '',
    priority: 50,
    documentation: ''
  }
}
```

### 连线数据格式

**原格式：**
```javascript
{
  id: 'connection1',
  source: 'node1',
  target: 'node2'
}
```

**新格式：**
```javascript
{
  id: 'connection1',
  source: 'node1',
  target: 'node2',
  name: '',
  condition: '',
  properties: {
    conditionType: 'expression',
    expression: '',
    script: '',
    priority: 1,
    skipExpression: ''
  }
}
```

## 🚀 性能优化

### 1. 懒加载
- 组件按需加载
- 大型流程图分块渲染

### 2. 虚拟滚动
- 大量节点时使用虚拟滚动
- 提高渲染性能

### 3. 缓存机制
- 状态缓存
- 计算结果缓存

## 🐛 常见问题

### Q: 如何保持现有数据兼容性？
A: 新组件提供数据转换函数，可以自动转换旧格式数据。

### Q: 如何自定义节点样式？
A: 通过 CSS 变量和主题配置来自定义样式。

### Q: 如何扩展新的节点类型？
A: 在组合式函数中添加新的节点类型配置。

### Q: 如何处理大型流程图？
A: 使用虚拟滚动和分块渲染来优化性能。

## 📚 相关文档

- [组件 API 文档](./API.md)
- [开发指南](./DEVELOPMENT.md)
- [样式指南](./STYLING.md)
- [最佳实践](./BEST_PRACTICES.md)

## 🤝 支持

如果在迁移过程中遇到问题，请：

1. 查看本指南和相关文档
2. 检查控制台错误信息
3. 提交 Issue 或联系开发团队

---

**注意：** 建议在迁移前备份现有代码，并在测试环境中验证新组件的功能。