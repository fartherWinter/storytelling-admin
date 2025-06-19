<template>
  <div class="process-connection-properties">
    <el-form :model="connectionData" label-width="80px" size="small">
      <!-- 基础信息 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">🔗 连线信息</span>
        </template>
        
        <el-form-item label="连线ID">
          <el-input v-model="connectionData.id" disabled />
        </el-form-item>
        
        <el-form-item label="连线名称">
          <el-input 
            v-model="connectionData.name" 
            placeholder="请输入连线名称"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="源节点">
          <el-input v-model="connectionData.sourceId" disabled />
        </el-form-item>
        
        <el-form-item label="目标节点">
          <el-input v-model="connectionData.targetId" disabled />
        </el-form-item>
      </el-card>
      
      <!-- 条件配置 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">⚡ 条件配置</span>
        </template>
        
        <el-form-item label="条件类型">
          <el-select 
            v-model="connectionData.conditionType" 
            placeholder="请选择条件类型"
            @change="handleUpdate"
          >
            <el-option label="无条件" value="none" />
            <el-option label="条件表达式" value="expression" />
            <el-option label="脚本" value="script" />
            <el-option label="默认流" value="default" />
          </el-select>
        </el-form-item>
        
        <el-form-item 
          v-if="connectionData.conditionType === 'expression'" 
          label="条件表达式"
        >
          <el-input 
            v-model="connectionData.conditionExpression" 
            type="textarea" 
            :rows="3"
            placeholder="请输入条件表达式，如：${amount > 1000}"
            @input="handleUpdate"
          />
          <div class="form-tip">
            <el-text size="small" type="info">
              💡 支持 UEL 表达式，如：${variable == 'value'}
            </el-text>
          </div>
        </el-form-item>
        
        <el-form-item 
          v-if="connectionData.conditionType === 'script'" 
          label="脚本语言"
        >
          <el-select 
            v-model="connectionData.scriptFormat" 
            placeholder="请选择脚本语言"
            @change="handleUpdate"
          >
            <el-option label="JavaScript" value="javascript" />
            <el-option label="Groovy" value="groovy" />
            <el-option label="Python" value="python" />
          </el-select>
        </el-form-item>
        
        <el-form-item 
          v-if="connectionData.conditionType === 'script'" 
          label="脚本内容"
        >
          <el-input 
            v-model="connectionData.script" 
            type="textarea" 
            :rows="4"
            placeholder="请输入脚本内容"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item v-if="connectionData.conditionType !== 'none'" label="条件描述">
          <el-input 
            v-model="connectionData.conditionDescription" 
            placeholder="请输入条件描述"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 执行监听器 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">👂 执行监听器</span>
        </template>
        
        <el-form-item>
          <template #label>
            <span>监听器列表</span>
            <el-button 
              type="primary" 
              size="small" 
              plain 
              @click="addListener"
              style="margin-left: 8px;"
            >
              添加
            </el-button>
          </template>
        </el-form-item>
        
        <div v-if="connectionData.executionListeners?.length" class="listener-list">
          <div 
            v-for="(listener, index) in connectionData.executionListeners" 
            :key="index"
            class="listener-item"
          >
            <el-card shadow="never" class="listener-card">
              <el-row :gutter="12">
                <el-col :span="8">
                  <el-form-item label="事件" size="small">
                    <el-select 
                      v-model="listener.event" 
                      size="small"
                      @change="handleUpdate"
                    >
                      <el-option label="take" value="take" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="类型" size="small">
                    <el-select 
                      v-model="listener.type" 
                      size="small"
                      @change="handleUpdate"
                    >
                      <el-option label="Java类" value="class" />
                      <el-option label="表达式" value="expression" />
                      <el-option label="委托表达式" value="delegateExpression" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="操作" size="small">
                    <el-button 
                      type="danger" 
                      size="small" 
                      plain 
                      @click="removeListener(index)"
                    >
                      删除
                    </el-button>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-form-item :label="getListenerValueLabel(listener.type)" size="small">
                <el-input 
                  v-model="listener.value" 
                  :placeholder="getListenerValuePlaceholder(listener.type)"
                  @input="handleUpdate"
                />
              </el-form-item>
            </el-card>
          </div>
        </div>
        
        <el-empty v-else description="暂无监听器" :image-size="60" />
      </el-card>
      
      <!-- 样式配置 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">🎨 样式配置</span>
        </template>
        
        <el-form-item label="线条颜色">
          <el-color-picker 
            v-model="connectionData.strokeColor" 
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="线条宽度">
          <el-slider 
            v-model="connectionData.strokeWidth" 
            :min="1" 
            :max="5" 
            :step="0.5"
            show-input
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="线条样式">
          <el-select 
            v-model="connectionData.strokeDasharray" 
            placeholder="请选择线条样式"
            @change="handleUpdate"
          >
            <el-option label="实线" value="" />
            <el-option label="虚线" value="5,5" />
            <el-option label="点线" value="2,2" />
            <el-option label="点划线" value="10,5,2,5" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="箭头样式">
          <el-select 
            v-model="connectionData.markerEnd" 
            placeholder="请选择箭头样式"
            @change="handleUpdate"
          >
            <el-option label="标准箭头" value="url(#arrowhead)" />
            <el-option label="圆形" value="url(#circle)" />
            <el-option label="菱形" value="url(#diamond)" />
            <el-option label="无箭头" value="" />
          </el-select>
        </el-form-item>
      </el-card>
      
      <!-- 扩展属性 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">🔧 扩展属性</span>
        </template>
        
        <el-form-item label="优先级">
          <el-input-number 
            v-model="connectionData.priority" 
            :min="0" 
            :max="100" 
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="跳过表达式">
          <el-input 
            v-model="connectionData.skipExpression" 
            placeholder="请输入跳过表达式"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="文档说明">
          <el-input 
            v-model="connectionData.documentation" 
            type="textarea" 
            :rows="3"
            placeholder="请输入文档说明"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue'

