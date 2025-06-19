import { computed, nextTick } from 'vue'
import { useProcessDesignerStore } from '@/stores/processDesigner.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { nodeConfig } from '@/components/process-designer/index.js'

/**
 * 流程设计器组合式函数
 * 提供高级业务逻辑和操作封装
 */
export function useProcessDesigner() {
  const store = useProcessDesignerStore()
  
  // 节点操作增强
  const createNodeFromDrop = async (nodeType, x, y) => {
    try {
      const config = nodeConfig[nodeType]
      if (!config) {
        throw new Error(`未知的节点类型: ${nodeType}`)
      }
      
      const node = store.addNode({
        type: nodeType,
        x,
        y,
        label: config.defaultLabel,
        props: { ...config.defaultProps }
      })
      
      // 自动选中新创建的节点
      await nextTick()
      store.selectNode(node.id)
      
      return node
    } catch (error) {
      ElMessage.error(`创建节点失败: ${error.message}`)
      return null
    }
  }
  
  const duplicateNode = async (nodeId) => {
    try {
      const originalNode = store.processNodes.find(node => node.id === nodeId)
      if (!originalNode) {
        throw new Error('节点不存在')
      }
      
      const newNode = store.addNode({
        ...originalNode,
        x: originalNode.x + 50,
        y: originalNode.y + 50,
        label: `${originalNode.label} (副本)`
      })
      
      await nextTick()
      store.selectNode(newNode.id)
      
      ElMessage.success('节点复制成功')
      return newNode
    } catch (error) {
      ElMessage.error(`复制节点失败: ${error.message}`)
      return null
    }
  }
  
  const deleteSelectedNode = async () => {
    if (!store.selectedNodeId) {
      ElMessage.warning('请先选择要删除的节点')
      return false
    }
    
    try {
      await ElMessageBox.confirm(
        '确定要删除选中的节点吗？相关连接线也会被删除。',
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      store.deleteNode(store.selectedNodeId)
      ElMessage.success('节点删除成功')
      return true
    } catch {
      return false
    }
  }
  
  // 连接线操作增强
  const deleteSelectedConnection = async () => {
    if (!store.selectedConnectionId) {
      ElMessage.warning('请先选择要删除的连接线')
      return false
    }
    
    try {
      await ElMessageBox.confirm(
        '确定要删除选中的连接线吗？',
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      store.deleteConnection(store.selectedConnectionId)
      ElMessage.success('连接线删除成功')
      return true
    } catch {
      return false
    }
  }
  
  // 流程操作增强
  const saveProcess = async () => {
    try {
      const validation = store.validateProcess()
      if (!validation.isValid) {
        ElMessage.error(`保存失败: ${validation.errors.join(', ')}`)
        return false
      }
      
      // 这里可以添加实际的保存逻辑，比如调用API
      const processData = store.processData
      console.log('保存流程数据:', processData)
      
      ElMessage.success('流程保存成功')
      return true
    } catch (error) {
      ElMessage.error(`保存失败: ${error.message}`)
      return false
    }
  }
  
  const deployProcess = async () => {
    try {
      const validation = store.validateProcess()
      if (!validation.isValid) {
        ElMessage.error(`部署失败: ${validation.errors.join(', ')}`)
        return false
      }
      
      await ElMessageBox.confirm(
        '确定要部署此流程吗？部署后流程将生效。',
        '确认部署',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      // 这里可以添加实际的部署逻辑
      const processData = store.processData
      console.log('部署流程数据:', processData)
      
      ElMessage.success('流程部署成功')
      return true
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error(`部署失败: ${error.message}`)
      }
      return false
    }
  }
  
  const clearProcess = async () => {
    if (store.processNodes.length === 0) {
      ElMessage.info('画布已经是空的')
      return false
    }
    
    try {
      await ElMessageBox.confirm(
        '确定要清空画布吗？所有节点和连接线都会被删除。',
        '确认清空',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      store.clearProcess()
      ElMessage.success('画布已清空')
      return true
    } catch {
      return false
    }
  }
  
  const exportProcess = () => {
    try {
      const processData = store.processData
      const dataStr = JSON.stringify(processData, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      
      const link = document.createElement('a')
      link.href = URL.createObjectURL(dataBlob)
      link.download = `${processData.name || '流程'}.json`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      
      ElMessage.success('流程导出成功')
      return true
    } catch (error) {
      ElMessage.error(`导出失败: ${error.message}`)
      return false
    }
  }
  
  const importProcess = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      
      reader.onload = (e) => {
        try {
          const processData = JSON.parse(e.target.result)
          
          // 验证数据格式
          if (!processData.nodes || !Array.isArray(processData.nodes)) {
            throw new Error('无效的流程数据格式')
          }
          
          store.loadProcess(processData)
          ElMessage.success('流程导入成功')
          resolve(processData)
        } catch (error) {
          ElMessage.error(`导入失败: ${error.message}`)
          reject(error)
        }
      }
      
      reader.onerror = () => {
        const error = new Error('文件读取失败')
        ElMessage.error(error.message)
        reject(error)
      }
      
      reader.readAsText(file)
    })
  }
  
  // 键盘快捷键处理
  const handleKeyboardShortcuts = (event) => {
    // Ctrl+Z: 撤销
    if (event.ctrlKey && event.key === 'z' && !event.shiftKey) {
      event.preventDefault()
      if (store.canUndo) {
        store.undo()
        ElMessage.info('已撤销')
      }
    }
    
    // Ctrl+Shift+Z 或 Ctrl+Y: 重做
    if ((event.ctrlKey && event.shiftKey && event.key === 'Z') || 
        (event.ctrlKey && event.key === 'y')) {
      event.preventDefault()
      if (store.canRedo) {
        store.redo()
        ElMessage.info('已重做')
      }
    }
    
    // Delete: 删除选中项
    if (event.key === 'Delete') {
      event.preventDefault()
      if (store.selectedNodeId) {
        deleteSelectedNode()
      } else if (store.selectedConnectionId) {
        deleteSelectedConnection()
      }
    }
    
    // Escape: 取消连接或清除选择
    if (event.key === 'Escape') {
      event.preventDefault()
      if (store.isConnecting) {
        store.cancelConnection()
      } else {
        store.clearSelection()
      }
    }
    
    // Ctrl+S: 保存
    if (event.ctrlKey && event.key === 's') {
      event.preventDefault()
      saveProcess()
    }
    
    // Ctrl+D: 复制节点
    if (event.ctrlKey && event.key === 'd' && store.selectedNodeId) {
      event.preventDefault()
      duplicateNode(store.selectedNodeId)
    }
  }
  
  // 画布工具函数
  const getNodeAtPosition = (x, y) => {
    return store.processNodes.find(node => {
      const nodeWidth = 120
      const nodeHeight = 80
      return x >= node.x && x <= node.x + nodeWidth &&
             y >= node.y && y <= node.y + nodeHeight
    })
  }
  
  const getConnectionAtPosition = (x, y, tolerance = 10) => {
    return store.processConnections.find(conn => {
      const sourceNode = store.processNodes.find(n => n.id === conn.sourceId)
      const targetNode = store.processNodes.find(n => n.id === conn.targetId)
      
      if (!sourceNode || !targetNode) return false
      
      // 简化的线段距离检测
      const distance = distanceToLine(
        x, y,
        sourceNode.x + 60, sourceNode.y + 40,
        targetNode.x + 60, targetNode.y + 40
      )
      
      return distance <= tolerance
    })
  }
  
  const distanceToLine = (px, py, x1, y1, x2, y2) => {
    const A = px - x1
    const B = py - y1
    const C = x2 - x1
    const D = y2 - y1
    
    const dot = A * C + B * D
    const lenSq = C * C + D * D
    
    if (lenSq === 0) return Math.sqrt(A * A + B * B)
    
    let param = dot / lenSq
    
    if (param < 0) {
      return Math.sqrt(A * A + B * B)
    } else if (param > 1) {
      const dx = px - x2
      const dy = py - y2
      return Math.sqrt(dx * dx + dy * dy)
    } else {
      const dx = px - (x1 + param * C)
      const dy = py - (y1 + param * D)
      return Math.sqrt(dx * dx + dy * dy)
    }
  }
  
  // 统计信息
  const statistics = computed(() => {
    const nodeTypes = {}
    store.processNodes.forEach(node => {
      nodeTypes[node.type] = (nodeTypes[node.type] || 0) + 1
    })
    
    return {
      totalNodes: store.processNodes.length,
      totalConnections: store.processConnections.length,
      nodeTypes,
      hasStartNode: store.processNodes.some(node => node.type === 'start'),
      hasEndNode: store.processNodes.some(node => node.type === 'end'),
      isValid: store.validateProcess().isValid
    }
  })
  
  return {
    // Store 状态和方法
    ...store,
    
    // 增强的操作方法
    createNodeFromDrop,
    duplicateNode,
    deleteSelectedNode,
    deleteSelectedConnection,
    saveProcess,
    deployProcess,
    clearProcess,
    exportProcess,
    importProcess,
    handleKeyboardShortcuts,
    getNodeAtPosition,
    getConnectionAtPosition,
    
    // 计算属性
    statistics
  }
}