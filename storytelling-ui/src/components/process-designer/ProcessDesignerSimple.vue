<template>
  <div class="process-designer">
    <!-- 头部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <h3 class="designer-title">流程设计器</h3>
        <el-input
          v-model="processName"
          placeholder="请输入流程名称"
          style="width: 200px; margin-left: 16px;"
          size="small"
        />
      </div>
      
      <div class="header-right">
        <el-button-group>
          <el-button size="small" :icon="RefreshLeft" @click="handleUndo" :disabled="!canUndo">
            撤销
          </el-button>
          <el-button size="small" :icon="RefreshRight" @click="handleRedo" :disabled="!canRedo">
            重做
          </el-button>
        </el-button-group>
        
        <el-button-group style="margin-left: 8px;">
          <el-button size="small" :icon="ZoomOut" @click="handleZoomOut">
            缩小
          </el-button>
          <el-button size="small" @click="handleResetZoom">
            {{ Math.round(scale * 100) }}%
          </el-button>
          <el-button size="small" :icon="ZoomIn" @click="handleZoomIn">
            放大
          </el-button>
        </el-button-group>
        
        <el-button-group style="margin-left: 8px;">
          <el-button size="small" :icon="View" @click="handlePreview">
            预览
          </el-button>
          <el-button size="small" :icon="Download" @click="handleExport">
            导出
          </el-button>
          <el-button size="small" type="primary" :icon="Check" @click="handleSave">
            保存
          </el-button>
        </el-button-group>
      </div>
    </div>
    
    <!-- 主体区域 -->
    <div class="designer-body">
      <!-- 组件面板 -->
      <div class="component-panel">
        <div class="panel-header">
          <span>流程组件</span>
        </div>
        
        <div class="component-groups">
          <div 
            v-for="group in nodeGroups" 
            :key="group.name" 
            class="component-group"
          >
            <div class="group-title">{{ group.title }}</div>
            <div class="group-items">
              <div
                v-for="nodeType in group.items"
                :key="nodeType"
                class="component-item"
                :draggable="true"
                @dragstart="handleDragStart($event, nodeType)"
              >
                <div class="item-icon">
                  <component :is="getNodeIcon(nodeType)" />
                </div>
                <div class="item-label">{{ getNodeLabel(nodeType) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 设计画布 -->
      <div class="canvas-container">
        <div class="canvas-toolbar">
          <el-button size="small" :icon="Delete" @click="clearProcess">
            清空画布
          </el-button>
          <el-button size="small" :icon="Grid" @click="toggleGrid">
            {{ showGrid ? '隐藏' : '显示' }}网格
          </el-button>
        </div>
        
        <div 
          ref="canvasRef"
          class="design-canvas"
          :class="{ 'show-grid': showGrid }"
          :style="canvasStyle"
          @click="handleCanvasClick"
          @dragover="handleDragOver"
          @drop="handleDrop"
        >
          <!-- SVG 连接线层 -->
          <svg class="connection-layer" :style="svgStyle">
            <component
              v-for="connection in processConnections"
              :key="connection.id"
              :is="componentMap['connection']"
              :connection-data="connection"
              :is-selected="selectedConnectionId === connection.id"
              :scale="canvasScale"
              @click="selectConnection"
              @delete="deleteConnection"
            />
            
            <!-- 临时连接线 -->
            <path
              v-if="tempConnection"
              :d="tempConnectionPath"
              stroke="#409eff"
              stroke-width="2"
              stroke-dasharray="5,5"
              fill="none"
              class="temp-connection"
            />
          </svg>
          
          <!-- 流程节点层 -->
          <component
            v-for="node in processNodes"
            :key="node.id"
            :is="componentMap[node.type]"
            :node-data="node"
            :selected="selectedNodeId === node.id"
            @click="selectNode(node.id)"
            @delete="deleteSelectedNode"
            @start-connection="startConnection"
            @finish-connection="finishConnection" />
        </div>
      </div>
      
      <!-- 属性面板 -->
      <div class="properties-panel">
        <div class="panel-header">
          <h3>属性配置</h3>
        </div>
        
        <div class="panel-content" v-if="selectedNode">
          <el-form :model="selectedNode" label-width="80px" size="small">
            <el-form-item label="节点ID">
              <el-input v-model="selectedNode.id" disabled />
            </el-form-item>
            
            <el-form-item label="节点名称">
              <el-input v-model="selectedNode.label" />
            </el-form-item>
            
            <el-form-item label="节点类型">
              <el-input v-model="selectedNode.type" disabled />
            </el-form-item>
            
            <!-- 用户任务特有属性 -->
            <template v-if="selectedNode.type === 'userTask'">
              <el-form-item label="分配用户">
                <el-input v-model="selectedNode.props.assignee" placeholder="输入用户ID" />
              </el-form-item>
              
              <el-form-item label="候选组">
                <el-input v-model="selectedNode.props.candidateGroups" placeholder="输入组名" />
              </el-form-item>
            </template>
            
            <!-- 服务任务特有属性 -->
            <template v-if="selectedNode.type === 'serviceTask'">
              <el-form-item label="服务类">
                <el-input v-model="selectedNode.props.serviceClass" placeholder="输入服务类名" />
              </el-form-item>
              
              <el-form-item label="方法名">
                <el-input v-model="selectedNode.props.method" placeholder="输入方法名" />
              </el-form-item>
            </template>
            
            <!-- 脚本任务特有属性 -->
            <template v-if="selectedNode.type === 'scriptTask'">
              <el-form-item label="脚本语言">
                <el-select v-model="selectedNode.props.scriptFormat">
                  <el-option label="JavaScript" value="javascript" />
                  <el-option label="Groovy" value="groovy" />
                  <el-option label="Python" value="python" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="脚本内容">
                <el-input 
                  v-model="selectedNode.props.script" 
                  type="textarea" 
                  :rows="4" 
                  placeholder="输入脚本代码" />
              </el-form-item>
            </template>
            
            <!-- 网关特有属性 -->
            <template v-if="selectedNode.type.includes('Gateway')">
              <el-form-item label="网关条件">
                <el-input 
                  v-model="selectedNode.props.condition" 
                  type="textarea" 
                  :rows="3" 
                  placeholder="输入条件表达式" />
              </el-form-item>
            </template>
            
            <!-- 定时事件特有属性 -->
            <template v-if="selectedNode.type === 'timer'">
              <el-form-item label="定时类型">
                <el-select v-model="selectedNode.props.timerType">
                  <el-option label="固定时间" value="timeDate" />
                  <el-option label="持续时间" value="timeDuration" />
                  <el-option label="循环周期" value="timeCycle" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="时间表达式">
                <el-input v-model="selectedNode.props.timerExpression" placeholder="输入时间表达式" />
              </el-form-item>
            </template>
          </el-form>
        </div>
        
        <div class="panel-content" v-else-if="selectedConnection">
          <el-form :model="selectedConnection" label-width="80px" size="small">
            <el-form-item label="连接ID">
              <el-input v-model="selectedConnection.id" disabled />
            </el-form-item>
            
            <el-form-item label="连接名称">
              <el-input v-model="selectedConnection.label" placeholder="输入连接名称" />
            </el-form-item>
            
            <el-form-item label="条件表达式">
              <el-input 
                v-model="selectedConnection.condition" 
                type="textarea" 
                :rows="3" 
                placeholder="输入条件表达式" />
            </el-form-item>
          </el-form>
        </div>
        
        <div class="panel-content" v-else>
          <div class="empty-state">
            <p>请选择节点或连接线以配置属性</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { componentMap, nodeConfig } from './index.js'
import { useProcessDesigner } from '@/composables/useProcessDesigner.js'

// 使用组合式函数
const {
  // 状态
  processName,
  processNodes,
  processConnections,
  selectedNodeId,
  selectedConnectionId,
  selectedNode,
  selectedConnection,
  canvasScale,
  showGrid,
  isConnecting,
  connectingFrom,
  tempConnection,
  canUndo,
  canRedo,
  statistics,
  
  // 方法
  createNodeFromDrop,
  selectNode,
  selectConnection,
  clearSelection,
  startConnection,
  updateTempConnection,
  finishConnection,
  cancelConnection,
  moveNode,
  deleteSelectedNode,
  deleteSelectedConnection,
  zoomIn,
  zoomOut,
  resetZoom,
  toggleGrid,
  undo,
  redo,
  clearProcess,
  saveProcess,
  exportProcess,
  importProcess,
  handleKeyboardShortcuts,
  getNodeAtPosition,
  getConnectionAtPosition,
  initializeHistory
} = useProcessDesigner()

// 节点分组配置
const nodeGroups = [
  {
    title: '事件节点',
    icon: 'Clock',
    nodes: [
      { type: 'start', label: '开始', icon: 'VideoPlay' },
      { type: 'end', label: '结束', icon: 'VideoPause' },
      { type: 'timer', label: '定时器', icon: 'Clock' }
    ]
  },
  {
    title: '任务节点', 
    icon: 'User',
    nodes: [
      { type: 'userTask', label: '用户任务', icon: 'User' },
      { type: 'serviceTask', label: '服务任务', icon: 'Setting' },
      { type: 'scriptTask', label: '脚本任务', icon: 'Document' }
    ]
  },
  {
    title: '网关节点',
    icon: 'Switch', 
    nodes: [
      { type: 'exclusiveGateway', label: '排他网关', icon: 'Switch' },
      { type: 'parallelGateway', label: '并行网关', icon: 'Plus' }
    ]
  }
]

// 计算属性
const canvasStyle = computed(() => ({
  transform: `scale(${scale.value})`,
  transformOrigin: 'top left'
}))

const svgStyle = computed(() => ({
  transform: `scale(${scale.value})`,
  transformOrigin: 'top left',
  width: `${100 / scale.value}%`,
  height: `${100 / scale.value}%`
}))

const canUndo = computed(() => historyIndex.value > 0)
const canRedo = computed(() => historyIndex.value < history.value.length - 1)

const tempConnectionPath = computed(() => {
  if (!tempConnection.value) return ''
  const { startX, startY, endX, endY } = tempConnection.value
  return `M ${startX} ${startY} L ${endX} ${endY}`
})

// 获取节点图标
const getNodeIcon = (nodeType) => {
  const iconMap = {
    'start-node': 'VideoPlay',
    'end-node': 'VideoPause', 
    'user-task': User,
    'service-task': Setting,
    'script-task': Document,
    'exclusive-gateway': Switch,
    'parallel-gateway': Plus,
    'timer-event': Clock
  }
  return iconMap[nodeType] || Document
}

// 获取节点标签
const getNodeLabel = (nodeType) => {
  const config = getNodeConfig(nodeType)
  return config?.label || '未知节点'
}

// 获取节点组件
const getNodeComponent = (nodeType) => {
  return componentMap[nodeType]
}

// 本地引用
const canvasRef = ref(null)

// 拖拽开始
const handleDragStart = (event, nodeType) => {
  event.dataTransfer.setData('nodeType', nodeType)
  event.dataTransfer.effectAllowed = 'copy'
}

// 拖拽悬停
const handleDragOver = (event) => {
  event.preventDefault()
  event.dataTransfer.dropEffect = 'copy'
}

// 拖拽放置
const handleDrop = (event) => {
  event.preventDefault()
  const nodeType = event.dataTransfer.getData('nodeType')
  if (!nodeType) return
  
  const rect = canvasRef.value.getBoundingClientRect()
  const x = (event.clientX - rect.left) / canvasScale.value
  const y = (event.clientY - rect.top) / canvasScale.value
  
  createNodeFromDrop(nodeType, x - 60, y - 40) // 居中
}

// 创建节点
const createNode = (nodeType, x, y) => {
  const config = getNodeConfig(nodeType)
  if (!config) return
  
  const node = {
    id: generateNodeId(),
    type: nodeType,
    label: config.label,
    x: x - (config.width || 120) / 2,
    y: y - (config.height || 80) / 2,
    width: config.width || 120,
    height: config.height || 80,
    properties: { ...config.defaultProps }
  }
  
  processNodes.value.push(node)
  saveToHistory()
  
  ElMessage.success(`已添加${config.label}`)
}

// 选择节点
const selectNode = (event, node) => {
  event.stopPropagation()
  selectedNode.value = node
  selectedConnection.value = null
}

// 选择连接线
const selectConnection = (event, connection) => {
  event.stopPropagation()
  selectedConnection.value = connection
  selectedNode.value = null
}

// 画布点击
const handleCanvasClick = () => {
  selectedNode.value = null
  selectedConnection.value = null
}

// 开始拖拽节点
const startDrag = (event, node) => {
  if (isConnecting.value) return
  
  isDragging.value = true
  dragNode.value = node
  
  const rect = canvasRef.value.getBoundingClientRect()
  dragOffset.x = (event.clientX - rect.left) / scale.value - node.x
  dragOffset.y = (event.clientY - rect.top) / scale.value - node.y
  
  document.addEventListener('mousemove', handleDragMove)
  document.addEventListener('mouseup', handleDragEnd)
}

// 拖拽移动
const handleDragMove = (event) => {
  if (!isDragging.value || !dragNode.value) return
  
  const rect = canvasRef.value.getBoundingClientRect()
  const x = (event.clientX - rect.left) / scale.value - dragOffset.x
  const y = (event.clientY - rect.top) / scale.value - dragOffset.y
  
  dragNode.value.x = Math.max(0, x)
  dragNode.value.y = Math.max(0, y)
  
  // 更新相关连接线
  updateNodeConnections(dragNode.value)
}

// 拖拽结束
const handleDragEnd = () => {
  if (isDragging.value) {
    saveToHistory()
  }
  
  isDragging.value = false
  dragNode.value = null
  
  document.removeEventListener('mousemove', handleDragMove)
  document.removeEventListener('mouseup', handleDragEnd)
}

// 画布事件处理
function handleCanvasMouseMove(event) {
  if (isConnecting.value) {
    const rect = canvasRef.value.getBoundingClientRect()
    const x = (event.clientX - rect.left) / canvasScale.value
    const y = (event.clientY - rect.top) / canvasScale.value
    updateTempConnection(x, y)
  }
}

function handleCanvasClick(event) {
  if (isConnecting.value) {
    cancelConnection()
  } else {
    clearSelection()
  }
}

// 文件导入处理
function handleFileImport(event) {
  const file = event.target.files[0]
  if (file) {
    importProcess(file)
    // 清空input值，允许重复选择同一文件
    event.target.value = ''
  }
}

// 预览功能
function previewProcess() {
  if (processNodes.value.length === 0) {
    ElMessage.warning('没有可预览的内容')
    return
  }
  
  console.log('预览流程:', {
    name: processName.value,
    nodes: processNodes.value,
    connections: processConnections.value
  })
  ElMessage.info('预览功能开发中...')
}

// 连接移动
const handleConnectionMove = (event) => {
  if (!isConnecting.value || !tempConnection.value) return
  
  const rect = canvasRef.value.getBoundingClientRect()
  const x = (event.clientX - rect.left) / scale.value
  const y = (event.clientY - rect.top) / scale.value
  
  tempConnection.value.endX = x
  tempConnection.value.endY = y
}

// 连接结束
const handleConnectionEnd = (event) => {
  if (!isConnecting.value) return
  
  // 检查是否连接到有效的输入点
  const targetElement = document.elementFromPoint(event.clientX, event.clientY)
  if (targetElement?.classList.contains('connection-point') && 
      targetElement?.classList.contains('input')) {
    // 创建连接线
    createConnection()
  }
  
  // 清理状态
  isConnecting.value = false
  connectionStart.value = null
  tempConnection.value = null
  
  document.removeEventListener('mousemove', handleConnectionMove)
  document.removeEventListener('mouseup', handleConnectionEnd)
}

// 创建连接线
const createConnection = () => {
  // 这里需要根据实际的目标节点来创建连接
  // 简化实现，实际项目中需要更复杂的逻辑
  const connection = {
    id: generateConnectionId(),
    sourceNodeId: connectionStart.value.node.id,
    targetNodeId: 'target-node-id', // 需要实际获取
    startX: tempConnection.value.startX,
    startY: tempConnection.value.startY,
    endX: tempConnection.value.endX,
    endY: tempConnection.value.endY,
    type: 'normal',
    label: ''
  }
  
  connections.value.push(connection)
  saveToHistory()
}

// 更新节点连接线
const updateNodeConnections = (node) => {
  connections.value.forEach(connection => {
    if (connection.sourceNodeId === node.id) {
      connection.startX = node.x + node.width
      connection.startY = node.y + node.height / 2
    }
    if (connection.targetNodeId === node.id) {
      connection.endX = node.x
      connection.endY = node.y + node.height / 2
    }
  })
}

// 编辑节点
const editNode = (node) => {
  selectedNode.value = node
}

// 删除节点
const deleteNode = (node) => {
  ElMessageBox.confirm(
    `确定要删除节点 "${node.label}" 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 删除相关连接线
    connections.value = connections.value.filter(
      conn => conn.sourceNodeId !== node.id && conn.targetNodeId !== node.id
    )
    
    // 删除节点
    const index = processNodes.value.findIndex(n => n.id === node.id)
    if (index > -1) {
      processNodes.value.splice(index, 1)
    }
    
    // 清除选中状态
    if (selectedNode.value?.id === node.id) {
      selectedNode.value = null
    }
    
    saveToHistory()
    ElMessage.success('节点已删除')
  })
}

// 删除连接线
const deleteConnection = (connection) => {
  const index = connections.value.findIndex(c => c.id === connection.id)
  if (index > -1) {
    connections.value.splice(index, 1)
    
    if (selectedConnection.value?.id === connection.id) {
      selectedConnection.value = null
    }
    
    saveToHistory()
    ElMessage.success('连接线已删除')
  }
}

// 缩放控制
const handleZoomIn = () => {
  scale.value = Math.min(scale.value * 1.2, 3)
}

const handleZoomOut = () => {
  scale.value = Math.max(scale.value / 1.2, 0.2)
}

const handleResetZoom = () => {
  scale.value = 1
}

// 切换网格
const toggleGrid = () => {
  showGrid.value = !showGrid.value
}

// 清空画布
const handleClearCanvas = () => {
  ElMessageBox.confirm(
    '确定要清空画布吗？此操作不可撤销。',
    '确认清空',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    processNodes.value = []
    connections.value = []
    selectedNode.value = null
    selectedConnection.value = null
    saveToHistory()
    ElMessage.success('画布已清空')
  })
}

// 历史记录管理
const saveToHistory = () => {
  const state = {
    nodes: JSON.parse(JSON.stringify(processNodes.value)),
    connections: JSON.parse(JSON.stringify(connections.value))
  }
  
  // 删除当前位置之后的历史记录
  history.value = history.value.slice(0, historyIndex.value + 1)
  history.value.push(state)
  historyIndex.value = history.value.length - 1
  
  // 限制历史记录数量
  if (history.value.length > 50) {
    history.value.shift()
    historyIndex.value--
  }
}

const handleUndo = () => {
  if (!canUndo.value) return
  
  historyIndex.value--
  const state = history.value[historyIndex.value]
  processNodes.value = JSON.parse(JSON.stringify(state.nodes))
  connections.value = JSON.parse(JSON.stringify(state.connections))
  selectedNode.value = null
  selectedConnection.value = null
}

const handleRedo = () => {
  if (!canRedo.value) return
  
  historyIndex.value++
  const state = history.value[historyIndex.value]
  processNodes.value = JSON.parse(JSON.stringify(state.nodes))
  connections.value = JSON.parse(JSON.stringify(state.connections))
  selectedNode.value = null
  selectedConnection.value = null
}

// 功能按钮
const handlePreview = () => {
  ElMessage.info('预览功能开发中...')
}

const handleExport = () => {
  const processData = {
    name: processName.value,
    nodes: processNodes.value,
    connections: connections.value
  }
  
  const blob = new Blob([JSON.stringify(processData, null, 2)], {
    type: 'application/json'
  })
  
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${processName.value}.json`
  a.click()
  URL.revokeObjectURL(url)
  
  ElMessage.success('流程已导出')
}

const handleSave = () => {
  if (!processName.value.trim()) {
    ElMessage.warning('请输入流程名称')
    return
  }
  
  if (processNodes.value.length === 0) {
    ElMessage.warning('请至少添加一个节点')
    return
  }
  
  // 这里应该调用API保存流程
  ElMessage.success('流程保存成功')
}

// 初始化
onMounted(() => {
  // 保存初始状态
  saveToHistory()
})
</script>

<style scoped>
.process-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.designer-header {
  height: 64px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-bottom: none;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
}

.header-left {
  display: flex;
  align-items: center;
}

.designer-title {
  margin: 0;
  color: #ffffff;
  font-size: 20px;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-right .el-button-group {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-right .el-button {
  border: none;
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  font-weight: 500;
  transition: all 0.3s ease;
}

.header-right .el-button:hover {
  background: rgba(255, 255, 255, 1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.header-right .el-button.is-disabled {
  background: rgba(255, 255, 255, 0.5);
  color: #c0c4cc;
}

.header-right .el-input {
  border-radius: 8px;
  overflow: hidden;
}

.header-right .el-input__wrapper {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.designer-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.component-panel {
  width: 260px;
  background: rgba(255, 255, 255, 0.95);
  border-right: none;
  display: flex;
  flex-direction: column;
  backdrop-filter: blur(10px);
  box-shadow: 2px 0 20px rgba(0, 0, 0, 0.1);
}

.panel-header {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid rgba(228, 231, 237, 0.6);
  font-weight: 700;
  color: #2c3e50;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(240, 248, 255, 0.8));
  font-size: 14px;
  letter-spacing: 0.5px;
}

.component-groups {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  scrollbar-width: thin;
  scrollbar-color: rgba(64, 158, 255, 0.3) transparent;
}

.component-groups::-webkit-scrollbar {
  width: 6px;
}

.component-groups::-webkit-scrollbar-track {
  background: transparent;
}

.component-groups::-webkit-scrollbar-thumb {
  background: rgba(64, 158, 255, 0.3);
  border-radius: 3px;
}

.component-group {
  margin-bottom: 20px;
}

.group-title {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 12px;
  padding: 0 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.group-items {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.component-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 12px;
  border: 2px solid transparent;
  border-radius: 12px;
  cursor: grab;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.component-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1), rgba(103, 194, 58, 0.1));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.component-item:hover {
  border-color: #409eff;
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.25);
  transform: translateY(-3px) scale(1.02);
}

.component-item:hover::before {
  opacity: 1;
}

.component-item:active {
  cursor: grabbing;
  transform: translateY(-1px) scale(0.98);
}

.item-icon {
  font-size: 24px;
  color: #667eea;
  margin-bottom: 8px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.component-item:hover .item-icon {
  color: #409eff;
  transform: scale(1.1);
}

.item-label {
  font-size: 12px;
  color: #4b5563;
  text-align: center;
  line-height: 1.3;
  font-weight: 500;
  position: relative;
  z-index: 1;
}

.component-item:hover .item-label {
  color: #1f2937;
}

.canvas-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-radius: 0 0 0 20px;
  overflow: hidden;
}

.canvas-toolbar {
  height: 48px;
  background: rgba(255, 255, 255, 0.9);
  border-bottom: 1px solid rgba(228, 231, 237, 0.6);
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 12px;
  backdrop-filter: blur(10px);
}

.canvas-toolbar .el-button {
  border-radius: 8px;
  border: 1px solid rgba(64, 158, 255, 0.2);
  background: rgba(255, 255, 255, 0.8);
  color: #667eea;
  font-weight: 500;
  transition: all 0.3s ease;
}

.canvas-toolbar .el-button:hover {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.design-canvas {
  flex: 1;
  position: relative;
  overflow: auto;
  background: #ffffff;
  cursor: default;
  border-radius: 20px 0 0 0;
  margin: 8px;
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.05);
}

.design-canvas.show-grid {
  background-image: 
    linear-gradient(to right, rgba(64, 158, 255, 0.1) 1px, transparent 1px),
    linear-gradient(to bottom, rgba(64, 158, 255, 0.1) 1px, transparent 1px);
  background-size: 24px 24px;
}

.connection-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.connection-layer > * {
  pointer-events: auto;
}

.temp-connection {
  pointer-events: none;
}

.properties-panel {
  width: 320px;
  background: rgba(255, 255, 255, 0.95);
  border-left: none;
  display: flex;
  flex-direction: column;
  backdrop-filter: blur(10px);
  box-shadow: -2px 0 20px rgba(0, 0, 0, 0.1);
}

.properties-panel .panel-header {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(240, 248, 255, 0.8));
}

.properties-panel .panel-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scrollbar-width: thin;
  scrollbar-color: rgba(64, 158, 255, 0.3) transparent;
}

.panel-content::-webkit-scrollbar {
  width: 6px;
}

.panel-content::-webkit-scrollbar-track {
  background: transparent;
}

.panel-content::-webkit-scrollbar-thumb {
  background: rgba(64, 158, 255, 0.3);
  border-radius: 3px;
}

.panel-content .el-form-item {
  margin-bottom: 20px;
}

.panel-content .el-form-item__label {
  color: #4b5563;
  font-weight: 600;
  font-size: 13px;
}

.panel-content .el-input__wrapper {
  border-radius: 8px;
  border: 1px solid rgba(64, 158, 255, 0.2);
  transition: all 0.3s ease;
}

.panel-content .el-input__wrapper:hover {
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.15);
}

.panel-content .el-select {
  width: 100%;
}

.panel-content .el-textarea__inner {
  border-radius: 8px;
  border: 1px solid rgba(64, 158, 255, 0.2);
  transition: all 0.3s ease;
}

.panel-content .el-textarea__inner:hover {
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.15);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #9ca3af;
  font-size: 14px;
}

.empty-state p {
  margin: 0;
  text-align: center;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .component-panel {
    width: 220px;
  }
  
  .properties-panel {
    width: 280px;
  }
}

@media (max-width: 1200px) {
  .component-panel {
    width: 200px;
  }
  
  .properties-panel {
    width: 260px;
  }
  
  .group-items {
    grid-template-columns: 1fr;
  }
  
  .component-item {
    padding: 12px 8px;
  }
}

@media (max-width: 992px) {
  .designer-header {
    height: auto;
    flex-wrap: wrap;
    padding: 16px;
  }
  
  .header-left {
    margin-bottom: 12px;
  }
  
  .header-right {
    flex-wrap: wrap;
    gap: 8px;
  }
}

@media (max-width: 768px) {
  .process-designer {
    height: 100vh;
  }
  
  .designer-header {
    flex-direction: column;
    height: auto;
    padding: 12px;
  }
  
  .header-left,
  .header-right {
    width: 100%;
    justify-content: center;
    margin: 6px 0;
  }
  
  .designer-title {
    font-size: 18px;
  }
  
  .component-panel {
    position: absolute;
    left: -260px;
    top: 0;
    height: 100%;
    z-index: 1000;
    transition: left 0.3s ease;
  }
  
  .component-panel.mobile-open {
    left: 0;
  }
  
  .properties-panel {
    position: absolute;
    right: -320px;
    top: 0;
    height: 100%;
    z-index: 1000;
    transition: right 0.3s ease;
  }
  
  .properties-panel.mobile-open {
    right: 0;
  }
  
  .canvas-container {
    border-radius: 0;
  }
  
  .design-canvas {
    border-radius: 0;
    margin: 0;
  }
}

@media (max-width: 480px) {
  .designer-header {
    padding: 8px;
  }
  
  .header-right .el-button-group {
    flex-direction: column;
  }
  
  .canvas-toolbar {
    padding: 0 12px;
    flex-wrap: wrap;
    height: auto;
    min-height: 48px;
  }
  
  .canvas-toolbar .el-button {
    margin: 2px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.component-panel {
  animation: slideInLeft 0.6s ease-out;
}

.properties-panel {
  animation: slideInRight 0.6s ease-out;
}

.canvas-container {
  animation: fadeInUp 0.8s ease-out;
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .process-designer {
    background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
  }
  
  .designer-header {
    background: linear-gradient(135deg, #374151 0%, #1f2937 100%);
  }
  
  .component-panel,
  .properties-panel {
    background: rgba(31, 41, 55, 0.95);
  }
  
  .panel-header {
    background: linear-gradient(135deg, rgba(55, 65, 81, 0.8), rgba(31, 41, 55, 0.8));
    color: #f9fafb;
  }
  
  .designer-title {
    color: #f9fafb;
  }
  
  .group-title {
    color: #9ca3af;
  }
  
  .component-item {
    background: linear-gradient(135deg, #374151 0%, #4b5563 100%);
    border-color: rgba(75, 85, 99, 0.3);
  }
  
  .item-label {
    color: #d1d5db;
  }
  
  .design-canvas {
    background: #1f2937;
  }
}
</style>