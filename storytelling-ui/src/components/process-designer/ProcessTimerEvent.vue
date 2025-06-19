<template>
  <div 
    :class="['process-node', 'timer-event', { selected: isSelected, preview: isPreview }]"
    :style="nodeStyle"
    @click="handleClick"
    @mousedown="handleMouseDown"
  >
    <!-- 圆形背景 -->
    <div class="circle-shape">
      <!-- 节点图标 -->
      <div class="node-icon">
        <el-icon><Clock /></el-icon>
      </div>
    </div>
    
    <!-- 节点标签 -->
    <div class="node-label">{{ nodeData.label || '定时事件' }}</div>
    
    <!-- 事件信息 -->
    <div v-if="nodeData.properties?.timerDefinition" class="node-info">
      <span class="timer-info">{{ formatTimerInfo(nodeData.properties.timerDefinition) }}</span>
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
import { Clock, Edit, Delete } from '@element-plus/icons-vue'

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
  const { x = 0, y = 0, width = 80, height = 80 } = props.nodeData
  return {
    left: `${x}px`,
    top: `${y}px`,
    width: `${width}px`,
    height: `${height + 30}px`, // 额外高度用于标签和信息
    transform: `scale(${props.scale})`
  }
})

// 格式化定时器信息
const formatTimerInfo = (timerDefinition) => {
  if (!timerDefinition) return ''
  
  if (timerDefinition.type === 'duration') {
    return `延时: ${timerDefinition.value}`
  } else if (timerDefinition.type === 'date') {
    return `日期: ${timerDefinition.value}`
  } else if (timerDefinition.type === 'cycle') {
    return `周期: ${timerDefinition.value}`
  }
  
  return timerDefinition.value || ''
}

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
  justify-content: flex-start;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  z-index: 10;
}

.timer-event {
  color: #e6a23c;
}

.timer-event:hover {
  transform: scale(1.05);
}

.timer-event.selected {
  filter: drop-shadow(0 0 8px rgba(103, 194, 58, 0.6));
}

.timer-event.preview {
  cursor: default;
  pointer-events: none;
}

.circle-shape {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #e6a23c, #ebb563);
  border: 3px solid #e6a23c;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(230, 162, 60, 0.3);
  transition: all 0.2s ease;
  position: relative;
}

/* 内圈边框效果 */
.circle-shape::before {
  content: '';
  position: absolute;
  width: 50px;
  height: 50px;
  border: 2px solid rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.timer-event:hover .circle-shape {
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.4);
}

.timer-event.selected .circle-shape {
  border-color: #67c23a;
}

.node-icon {
  font-size: 20px;
  color: white;
  z-index: 1;
}

.node-label {
  font-size: 11px;
  font-weight: 500;
  text-align: center;
  line-height: 1.2;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 6px;
  color: #606266;
}

.node-info {
  font-size: 9px;
  text-align: center;
  margin-top: 2px;
  color: #909399;
}

.timer-info {
  background: rgba(230, 162, 60, 0.1);
  padding: 1px 4px;
  border-radius: 2px;
  border: 1px solid rgba(230, 162, 60, 0.3);
  font-size: 8px;
  max-width: 70px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
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
  top: 30px;
  transform: translateY(-50%);
}

.connection-point.output {
  right: -6px;
  top: 30px;
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
  .circle-shape {
    width: 50px;
    height: 50px;
  }
  
  .circle-shape::before {
    width: 40px;
    height: 40px;
  }
  
  .node-icon {
    font-size: 16px;
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