// Props
const props = defineProps({
  connection: {
    type: Object,
    required: true
  }
})

// Emits
const emit = defineEmits(['update'])

// 响应式数据
const connectionData = reactive({ 
  ...props.connection,
  // 设置默认值
  conditionType: props.connection.conditionType || 'none',
  strokeColor: props.connection.strokeColor || '#666666',
  strokeWidth: props.connection.strokeWidth || 2,
  strokeDasharray: props.connection.strokeDasharray || '',
  markerEnd: props.connection.markerEnd || 'url(#arrowhead)',
  priority: props.connection.priority || 0,
  executionListeners: props.connection.executionListeners || []
})

// 监听 props 变化
watch(
  () => props.connection,
  (newConnection) => {
    Object.assign(connectionData, {
      ...newConnection,
      conditionType: newConnection.conditionType || 'none',
      strokeColor: newConnection.strokeColor || '#666666',
      strokeWidth: newConnection.strokeWidth || 2,
      strokeDasharray: newConnection.strokeDasharray || '',
      markerEnd: newConnection.markerEnd || 'url(#arrowhead)',
      priority: newConnection.priority || 0,
      executionListeners: newConnection.executionListeners || []
    })
  },
  { deep: true }
)

// 处理更新
const handleUpdate = () => {
  emit('update', { ...connectionData })
}

// 添加监听器
const addListener = () => {
  if (!connectionData.executionListeners) {
    connectionData.executionListeners = []
  }
  connectionData.executionListeners.push({
    event: 'take',
    type: 'class',
    value: ''
  })
  handleUpdate()
}

// 删除监听器
const removeListener = (index) => {
  connectionData.executionListeners.splice(index, 1)
  handleUpdate()
}

// 获取监听器值标签
const getListenerValueLabel = (type) => {
  const labelMap = {
    class: 'Java类',
    expression: '表达式',
    delegateExpression: '委托表达式'
  }
  return labelMap[type] || '值'
}

// 获取监听器值占位符
const getListenerValuePlaceholder = (type) => {
  const placeholderMap = {
    class: '请输入完整类名',
    expression: '请输入表达式',
    delegateExpression: '请输入委托表达式'
  }
  return placeholderMap[type] || '请输入值'
}
</script>

<style scoped>
.process-connection-properties {
  padding: 0;
}

.property-card {
  margin-bottom: 16px;
  border: 1px solid #e4e7ed;
}

.property-card:last-child {
  margin-bottom: 0;
}

.card-title {
  font-weight: 600;
  color: #303133;
}

.form-tip {
  margin-top: 4px;
}

.listener-list {
  max-height: 300px;
  overflow-y: auto;
}

.listener-item {
  margin-bottom: 12px;
}

.listener-item:last-child {
  margin-bottom: 0;
}

.listener-card {
  border: 1px solid #e4e7ed;
  background: #fafafa;
}

:deep(.el-card__header) {
  padding: 12px 16px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-card__body) {
  padding: 16px;
}

:deep(.el-form-item) {
  margin-bottom: 16px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  border-radius: 4px;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-slider) {
  margin: 8px 0;
}

:deep(.el-color-picker) {
  width: 100%;
}

:deep(.listener-card .el-card__body) {
  padding: 12px;
}

:deep(.listener-card .el-form-item) {
  margin-bottom: 8px;
}
</style>