<template>
  <div 
    :class="['process-node', 'end-node', { selected: isSelected, preview: isPreview }]"
    :style="nodeStyle"
    @click="handleClick"
    @mousedown="handleMouseDown"
  >
    <!-- 节点图标 -->
    <div class="node-icon">
      <el-icon><VideoPause /></el-icon>
    </div>
    
    <!-- 节点标签 -->
    <div class="node-label">{{ nodeData.label || '结束' }}</div>
    
    <!-- 连接点 -->
    <div v-if="!isPreview" class="connection-points">
      <div 
        class="connection-point input"
        @click.stop="handleConnectionClick('input')"
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
import { VideoPause, Delete } from '@element-plus/icons-vue'

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
  border: 2px solid #f56c6c;
  border-radius: 50%;
  background: #f0f9ff;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  z-index: 10;
}

.end-node {
  background: linear-gradient(135deg, #f56c6c, #f78989);
  color: white;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.end-node:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
}

.end-node.selected {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.3);
}

.end-node.preview {
  cursor: default;
  pointer-events: none;
}

.node-icon {
  font-size: 20px;
  margin-bottom: 4px;
}

.node-label {
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  line-height: 1.2;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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

.connection-point:hover {
  background: #67c23a;
  transform: translateY(-50%) scale(1.2);
}

.node-actions {
  position: absolute;
  top: -15px;
  right: -15px;
  z-index: 20;
}

.node-actions .el-button {
  width: 24px;
  height: 24px;
  padding: 0;
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