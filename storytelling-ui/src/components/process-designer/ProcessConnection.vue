<template>
  <g 
    :class="['process-connection', { selected: isSelected, preview: isPreview }]"
    @click="handleClick"
  >
    <!-- 主连接线 -->
    <path
      :d="pathData"
      :stroke="strokeColor"
      :stroke-width="strokeWidth"
      fill="none"
      :stroke-dasharray="strokeDasharray"
      class="connection-line"
    />
    
    <!-- 箭头 -->
    <polygon
      :points="arrowPoints"
      :fill="strokeColor"
      class="connection-arrow"
    />
    
    <!-- 连接线标签 -->
    <g v-if="connectionData.label" class="connection-label-group">
      <rect
        :x="labelPosition.x - labelSize.width / 2"
        :y="labelPosition.y - labelSize.height / 2"
        :width="labelSize.width"
        :height="labelSize.height"
        fill="white"
        stroke="#dcdfe6"
        stroke-width="1"
        rx="4"
        class="label-background"
      />
      <text
        :x="labelPosition.x"
        :y="labelPosition.y + 4"
        text-anchor="middle"
        class="connection-label"
        :fill="labelColor"
      >
        {{ connectionData.label }}
      </text>
    </g>
    
    <!-- 选中状态的删除按钮 -->
    <g v-if="isSelected && !isPreview" class="connection-actions">
      <circle
        :cx="deleteButtonPosition.x"
        :cy="deleteButtonPosition.y"
        r="10"
        fill="#f56c6c"
        stroke="white"
        stroke-width="2"
        class="delete-button"
        @click.stop="handleDelete"
      />
      <text
        :x="deleteButtonPosition.x"
        :y="deleteButtonPosition.y + 3"
        text-anchor="middle"
        fill="white"
        font-size="12"
        font-weight="bold"
        class="delete-icon"
        @click.stop="handleDelete"
      >
        ×
      </text>
    </g>
  </g>
</template>

<script setup>
import { computed } from 'vue'

