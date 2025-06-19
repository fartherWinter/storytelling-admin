<template>
  <div class="process-designer-unified">
    <!-- 头部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <el-input 
          v-model="store.processName" 
          placeholder="请输入流程名称"
          style="width: 200px;"
          size="small"
        />
      </div>
      
      <div class="header-center">
        <el-button-group size="small">
          <el-button :disabled="!historyOperations.canUndo.value" @click="historyOperations.undo">
            <el-icon><Back /></el-icon>
            撤销
          </el-button>
          <el-button :disabled="!historyOperations.canRedo.value" @click="historyOperations.redo">
            <el-icon><Right /></el-icon>
            重做
          </el-button>
        </el-button-group>
        
        <el-divider direction="vertical" />
        
        <el-button-group size="small">
          <el-button @click="zoomOut">
            <el-icon><ZoomOut /></el-icon>
          </el-button>
          <el-button disabled>{{ Math.round(store.zoom * 100) }}%</el-button>
          <el-button @click="zoomIn">
            <el-icon><ZoomIn /></el-icon>
          </el-button>
        </el-button-group>
        
        <el-button size="small" @click="zoomToFit">
          <el-icon><FullScreen /></el-icon>
          适应画布
        </el-button>
        
        <el-button size="small" @click="autoLayout">
          <el-icon><Grid /></el-icon>
          自动布局
        </el-button>
        
        <el-button size="small" @click="showValidationResult">
          <el-icon><CircleCheck /></el-icon>
          验证
        </el-button>
      </div>
      
      <div class="header-right">
        <el-dropdown @command="handleExport">
          <el-button size="small">
            <el-icon><Download /></el-icon>
            导出
            <el-icon><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="json">导出 JSON</el-dropdown-item>
              <el-dropdown-item command="bpmn">导出 BPMN</el-dropdown-item>
              <el-dropdown-item command="image">导出图片</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        
        <el-upload
          :show-file-list="false"
          :before-upload="handleImport"
          accept=".json"
          style="display: inline-block; margin-left: 8px;"
        >
          <el-button size="small">
            <el-icon><Upload /></el-icon>
            导入
          </el-button>
        </el-upload>
        
        <el-button-group size="small" style="margin-left: 8px;">
          <el-button type="primary" @click="saveProcess">
            <el-icon><Document /></el-icon>
            保存
          </el-button>
          <el-button @click="previewProcess">
            <el-icon><View /></el-icon>
            预览
          </el-button>
          <el-button type="success" @click="deployProcess">
            <el-icon><Promotion /></el-icon>
            部署
          </el-button>
        </el-button-group>
        
        <el-button size="small" type="danger" plain @click="clearCanvas" style="margin-left: 8px;">
          <el-icon><Delete /></el-icon>
          清空
        </el-button>
      </div>
    </div>
    
    <!-- 主体区域 -->
    <div class="designer-body">
      <!-- 组件面板 -->
      <div v-if="showComponentPanel" class="component-panel">
        <div class="panel-header">
          <span class="panel-title">{{ componentPanelTitle || '流程组件' }}</span>
          <el-button 
            size="small" 
            text 
            @click="state.showGrid = !state.showGrid"
            :type="state.showGrid ? 'primary' : 'default'"
          >
            <el-icon><Grid /></el-icon>
          </el-button>
        </div>
        
        <div class="component-groups">
          <div 
            v-for="group in filteredNodeGroups" 
            :key="group.name" 
            class="component-group"
          >
            <div class="group-header">
              <span class="group-icon">{{ group.icon || '📁' }}</span>
              <span>{{ group.title }}</span>
            </div>
            <div class="group-content">
              <div
                v-for="nodeType in group.items"
                :key="nodeType"
                class="component-item"
                :draggable="true"
                @dragstart="handleDragStart($event, nodeType)"
                @dragend="endDrag"
              >
                <div class="component-icon" :class="`${nodeType}-node`">
                  <component :is="getNodeIcon(nodeType)" />
                </div>
                <div class="component-name">{{ getNodeLabel(nodeType) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 设计画布 -->
      <div class="canvas-container">
        <div class="canvas-toolbar">
          <div class="toolbar-left">
            <el-button size="small" :icon="Delete" @click="clearCanvas">
              清空画布
            </el-button>
            <el-button v-if="showAutoLayout" size="small" :icon="Grid" @click="autoLayout">
              自动布局
            </el-button>
          </div>
          <div class="toolbar-right">
            <span class="canvas-info">节点: {{ processNodes.length }}</span>
            <span class="canvas-info">连线: {{ processConnections.length }}</span>
          </div>
        </div>
        
        <div 
          ref="canvasRef"
          class="design-canvas"
          :style="{ transform: `scale(${store.zoom})` }"
          @drop="handleDrop"
          @dragover="handleDragOver"
          @click="handleCanvasClick"
          @wheel="handleWheel"
          @mousedown="startPan"
        >
          <!-- 网格背景 -->
          <div v-if="showGrid" class="canvas-grid"></div>
          
          <!-- 连线层 -->
          <svg class="connections-layer">
            <defs>
              <marker id="arrowhead" markerWidth="10" markerHeight="7" 
                      refX="9" refY="3.5" orient="auto">
                <polygon points="0 0, 10 3.5, 0 7" fill="#666" />
              </marker>
            </defs>
            <g v-for="connection in processConnections" :key="connection.id">
              <ProcessConnection
                :connection="connection"
                :selected="selectedConnectionId === connection.id"
                @select="selectConnection"
                @delete="deleteConnection"
              />
            </g>
          </svg>
          
          <!-- 节点层 -->
          <div class="nodes-layer">
            <component
              v-for="node in processNodes"
              :key="node.id"
              :is="getNodeComponent(node.type)"
              :node-data="node"
              :selected="selectedNodeId === node.id"
              :scale="scale"
              @select="selectNode"
              @delete="deleteNode"
              @drag="handleNodeDrag"
              @connect="handleNodeConnect"
            />
          </div>
        </div>
      </div>
      
      <!-- 属性面板 -->
      <div v-if="showPropertyPanel" class="property-panel">
        <div class="panel-header">
          <span>属性配置</span>
          <el-button 
            v-if="selectedNode || selectedConnection"
            size="small" 
            text 
            type="danger"
            @click="clearSelection"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="panel-content">
          <!-- 节点属性 -->
          <ProcessNodeProperties
            v-if="selectedNode"
            :node="selectedNode"
            @update="updateNodeProperties"
          />
          
          <!-- 连线属性 -->
          <ProcessConnectionProperties
            v-else-if="selectedConnection"
            :connection="selectedConnection"
            @update="updateConnectionProperties"
          />
          
          <!-- 画布属性 -->
          <div v-else class="canvas-properties">
            <el-card shadow="never" class="property-card">
              <template #header>
                <span class="card-title">🎨 画布设置</span>
              </template>
              
              <el-form label-width="80px" size="small">
                <el-form-item label="流程名称">
                  <el-input 
                    v-model="store.processName" 
                    placeholder="请输入流程名称"
                  />
                </el-form-item>
                
                <el-form-item label="显示网格">
                  <el-switch v-model="showGrid" />
                </el-form-item>
                
                <el-form-item label="网格大小">
                  <el-slider 
                    v-model="gridSize" 
                    :min="10" 
                    :max="50" 
                    :step="5"
                    show-input
                  />
                </el-form-item>
                
                <el-form-item label="网格对齐">
                  <el-switch v-model="snapToGrid" />
                </el-form-item>
              </el-form>
            </el-card>
            
            <el-card shadow="never" class="property-card">
              <template #header>
                <span class="card-title">📊 统计信息</span>
              </template>
              
              <div class="stats-grid">
                <div class="stat-item">
                  <div class="stat-value">{{ processNodes.length }}</div>
                  <div class="stat-label">节点数量</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ processConnections.length }}</div>
                  <div class="stat-label">连线数量</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ getNodeTypeCount('userTask') }}</div>
                  <div class="stat-label">用户任务</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ getNodeTypeCount('serviceTask') }}</div>
                  <div class="stat-label">服务任务</div>
                </div>
              </div>
            </el-card>
            
            <el-card shadow="never" class="property-card">
              <template #header>
                <span class="card-title">🔧 快捷操作</span>
              </template>
              
              <div class="quick-actions">
                <el-button size="small" @click="autoLayout" block>
                  <el-icon><Grid /></el-icon>
                  自动布局
                </el-button>
                <el-button size="small" @click="resetZoom" block>
                  <el-icon><FullScreen /></el-icon>
                  适应画布
                </el-button>
                <el-button size="small" @click="showValidationResult" block>
                  <el-icon><Check /></el-icon>
                  验证流程
                </el-button>
                <el-button size="small" type="danger" plain @click="clearCanvas" block>
                  <el-icon><Delete /></el-icon>
                  清空画布
                </el-button>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { 
  Back, Right, ZoomIn, ZoomOut, FullScreen, Grid, CircleCheck,
  Download, Upload, ArrowDown, Document, View, Promotion, Delete,
  Close, Check
} from '@element-plus/icons-vue'
import { useProcessDesignerStore } from '@/stores/processDesigner'
import { useProcessDesignerUnified } from '@/composables/useProcessDesignerUnified'
import ProcessConnection from './ProcessConnection.vue'
import ProcessNodeProperties from './ProcessNodeProperties.vue'
import ProcessConnectionProperties from './ProcessConnectionProperties.vue'

