<template>
  <div 
    :class="['process-node', 'exclusive-gateway', { selected: isSelected, preview: isPreview }]"
    :style="nodeStyle"
    @click="handleClick"
    @mousedown="handleMouseDown"
  >
    <!-- 菱形背景 -->
    <div class="diamond-shape">
      <!-- 节点图标 -->
      <div class="node-icon">
        <el-icon><Switch /></el-icon>
      </div>
    </div>
    
    <!-- 节点标签 -->
    <div class="node-label">{{ nodeData.label || '排他网关' }}</div>
    
    <!-- 连接点 -->
    <div v-if="!isPreview" class="connection-points">
      <div 
        class="connection-point input"
        @click.stop="handleConnectionClick('input')"
        @mousedown.stop
      ></div>
      <div 
        class="connection-point output-top"
        @click.stop="handleConnectionClick('output')"
        @mousedown.stop
      ></div>
      <div 
        class="connection-point output-bottom"
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
import { Switch, Edit, Delete } from '@element-plus/icons-vue'

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
    height: `${height + 20}px`, // 额外高度用于标签
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
  justify-content: flex-start;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  z-index: 10;
}

.exclusive-gateway {
  color: #f56c6c;
}

.exclusive-gateway:hover {
  transform: scale(1.05);
}

.exclusive-gateway.selected {
  filter: drop-shadow(0 0 8px rgba(103, 194, 58, 0.6));
}

.exclusive-gateway.preview {
  cursor: default;
  pointer-events: none;
}

.diamond-shape {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #f56c6c, #f78989);
  border: 2px solid #f56c6c;
  transform: rotate(45deg);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
  transition: all 0.2s ease;
}

.exclusive-gateway:hover .diamond-shape {
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
}

.exclusive-gateway.selected .diamond-shape {
  border-color: #67c23a;
}

.node-icon {
  transform: rotate(-45deg);
  font-size: 20px;
  color: white;
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
  margin-top: 8px;
  color: #606266;
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
  left: 50%;
  top: 10px;
  transform: translateX(-50%);
}

.connection-point.output-top {
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point.output-bottom {
  left: 50%;
  bottom: 20px;
  transform: translateX(-50%);
}

.connection-point:hover {
  background: #67c23a;
  transform: translateX(-50%) scale(1.2);
}

.connection-point.output-top:hover {
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
  .diamond-shape {
    width: 50px;
    height: 50px;
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