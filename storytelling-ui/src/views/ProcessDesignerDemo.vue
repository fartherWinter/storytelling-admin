<template>
  <div class="process-designer-demo">
    <!-- 顶部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <h2>📋 流程设计器</h2>
        <span class="subtitle">简单易用，拖拽即可完成流程设计</span>
      </div>
      <div class="header-actions">
        <el-button-group>
          <el-button type="primary" icon="el-icon-check" @click="saveProcess">保存</el-button>
          <el-button icon="el-icon-view" @click="previewProcess">预览</el-button>
          <el-button icon="el-icon-download" @click="exportProcess">导出</el-button>
        </el-button-group>
      </div>
    </div>

    <div class="designer-content">
      <!-- 左侧节点面板 -->
      <div class="designer-sidebar">
        <!-- 快速开始 -->
        <div class="quick-start">
          <h4>🚀 快速开始</h4>
          <div class="quick-actions">
            <el-button size="small" type="success" @click="createSimpleFlow">创建简单审批</el-button>
            <el-button size="small" type="warning" @click="createComplexFlow">创建复杂流程</el-button>
          </div>
        </div>

        <!-- 基础节点 -->
        <div class="node-category">
          <h4>📌 基础节点</h4>
          <div class="node-list">
            <div class="node-item start" draggable="true" @dragstart="onDragStart($event, 'start')">
              <div class="node-icon">🎯</div>
              <div class="node-info">
                <span class="node-name">开始节点</span>
                <span class="node-desc">流程开始</span>
              </div>
            </div>
            <div class="node-item task" draggable="true" @dragstart="onDragStart($event, 'task')">
              <div class="node-icon">📝</div>
              <div class="node-info">
                <span class="node-name">审批节点</span>
                <span class="node-desc">人工审批</span>
              </div>
            </div>
            <div class="node-item condition" draggable="true" @dragstart="onDragStart($event, 'condition')">
              <div class="node-icon">🔀</div>
              <div class="node-info">
                <span class="node-name">条件分支</span>
                <span class="node-desc">条件判断</span>
              </div>
            </div>
            <div class="node-item end" draggable="true" @dragstart="onDragStart($event, 'end')">
              <div class="node-icon">🏁</div>
              <div class="node-info">
                <span class="node-name">结束节点</span>
                <span class="node-desc">流程结束</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 高级节点 -->
        <div class="node-category">
          <h4>⚡ 高级节点</h4>
          <div class="node-list">
            <div class="node-item parallel" draggable="true" @dragstart="onDragStart($event, 'parallel')">
              <div class="node-icon">🔄</div>
              <div class="node-info">
                <span class="node-name">并行网关</span>
                <span class="node-desc">并行处理</span>
              </div>
            </div>
            <div class="node-item timer" draggable="true" @dragstart="onDragStart($event, 'timer')">
              <div class="node-icon">⏰</div>
              <div class="node-info">
                <span class="node-name">定时器</span>
                <span class="node-desc">定时触发</span>
              </div>
            </div>
            <div class="node-item script" draggable="true" @dragstart="onDragStart($event, 'script')">
              <div class="node-icon">💻</div>
              <div class="node-info">
                <span class="node-name">脚本任务</span>
                <span class="node-desc">自动执行</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作提示 -->
        <div class="operation-tips">
          <h4>💡 操作提示</h4>
          <div class="tips-content">
            <p>• 拖拽节点到画布创建流程</p>
            <p>• 点击节点右侧圆点开始连线</p>
            <p>• 点击节点可编辑属性</p>
            <p>• 右键节点可删除</p>
          </div>
        </div>
      </div>
      <!-- 中央画布区域 -->
      <div class="designer-canvas-container">
        <!-- 画布工具栏 -->
        <div class="canvas-toolbar">
          <div class="canvas-tools">
            <el-button-group size="small">
              <el-button icon="el-icon-zoom-in" @click="zoomIn">放大</el-button>
              <el-button icon="el-icon-zoom-out" @click="zoomOut">缩小</el-button>
              <el-button icon="el-icon-refresh" @click="resetZoom">重置</el-button>
            </el-button-group>
            <el-divider direction="vertical"></el-divider>
            <el-button-group size="small">
              <el-button icon="el-icon-delete" type="danger" @click="clearCanvas">清空</el-button>
              <el-button icon="el-icon-magic-stick" @click="autoLayout">自动布局</el-button>
            </el-button-group>
          </div>
          <div class="canvas-info">
            <span class="zoom-info">缩放: {{ Math.round(zoomLevel * 100) }}%</span>
            <span class="node-count">节点: {{ nodes.length }}</span>
          </div>
        </div>

        <!-- 画布主体 -->
        <div class="designer-canvas"
             ref="canvas"
             @drop="onDrop"
             @dragover="onDragOver"
             @click="selectNode(null)"
             :style="{ transform: `scale(${zoomLevel})` }">

          <!-- 网格背景 -->
          <div class="canvas-grid"></div>

          <!-- 连线层 -->
          <div class="connections-layer" style="pointer-events: none;">
            <svg class="connection-svg" width="100%" height="100%" style="pointer-events: none;">
              <defs>
                <marker id="arrowhead" markerWidth="10" markerHeight="7"
                        refX="9" refY="3.5" orient="auto">
                  <polygon points="0 0, 10 3.5, 0 7" fill="#409EFF"/>
                </marker>
                <marker id="arrowhead-selected" markerWidth="10" markerHeight="7"
                        refX="9" refY="3.5" orient="auto">
                  <polygon points="0 0, 10 3.5, 0 7" fill="#E6A23C"/>
                </marker>
              </defs>
              <path
                  v-for="connection in connections"
                  :key="connection.id"
                  :d="getConnectionPath(connection)"
                  :stroke="selectedConnection?.id === connection.id ? '#E6A23C' : '#409EFF'"
                  :stroke-width="selectedConnection?.id === connection.id ? '3' : '2'"
                  fill="none"
                  :marker-end="selectedConnection?.id === connection.id ? 'url(#arrowhead-selected)' : 'url(#arrowhead)'"
                  class="connection-line"
                  @click.stop="selectConnection(connection)"
              />
            </svg>
          </div>

          <!-- 节点 -->
          <div
              v-for="node in nodes"
              :key="node.id"
              :class="['process-node', node.type, { selected: selectedNode?.id === node.id, connecting: isConnecting }]"
              :style="{ left: node.x + 'px', top: node.y + 'px' }"
          >
            <!-- 连接点 -->
            <div class="connection-points">
              <div
                  class="connection-point input"
                  @click.stop="endConnection(node, 'input')"
                  @mouseenter="highlightConnectionPoint(node, 'input')"
                  @mouseleave="unhighlightConnectionPoint()"
              >
                <div class="connection-dot"></div>
              </div>
              <div
                  class="connection-point output"
                  @click.stop="startConnectionFromPoint(node, 'output', $event)"
                  @mouseenter="highlightConnectionPoint(node, 'output')"
                  @mouseleave="unhighlightConnectionPoint()"
              >
                <div class="connection-dot"></div>
              </div>
            </div>

            <!-- 节点主体 -->
            <div class="node-body" @click="selectNode(node)" @mousedown="startNodeDrag(node, $event)" @click.stop>
              <!-- 节点头部 -->
              <div class="node-header">
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji(node.type) }}</span>
                </div>
                <div class="node-title">{{ node.name }}</div>
                <div class="node-actions">
                  <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-close"
                      @click.stop="deleteNode(node)"
                      class="delete-btn"
                  ></el-button>
                </div>
              </div>

              <!-- 节点内容 -->
              <div class="node-content" v-if="getNodeDescription(node)">
                <div class="node-description">{{ getNodeDescription(node) }}</div>
              </div>

              <!-- 节点状态 -->
              <div class="node-status" v-if="node.status">
                <el-tag :type="getStatusType(node.status)" size="mini">{{ node.status }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 右侧属性面板 -->
      <div class="designer-properties">
        <!-- 节点属性 -->
        <div v-if="selectedNode" class="property-panel">
          <div class="panel-header">
            <h3>🔧 节点配置</h3>
            <el-tag :type="getNodeTypeColor(selectedNode.type)">{{ getNodeTypeName(selectedNode.type) }}</el-tag>
          </div>

          <!-- 基础信息 -->
          <el-card class="property-card" shadow="never">
            <template #header>
              <span class="card-title">📝 基础信息</span>
            </template>

            <el-form :model="selectedNode" label-width="80px" size="small">
              <el-form-item label="节点名称">
                <el-input
                    v-model="selectedNode.name"
                    placeholder="请输入节点名称"
                    clearable
                ></el-input>
              </el-form-item>

              <el-form-item label="节点描述">
                <el-input
                    v-model="selectedNode.description"
                    type="textarea"
                    placeholder="请输入节点描述"
                    :rows="2"
                    maxlength="200"
                    show-word-limit
                ></el-input>
              </el-form-item>

              <el-form-item label="节点类型">
                <el-select v-model="selectedNode.type" disabled>
                  <el-option label="开始节点" value="start"></el-option>
                  <el-option label="任务节点" value="task"></el-option>
                  <el-option label="网关节点" value="gateway"></el-option>
                  <el-option label="结束节点" value="end"></el-option>
                  <el-option label="条件分支" value="condition"></el-option>
                  <el-option label="并行网关" value="parallel"></el-option>
                  <el-option label="定时器" value="timer"></el-option>
                  <el-option label="脚本任务" value="script"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 任务节点配置 -->
          <template v-if="selectedNode.type === 'task'">
            <el-form-item label="执行人类型">
              <el-radio-group v-model="selectedNode.assigneeType">
                <el-radio label="user">指定用户</el-radio>
                <el-radio label="role">角色</el-radio>
                <el-radio label="group">用户组</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="执行人">
              <el-input v-model="selectedNode.assignee" placeholder="请输入执行人"></el-input>
            </el-form-item>
            <el-form-item label="表单">
              <el-select v-model="selectedNode.formKey" placeholder="请选择表单">
                <el-option label="默认表单" value="default"></el-option>
                <el-option label="审批表单" value="approval"></el-option>
                <el-option label="申请表单" value="application"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="权限控制">
              <el-checkbox-group v-model="selectedNode.permissions">
                <el-checkbox label="read">只读</el-checkbox>
                <el-checkbox label="write">编辑</el-checkbox>
                <el-checkbox label="approve">审批</el-checkbox>
                <el-checkbox label="reject">拒绝</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="超时设置">
              <el-input-number v-model="selectedNode.timeout" :min="1" :max="999" placeholder="小时"></el-input-number>
              <span style="margin-left: 8px;">小时</span>
            </el-form-item>
          </template>
          <el-form>
            <!-- 条件分支配置 -->
            <template v-if="selectedNode.type === 'condition'">
              <el-form-item label="条件表达式">
                <el-input
                    v-model="selectedNode.conditionExpression"
                    type="textarea"
                    :rows="3"
                    placeholder="例如: ${amount > 1000}"
                ></el-input>
              </el-form-item>
              <el-form-item label="条件说明">
                <el-input v-model="selectedNode.conditionDescription" placeholder="条件说明"></el-input>
              </el-form-item>
            </template>

            <!-- 定时器配置 -->
            <template v-if="selectedNode.type === 'timer'">
              <el-form-item label="定时类型">
                <el-radio-group v-model="selectedNode.timerType">
                  <el-radio label="duration">持续时间</el-radio>
                  <el-radio label="date">指定时间</el-radio>
                  <el-radio label="cycle">循环</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="selectedNode.timerType === 'duration'" label="持续时间">
                <el-input v-model="selectedNode.timerDuration" placeholder="例如: PT1H (1小时)"></el-input>
              </el-form-item>
              <el-form-item v-if="selectedNode.timerType === 'date'" label="指定时间">
                <el-date-picker v-model="selectedNode.timerDate" type="datetime"></el-date-picker>
              </el-form-item>
              <el-form-item v-if="selectedNode.timerType === 'cycle'" label="循环表达式">
                <el-input v-model="selectedNode.timerCycle" placeholder="例如: R3/PT10M (重复3次，每10分钟)"></el-input>
              </el-form-item>
            </template>

            <!-- 脚本任务配置 -->
            <template v-if="selectedNode.type === 'script'">
              <el-form-item label="脚本语言">
                <el-select v-model="selectedNode.scriptFormat">
                  <el-option label="JavaScript" value="javascript"></el-option>
                  <el-option label="Groovy" value="groovy"></el-option>
                  <el-option label="Python" value="python"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="脚本内容">
                <el-input
                    v-model="selectedNode.scriptContent"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入脚本内容"
                ></el-input>
              </el-form-item>
            </template>

            <!-- 通用配置 -->
            <el-form-item label="描述">
              <el-input
                  v-model="selectedNode.description"
                  type="textarea"
                  :rows="2"
                  placeholder="节点描述"
              ></el-input>
            </el-form-item>
          </el-form>

          <!-- 连接管理 -->
          <div class="connection-management" v-if="selectedNode">
            <h4>连接管理</h4>
            <el-button size="small" @click="deleteNode(selectedNode)" type="danger">删除节点</el-button>
          </div>
        </div>

        <div v-else-if="selectedConnection">
          <h4>连接属性</h4>
          <el-form :model="selectedConnection" label-width="100px" size="small">
            <el-form-item label="连接名称">
              <el-input v-model="selectedConnection.name"></el-input>
            </el-form-item>
            <el-form-item label="条件表达式">
              <el-input
                  v-model="selectedConnection.conditionExpression"
                  type="textarea"
                  :rows="2"
                  placeholder="例如: ${approved == true}"
              ></el-input>
            </el-form-item>
            <el-form-item label="优先级">
              <el-input-number v-model="selectedConnection.priority" :min="1" :max="100"></el-input-number>
            </el-form-item>
            <el-button size="small" @click="deleteConnection(selectedConnection)" type="danger">删除连接</el-button>
          </el-form>
        </div>

        <div v-else>
          <p>请选择一个节点或连接来编辑属性</p>
        </div>
      </div>
    </div>


    

  </div>
</template>

<script>
import {ref, reactive} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'

export default {
  name: 'ProcessDesignerDemo',
  setup() {
    const canvas = ref(null)
    const nodes = ref([])
    const connections = ref([])
    const selectedNode = ref(null)
    const selectedConnection = ref(null)
    let nodeIdCounter = 1
    let connectionIdCounter = 1
    let isConnecting = ref(false)
    let connectionStart = ref(null)
    const zoomLevel = ref(1)
    
    // 拖拽相关状态
    const isDragging = ref(false)
    const dragNode = ref(null)
    const dragOffset = ref({ x: 0, y: 0 })

    const onDragStart = (event, nodeType) => {
      console.log('Drag start:', nodeType)
      event.dataTransfer.setData('nodeType', nodeType)
      event.dataTransfer.effectAllowed = 'copy'
    }

    const onDragOver = (event) => {
      event.preventDefault()
      event.stopPropagation()
      // 允许拖拽放置
      event.dataTransfer.dropEffect = 'copy'
    }

    const onDrop = (event) => {
      event.preventDefault()
      event.stopPropagation()
      const nodeType = event.dataTransfer.getData('nodeType')

      console.log('Drop event triggered:', {nodeType, canvas: canvas.value})

      if (!canvas.value) {
        ElMessage.error('画布未初始化')
        return
      }

      if (!nodeType) {
        ElMessage.error('未获取到节点类型')
        return
      }

      const rect = canvas.value.getBoundingClientRect()
      const x = event.clientX - rect.left
      const y = event.clientY - rect.top

      console.log('Drop position:', {x, y})

      const newNode = {
        id: `node_${nodeIdCounter++}`,
        type: nodeType,
        name: getDefaultNodeName(nodeType),
        x: x - 50, // 居中
        y: y - 25,
        description: '',
        // 任务节点属性
        assignee: '',
        assigneeType: 'user',
        formKey: '',
        permissions: [],
        timeout: 24,
        // 条件分支属性
        conditionExpression: '',
        conditionDescription: '',
        // 定时器属性
        timerType: 'duration',
        timerDuration: '',
        timerDate: null,
        timerCycle: '',
        // 脚本任务属性
        scriptFormat: 'javascript',
        scriptContent: ''
      }

      nodes.value.push(newNode)
      console.log('Node created:', newNode)
      ElMessage.success(`已创建${getDefaultNodeName(nodeType)}`)
    }

    const getDefaultNodeName = (type) => {
      const names = {
        start: '开始',
        task: '任务节点',
        gateway: '网关',
        end: '结束',
        condition: '条件分支',
        parallel: '并行网关',
        timer: '定时器',
        script: '脚本任务'
      }
      return names[type] || '未知节点'
    }

    const getNodeIcon = (type) => {
      const icons = {
        start: 'el-icon-video-play',
        task: 'el-icon-document',
        gateway: 'el-icon-share',
        end: 'el-icon-video-pause',
        condition: 'el-icon-s-operation',
        parallel: 'el-icon-copy-document',
        timer: 'el-icon-timer',
        script: 'el-icon-cpu'
      }
      return icons[type] || 'el-icon-question'
    }

    const selectNode = (node) => {
      // 如果正在拖拽，不触发选择
      if (isDragging.value) return
      
      console.log('Selecting node:', node)
      selectedNode.value = node
      selectedConnection.value = null
    }

    // 节点拖拽功能
    const startNodeDrag = (node, event) => {
      event.stopPropagation()
      
      // 先选择节点
      selectNode(node)
      
      // 设置拖拽相关变量
      dragNode.value = node
      
      const rect = canvas.value.getBoundingClientRect()
      dragOffset.value = {
        x: event.clientX - rect.left - node.x,
        y: event.clientY - rect.top - node.y
      }
      
      // 添加鼠标移动监听，但延迟设置拖拽状态
      const handleMouseMove = (e) => {
        if (!isDragging.value) {
          // 检查是否真的在拖拽（移动距离超过阈值）
          const deltaX = Math.abs(e.clientX - event.clientX)
          const deltaY = Math.abs(e.clientY - event.clientY)
          if (deltaX > 5 || deltaY > 5) {
            isDragging.value = true
          }
        }
        if (isDragging.value) {
          onNodeDrag(e)
        }
      }
      
      const handleMouseUp = () => {
        document.removeEventListener('mousemove', handleMouseMove)
        document.removeEventListener('mouseup', handleMouseUp)
        if (isDragging.value) {
          endNodeDrag()
        }
      }
      
      document.addEventListener('mousemove', handleMouseMove)
      document.addEventListener('mouseup', handleMouseUp)
    }

    const onNodeDrag = (event) => {
      if (!isDragging.value || !dragNode.value || !canvas.value) return
      
      const rect = canvas.value.getBoundingClientRect()
      const newX = (event.clientX - rect.left - dragOffset.value.x) / zoomLevel.value
      const newY = (event.clientY - rect.top - dragOffset.value.y) / zoomLevel.value
      
      // 限制节点在画布范围内
      dragNode.value.x = Math.max(0, Math.min(newX, rect.width / zoomLevel.value - 100))
      dragNode.value.y = Math.max(0, Math.min(newY, rect.height / zoomLevel.value - 50))
      
      // 更新相关连线
      updateConnectionsForNode(dragNode.value)
    }

    const endNodeDrag = () => {
      isDragging.value = false
      dragNode.value = null
      document.removeEventListener('mousemove', onNodeDrag)
      document.removeEventListener('mouseup', endNodeDrag)
    }

    // 更新节点相关的连线位置
    const updateConnectionsForNode = (node) => {
      connections.value.forEach(connection => {
        if (connection.sourceId === node.id) {
          const outputPoint = getConnectionPoint(node, 'output')
          connection.startX = outputPoint.x
          connection.startY = outputPoint.y
        }
        if (connection.targetId === node.id) {
          const inputPoint = getConnectionPoint(node, 'input')
          connection.endX = inputPoint.x
          connection.endY = inputPoint.y
        }
      })
    }

    // 获取连接点的精确位置
    const getConnectionPoint = (node, type) => {
      const nodeWidth = 100
      const nodeHeight = 50
      
      if (type === 'output') {
        return {
          x: node.x + nodeWidth,
          y: node.y + nodeHeight / 2
        }
      } else { // input
        return {
          x: node.x,
          y: node.y + nodeHeight / 2
        }
      }
    }

    // 生成连线的贝塞尔曲线路径
    const getConnectionPath = (connection) => {
      const { startX, startY, endX, endY } = connection
      
      // 计算控制点，创建平滑的曲线
      const deltaX = endX - startX
      const deltaY = endY - startY
      
      // 控制点偏移量，根据距离调整
      const controlOffset = Math.min(Math.abs(deltaX) * 0.5, 100)
      
      const cp1x = startX + controlOffset
      const cp1y = startY
      const cp2x = endX - controlOffset
      const cp2y = endY
      
      // 使用三次贝塞尔曲线
      return `M ${startX} ${startY} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${endX} ${endY}`
    }

    const selectConnection = (connection) => {
      selectedConnection.value = connection
      selectedNode.value = null
    }

    const startConnection = (node, event) => {
      if (event.ctrlKey || event.metaKey) {
        event.stopPropagation()
        connectionStart.value = node
        isConnecting.value = true
        ElMessage.info('请点击目标节点的输入连接点完成连线')
      }
    }

    const startConnectionFromPoint = (node, type, event) => {
      event.stopPropagation()
      event.preventDefault()

      if (type === 'output') {
        connectionStart.value = node
        isConnecting.value = true
        ElMessage.info('请点击目标节点的输入连接点完成连线')
      }
    }

    const endConnection = (node, type) => {
      if (isConnecting.value && connectionStart.value && connectionStart.value.id !== node.id && type === 'input') {
        const startNode = connectionStart.value
        const endNode = node

        // 检查是否已存在连接
        const existingConnection = connections.value.find(
            conn => conn.sourceId === startNode.id && conn.targetId === endNode.id
        )

        if (existingConnection) {
          ElMessage.warning('节点间已存在连接')
          isConnecting.value = false
          connectionStart.value = null
          return
        }

        // 使用精确的连接点位置
        const startPoint = getConnectionPoint(startNode, 'output')
        const endPoint = getConnectionPoint(endNode, 'input')

        const newConnection = {
          id: `connection_${connectionIdCounter++}`,
          name: `${startNode.name} -> ${endNode.name}`,
          sourceId: startNode.id,
          targetId: endNode.id,
          startX: startPoint.x,
          startY: startPoint.y,
          endX: endPoint.x,
          endY: endPoint.y,
          conditionExpression: '',
          priority: 1
        }

        connections.value.push(newConnection)
        ElMessage.success('连接创建成功')
      }

      isConnecting.value = false
      connectionStart.value = null
    }

    const highlightConnectionPoint = (node, type) => {
      // 可以在这里添加连接点高亮逻辑
    }

    const unhighlightConnectionPoint = () => {
      // 可以在这里添加取消连接点高亮逻辑
    }

    // 缩放功能
    const zoomIn = () => {
      if (zoomLevel.value < 2) {
        zoomLevel.value += 0.1
      }
    }

    const zoomOut = () => {
      if (zoomLevel.value > 0.5) {
        zoomLevel.value -= 0.1
      }
    }

    const resetZoom = () => {
      zoomLevel.value = 1
    }

    // 画布操作
    const clearCanvas = () => {
      ElMessageBox.confirm('确定要清空画布吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        nodes.value = []
        connections.value = []
        selectedNode.value = null
        selectedConnection.value = null
        ElMessage.success('画布已清空')
      })
    }

    const autoLayout = () => {
      // 简单的自动布局算法
      const startNodes = nodes.value.filter(n => n.type === 'start')
      const endNodes = nodes.value.filter(n => n.type === 'end')
      const otherNodes = nodes.value.filter(n => n.type !== 'start' && n.type !== 'end')

      let y = 100
      const spacing = 150

      // 布局开始节点
      startNodes.forEach((node, index) => {
        node.x = 100
        node.y = y + index * spacing
      })

      // 布局中间节点
      otherNodes.forEach((node, index) => {
        node.x = 300 + (index % 3) * 200
        node.y = y + Math.floor(index / 3) * spacing
      })

      // 布局结束节点
      endNodes.forEach((node, index) => {
        node.x = 700
        node.y = y + index * spacing
      })

      ElMessage.success('自动布局完成')
    }

    // 快速创建流程
    const createSimpleFlow = () => {
      clearCanvas()

      // 创建简单审批流程
      const startNode = {
        id: Date.now() + '_start',
        type: 'start',
        name: '开始',
        x: 100,
        y: 200
      }

      const taskNode = {
        id: Date.now() + '_task',
        type: 'task',
        name: '审批',
        x: 300,
        y: 200,
        assignee: '审批人',
        description: '请审批此申请'
      }

      const endNode = {
        id: Date.now() + '_end',
        type: 'end',
        name: '结束',
        x: 500,
        y: 200
      }

      nodes.value = [startNode, taskNode, endNode]

      // 创建连线
      connections.value = [
        {
          id: Date.now() + '_conn1',
          sourceId: startNode.id,
          targetId: taskNode.id,
          startX: startNode.x + 100,
          startY: startNode.y + 25,
          endX: taskNode.x,
          endY: taskNode.y + 25
        },
        {
          id: Date.now() + '_conn2',
          sourceId: taskNode.id,
          targetId: endNode.id,
          startX: taskNode.x + 100,
          startY: taskNode.y + 25,
          endX: endNode.x,
          endY: endNode.y + 25
        }
      ]

      ElMessage.success('简单审批流程创建完成')
    }

    const createComplexFlow = () => {
      clearCanvas()

      // 创建复杂流程（包含条件分支）
      const startNode = {
        id: Date.now() + '_start',
        type: 'start',
        name: '开始',
        x: 100,
        y: 200
      }

      const conditionNode = {
        id: Date.now() + '_condition',
        type: 'condition',
        name: '条件判断',
        x: 300,
        y: 200,
        condition: '金额 > 1000'
      }

      const task1Node = {
        id: Date.now() + '_task1',
        type: 'task',
        name: '经理审批',
        x: 500,
        y: 150,
        assignee: '部门经理'
      }

      const task2Node = {
        id: Date.now() + '_task2',
        type: 'task',
        name: '主管审批',
        x: 500,
        y: 250,
        assignee: '部门主管'
      }

      const endNode = {
        id: Date.now() + '_end',
        type: 'end',
        name: '结束',
        x: 700,
        y: 200
      }

      nodes.value = [startNode, conditionNode, task1Node, task2Node, endNode]

      ElMessage.success('复杂流程创建完成')
    }

    // 节点信息获取
    const getNodeEmoji = (type) => {
      const emojiMap = {
        start: '🎯',
        task: '📝',
        condition: '🔀',
        parallel: '🔄',
        timer: '⏰',
        script: '💻',
        end: '🏁'
      }
      return emojiMap[type] || '📄'
    }

    const getNodeDescription = (node) => {
      if (node.assignee) return `执行人: ${node.assignee}`
      if (node.condition) return `条件: ${node.condition}`
      if (node.script) return `脚本: ${node.script}`
      return node.description || ''
    }

    const getStatusType = (status) => {
      const typeMap = {
        '待处理': 'warning',
        '进行中': 'primary',
        '已完成': 'success',
        '已拒绝': 'danger'
      }
      return typeMap[status] || 'info'
    }

    const getNodeTypeColor = (type) => {
      const colorMap = {
        start: 'success',
        task: 'primary',
        condition: 'warning',
        parallel: 'info',
        timer: 'warning',
        script: 'info',
        end: 'danger'
      }
      return colorMap[type] || 'info'
    }

    const getNodeTypeName = (type) => {
      const nameMap = {
        start: '开始节点',
        task: '任务节点',
        condition: '条件分支',
        parallel: '并行网关',
        timer: '定时器',
        script: '脚本任务',
        end: '结束节点'
      }
      return nameMap[type] || '未知节点'
    }

    const deleteNode = (node) => {
      // 删除节点
      const nodeIndex = nodes.value.findIndex(n => n.id === node.id)
      if (nodeIndex > -1) {
        nodes.value.splice(nodeIndex, 1)
      }

      // 删除相关连接
      connections.value = connections.value.filter(
          conn => conn.sourceId !== node.id && conn.targetId !== node.id
      )

      selectedNode.value = null
      ElMessage.success('节点删除成功')
    }

    const deleteConnection = (connection) => {
      const index = connections.value.findIndex(c => c.id === connection.id)
      if (index > -1) {
        connections.value.splice(index, 1)
        selectedConnection.value = null
        ElMessage.success('连接删除成功')
      }
    }

    const saveProcess = async () => {
      if (nodes.value.length === 0) {
        ElMessage.warning('请至少添加一个节点')
        return
      }

      // 验证流程完整性
      const hasStart = nodes.value.some(node => node.type === 'start')
      const hasEnd = nodes.value.some(node => node.type === 'end')

      if (!hasStart) {
        ElMessage.warning('流程必须包含开始节点')
        return
      }

      if (!hasEnd) {
        ElMessage.warning('流程必须包含结束节点')
        return
      }

      const processData = {
        id: `process_${Date.now()}`,
        name: '新建流程',
        description: '通过简化设计器创建的流程',
        version: '1.0',
        nodes: nodes.value,
        connections: connections.value,
        createTime: new Date().toISOString(),
        metadata: {
          nodeCount: nodes.value.length,
          connectionCount: connections.value.length,
          hasAdvancedFeatures: nodes.value.some(node =>
              ['condition', 'parallel', 'timer', 'script'].includes(node.type)
          )
        }
      }

      console.log('保存流程数据:', processData)
      ElMessage.success('流程保存成功')

      // 这里可以调用API保存到后端
      await saveWorkflowProcess(processData)
    }

    const previewProcess = () => {
      if (nodes.value.length === 0) {
        ElMessage.warning('请至少添加一个节点')
        return
      }

      ElMessage.info('预览功能开发中...')
    }

    const exportProcess = () => {
      if (nodes.value.length === 0) {
        ElMessage.warning('请至少添加一个节点')
        return
      }

      const processData = {
        id: `export_${Date.now()}`,
        name: '导出流程',
        description: '从流程设计器导出的流程定义',
        version: '1.0',
        nodes: nodes.value,
        connections: connections.value,
        exportTime: new Date().toISOString(),
        metadata: {
          nodeCount: nodes.value.length,
          connectionCount: connections.value.length,
          exportFormat: 'json',
          designerVersion: '2.0'
        }
      }

      const blob = new Blob([JSON.stringify(processData, null, 2)], {
        type: 'application/json'
      })

      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `process_${new Date().getTime()}.json`
      a.click()
      URL.revokeObjectURL(url)

      ElMessage.success('流程导出成功')
    }

    return {
      canvas,
      nodes,
      connections,
      selectedNode,
      selectedConnection,
      isConnecting,
      onDragStart,
      onDragOver,
      onDrop,
      getNodeIcon,
      selectNode,
      selectConnection,
      startConnection,
      startConnectionFromPoint,
      endConnection,
      deleteNode,
      deleteConnection,
      saveProcess,
      previewProcess,
      exportProcess,
      highlightConnectionPoint,
      unhighlightConnectionPoint,
      startNodeDrag,
      getConnectionPath,
      zoomLevel,
      zoomIn,
      zoomOut,
      resetZoom,
      clearCanvas,
      autoLayout,
      createSimpleFlow,
      createComplexFlow,
      getNodeEmoji,
      getNodeDescription,
      getStatusType,
      getNodeTypeColor,
      getNodeTypeName
    }
  }
}
</script>