// Props 定义
const props = defineProps({
  // 基础配置
  title: {
    type: String,
    default: '流程设计器'
  },
  mode: {
    type: String,
    default: 'full', // full, simple, readonly
    validator: (value) => ['full', 'simple', 'readonly'].includes(value)
  },
  
  // 面板显示控制
  showComponentPanel: {
    type: Boolean,
    default: true
  },
  showPropertyPanel: {
    type: Boolean,
    default: true
  },
  componentPanelTitle: {
    type: String,
    default: ''
  },
  
  // 功能开关
  showPreview: {
    type: Boolean,
    default: true
  },
  showExport: {
    type: Boolean,
    default: true
  },
  showDeploy: {
    type: Boolean,
    default: false
  },
  showAutoLayout: {
    type: Boolean,
    default: true
  },
  showGrid: {
    type: Boolean,
    default: true
  },
  
  // 节点组配置
  enabledNodeTypes: {
    type: Array,
    default: () => ['start', 'end', 'userTask', 'serviceTask', 'scriptTask', 'exclusiveGateway', 'parallelGateway']
  },
  
  // 初始数据
  initialData: {
    type: Object,
    default: () => ({})
  }
})

// Emits 定义
const emit = defineEmits([
  'save', 'preview', 'export', 'deploy', 'change',
  'node-select', 'connection-select', 'canvas-click'
])

