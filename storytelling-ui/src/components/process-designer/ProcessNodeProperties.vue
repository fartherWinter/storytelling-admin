<template>
  <div class="process-node-properties">
    <el-form :model="nodeData" label-width="80px" size="small">
      <!-- 基础信息 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">📝 基础信息</span>
        </template>
        
        <el-form-item label="节点ID">
          <el-input v-model="nodeData.id" disabled />
        </el-form-item>
        
        <el-form-item label="节点名称">
          <el-input 
            v-model="nodeData.name" 
            placeholder="请输入节点名称"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="节点类型">
          <el-tag :type="getNodeTypeColor(nodeData.type)">
            {{ getNodeTypeName(nodeData.type) }}
          </el-tag>
        </el-form-item>
        
        <el-form-item label="描述">
          <el-input 
            v-model="nodeData.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入节点描述"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 位置信息 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">📍 位置信息</span>
        </template>
        
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="X坐标">
              <el-input-number 
                v-model="nodeData.x" 
                :min="0" 
                :step="10"
                @change="handleUpdate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Y坐标">
              <el-input-number 
                v-model="nodeData.y" 
                :min="0" 
                :step="10"
                @change="handleUpdate"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="宽度">
              <el-input-number 
                v-model="nodeData.width" 
                :min="60" 
                :step="10"
                @change="handleUpdate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高度">
              <el-input-number 
                v-model="nodeData.height" 
                :min="40" 
                :step="10"
                @change="handleUpdate"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 用户任务特有属性 -->
      <el-card v-if="nodeData.type === 'userTask'" class="property-card" shadow="never">
        <template #header>
          <span class="card-title">👤 用户任务配置</span>
        </template>
        
        <el-form-item label="执行人">
          <el-input 
            v-model="nodeData.properties.assignee" 
            placeholder="请输入执行人"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="候选用户">
          <el-input 
            v-model="nodeData.properties.candidateUsers" 
            placeholder="多个用户用逗号分隔"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="候选组">
          <el-input 
            v-model="nodeData.properties.candidateGroups" 
            placeholder="多个组用逗号分隔"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="表单Key">
          <el-input 
            v-model="nodeData.properties.formKey" 
            placeholder="请输入表单Key"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="优先级">
          <el-slider 
            v-model="nodeData.properties.priority" 
            :min="0" 
            :max="100" 
            show-input
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="到期时间">
          <el-input 
            v-model="nodeData.properties.dueDate" 
            placeholder="如：P1D（1天后）"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 服务任务特有属性 -->
      <el-card v-if="nodeData.type === 'serviceTask'" class="property-card" shadow="never">
        <template #header>
          <span class="card-title">⚙️ 服务任务配置</span>
        </template>
        
        <el-form-item label="实现方式">
          <el-select 
            v-model="nodeData.properties.implementation" 
            placeholder="请选择实现方式"
            @change="handleUpdate"
          >
            <el-option label="Java类" value="class" />
            <el-option label="表达式" value="expression" />
            <el-option label="委托表达式" value="delegateExpression" />
            <el-option label="外部任务" value="external" />
          </el-select>
        </el-form-item>
        
        <el-form-item v-if="nodeData.properties.implementation === 'class'" label="Java类">
          <el-input 
            v-model="nodeData.properties.class" 
            placeholder="请输入完整类名"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item v-if="nodeData.properties.implementation === 'expression'" label="表达式">
          <el-input 
            v-model="nodeData.properties.expression" 
            placeholder="请输入表达式"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item v-if="nodeData.properties.implementation === 'delegateExpression'" label="委托表达式">
          <el-input 
            v-model="nodeData.properties.delegateExpression" 
            placeholder="请输入委托表达式"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item v-if="nodeData.properties.implementation === 'external'" label="主题">
          <el-input 
            v-model="nodeData.properties.topic" 
            placeholder="请输入外部任务主题"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 脚本任务特有属性 -->
      <el-card v-if="nodeData.type === 'scriptTask'" class="property-card" shadow="never">
        <template #header>
          <span class="card-title">📜 脚本任务配置</span>
        </template>
        
        <el-form-item label="脚本格式">
          <el-select 
            v-model="nodeData.properties.scriptFormat" 
            placeholder="请选择脚本格式"
            @change="handleUpdate"
          >
            <el-option label="JavaScript" value="javascript" />
            <el-option label="Groovy" value="groovy" />
            <el-option label="Python" value="python" />
            <el-option label="JRuby" value="jruby" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="脚本内容">
          <el-input 
            v-model="nodeData.properties.script" 
            type="textarea" 
            :rows="6"
            placeholder="请输入脚本内容"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="结果变量">
          <el-input 
            v-model="nodeData.properties.resultVariable" 
            placeholder="请输入结果变量名"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 网关特有属性 -->
      <el-card v-if="isGateway(nodeData.type)" class="property-card" shadow="never">
        <template #header>
          <span class="card-title">🔀 网关配置</span>
        </template>
        
        <el-form-item v-if="nodeData.type === 'exclusiveGateway'" label="默认流">
          <el-input 
            v-model="nodeData.properties.defaultFlow" 
            placeholder="请输入默认流ID"
            @input="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="网关方向">
          <el-select 
            v-model="nodeData.properties.gatewayDirection" 
            placeholder="请选择网关方向"
            @change="handleUpdate"
          >
            <el-option label="发散" value="diverging" />
            <el-option label="汇聚" value="converging" />
            <el-option label="混合" value="mixed" />
          </el-select>
        </el-form-item>
      </el-card>
      
      <!-- 定时事件特有属性 -->
      <el-card v-if="nodeData.type === 'timerEvent'" class="property-card" shadow="never">
        <template #header>
          <span class="card-title">⏰ 定时事件配置</span>
        </template>
        
        <el-form-item label="定时类型">
          <el-select 
            v-model="nodeData.properties.timerType" 
            placeholder="请选择定时类型"
            @change="handleUpdate"
          >
            <el-option label="时间日期" value="timeDate" />
            <el-option label="持续时间" value="timeDuration" />
            <el-option label="循环" value="timeCycle" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="定时表达式">
          <el-input 
            v-model="nodeData.properties.timerDefinition" 
            placeholder="请输入定时表达式"
            @input="handleUpdate"
          />
        </el-form-item>
      </el-card>
      
      <!-- 扩展属性 -->
      <el-card class="property-card" shadow="never">
        <template #header>
          <span class="card-title">🔧 扩展属性</span>
        </template>
        
        <el-form-item label="异步执行">
          <el-switch 
            v-model="nodeData.properties.async" 
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="排他执行">
          <el-switch 
            v-model="nodeData.properties.exclusive" 
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="重试次数">
          <el-input-number 
            v-model="nodeData.properties.retryTimeCycle" 
            :min="0" 
            :max="10"
            @change="handleUpdate"
          />
        </el-form-item>
        
        <el-form-item label="文档说明">
          <el-input 
            v-model="nodeData.properties.documentation" 
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
  node: {
    type: Object,
    required: true
  }
})