<style scoped>

.process-designer-demo {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 顶部工具栏 */
.designer-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.designer-header h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 24px;
  font-weight: 600;
}

.subtitle {
  color: #7f8c8d;
  font-size: 14px;
  margin-top: 4px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.designer-content {
  flex: 1;
  display: flex;
  flex-direction: row;
  height: calc(100vh - 80px);
  gap: 0;
}

/* 左侧节点面板 */
.designer-sidebar {
  width: 280px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(0, 0, 0, 0.1);
  padding: 20px;
  overflow-y: auto;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

/* 快速开始 */
.quick-start {
  margin-bottom: 24px;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.quick-start h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.quick-actions .el-button {
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 8px;
  transition: all 0.3s;
}

.quick-actions .el-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* 节点分类 */
.node-category {
  margin-bottom: 24px;
}

.node-category h4 {
  margin: 0 0 16px 0;
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
  padding-bottom: 8px;
  border-bottom: 2px solid #ecf0f1;
}

.node-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 节点项 */
.node-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: white;
  border: 2px solid #ecf0f1;
  border-radius: 12px;
  cursor: grab;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.node-item:hover {
  border-color: #3498db;
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.2);
}

.node-item:active {
  cursor: grabbing;
  transform: translateY(-1px);
}

.node-icon {
  font-size: 24px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.node-info {
  flex: 1;
}

.node-name {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.node-desc {
  display: block;
  font-size: 12px;
  color: #7f8c8d;
}

/* 操作提示 */
.operation-tips {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #3498db;
}

.operation-tips h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 14px;
  font-weight: 600;
}

.tips-content p {
  margin: 8px 0;
  font-size: 12px;
  color: #7f8c8d;
  line-height: 1.5;
}

.tool-group h4 {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
}

.tool-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: grab;
  transition: all 0.3s;
}

.tool-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
}

.tool-item i {
  margin-right: 8px;
  color: #409eff;
}

/* 画布容器 */
.designer-canvas-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

/* 画布工具栏 */
.canvas-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: white;
  border-bottom: 1px solid #ecf0f1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.canvas-tools {
  display: flex;
  align-items: center;
  gap: 16px;
}

