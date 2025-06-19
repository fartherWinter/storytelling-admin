# 统一流程设计器 (ProcessDesignerUnified)

一个现代化、功能完整的流程设计器组件，整合了原有多个流程设计器的功能，提供统一的用户体验。

## ✨ 特性

### 🎨 现代化 UI
- 渐变色彩设计
- 响应式布局
- 流畅的动画效果
- 暗色主题支持

### 🔧 强大功能
- 拖拽式节点创建
- 可视化连线编辑
- 实时属性配置
- 撤销/重做操作
- 自动布局算法
- 流程验证检查

### 📊 多格式支持
- JSON 导入导出
- BPMN 格式支持
- PNG/SVG 图片导出
- 流程模板管理

### 🚀 性能优化
- 虚拟滚动渲染
- 懒加载组件
- 状态缓存机制
- 内存优化

## 📦 安装使用

### 基础用法

```vue
<template>
  <ProcessDesignerUnified
    :initial-data="processData"
    @save="handleSave"
    @preview="handlePreview"
  />
</template>

<script setup>
import ProcessDesignerUnified from '@/components/process-designer/ProcessDesignerUnified.vue'

const processData = {
  name: '示例流程',
  nodes: [],
  connections: []
}

const handleSave = (data) => {
  console.log('保存流程:', data)
}

const handlePreview = (data) => {
  console.log('预览流程:', data)
}
</script>
```

### 高级配置

```vue
<template>
  <ProcessDesignerUnified
    :initial-data="processData"
    :enabled-node-types="enabledTypes"
    :readonly="false"
    :toolbar-config="toolbarConfig"
    @save="handleSave"
    @preview="handlePreview"
    @deploy="handleDeploy"
    @change="handleChange"
    @canvas-click="handleCanvasClick"
  />
</template>

<script setup>
const enabledTypes = [
  'start', 'end', 'userTask', 'serviceTask', 
  'exclusiveGateway', 'parallelGateway'
]

const toolbarConfig = {
  showSave: true,
  showPreview: true,
  showExport: true,
  showDeploy: false,
  showImport: true,
  showValidate: true
}
</script>
```

## 🔧 API 参考

### Props

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `initialData` | Object | `{ name: '', nodes: [], connections: [] }` | 初始流程数据 |
| `enabledNodeTypes` | Array | `['start', 'end', 'userTask', ...]` | 启用的节点类型 |
| `readonly` | Boolean | `false` | 是否只读模式 |
| `toolbarConfig` | Object | `{ showSave: true, ... }` | 工具栏配置 |

### Events

| 事件 | 参数 | 说明 |
|------|------|------|
| `save` | `(processData)` | 保存流程时触发 |
| `preview` | `(processData)` | 预览流程时触发 |
| `deploy` | `(processData)` | 部署流程时触发 |
| `change` | `()` | 流程变更时触发 |
| `canvas-click` | `()` | 画布点击时触发 |

### 数据格式

#### 流程数据
```javascript
{
  name: '流程名称',
  version: '1.0',
  nodes: [...],
  connections: [...],
  createdAt: '2024-01-01T00:00:00.000Z'
}
```

#### 节点数据
```javascript
{
  id: 'node_1234567890',
  type: 'userTask',
  name: '用户任务',
  x: 100,
  y: 100,
  width: 120,
  height: 80,
  properties: {
    assignee: 'user1',
    candidateGroups: 'group1',
    formKey: 'form1',
    priority: 50,
    documentation: '任务说明'
  }
}
```

#### 连线数据
```javascript
{
  id: 'connection_1234567890',
  source: 'node_1',
  target: 'node_2',
  name: '连线名称',
  condition: '${amount > 1000}',
  properties: {
    conditionType: 'expression',
    expression: '${amount > 1000}',
    script: '',
    priority: 1,
    skipExpression: ''
  }
}
```

## 🎯 节点类型

### 开始/结束节点
- `start` - 开始事件
- `end` - 结束事件

### 任务节点
- `userTask` - 用户任务
- `serviceTask` - 服务任务
- `scriptTask` - 脚本任务

### 网关节点
- `exclusiveGateway` - 排他网关
- `parallelGateway` - 并行网关

### 事件节点
- `timerEvent` - 定时事件
- `messageEvent` - 消息事件

## 🎨 主题定制

### CSS 变量

```css
.process-designer {
  --primary-color: #409eff;
  --secondary-color: #67c23a;
  --danger-color: #f56c6c;
  --warning-color: #e6a23c;
  --info-color: #909399;
  --success-color: #67c23a;
  
  --background-color: #f5f5f5;
  --surface-color: #ffffff;
  --border-color: #e4e7ed;
  
  --text-primary: #303133;
  --text-regular: #606266;
  --text-secondary: #909399;
  --text-placeholder: #c0c4cc;
}
```

### 暗色主题

