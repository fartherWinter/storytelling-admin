import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { generateNodeId, generateConnectionId } from '@/components/process-designer/index.js'

/**
 * 流程设计器状态管理
 */
export const useProcessDesignerStore = defineStore('processDesigner', () => {
  // 基础状态
  const processName = ref('新建流程')
  const processCategory = ref('')
  const processDescription = ref('')
  const processNodes = ref([])
  const processConnections = ref([])
  const selectedNodeId = ref(null)
  const selectedConnectionId = ref(null)
  const canvasScale = ref(1)
  const showGrid = ref(true)
  const isConnecting = ref(false)
  const connectingFrom = ref(null)
  const tempConnection = ref(null)
  
  // 历史记录管理
  const history = ref([])
  const historyIndex = ref(-1)
  const maxHistorySize = ref(50)
  
  // 画布状态
  const canvasOffset = ref({ x: 0, y: 0 })
  const isDragging = ref(false)
  const dragStartPos = ref({ x: 0, y: 0 })
  
  // 计算属性
  const selectedNode = computed(() => {
    return processNodes.value.find(node => node.id === selectedNodeId.value) || null
  })
  
  const selectedConnection = computed(() => {
    return processConnections.value.find(conn => conn.id === selectedConnectionId.value) || null
  })
  
  const canUndo = computed(() => {
    return historyIndex.value > 0
  })
  
  const canRedo = computed(() => {
    return historyIndex.value < history.value.length - 1
  })
  
  const processData = computed(() => {
    return {
      name: processName.value,
      category: processCategory.value,
      description: processDescription.value,
      nodes: processNodes.value,
      connections: processConnections.value,
      createdAt: new Date().toISOString(),
      version: '1.0'
    }
  })
  
  // 节点操作
  function addNode(nodeData) {
    const node = {
      id: generateNodeId(),
      type: nodeData.type,
      x: nodeData.x || 100,
      y: nodeData.y || 100,
      label: nodeData.label || '',
      props: nodeData.props || {},
      ...nodeData
    }
    
    processNodes.value.push(node)
    saveToHistory()
    return node
  }
  
  function updateNode(nodeId, updates) {
    const nodeIndex = processNodes.value.findIndex(node => node.id === nodeId)
    if (nodeIndex !== -1) {
      processNodes.value[nodeIndex] = {
        ...processNodes.value[nodeIndex],
        ...updates
      }
      saveToHistory()
    }
  }
  
  function deleteNode(nodeId) {
    // 删除节点
    processNodes.value = processNodes.value.filter(node => node.id !== nodeId)
    
    // 删除相关连接线
    processConnections.value = processConnections.value.filter(
      conn => conn.sourceId !== nodeId && conn.targetId !== nodeId
    )
    
    // 清除选中状态
    if (selectedNodeId.value === nodeId) {
      selectedNodeId.value = null
    }
    
    saveToHistory()
  }
  
  function moveNode(nodeId, x, y) {
    const node = processNodes.value.find(node => node.id === nodeId)
    if (node) {
      node.x = x
      node.y = y
    }
  }
  
  // 连接线操作
  function addConnection(sourceId, targetId, label = '') {
    const connection = {
      id: generateConnectionId(),
      sourceId,
      targetId,
      label,
      props: {}
    }
    
    processConnections.value.push(connection)
    saveToHistory()
    return connection
  }
  
  function updateConnection(connectionId, updates) {
    const connIndex = processConnections.value.findIndex(conn => conn.id === connectionId)
    if (connIndex !== -1) {
      processConnections.value[connIndex] = {
        ...processConnections.value[connIndex],
        ...updates
      }
      saveToHistory()
    }
  }
  
  function deleteConnection(connectionId) {
    processConnections.value = processConnections.value.filter(
      conn => conn.id !== connectionId
    )
    
    if (selectedConnectionId.value === connectionId) {
      selectedConnectionId.value = null
    }
    
    saveToHistory()
  }
  
  // 选择操作
  function selectNode(nodeId) {
    selectedNodeId.value = nodeId
    selectedConnectionId.value = null
  }
  
  function selectConnection(connectionId) {
    selectedConnectionId.value = connectionId
    selectedNodeId.value = null
  }
  
  function clearSelection() {
    selectedNodeId.value = null
    selectedConnectionId.value = null
  }
  
  // 连接线绘制
  function startConnection(nodeId, connectionPoint) {
    isConnecting.value = true
    connectingFrom.value = { nodeId, connectionPoint }
  }
  
  function updateTempConnection(x, y) {
    if (isConnecting.value && connectingFrom.value) {
      tempConnection.value = {
        sourceId: connectingFrom.value.nodeId,
        targetX: x,
        targetY: y
      }
    }
  }
  
  function finishConnection(targetNodeId, targetConnectionPoint) {
    if (isConnecting.value && connectingFrom.value) {
      const sourceId = connectingFrom.value.nodeId
      
      // 检查是否已存在连接
      const existingConnection = processConnections.value.find(
        conn => conn.sourceId === sourceId && conn.targetId === targetNodeId
      )
      
      if (!existingConnection && sourceId !== targetNodeId) {
        addConnection(sourceId, targetNodeId)
      }
    }
    
    cancelConnection()
  }
  
  function cancelConnection() {
    isConnecting.value = false
    connectingFrom.value = null
    tempConnection.value = null
  }
  
  // 画布操作
  function setCanvasScale(scale) {
    canvasScale.value = Math.max(0.25, Math.min(2, scale))
  }
  
  function zoomIn() {
    setCanvasScale(canvasScale.value + 0.1)
  }
  
  function zoomOut() {
    setCanvasScale(canvasScale.value - 0.1)
  }
  
  function resetZoom() {
    setCanvasScale(1)
  }
  
  function toggleGrid() {
    showGrid.value = !showGrid.value
  }
  
  // 历史记录操作
  function saveToHistory() {
    const state = {
      nodes: JSON.parse(JSON.stringify(processNodes.value)),
      connections: JSON.parse(JSON.stringify(processConnections.value))
    }
    
    // 移除当前位置之后的历史记录
    history.value = history.value.slice(0, historyIndex.value + 1)
    
    // 添加新状态
    history.value.push(state)
    historyIndex.value = history.value.length - 1
    
    // 限制历史记录大小
    if (history.value.length > maxHistorySize.value) {
      history.value.shift()
      historyIndex.value--
    }
  }
  
  function undo() {
    if (canUndo.value) {
      historyIndex.value--
      const state = history.value[historyIndex.value]
      processNodes.value = JSON.parse(JSON.stringify(state.nodes))
      processConnections.value = JSON.parse(JSON.stringify(state.connections))
      clearSelection()
    }
  }
  
  function redo() {
    if (canRedo.value) {
      historyIndex.value++
      const state = history.value[historyIndex.value]
      processNodes.value = JSON.parse(JSON.stringify(state.nodes))
      processConnections.value = JSON.parse(JSON.stringify(state.connections))
      clearSelection()
    }
  }
  
  // 流程操作
  function clearProcess() {
    processNodes.value = []
    processConnections.value = []
    clearSelection()
    saveToHistory()
  }
  
  function loadProcess(data) {
    processName.value = data.name || '新建流程'
    processCategory.value = data.category || ''
    processDescription.value = data.description || ''
    processNodes.value = data.nodes || []
    processConnections.value = data.connections || []
    clearSelection()
    
    // 重置历史记录
    history.value = []
    historyIndex.value = -1
    saveToHistory()
  }
  
  function validateProcess() {
    const errors = []
    
    // 检查流程名称
    if (!processName.value.trim()) {
      errors.push('流程名称不能为空')
    }
    
    // 检查是否有节点
    if (processNodes.value.length === 0) {
      errors.push('流程至少需要一个节点')
    }
    
    // 检查开始节点
    const startNodes = processNodes.value.filter(node => node.type === 'start')
    if (startNodes.length === 0) {
      errors.push('流程必须有一个开始节点')
    } else if (startNodes.length > 1) {
      errors.push('流程只能有一个开始节点')
    }
    
    // 检查结束节点
    const endNodes = processNodes.value.filter(node => node.type === 'end')
    if (endNodes.length === 0) {
      errors.push('流程必须有至少一个结束节点')
    }
    
    return {
      isValid: errors.length === 0,
      errors
    }
  }
  
  // 初始化历史记录
  function initializeHistory() {
    history.value = []
    historyIndex.value = -1
    saveToHistory()
  }
  
  return {
    // 状态
    processName,
    processCategory,
    processDescription,
    processNodes,
    processConnections,
    selectedNodeId,
    selectedConnectionId,
    canvasScale,
    showGrid,
    isConnecting,
    connectingFrom,
    tempConnection,
    canvasOffset,
    isDragging,
    dragStartPos,
    
    // 计算属性
    selectedNode,
    selectedConnection,
    canUndo,
    canRedo,
    processData,
    
    // 节点操作
    addNode,
    updateNode,
    deleteNode,
    moveNode,
    
    // 连接线操作
    addConnection,
    updateConnection,
    deleteConnection,
    
    // 选择操作
    selectNode,
    selectConnection,
    clearSelection,
    
    // 连接线绘制
    startConnection,
    updateTempConnection,
    finishConnection,
    cancelConnection,
    
    // 画布操作
    setCanvasScale,
    zoomIn,
    zoomOut,
    resetZoom,
    toggleGrid,
    
    // 历史记录操作
    saveToHistory,
    undo,
    redo,
    
    // 流程操作
    clearProcess,
    loadProcess,
    validateProcess,
    initializeHistory
  }
})