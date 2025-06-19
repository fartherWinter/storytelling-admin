<template>
  <div 
    :class="['process-node', 'script-task', { selected: isSelected, preview: isPreview }]"
    :style="nodeStyle"
    @click="handleClick"
    @mousedown="handleMouseDown"
  >
    <!-- 节点图标 -->
    <div class="node-icon">
      <el-icon><Document /></el-icon>
    </div>
    
    <!-- 节点标签 -->
    <div class="node-label">{{ nodeData.label || '脚本任务' }}</div>
    
    <!-- 任务信息 -->
    <div v-if="nodeData.properties?.scriptFormat" class="node-info">
      <span class="script-format">{{ nodeData.properties.scriptFormat }}</span>
    </div>
    
    <!-- 连接点 -->
    <div v-if="!isPreview" class="connection-points">
      <div 
        class="connection-point input"
        @click.stop="handleConnectionClick('input')"
        @mousedown.stop
      ></div>
      <div 
        class="connection-point output"
        @click.stop="handleConnectionClick('output')"
        @mousedown.stop
      ></div>
    </div>
    
    <!-- 选中状态的操作按钮 -->
    <div v-if="isSelected && !isPreview" class="node-actions">
      <el-button 
        size="small" 
        type="primary" 
        :icon="Edit" 
        circle 
        @click.stop="handleEdit"
      />
      <el-button 
        size="small" 
        type="danger" 
        :icon="Delete" 
        circle 
        @click.stop="handleDelete"
      />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Document, Edit, Delete } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  nodeData: {
    type: Object,
    required: true
  },
  isSelected: {
    type: Boolean,
    default: false
  },
  isPreview: {
    type: Boolean,
    default: false
  },
  scale: {
    type: Number,
    default: 1
  }
})

// Emits
const emit = defineEmits([
  'click',
  'mousedown',
  'connection-click',
  'edit',
  'delete'
])

// 计算节点样式
const nodeStyle = computed(() => {
  const { x = 0, y = 0, width = 120, height = 80 } = props.nodeData
  return {
    left: `${x}px`,
    top: `${y}px`,
    width: `${width}px`,
    height: `${height}px`,
    transform: `scale(${props.scale})`
  }
})

// 处理点击事件
const handleClick = (event) => {
  emit('click', event, props.nodeData)
}

// 处理鼠标按下事件
const handleMouseDown = (event) => {
  if (props.isPreview) return
  emit('mousedown', event, props.nodeData)
}

// 处理连接点击事件
const handleConnectionClick = (type) => {
  emit('connection-click', type, props.nodeData)
}

// 处理编辑事件
const handleEdit = () => {
  emit('edit', props.nodeData)
}

// 处理删除事件
const handleDelete = () => {
  emit('delete', props.nodeData)
}
</script>

<style scoped>
.process-node {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px solid #909399;
  border-radius: 8px;
  background: #f0f9ff;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  z-index: 10;
  padding: 8px;
}

.script-task {
  background: linear-gradient(135deg, #909399, #a6a9ad);
  color: white;
  box-shadow: 0 2px 8px rgba(144, 147, 153, 0.3);
}

.script-task:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(144, 147, 153, 0.4);
}

.script-task.selected {
  border-color: #67c23a;
  box-shadow: 0 0 0 2px rgba(103, 194, 58, 0.3);
}

.script-task.preview {
  cursor: default;
  pointer-events: none;
}

.node-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.node-label {
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  line-height: 1.2;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 2px;
}

.node-info {
  font-size: 10px;
  opacity: 0.9;
  text-align: center;
}

.script-format {
  background: rgba(255, 255, 255, 0.2);
  padding: 1px 4px;
  border-radius: 2px;
  font-size: 9px;
  text-transform: uppercase;
}

.connection-points {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.connection-point {
  position: absolute;
  width: 8px;
  height: 8px;
  background: #409eff;
  border: 2px solid #fff;
  border-radius: 50%;
  cursor: crosshair;
  pointer-events: auto;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.process-node:hover .connection-point {
  opacity: 1;
}

.connection-point.input {
  left: -6px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point.output {
  right: -6px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point:hover {
  background: #67c23a;
  transform: translateY(-50%) scale(1.2);
}

.node-actions {
  position: absolute;
  top: -15px;
  right: -15px;
  z-index: 20;
  display: flex;
  gap: 4px;
}

.node-actions .el-button {
  width: 24px;
  height: 24px;
  padding: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .node-icon {
    font-size: 20px;
  }
  
  .node-label {
    font-size: 10px;
  }
  
  .connection-point {
    width: 10px;
    height: 10px;
  }
}
</style>