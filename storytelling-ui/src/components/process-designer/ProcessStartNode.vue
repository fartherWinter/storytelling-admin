<template>
  <div 
    :class="['process-node', 'start-node', { selected: isSelected, preview: isPreview }]"
    :style="nodeStyle"
    @click="handleClick"
    @mousedown="handleMouseDown"
  >
    <!-- 节点图标 -->
    <div class="node-icon">
      <el-icon><VideoPlay /></el-icon>
    </div>
    
    <!-- 节点标签 -->
    <div class="node-label">{{ nodeData.label || '开始' }}</div>
    
    <!-- 连接点 -->
    <div v-if="!isPreview" class="connection-points">
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
import { VideoPlay, Delete } from '@element-plus/icons-vue'

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
  'delete'
])

// 计算节点样式
const nodeStyle = computed(() => {
  const { x = 0, y = 0, width = 100, height = 50 } = props.nodeData
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
  border: 3px solid transparent;
  border-radius: 50%;
  background: #f0f9ff;
  cursor: pointer;
  user-select: none;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 10;
  position: relative;
  overflow: hidden;
}

.process-node::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(135deg, #67c23a, #85ce61, #a8e6cf);
  border-radius: 50%;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.start-node {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 50%, #a8e6cf 100%);
  color: white;
  box-shadow: 
    0 4px 20px rgba(103, 194, 58, 0.3),
    0 2px 10px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.start-node:hover {
  transform: scale(1.08) translateY(-2px);
  box-shadow: 
    0 8px 30px rgba(103, 194, 58, 0.4),
    0 4px 20px rgba(0, 0, 0, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.start-node:hover::before {
  opacity: 1;
}

.start-node.selected {
  border-color: rgba(64, 158, 255, 0.8);
  box-shadow: 
    0 0 0 4px rgba(64, 158, 255, 0.3),
    0 8px 30px rgba(103, 194, 58, 0.4),
    0 4px 20px rgba(0, 0, 0, 0.15);
  transform: scale(1.1);
}

.start-node.preview {
  cursor: default;
  pointer-events: none;
  filter: brightness(0.95) saturate(0.9);
  transform: scale(0.95);
  transition: all 0.4s ease;
}

.start-node.preview:hover {
  transform: scale(0.98);
  filter: brightness(1) saturate(1);
}

.start-node.preview .connection-point {
  display: none;
}

.start-node.preview .node-actions {
  display: none;
}

.start-node.preview .node-icon {
  opacity: 0.9;
}

.start-node.preview .node-label {
  opacity: 0.9;
  font-weight: 500;
}

.node-icon {
  font-size: 28px;
  margin-bottom: 6px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  transition: all 0.3s ease;
}

.start-node:hover .node-icon {
  transform: scale(1.1) rotate(5deg);
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.node-label {
  font-size: 13px;
  font-weight: 600;
  text-align: center;
  line-height: 1.2;
  max-width: 90px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.start-node:hover .node-label {
  transform: translateY(-1px);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
}

.connection-points {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.connection-point {
  position: absolute;
  width: 10px;
  height: 10px;
  background: linear-gradient(135deg, #409eff, #66b3ff);
  border: 3px solid white;
  border-radius: 50%;
  cursor: crosshair;
  pointer-events: auto;
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 
    0 2px 8px rgba(64, 158, 255, 0.3),
    0 1px 4px rgba(0, 0, 0, 0.1);
  transform-origin: center;
}

.process-node:hover .connection-point {
  opacity: 1;
}

.connection-point.output {
  right: -8px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point:hover {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  transform: translateY(-50%) scale(1.6);
  box-shadow: 
    0 4px 16px rgba(103, 194, 58, 0.4),
    0 2px 8px rgba(0, 0, 0, 0.15);
  border-color: rgba(255, 255, 255, 0.9);
}

.node-actions {
  position: absolute;
  top: -12px;
  right: -12px;
  display: flex;
  gap: 6px;
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform: translateY(10px);
  z-index: 20;
}

.process-node:hover .node-actions {
  opacity: 1;
  transform: translateY(0);
}

.node-actions .el-button {
  width: 24px;
  height: 24px;
  padding: 0;
  border: 2px solid white;
  box-shadow: 
    0 2px 8px rgba(0, 0, 0, 0.15),
    0 1px 4px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.node-actions .el-button:hover {
  transform: scale(1.15) rotate(5deg);
  box-shadow: 
    0 4px 16px rgba(245, 108, 108, 0.4),
    0 2px 8px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
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