// Props
const props = defineProps({
  connectionData: {
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
  'delete'
])

// 计算连接线路径
const pathData = computed(() => {
  const { startX, startY, endX, endY } = props.connectionData
  
  // 计算控制点，创建平滑的贝塞尔曲线
  const deltaX = endX - startX
  const deltaY = endY - startY
  
  // 水平距离较大时使用水平控制点，否则使用垂直控制点
  if (Math.abs(deltaX) > Math.abs(deltaY)) {
    const controlX1 = startX + deltaX * 0.5
    const controlY1 = startY
    const controlX2 = startX + deltaX * 0.5
    const controlY2 = endY
    
    return `M ${startX} ${startY} C ${controlX1} ${controlY1}, ${controlX2} ${controlY2}, ${endX} ${endY}`
  } else {
    const controlX1 = startX
    const controlY1 = startY + deltaY * 0.5
    const controlX2 = endX
    const controlY2 = startY + deltaY * 0.5
    
    return `M ${startX} ${startY} C ${controlX1} ${controlY1}, ${controlX2} ${controlY2}, ${endX} ${endY}`
  }
})

// 计算箭头位置和角度
const arrowPoints = computed(() => {
  const { startX, startY, endX, endY } = props.connectionData
  
  // 计算箭头角度
  const angle = Math.atan2(endY - startY, endX - startX)
  const arrowLength = 12
  const arrowWidth = 8
  
  // 箭头三个点的坐标
  const x1 = endX
  const y1 = endY
  const x2 = endX - arrowLength * Math.cos(angle - Math.PI / 6)
  const y2 = endY - arrowLength * Math.sin(angle - Math.PI / 6)
  const x3 = endX - arrowLength * Math.cos(angle + Math.PI / 6)
  const y3 = endY - arrowLength * Math.sin(angle + Math.PI / 6)
  
  return `${x1},${y1} ${x2},${y2} ${x3},${y3}`
})

// 计算标签位置
const labelPosition = computed(() => {
  const { startX, startY, endX, endY } = props.connectionData
  return {
    x: (startX + endX) / 2,
    y: (startY + endY) / 2
  }
})

// 计算标签尺寸
const labelSize = computed(() => {
  const label = props.connectionData.label || ''
  const charWidth = 8 // 估算字符宽度
  const padding = 8
  return {
    width: Math.max(label.length * charWidth + padding, 40),
    height: 20
  }
})

// 计算删除按钮位置
const deleteButtonPosition = computed(() => {
  const { startX, startY, endX, endY } = props.connectionData
  return {
    x: (startX + endX) / 2 + 20,
    y: (startY + endY) / 2 - 20
  }
})

// 连接线颜色
const strokeColor = computed(() => {
  if (props.isSelected) return '#67c23a'
  if (props.connectionData.type === 'conditional') return '#e6a23c'
  if (props.connectionData.type === 'default') return '#909399'
  return '#409eff'
})

// 连接线宽度
const strokeWidth = computed(() => {
  return props.isSelected ? 3 : 2
})

// 连接线样式
const strokeDasharray = computed(() => {
  if (props.connectionData.type === 'conditional') return '5,5'
  return 'none'
})

// 标签颜色
const labelColor = computed(() => {
  return props.isSelected ? '#67c23a' : '#606266'
})

// 处理点击事件
const handleClick = (event) => {
  if (props.isPreview) return
  emit('click', event, props.connectionData)
}

// 处理删除事件
const handleDelete = () => {
  emit('delete', props.connectionData)
}
</script>

<style scoped>
.process-connection {
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.process-connection.preview {
  cursor: default;
  pointer-events: none;
  opacity: 0.8;
  filter: saturate(0.8);
}

.connection-line {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  stroke-linecap: round;
  stroke-linejoin: round;
}

.connection-line:hover {
  stroke-width: 4;
  filter: drop-shadow(0 4px 12px rgba(64, 158, 255, 0.3));
}

.process-connection.selected .connection-line {
  stroke-width: 4;
  filter: drop-shadow(0 4px 16px rgba(103, 194, 58, 0.4));
  animation: pulse-connection 2s infinite;
}

@keyframes pulse-connection {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.connection-arrow {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.process-connection:hover .connection-arrow {
  filter: drop-shadow(0 4px 8px rgba(64, 158, 255, 0.3));
  transform-origin: center;
  animation: arrow-bounce 0.6s ease-out;
}

@keyframes arrow-bounce {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.connection-label {
  font-size: 13px;
  font-weight: 600;
  user-select: none;
  letter-spacing: 0.3px;
  transition: all 0.3s ease;
}

.label-background {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 8px rgba(0, 0, 0, 0.1));
}

.process-connection:hover .label-background {
  stroke: #409eff;
  stroke-width: 2;
  fill: rgba(64, 158, 255, 0.05);
  filter: drop-shadow(0 4px 12px rgba(64, 158, 255, 0.2));
}

.process-connection.selected .label-background {
  stroke: #67c23a;
  stroke-width: 2;
  fill: rgba(103, 194, 58, 0.05);
  filter: drop-shadow(0 4px 16px rgba(103, 194, 58, 0.3));
}

.process-connection:hover .connection-label {
  fill: #409eff;
  font-weight: 700;
}

.process-connection.selected .connection-label {
  fill: #67c23a;
  font-weight: 700;
}

.connection-actions {
  opacity: 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform: scale(0.8);
}

.process-connection.selected .connection-actions {
  opacity: 1;
  transform: scale(1);
}

.delete-button {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  filter: drop-shadow(0 2px 8px rgba(245, 108, 108, 0.3));
}

.delete-button:hover {
  fill: #f78989;
  transform: scale(1.2) rotate(5deg);
  filter: drop-shadow(0 4px 16px rgba(245, 108, 108, 0.5));
}

.delete-icon {
  cursor: pointer;
  pointer-events: none;
  transition: all 0.3s ease;
}

.delete-button:hover + .delete-icon {
  transform: rotate(90deg);
}

/* 动画效果 */
@keyframes connection-draw {
  from {
    stroke-dasharray: 1000;
    stroke-dashoffset: 1000;
  }
  to {
    stroke-dasharray: 1000;
    stroke-dashoffset: 0;
  }
}

.connection-line.new {
  animation: connection-draw 0.8s ease-out;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .connection-label {
    font-size: 11px;
  }
  
  .delete-button {
    r: 8;
  }
  
  .delete-icon {
    font-size: 10px;
  }
  
  .connection-line:hover {
    stroke-width: 3;
  }
  
  .process-connection.selected .connection-line {
    stroke-width: 3;
  }
}

@media (max-width: 480px) {
  .connection-label {
    font-size: 10px;
  }
  
  .label-background {
    rx: 2;
  }
}
</style>