// 使用状态管理和组合式函数
const store = useProcessDesignerStore()
const {
  // 状态
  state,
  historyOperations,
  // 节点操作
  createNode,
  copyNode,
  deleteNode,
  updateNode,
  selectNode,
  moveNode,
  // 连线操作
  startConnection,
  completeConnection,
  cancelConnection,
  deleteConnection,
  updateConnection,
  selectConnection,
  // 画布操作
  zoomIn,
  zoomOut,
  resetZoom,
  zoomToFit,
  clearCanvas,
  autoLayout,
  // 导入导出
  exportToJSON,
  importFromJSON,
  exportToBPMN,
  exportToImage,
  // 验证
  validateProcess,
  showValidationResult,
  // 工具函数
  getNodeIcon,
  getNodeColor,
  calculateConnectionPath,
  snapToGrid: snapPosition,
  // 配置
  nodeTypes,
  nodeGroups
} = useProcessDesignerUnified()

// 响应式数据
const canvasRef = ref(null)
const isDragging = ref(false)
const dragStartPos = ref({ x: 0, y: 0 })
const tempConnection = ref(null)

// 计算属性
const processName = computed({
  get: () => store.processName,
  set: (value) => store.setProcessName(value)
})

const processNodes = computed(() => store.nodes)
const processConnections = computed(() => store.connections)
const selectedNodeId = computed(() => store.selectedNodeId)
const selectedConnectionId = computed(() => store.selectedConnectionId)
const selectedNode = computed(() => store.selectedNode)
const selectedConnection = computed(() => store.selectedConnection)
const scale = computed(() => store.zoom)
const showGrid = computed({
  get: () => state.showGrid,
  set: (value) => state.showGrid = value
})
const gridSize = computed({
  get: () => state.gridSize,
  set: (value) => state.gridSize = value
})
const snapToGrid = computed({
  get: () => state.snapToGrid,
  set: (value) => state.snapToGrid = value
})

// 节点组配置（过滤启用的节点类型）
const filteredNodeGroups = computed(() => {
  return nodeGroups.value.map(group => ({
    ...group,
    items: group.items.filter(type => props.enabledNodeTypes.includes(type))
  })).filter(group => group.items.length > 0)
})

