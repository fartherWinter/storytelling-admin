import { ref, reactive, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useProcessDesignerStore } from '@/stores/processDesigner'

/**
 * 统一的流程设计器组合式函数
 * 整合所有流程设计器的通用逻辑
 */
export function useProcessDesignerUnified() {
  const store = useProcessDesignerStore()
  
  // 响应式状态
  const state = reactive({
    // 画布状态
    canvasSize: { width: 2000, height: 1500 },
    viewBox: { x: 0, y: 0, width: 1200, height: 800 },
    
    // 拖拽状态
    isDragging: false,
    dragStartPos: { x: 0, y: 0 },
    dragOffset: { x: 0, y: 0 },
    dragThreshold: 5,
    
    // 连线状态
    isConnecting: false,
    connectionStart: null,
    tempConnection: null,
    
    // 选择状态
    selectedNodes: [],
    selectionBox: null,
    
    // 画布操作状态
    isPanning: false,
    panStart: { x: 0, y: 0 },
    
    // 网格设置
    gridSize: 20,
    showGrid: true,
    snapToGrid: true,
    
    // 缩放设置
    minZoom: 0.1,
    maxZoom: 3,
    zoomStep: 0.1
  })
  
  // 计算属性
  const canvasStyle = computed(() => ({
    width: `${state.canvasSize.width}px`,
    height: `${state.canvasSize.height}px`,
    transform: `scale(${store.zoom})`,
    transformOrigin: '0 0'
  }))
  
  const gridPattern = computed(() => {
    if (!state.showGrid) return null
    const size = state.gridSize * store.zoom
    return {
      width: size,
      height: size,
      x: (state.viewBox.x % state.gridSize) * store.zoom,
      y: (state.viewBox.y % state.gridSize) * store.zoom
    }
  })
  
  // 节点操作
  const nodeOperations = {
    // 创建节点
    createNode(type, position) {
      const nodeId = `${type}_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
      
      const nodeConfig = {
        start: { name: '开始', width: 80, height: 80, color: '#67C23A' },
        end: { name: '结束', width: 80, height: 80, color: '#F56C6C' },
        userTask: { name: '用户任务', width: 120, height: 80, color: '#409EFF' },
        serviceTask: { name: '服务任务', width: 120, height: 80, color: '#E6A23C' },
        scriptTask: { name: '脚本任务', width: 120, height: 80, color: '#909399' },
        exclusiveGateway: { name: '排他网关', width: 80, height: 80, color: '#E6A23C' },
        parallelGateway: { name: '并行网关', width: 80, height: 80, color: '#67C23A' },
        timerEvent: { name: '定时事件', width: 80, height: 80, color: '#409EFF' }
      }
      
      const config = nodeConfig[type] || nodeConfig.userTask
      
      const node = {
        id: nodeId,
        type,
        name: config.name,
        x: position.x,
        y: position.y,
        width: config.width,
        height: config.height,
        color: config.color,
        description: '',
        properties: {
          // 通用属性
          async: false,
          exclusive: true,
          retryTimeCycle: 0,
          documentation: '',
          
          // 用户任务属性
          ...(type === 'userTask' && {
            assignee: '',
            candidateUsers: '',
            candidateGroups: '',
            formKey: '',
            priority: 50,
            dueDate: ''
          }),
          
          // 服务任务属性
          ...(type === 'serviceTask' && {
            implementation: 'class',
            class: '',
            expression: '',
            delegateExpression: '',
            topic: ''
          }),
          
          // 脚本任务属性
          ...(type === 'scriptTask' && {
            scriptFormat: 'javascript',
            script: '',
            resultVariable: ''
          }),
          
          // 网关属性
          ...(['exclusiveGateway', 'parallelGateway'].includes(type) && {
            defaultFlow: '',
            gatewayDirection: 'diverging'
          }),
          
          // 定时事件属性
          ...(type === 'timerEvent' && {
            timerType: 'timeDate',
            timerDefinition: ''
          })
        }
      }
      
      store.addNode(node)
      store.addToHistory('添加节点', { type: 'add', target: 'node', data: node })
      
      return node
    },
    
    // 复制节点
    duplicateNode(node) {
      const newNode = {
        ...node,
        id: `${node.type}_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
        name: `${node.name}_副本`,
        x: node.x + 20,
        y: node.y + 20
      }
      
      store.addNode(newNode)
      store.addToHistory('复制节点', { type: 'add', target: 'node', data: newNode })
      
      return newNode
    },
    
    // 删除节点
    deleteNode(nodeId) {
      const node = store.nodes.find(n => n.id === nodeId)
      if (!node) return
      
      // 删除相关连线
      const relatedConnections = store.connections.filter(
        conn => conn.sourceId === nodeId || conn.targetId === nodeId
      )
      
      relatedConnections.forEach(conn => {
        store.removeConnection(conn.id)
      })
      
      store.removeNode(nodeId)
      store.addToHistory('删除节点', { 
        type: 'delete', 
        target: 'node', 
        data: { node, connections: relatedConnections }
      })
      
      // 清除选择
      if (store.selectedNode?.id === nodeId) {
        store.setSelectedNode(null)
      }
    },
    
    // 更新节点
    updateNode(nodeId, updates) {
      const oldNode = store.nodes.find(n => n.id === nodeId)
      if (!oldNode) return
      
      const newNode = { ...oldNode, ...updates }
      store.updateNode(nodeId, updates)
      
      store.addToHistory('更新节点', {
        type: 'update',
        target: 'node',
        data: { old: oldNode, new: newNode }
      })
    },
    
    // 选择节点
    selectNode(node, multiSelect = false) {
      if (state.isDragging) return
      
      if (multiSelect) {
        const index = state.selectedNodes.findIndex(n => n.id === node.id)
        if (index > -1) {
          state.selectedNodes.splice(index, 1)
        } else {
          state.selectedNodes.push(node)
        }
      } else {
        state.selectedNodes = [node]
        store.setSelectedNode(node)
        store.setSelectedConnection(null)
      }
    },
    
    // 移动节点
    moveNode(nodeId, position) {
      const node = store.nodes.find(n => n.id === nodeId)
      if (!node) return
      
      let { x, y } = position
      
      // 网格对齐
      if (state.snapToGrid) {
        x = Math.round(x / state.gridSize) * state.gridSize
        y = Math.round(y / state.gridSize) * state.gridSize
      }
      
      // 边界限制
      x = Math.max(0, Math.min(x, state.canvasSize.width - node.width))
      y = Math.max(0, Math.min(y, state.canvasSize.height - node.height))
      
      nodeOperations.updateNode(nodeId, { x, y })
    }
  }
  
  // 连线操作
  const connectionOperations = {
    // 开始连线
    startConnection(node, connectionPoint) {
      state.isConnecting = true
      state.connectionStart = {
        nodeId: node.id,
        point: connectionPoint,
        x: node.x + connectionPoint.x,
        y: node.y + connectionPoint.y
      }
    },
    
    // 完成连线
    finishConnection(targetNode, connectionPoint) {
      if (!state.connectionStart || !targetNode) {
        connectionOperations.cancelConnection()
        return
      }
      
      // 检查是否连接到自己
      if (state.connectionStart.nodeId === targetNode.id) {
        ElMessage.warning('不能连接到自己')
        connectionOperations.cancelConnection()
        return
      }
      
      // 检查是否已存在连线
      const existingConnection = store.connections.find(
        conn => conn.sourceId === state.connectionStart.nodeId && conn.targetId === targetNode.id
      )
      
      if (existingConnection) {
        ElMessage.warning('节点间已存在连线')
        connectionOperations.cancelConnection()
        return
      }
      
      // 创建连线
      const connectionId = `conn_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
      const connection = {
        id: connectionId,
        sourceId: state.connectionStart.nodeId,
        targetId: targetNode.id,
        sourcePoint: state.connectionStart.point,
        targetPoint: connectionPoint,
        name: '',
        conditionType: 'none',
        conditionExpression: '',
        conditionDescription: '',
        strokeColor: '#666666',
        strokeWidth: 2,
        strokeDasharray: '',
        markerEnd: 'url(#arrowhead)',
        priority: 0,
        documentation: '',
        executionListeners: []
      }
      
      store.addConnection(connection)
      store.addToHistory('添加连线', { type: 'add', target: 'connection', data: connection })
      
      connectionOperations.cancelConnection()
      ElMessage.success('连线创建成功')
    },
    
    // 取消连线
    cancelConnection() {
      state.isConnecting = false
      state.connectionStart = null
      state.tempConnection = null
    },
    
    // 删除连线
    deleteConnection(connectionId) {
      const connection = store.connections.find(c => c.id === connectionId)
      if (!connection) return
      
      store.removeConnection(connectionId)
      store.addToHistory('删除连线', { type: 'delete', target: 'connection', data: connection })
      
      // 清除选择
      if (store.selectedConnection?.id === connectionId) {
        store.setSelectedConnection(null)
      }
    },
    
    // 更新连线
    updateConnection(connectionId, updates) {
      const oldConnection = store.connections.find(c => c.id === connectionId)
      if (!oldConnection) return
      
      const newConnection = { ...oldConnection, ...updates }
      store.updateConnection(connectionId, updates)
      
      store.addToHistory('更新连线', {
        type: 'update',
        target: 'connection',
        data: { old: oldConnection, new: newConnection }
      })
    },
    
    // 选择连线
    selectConnection(connection) {
      store.setSelectedConnection(connection)
      store.setSelectedNode(null)
      state.selectedNodes = []
    }
  }
  
  // 画布操作
  const canvasOperations = {
    // 缩放
    zoomIn() {
      const newZoom = Math.min(store.zoom + state.zoomStep, state.maxZoom)
      store.setZoom(newZoom)
    },
    
    zoomOut() {
      const newZoom = Math.max(store.zoom - state.zoomStep, state.minZoom)
      store.setZoom(newZoom)
    },
    
    zoomToFit() {
      if (store.nodes.length === 0) {
        store.setZoom(1)
        return
      }
      
      // 计算所有节点的边界
      const bounds = store.nodes.reduce(
        (acc, node) => ({
          minX: Math.min(acc.minX, node.x),
          minY: Math.min(acc.minY, node.y),
          maxX: Math.max(acc.maxX, node.x + node.width),
          maxY: Math.max(acc.maxY, node.y + node.height)
        }),
        { minX: Infinity, minY: Infinity, maxX: -Infinity, maxY: -Infinity }
      )
      
      const contentWidth = bounds.maxX - bounds.minX
      const contentHeight = bounds.maxY - bounds.minY
      const padding = 50
      
      const scaleX = (state.viewBox.width - padding * 2) / contentWidth
      const scaleY = (state.viewBox.height - padding * 2) / contentHeight
      const scale = Math.min(scaleX, scaleY, state.maxZoom)
      
      store.setZoom(Math.max(scale, state.minZoom))
    },
    
    resetZoom() {
      store.setZoom(1)
    },
    
    // 清空画布
    async clearCanvas() {
      try {
        await ElMessageBox.confirm(
          '确定要清空画布吗？此操作不可撤销。',
          '确认清空',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        store.clearAll()
        ElMessage.success('画布已清空')
      } catch {
        // 用户取消
      }
    },
    
    // 自动布局
    autoLayout() {
      if (store.nodes.length === 0) return
      
      // 简单的层次布局算法
      const layers = []
      const visited = new Set()
      const nodeMap = new Map(store.nodes.map(node => [node.id, node]))
      
      // 找到开始节点
      const startNodes = store.nodes.filter(node => 
        node.type === 'start' || 
        !store.connections.some(conn => conn.targetId === node.id)
      )
      
      if (startNodes.length === 0) {
        ElMessage.warning('未找到开始节点，无法自动布局')
        return
      }
      
      // BFS 分层
      let currentLayer = [...startNodes]
      while (currentLayer.length > 0) {
        layers.push([...currentLayer])
        const nextLayer = []
        
        currentLayer.forEach(node => {
          visited.add(node.id)
          const outgoingConnections = store.connections.filter(conn => conn.sourceId === node.id)
          
          outgoingConnections.forEach(conn => {
            const targetNode = nodeMap.get(conn.targetId)
            if (targetNode && !visited.has(targetNode.id) && !nextLayer.includes(targetNode)) {
              nextLayer.push(targetNode)
            }
          })
        })
        
        currentLayer = nextLayer
      }
      
      // 布局节点
      const layerSpacing = 200
      const nodeSpacing = 150
      const startX = 100
      const startY = 100
      
      layers.forEach((layer, layerIndex) => {
        const layerY = startY + layerIndex * layerSpacing
        const totalWidth = (layer.length - 1) * nodeSpacing
        const startLayerX = startX - totalWidth / 2
        
        layer.forEach((node, nodeIndex) => {
          const x = startLayerX + nodeIndex * nodeSpacing
          nodeOperations.updateNode(node.id, { x, y: layerY })
        })
      })
      
      ElMessage.success('自动布局完成')
    }
  }
  
  // 历史操作
  const historyOperations = {
    undo() {
      store.undo()
    },
    
    redo() {
      store.redo()
    },
    
    canUndo: computed(() => store.canUndo),
    canRedo: computed(() => store.canRedo)
  }
  
  // 导入导出
  const importExportOperations = {
    // 导出为 JSON
    exportToJSON() {
      const data = {
        processName: store.processName,
        nodes: store.nodes,
        connections: store.connections,
        metadata: {
          version: '1.0.0',
          exportTime: new Date().toISOString(),
          canvasSize: state.canvasSize,
          zoom: store.zoom
        }
      }
      
      const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `${store.processName || '流程图'}.json`
      a.click()
      URL.revokeObjectURL(url)
      
      ElMessage.success('导出成功')
    },
    
    // 从 JSON 导入
    async importFromJSON(file) {
      try {
        const text = await file.text()
        const data = JSON.parse(text)
        
        if (!data.nodes || !Array.isArray(data.nodes)) {
          throw new Error('无效的文件格式')
        }
        
        // 清空当前数据
        store.clearAll()
        
        // 导入数据
        store.setProcessName(data.processName || '导入的流程')
        data.nodes.forEach(node => store.addNode(node))
        data.connections?.forEach(conn => store.addConnection(conn))
        
        if (data.metadata?.canvasSize) {
          Object.assign(state.canvasSize, data.metadata.canvasSize)
        }
        
        if (data.metadata?.zoom) {
          store.setZoom(data.metadata.zoom)
        }
        
        ElMessage.success('导入成功')
      } catch (error) {
        ElMessage.error(`导入失败: ${error.message}`)
      }
    },
    
    // 导出为 BPMN XML
    exportToBPMN() {
      // 这里可以实现 BPMN XML 导出逻辑
      ElMessage.info('BPMN 导出功能开发中...')
    },
    
    // 导出为图片
    async exportToImage(format = 'png') {
      try {
        // 这里可以使用 html2canvas 或类似库来实现
        ElMessage.info('图片导出功能开发中...')
      } catch (error) {
        ElMessage.error(`导出失败: ${error.message}`)
      }
    }
  }
  
  // 验证操作
  const validationOperations = {
    // 验证流程
    validateProcess() {
      const errors = []
      const warnings = []
      
      // 检查开始节点
      const startNodes = store.nodes.filter(node => node.type === 'start')
      if (startNodes.length === 0) {
        errors.push('流程必须包含至少一个开始节点')
      } else if (startNodes.length > 1) {
        warnings.push('流程包含多个开始节点')
      }
      
      // 检查结束节点
      const endNodes = store.nodes.filter(node => node.type === 'end')
      if (endNodes.length === 0) {
        warnings.push('流程建议包含至少一个结束节点')
      }
      
      // 检查孤立节点
      store.nodes.forEach(node => {
        const hasIncoming = store.connections.some(conn => conn.targetId === node.id)
        const hasOutgoing = store.connections.some(conn => conn.sourceId === node.id)
        
        if (!hasIncoming && node.type !== 'start') {
          warnings.push(`节点 "${node.name}" 没有输入连线`)
        }
        
        if (!hasOutgoing && node.type !== 'end') {
          warnings.push(`节点 "${node.name}" 没有输出连线`)
        }
      })
      
      // 检查网关
      store.nodes.filter(node => ['exclusiveGateway', 'parallelGateway'].includes(node.type))
        .forEach(gateway => {
          const outgoingConnections = store.connections.filter(conn => conn.sourceId === gateway.id)
          
          if (gateway.type === 'exclusiveGateway' && outgoingConnections.length > 1) {
            const hasDefault = outgoingConnections.some(conn => conn.conditionType === 'default')
            const hasConditions = outgoingConnections.some(conn => 
              conn.conditionType === 'expression' && conn.conditionExpression
            )
            
            if (!hasDefault && !hasConditions) {
              warnings.push(`排他网关 "${gateway.name}" 的输出连线缺少条件或默认流`)
            }
          }
        })
      
      return { errors, warnings }
    },
    
    // 显示验证结果
    showValidationResult() {
      const { errors, warnings } = validationOperations.validateProcess()
      
      if (errors.length === 0 && warnings.length === 0) {
        ElMessage.success('流程验证通过')
        return true
      }
      
      let message = ''
      if (errors.length > 0) {
        message += `错误 (${errors.length}):\n${errors.join('\n')}\n\n`
      }
      if (warnings.length > 0) {
        message += `警告 (${warnings.length}):\n${warnings.join('\n')}`
      }
      
      ElMessageBox.alert(message, '验证结果', {
        confirmButtonText: '确定',
        type: errors.length > 0 ? 'error' : 'warning'
      })
      
      return errors.length === 0
    }
  }
  
  // 工具函数
  const utils = {
    // 获取节点连接点
    getNodeConnectionPoints(node) {
      const points = [
        { id: 'top', x: node.width / 2, y: 0, direction: 'input' },
        { id: 'right', x: node.width, y: node.height / 2, direction: 'output' },
        { id: 'bottom', x: node.width / 2, y: node.height, direction: 'output' },
        { id: 'left', x: 0, y: node.height / 2, direction: 'input' }
      ]
      
      return points.map(point => ({
        ...point,
        absoluteX: node.x + point.x,
        absoluteY: node.y + point.y
      }))
    },
    
    // 计算贝塞尔曲线路径
    calculateBezierPath(sourcePoint, targetPoint) {
      const dx = targetPoint.x - sourcePoint.x
      const dy = targetPoint.y - sourcePoint.y
      
      const controlOffset = Math.max(Math.abs(dx) * 0.5, 50)
      
      const cp1x = sourcePoint.x + controlOffset
      const cp1y = sourcePoint.y
      const cp2x = targetPoint.x - controlOffset
      const cp2y = targetPoint.y
      
      return `M ${sourcePoint.x} ${sourcePoint.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${targetPoint.x} ${targetPoint.y}`
    },
    
    // 检查点是否在矩形内
    isPointInRect(point, rect) {
      return point.x >= rect.x && 
             point.x <= rect.x + rect.width && 
             point.y >= rect.y && 
             point.y <= rect.y + rect.height
    },
    
    // 获取鼠标在画布上的坐标
    getCanvasPosition(event, canvasElement) {
      const rect = canvasElement.getBoundingClientRect()
      return {
        x: (event.clientX - rect.left) / store.zoom,
        y: (event.clientY - rect.top) / store.zoom
      }
    }
  }
  
  return {
    // 状态
    state,
    store,
    
    // 计算属性
    canvasStyle,
    gridPattern,
    
    // 操作方法
    ...nodeOperations,
    ...connectionOperations,
    ...canvasOperations,
    ...historyOperations,
    ...importExportOperations,
    ...validationOperations,
    
    // 工具函数
    utils
  }
}

// 导出默认配置
export const defaultProcessConfig = {
  nodeTypes: [
    { type: 'start', name: '开始节点', icon: '▶️', category: 'events' },
    { type: 'end', name: '结束节点', icon: '⏹️', category: 'events' },
    { type: 'userTask', name: '用户任务', icon: '👤', category: 'tasks' },
    { type: 'serviceTask', name: '服务任务', icon: '⚙️', category: 'tasks' },
    { type: 'scriptTask', name: '脚本任务', icon: '📜', category: 'tasks' },
    { type: 'exclusiveGateway', name: '排他网关', icon: '◆', category: 'gateways' },
    { type: 'parallelGateway', name: '并行网关', icon: '✚', category: 'gateways' },
    { type: 'timerEvent', name: '定时事件', icon: '⏰', category: 'events' }
  ],
  
  categories: [
    { id: 'events', name: '事件', icon: '⚡' },
    { id: 'tasks', name: '任务', icon: '📋' },
    { id: 'gateways', name: '网关', icon: '🔀' }
  ]
}