.canvas-info {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 14px;
  color: #7f8c8d;
}

.zoom-info, .node-count {
  padding: 4px 8px;
  background: #f8f9fa;
  border-radius: 6px;
  font-weight: 500;
}

/* 画布主体 */
.designer-canvas {
  flex: 1;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;
  transform-origin: center;
  transition: transform 0.3s ease;
  pointer-events: auto;
}

/* 网格背景 */
.canvas-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(circle, #bdc3c7 1px, transparent 1px);
  background-size: 20px 20px;
  pointer-events: none;
  opacity: 0.5;
}

.canvas-content {
  width: 100%;
  height: 100%;
  position: relative;
  background-image: radial-gradient(circle, #ddd 1px, transparent 1px);
  background-size: 20px 20px;
}

/* 流程节点 */
.process-node {
  position: absolute;
  min-width: 140px;
  background: white;
  border: 2px solid #ecf0f1;
  border-radius: 16px;
  cursor: move;
  transition: all 0.3s ease;
  z-index: 2;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.process-node:hover {
  border-color: #3498db;
  box-shadow: 0 8px 30px rgba(52, 152, 219, 0.3);
  transform: translateY(-4px);
}

.process-node.selected {
  border-color: #e74c3c;
  box-shadow: 0 8px 30px rgba(231, 76, 60, 0.3);
  transform: translateY(-4px);
}

.process-node.connecting {
  border-color: #27ae60;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(39, 174, 96, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(39, 174, 96, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(39, 174, 96, 0);
  }
}

/* 节点主体 */
.node-body {
  padding: 16px;
}

/* 节点头部 */
.node-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.node-header .node-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.icon-emoji {
  font-size: 16px;
}

.node-title {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.node-actions {
  opacity: 0;
  transition: opacity 0.3s;
}

.process-node:hover .node-actions {
  opacity: 1;
}

.delete-btn {
  color: #e74c3c !important;
  padding: 4px !important;
}

.delete-btn:hover {
  background: rgba(231, 76, 60, 0.1) !important;
}

/* 节点内容 */
.node-content {
  margin-bottom: 8px;
}

.node-description {
  font-size: 12px;
  color: #7f8c8d;
  line-height: 1.4;
}

/* 节点状态 */
.node-status {
  display: flex;
  justify-content: flex-end;
}

/* 节点类型样式 */
.process-node.start {
  border-color: #27ae60;
}

.process-node.start .node-header .node-icon {
  background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
}

.process-node.task {
  border-color: #3498db;
}

.process-node.task .node-header .node-icon {
  background: linear-gradient(135deg, #3498db 0%, #5dade2 100%);
}

.process-node.condition {
  border-color: #f39c12;
}

.process-node.condition .node-header .node-icon {
  background: linear-gradient(135deg, #f39c12 0%, #f7dc6f 100%);
}

.process-node.end {
  border-color: #e74c3c;
}

.process-node.end .node-header .node-icon {
  background: linear-gradient(135deg, #e74c3c 0%, #ec7063 100%);
}

.process-node.parallel {
  border-color: #9b59b6;
}

.process-node.parallel .node-header .node-icon {
  background: linear-gradient(135deg, #9b59b6 0%, #bb8fce 100%);
}

.process-node.timer {
  border-color: #e67e22;
}

.process-node.timer .node-header .node-icon {
  background: linear-gradient(135deg, #e67e22 0%, #f8c471 100%);
}

.process-node.script {
  border-color: #34495e;
}

.process-node.script .node-header .node-icon {
  background: linear-gradient(135deg, #34495e 0%, #85929e 100%);
}

.connection-points {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

/* 连接点 */
.connection-point {
  position: absolute;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  cursor: pointer;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 3;
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: auto;
}

.connection-dot {
  width: 8px;
  height: 8px;
  background: #3498db;
  border: 2px solid white;
  border-radius: 50%;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
}

.process-node:hover .connection-point,
.process-node.connecting .connection-point {
  opacity: 1;
}

.connection-point.input {
  left: -8px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point.output {
  right: -8px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point:hover .connection-dot {
  background: #27ae60;
  transform: scale(1.3);
  box-shadow: 0 4px 12px rgba(39, 174, 96, 0.4);
}

.process-node.connecting .connection-point .connection-dot {
  animation: connectionPulse 1.5s infinite;
}

@keyframes connectionPulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(52, 152, 219, 0.7);
  }
  70% {
    transform: scale(1.2);
    box-shadow: 0 0 0 8px rgba(52, 152, 219, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(52, 152, 219, 0);
  }
}

/* 连线层 */
.connections-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.connection-svg {
  width: 100%;
  height: 100%;
}

.connection-line {
  pointer-events: stroke;
  cursor: pointer;
  stroke-linecap: round;
  transition: all 0.2s ease;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.connection-line:hover {
  stroke-width: 3px !important;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

/* 右侧属性面板 */
.designer-properties {
  width: 320px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-left: 1px solid rgba(0, 0, 0, 0.1);
  padding: 20px;
  overflow-y: auto;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.1);
}

.property-panel {
  background: transparent;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid #ecf0f1;
}

.panel-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 18px;
  font-weight: 600;
}

.property-card {
  margin-bottom: 20px;
  border: 1px solid #ecf0f1;
  border-radius: 12px;
  overflow: hidden;
}

.property-card .el-card__header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 12px 16px;
  border-bottom: 1px solid #ecf0f1;
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
}

.property-card .el-card__body {
  padding: 16px;
}

.property-group {
  margin-bottom: 16px;
}

.property-group label {
  display: block;
  margin-bottom: 8px;
  color: #2c3e50;
  font-size: 14px;
  font-weight: 500;
}

/* 表单样式优化 */
.property-panel .el-form-item {
  margin-bottom: 16px;
}

.property-panel .el-form-item__label {
  color: #2c3e50;
  font-weight: 500;
}

.property-panel .el-input__inner,
.property-panel .el-textarea__inner,
.property-panel .el-select .el-input__inner {
  border-radius: 8px;
  border: 1px solid #ecf0f1;
  transition: all 0.3s;
}

.property-panel .el-input__inner:focus,
.property-panel .el-textarea__inner:focus {
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #7f8c8d;
  text-align: center;
}

.empty-state .empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state .empty-text {
  font-size: 16px;
  margin-bottom: 8px;
}

.empty-state .empty-desc {
  font-size: 14px;
  opacity: 0.7;
}

.designer-properties h4 {
  margin: 0 0 16px 0;
  color: #303133;
}

.connection-management {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.connection-management h4 {
  margin-bottom: 12px;
  font-size: 14px;
}

.el-radio-group .el-radio {
  display: block;
  margin-bottom: 8px;
}

.el-checkbox-group .el-checkbox {
  display: block;
  margin-bottom: 8px;
}

.connection-help {
  margin-bottom: 16px;
}

.connection-help .el-alert {
  border-radius: 6px;
}

.connection-help .el-alert__content p {
  margin: 4px 0;
  font-size: 12px;
  line-height: 1.4;
}

.process-node.connecting {
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
}
</style>