// 获取节点标签
const getNodeLabel = (nodeType) => {
  const labels = {
    start: '开始',
    end: '结束',
    userTask: '用户任务',
    serviceTask: '服务任务',
    scriptTask: '脚本任务',
    exclusiveGateway: '排他网关',
    parallelGateway: '并行网关',
    timerEvent: '定时事件',
    messageEvent: '消息事件'
  }
  return labels[nodeType] || nodeType
}

// 获取节点组件
const getNodeComponent = (nodeType) => {
  // 这里应该返回对应的节点组件
  return 'div' // 临时返回
}

// 拖拽处理
const handleDragStart = (event, nodeType) => {
  event.dataTransfer.setData('application/json', JSON.stringify({ nodeType }))
  event.dataTransfer.effectAllowed = 'copy'
  state.isDragging = true
}

const endDrag = () => {
  state.isDragging = false
}

// 拖拽悬停
const handleDragOver = (event) => {
  event.preventDefault()
  event.dataTransfer.dropEffect = 'copy'
}

// 拖拽放置
const handleDrop = (event) => {
  event.preventDefault()
  
  try {
    const data = JSON.parse(event.dataTransfer.getData('application/json'))
    const rect = canvasRef.value.getBoundingClientRect()
    const x = (event.clientX - rect.left) / store.zoom - store.panX
    const y = (event.clientY - rect.top) / store.zoom - store.panY
    
    const position = state.snapToGrid ? snapPosition(x, y) : { x, y }
    createNode(data.nodeType, position.x, position.y)
    
    emit('change')
  } catch (error) {
    console.error('拖拽放置失败:', error)
  }
  state.isDragging = false
}

// 画布交互
const handleCanvasClick = (event) => {
  if (event.target.closest('.process-node') || event.target.closest('.process-connection')) {
    return
  }
  clearSelection()
  emit('canvas-click')
}

const clearSelection = () => {
  store.clearSelection()
}

const startPan = (event) => {
  if (event.target === canvasRef.value || event.target.closest('.canvas-background')) {
    isDragging.value = true
    dragStartPos.value = { x: event.clientX, y: event.clientY }
    clearSelection()
  }
}

const handleWheel = (event) => {
  event.preventDefault()
  const rect = canvasRef.value.getBoundingClientRect()
  
  const delta = event.deltaY > 0 ? -0.1 : 0.1
  const newZoom = Math.max(state.minZoom, Math.min(state.maxZoom, store.zoom + delta))
  
  // 以鼠标位置为中心缩放
  const mouseX = event.clientX - rect.left
  const mouseY = event.clientY - rect.top
  
  const zoomRatio = newZoom / store.zoom
  const newPanX = mouseX - (mouseX - store.panX) * zoomRatio
  const newPanY = mouseY - (mouseY - store.panY) * zoomRatio
  
  store.setZoom(newZoom)
  store.setPan(newPanX, newPanY)
}

// 鼠标事件处理
const handleMouseMove = (event) => {
  if (isDragging.value) {
    const deltaX = event.clientX - dragStartPos.value.x
    const deltaY = event.clientY - dragStartPos.value.y
    store.setPan(store.panX + deltaX, store.panY + deltaY)
    dragStartPos.value = { x: event.clientX, y: event.clientY }
  }
}

const handleMouseUp = () => {
  isDragging.value = false
}

// 节点拖拽
const handleNodeDrag = (nodeId, position) => {
  const node = processNodes.value.find(n => n.id === nodeId)
  if (node) {
    node.x = position.x
    node.y = position.y
    emit('change')
  }
}

// 节点连接
const handleNodeConnect = (fromNodeId, toNodeId, fromPort, toPort) => {
  // 实现节点连接逻辑
  emit('change')
}

// 选择处理
const handleNodeUpdate = (properties) => {
  if (store.selectedNode) {
    updateNode(store.selectedNode.id, properties)
  }
}

const handleConnectionUpdate = (properties) => {
  if (store.selectedConnection) {
    updateConnection(store.selectedConnection.id, properties)
  }
}