// Emits
const emit = defineEmits(['update'])

// 响应式数据
const nodeData = reactive({ ...props.node })

// 监听 props 变化
watch(
  () => props.node,
  (newNode) => {
    Object.assign(nodeData, newNode)
  },
  { deep: true }
)

// 处理更新
const handleUpdate = () => {
  emit('update', { ...nodeData })
}

// 获取节点类型颜色
const getNodeTypeColor = (type) => {
  const colorMap = {
    start: 'success',
    end: 'danger',
    userTask: 'primary',
    serviceTask: 'warning',
    scriptTask: 'info',
    exclusiveGateway: 'warning',
    parallelGateway: 'success',
    timerEvent: 'primary'
  }
  return colorMap[type] || 'info'
}

// 获取节点类型名称
const getNodeTypeName = (type) => {
  const nameMap = {
    start: '开始节点',
    end: '结束节点',
    userTask: '用户任务',
    serviceTask: '服务任务',
    scriptTask: '脚本任务',
    exclusiveGateway: '排他网关',
    parallelGateway: '并行网关',
    timerEvent: '定时事件'
  }
  return nameMap[type] || type
}

// 判断是否为网关
const isGateway = (type) => {
  return ['exclusiveGateway', 'parallelGateway', 'inclusiveGateway'].includes(type)
}
</script>

<style scoped>
.process-node-properties {
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
</style>