```css
@media (prefers-color-scheme: dark) {
  .process-designer {
    --background-color: #1a1a1a;
    --surface-color: #2d2d2d;
    --border-color: #404040;
    
    --text-primary: #e0e0e0;
    --text-regular: #b0b0b0;
    --text-secondary: #808080;
    --text-placeholder: #606060;
  }
}
```

## 🔌 扩展开发

### 自定义节点类型

```javascript
// 在 useProcessDesignerUnified.js 中添加
const customNodeTypes = {
  customTask: {
    name: '自定义任务',
    icon: 'Custom',
    color: '#ff6b6b',
    category: 'tasks',
    defaultProps: {
      name: '自定义任务',
      executor: '',
      timeout: 3600
    },
    validator: (node) => {
      return {
        isValid: !!node.properties.executor,
        errors: node.properties.executor ? [] : ['执行器不能为空']
      }
    }
  }
}
```

### 自定义属性组件

```vue
<!-- CustomNodeProperties.vue -->
<template>
  <div class="custom-properties">
    <el-form label-width="80px" size="small">
      <el-form-item label="执行器">
        <el-input 
          v-model="localNode.properties.executor"
          @input="handleUpdate"
        />
      </el-form-item>
      
      <el-form-item label="超时时间">
        <el-input-number 
          v-model="localNode.properties.timeout"
          :min="1"
          @change="handleUpdate"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
const props = defineProps(['node'])
const emit = defineEmits(['update'])

const localNode = ref({ ...props.node })

const handleUpdate = () => {
  emit('update', localNode.value.properties)
}
</script>
```

## 📈 性能优化

### 大型流程图优化

```javascript
// 启用虚拟滚动
const config = {
  virtualScroll: true,
  chunkSize: 50,
  renderDistance: 100
}

// 启用节点缓存
const cacheConfig = {
  enableNodeCache: true,
  cacheSize: 1000,
  cacheTTL: 300000 // 5分钟
}
```

### 内存管理

```javascript
// 清理资源
const cleanup = () => {
  // 清理事件监听器
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseup', handleMouseUp)
  
  // 清理定时器
  if (autoSaveTimer) {
    clearInterval(autoSaveTimer)
  }
  
  // 清理缓存
  nodeCache.clear()
  connectionCache.clear()
}

// 在组件卸载时调用
onUnmounted(() => {
  cleanup()
})
```

## 🧪 测试

### 单元测试

```javascript
import { mount } from '@vue/test-utils'
import ProcessDesignerUnified from './ProcessDesignerUnified.vue'

describe('ProcessDesignerUnified', () => {
  test('renders correctly', () => {
    const wrapper = mount(ProcessDesignerUnified, {
      props: {
        initialData: {
          name: 'Test Process',
          nodes: [],
          connections: []
        }
      }
    })
    
    expect(wrapper.find('.process-designer').exists()).toBe(true)
  })
  
  test('creates node on drop', async () => {
    const wrapper = mount(ProcessDesignerUnified)
    
    // 模拟拖拽放置
    const canvas = wrapper.find('.design-canvas')
    await canvas.trigger('drop', {
      dataTransfer: {
        getData: () => JSON.stringify({ nodeType: 'userTask' })
      },
      clientX: 100,
      clientY: 100
    })
    
    expect(wrapper.emitted('change')).toBeTruthy()
  })
})
```

### E2E 测试

```javascript
// cypress/integration/process-designer.spec.js
describe('Process Designer', () => {
  beforeEach(() => {
    cy.visit('/workflow/designer')
  })
  
  it('should create a simple process', () => {
    // 拖拽开始节点
    cy.get('[data-node-type="start"]')
      .trigger('dragstart')
    
    cy.get('.design-canvas')
      .trigger('drop', { clientX: 200, clientY: 200 })
    
    // 验证节点已创建
    cy.get('.process-node').should('have.length', 1)
    
    // 保存流程
    cy.get('[data-action="save"]').click()
    
    // 验证保存成功
    cy.get('.el-message--success').should('be.visible')
  })
})
```

## 📝 更新日志

### v1.0.0 (2024-01-01)
- ✨ 初始版本发布
- 🎨 现代化 UI 设计
- 🔧 统一多个流程设计器功能
- 📊 支持多种导入导出格式
- 🚀 性能优化和内存管理

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 开发环境

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 运行测试
npm run test

# 构建生产版本
npm run build
```

### 代码规范

- 使用 ESLint 和 Prettier
- 遵循 Vue 3 Composition API 最佳实践
- 编写单元测试和文档
- 提交前运行 `npm run lint`

## 📄 许可证

MIT License

## 🔗 相关链接

- [迁移指南](./MIGRATION_GUIDE.md)
- [API 文档](./API.md)
- [开发指南](./DEVELOPMENT.md)
- [最佳实践](./BEST_PRACTICES.md)