// 工具栏方法
const saveProcess = async () => {
  try {
    const processData = {
      name: store.processName,
      nodes: store.nodes,
      connections: store.connections,
      version: '1.0',
      createdAt: new Date().toISOString()
    }
    
    emit('save', processData)
    console.log('保存流程:', processData)
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const previewProcess = () => {
  const previewData = {
    name: store.processName,
    nodes: store.nodes,
    connections: store.connections
  }
  emit('preview', previewData)
  console.log('预览流程:', previewData)
}

const handleExport = (command) => {
  switch (command) {
    case 'json':
      exportToJSON()
      break
    case 'bpmn':
      exportToBPMN()
      break
    case 'image':
      exportToImage()
      break
  }
}

const deployProcess = async () => {
  try {
    const validation = validateProcess()
    if (!validation.isValid) {
      showValidationResult()
      return
    }
    
    const deployData = {
      name: store.processName,
      nodes: store.nodes,
      connections: store.connections
    }
    emit('deploy', deployData)
    console.log('部署流程:', deployData)
  } catch (error) {
    console.error('部署失败:', error)
  }
}

const handleImport = (file) => {
  return false // 阻止默认上传行为
}

const clearCanvas = () => {
  store.clearAll()
  emit('change')
}

// 统计方法
const getNodeTypeCount = (type) => {
  return store.nodes.filter(node => node.type === type).length
}

// 初始化
const initialize = () => {
  if (props.initialData.name) {
    store.setProcessName(props.initialData.name)
  }
  if (props.initialData.nodes) {
    store.setNodes([...props.initialData.nodes])
  }
  if (props.initialData.connections) {
    store.setConnections([...props.initialData.connections])
  }
}

// 生命周期
onMounted(() => {
  initialize()
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseup', handleMouseUp)
})
</script>

<style scoped>
.process-designer-unified {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f7fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.designer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-center {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.designer-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.component-panel {
  width: 260px;
  background: white;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 600;
  color: #303133;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.panel-title {
  font-size: 14px;
}

.component-groups {
  padding: 16px;
}

.component-group {
  margin-bottom: 20px;
}

.group-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  font-size: 13px;
  font-weight: 600;
  color: #495057;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 8px;
  margin-bottom: 12px;
  border: 1px solid #dee2e6;
}

.group-icon {
  font-size: 16px;
}

.group-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.component-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 14px 10px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  cursor: grab;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  position: relative;
  overflow: hidden;
}

.component-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(64, 158, 255, 0.1), transparent);
  transition: left 0.5s;
}

.component-item:hover {
  border-color: #409eff;
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.component-item:hover::before {
  left: 100%;
}

.component-item:active {
  cursor: grabbing;
  transform: scale(0.95) translateY(-2px);
}

.component-icon {
  font-size: 22px;
  color: #409eff;
  transition: transform 0.3s;
}

.component-item:hover .component-icon {
  transform: scale(1.1);
}

.component-name {
  font-size: 11px;
  color: #606266;
  text-align: center;
  line-height: 1.3;
  font-weight: 500;
}

.canvas-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.canvas-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  gap: 16px;
}

.canvas-info {
  font-size: 12px;
  color: #909399;
}

.design-canvas {
  flex: 1;
  position: relative;
  overflow: hidden;
  background: #fafbfc;
  transform-origin: 0 0;
  cursor: grab;
}

.design-canvas:active {
  cursor: grabbing;
}

.canvas-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(to right, #e4e7ed 1px, transparent 1px),
    linear-gradient(to bottom, #e4e7ed 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.5;
}

.connections-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.nodes-layer {
  position: relative;
  width: 100%;
  height: 100%;
  z-index: 2;
}

.property-panel {
  width: 320px;
  background: white;
  border-left: 1px solid #e4e7ed;
  overflow-y: auto;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.panel-content {
  padding: 0;
}

.canvas-properties {
  padding: 20px;
}

.property-card {
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
}

.property-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid #dee2e6;
  padding: 12px 16px;
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: #495057;
}

.property-card :deep(.el-card__body) {
  padding: 16px;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #6c757d;
  font-weight: 500;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.quick-actions .el-button {
  justify-content: flex-start;
  text-align: left;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .component-panel {
    width: 220px;
  }
  
  .property-panel {
    width: 280px;
  }
}

@media (max-width: 1200px) {
  .component-panel {
    width: 200px;
  }
  
  .property-panel {
    width: 260px;
  }
  
  .group-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .designer-header {
    padding: 8px 16px;
  }
  
  .header-left {
    gap: 12px;
  }
  
  .component-panel {
    width: 180px;
  }
  
  .property-panel {
    width: 240px;
  }
}
</style>