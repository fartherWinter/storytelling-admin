<template>
  <div class="simplified-process-designer">
    <div class="designer-header">
      <div class="header-left">
        <h2>流程设计器</h2>
        <div class="subtitle">融合可视化设计和工作流自定义功能</div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="saveProcess">
          <el-icon>
            <DocumentAdd/>
          </el-icon>
          保存
        </el-button>
        <el-button @click="previewProcess">
          <el-icon>
            <View/>
          </el-icon>
          预览
        </el-button>
        <el-button @click="exportProcess">
          <el-icon>
            <Download/>
          </el-icon>
          导出
        </el-button>
      </div>
    </div>

    <div class="designer-content">
      <!-- 设计器主体 -->
      <div class="designer-main">
        <!-- 左侧节点面板 -->
        <div class="designer-sidebar">
          <!-- 快速开始 -->
          <div class="quick-start">
            <h4>🚀 快速开始</h4>
            <div class="quick-actions">
              <el-button @click="createSimpleProcess" size="small">创建简单流程</el-button>
              <el-button @click="createComplexProcess" size="small">创建复杂流程</el-button>
              <el-button @click="clearCanvas" size="small">清空画布</el-button>
              <el-button @click="autoLayout" size="small">自动布局</el-button>
            </div>
          </div>

          <!-- 基础节点 -->
          <div class="node-category">
            <h4>基础节点</h4>
            <div class="node-list">
              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'start')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('start') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">开始节点</span>
                  <span class="node-desc">{{ getNodeDescription('start') }}</span>
                </div>
              </div>

              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'task')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('task') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">任务节点</span>
                  <span class="node-desc">{{ getNodeDescription('task') }}</span>
                </div>
              </div>

              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'condition')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('condition') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">条件节点</span>
                  <span class="node-desc">{{ getNodeDescription('condition') }}</span>
                </div>
              </div>

              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'end')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('end') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">结束节点</span>
                  <span class="node-desc">{{ getNodeDescription('end') }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 高级节点 -->
          <div class="node-category">
            <h4>高级节点</h4>
            <div class="node-list">
              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'parallel')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('parallel') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">并行节点</span>
                  <span class="node-desc">{{ getNodeDescription('parallel') }}</span>
                </div>
              </div>

              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'timer')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('timer') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">定时器</span>
                  <span class="node-desc">{{ getNodeDescription('timer') }}</span>
                </div>
              </div>

              <div
                  class="node-item"
                  draggable="true"
                  @dragstart="onDragStart($event, 'script')"
              >
                <div class="node-icon">
                  <span class="icon-emoji">{{ getNodeEmoji('script') }}</span>
                </div>
                <div class="node-info">
                  <span class="node-name">脚本节点</span>
                  <span class="node-desc">{{ getNodeDescription('script') }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 操作提示 -->
          <div class="operation-tips">
            <h4>💡 操作提示</h4>
            <div class="tips-content">
              <p>• 拖拽节点到画布创建流程</p>
              <p>• 点击节点连接点创建连线</p>
              <p>• 双击节点编辑属性</p>
              <p>• 右键删除节点或连线</p>
              <p>• 使用滚轮缩放画布</p>
            </div>
          </div>
        </div>

        <!-- 画布容器 -->
        <div class="designer-canvas-container">
          <!-- 画布工具栏 -->
          <!-- 画布主体 -->
          <div class="canvas-main">
            <div
                ref="canvasRef"
                class="canvas"
                :style="{
                transform: `scale(${zoomLevel / 100})`,
                transformOrigin: 'top left'
              }"
                @click="handleCanvasClick"
                @dragover="onDragOver"
                @drop="onDrop"
            >
              <!-- 网格背景 -->
              <div class="grid-background"></div>

              <!-- 流程节点 -->
              <div
                  v-for="node in nodes"
                  :key="node.id"
                  class="flow-node"
                  :data-node-id="node.id"
                  :class="{
                  'selected': selectedNode?.id === node.id,
                  'dragging': draggingNode?.id === node.id,
                  [`node-${node.type}`]: true
                }"
                  :style="{
                  left: node.x + 'px',
                  top: node.y + 'px'
                }"
                  @click.stop="selectNode(node, $event)"
                  @mousedown="startNodeDrag($event, node)"
              >
                <div class="node-content">
                  <div class="node-icon">
                    <span class="icon-emoji">{{ getNodeEmoji(node.type) }}</span>
                  </div>
                  <div class="node-name">{{ node.name }}</div>
                  <div class="node-status" :class="getNodeStatusClass(node.status)"></div>
                </div>

                <!-- 连接点 -->
                <div
                    class="connection-point top"
                    :class="{ 'highlighted': highlightedPoint?.nodeId === node.id && highlightedPoint?.position === 'top' }"
                    @mousedown.stop="startConnection($event, node, 'top')"
                ></div>
                <div
                    class="connection-point right"
                    :class="{ 'highlighted': highlightedPoint?.nodeId === node.id && highlightedPoint?.position === 'right' }"
                    @mousedown.stop="startConnection($event, node, 'right')"
                ></div>
                <div
                    class="connection-point bottom"
                    :class="{ 'highlighted': highlightedPoint?.nodeId === node.id && highlightedPoint?.position === 'bottom' }"
                    @mousedown.stop="startConnection($event, node, 'bottom')"
                ></div>
                <div
                    class="connection-point left"
                    :class="{ 'highlighted': highlightedPoint?.nodeId === node.id && highlightedPoint?.position === 'left' }"
                    @mousedown.stop="startConnection($event, node, 'left')"
                ></div>
              </div>

              <!-- 连接线层 -->
              <svg class="connections-layer" :style="{ width: '100%', height: '100%' }">
                <defs>
                  <marker
                      id="arrowhead"
                      markerWidth="10"
                      markerHeight="7"
                      refX="9"
                      refY="3.5"
                      orient="auto"
                  >
                    <polygon
                        points="0 0, 10 3.5, 0 7"
                        fill="#409eff"
                    />
                  </marker>
                </defs>

                <!-- 现有连接线 -->
                <path
                    v-for="connection in connections"
                    :key="`${connection.id}-${connection.updated || 0}`"
                    :d="getCachedConnectionPath(connection)"
                    class="connection-line"
                    :class="{ 'selected': selectedConnection?.id === connection.id }"
                    stroke="#409eff"
                    stroke-width="2"
                    fill="none"
                    marker-end="url(#arrowhead)"
                    @click.stop="selectConnection(connection, $event)"
                />

                <!-- 临时连接线 -->
                <path
                    v-if="connecting.active"
                    :d="getTempConnectionPath()"
                    class="temp-connection"
                    stroke="#67c23a"
                    stroke-width="2"
                    stroke-dasharray="5,5"
                    fill="none"
                />
              </svg>
            </div>
          </div>
        </div>

        <!-- 右侧属性面板 -->
        <div class="designer-properties" v-if="selectedNode || selectedConnection">
          <div class="properties-header">
            <h4>属性面板</h4>
          </div>

          <div v-if="selectedNode" class="properties-content">
            <el-form label-width="80px" size="small">
              <el-form-item label="节点名称">
                <el-input v-model="selectedNode.name" placeholder="请输入节点名称"/>
              </el-form-item>

              <el-form-item label="节点类型">
                <el-tag :type="getNodeTypeColor(selectedNode.type)">{{ getNodeTypeName(selectedNode.type) }}</el-tag>
              </el-form-item>

              <!-- 任务节点属性 -->
              <template v-if="selectedNode.type === 'task'">
                <el-form-item label="执行人">
                  <el-input v-model="selectedNode.assignee" placeholder="请输入执行人"/>
                </el-form-item>
                <el-form-item label="候选人">
                  <el-input v-model="selectedNode.candidateUsers" placeholder="多个用户用逗号分隔"/>
                </el-form-item>
                <el-form-item label="优先级">
                  <el-select v-model="selectedNode.priority">
                    <el-option label="低" value="low"/>
                    <el-option label="中" value="medium"/>
                    <el-option label="高" value="high"/>
                  </el-select>
                </el-form-item>
              </template>

              <!-- 条件节点属性 -->
              <template v-if="selectedNode.type === 'condition'">
                <el-form-item label="条件表达式">
                  <el-input
                      v-model="selectedNode.condition"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入条件表达式"
                  />
                </el-form-item>
              </template>

              <!-- 脚本节点属性 -->
              <template v-if="selectedNode.type === 'script'">
                <el-form-item label="脚本语言">
                  <el-select v-model="selectedNode.scriptFormat">
                    <el-option label="JavaScript" value="javascript"/>
                    <el-option label="Python" value="python"/>
                    <el-option label="Groovy" value="groovy"/>
                  </el-select>
                </el-form-item>
                <el-form-item label="脚本内容">
                  <el-input
                      v-model="selectedNode.script"
                      type="textarea"
                      :rows="5"
                      placeholder="请输入脚本内容"
                  />
                </el-form-item>
              </template>

              <!-- 定时器属性 -->
              <template v-if="selectedNode.type === 'timer'">
                <el-form-item label="定时周期">
                  <el-input v-model="selectedNode.timerCycle" placeholder="如：0 0 12 * * ?"/>
                </el-form-item>
                <el-form-item label="延迟时间">
                  <el-input v-model="selectedNode.timerDelay" placeholder="如：PT1H（1小时）"/>
                </el-form-item>
              </template>

              <el-form-item>
                <el-button @click="deleteSelected" type="danger" size="small">
                  <el-icon>
                    <Delete/>
                  </el-icon>
                  删除节点
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <div v-else-if="selectedConnection" class="properties-content">
            <el-form label-width="80px" size="small">
              <el-form-item label="连线名称">
                <el-input v-model="selectedConnection.name" placeholder="请输入连线名称"/>
              </el-form-item>
              <el-form-item label="条件">
                <el-input v-model="selectedConnection.condition" placeholder="请输入条件表达式"/>
              </el-form-item>
              <el-form-item>
                <el-button @click="deleteSelected" type="danger" size="small">
                  <el-icon>
                    <Delete/>
                  </el-icon>
                  删除连线
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <div v-else class="no-selection">
            <div class="empty-state">
              <el-icon size="48" color="#c0c4cc">
                <Document/>
              </el-icon>
              <p>请选择节点或连线来编辑属性</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 预览对话框 -->
    <el-dialog v-model="showPreviewDialog" title="流程预览" width="80%" top="5vh">
      <div class="preview-content">
        <div class="preview-info">
          <h3>{{ processInfo.name || '未命名流程' }}</h3>
          <p>{{ processInfo.description || '暂无描述' }}</p>
        </div>

        <div class="preview-canvas">
          <div class="preview-placeholder">
            <el-icon size="48">
              <Document/>
            </el-icon>
            <p>流程预览功能开发中...</p>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPreviewDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 模板保存对话框 -->
    <el-dialog v-model="templateDialogVisible" title="保存为模板" width="30%">
      <el-form :model="templateForm" label-width="100px">
        <el-form-item label="模板名称" required>
          <el-input v-model="templateForm.name"/>
        </el-form-item>
        <el-form-item label="模板描述">
          <el-input type="textarea" v-model="templateForm.description"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="templateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveTemplate">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 模板列表对话框 -->
    <el-dialog v-model="templateListDialogVisible" title="加载模板" width="50%">
      <el-table :data="templateList" style="width: 100%" @row-click="row => selectedTemplate = row.id">
        <el-table-column prop="name" label="模板名称" width="180"/>
        <el-table-column prop="description" label="模板描述"/>
        <el-table-column prop="createTime" label="创建时间" width="180"/>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="templateListDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="loadTemplate(selectedTemplate)"
                     :disabled="!selectedTemplate">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- XML导入对话框 -->
    <el-dialog v-model="importXMLDialogVisible" title="导入BPMN文件" width="30%">
      <el-upload
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :limit="1"
          accept=".xml,.bpmn"
      >
        <template #trigger>
          <el-button type="primary">选择文件</el-button>
        </template>
        <template #tip>
          <div class="el-upload__tip">请选择 XML 或 BPMN 文件</div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importXMLDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitImportXML">导入</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 工作流验证结果对话框 -->
    <el-dialog v-model="validationDialogVisible" title="流程验证" width="50%">
      <div v-if="validationResult">
        <el-alert
            :title="validationResult.valid ? '验证成功' : '验证失败'"
            :type="validationResult.valid ? 'success' : 'error'"
            :description="validationResult.message"
            show-icon
        />
        <div v-if="!validationResult.valid && validationResult.errors" class="validation-errors">
          <h4>错误列表:</h4>
          <ul>
            <li v-for="(error, index) in validationResult.errors" :key="index">
              {{ error.message }}
            </li>
          </ul>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="validationDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted, nextTick} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {
  DocumentAdd,
  Upload,
  View,
  RefreshLeft,
  RefreshRight,
  ZoomIn,
  ZoomOut,
  Refresh,
  VideoPlay,
  VideoPause,
  User,
  Setting,
  Document,
  Switch,
  Grid,
  Connection,
  Timer,
  Message,
  Bell,
  Delete,
  Download,
  UploadFilled,
  ArrowDown
} from '@element-plus/icons-vue'
import {getCategoryTree} from '@/api/workflowConfig'
import {
  saveModel,
  deployModel,
  createModel,
  getModelList,
  exportModel,
  importWorkflowXML,
  saveWorkflowTemplate,
  getWorkflowTemplates,
  getWorkflowTemplateDetail,
  exportWorkflowXML,
  validateWorkflow
} from '@/api/workflow'

// 响应式数据
const processName = ref('新建流程')
const processCategory = ref('')
const categories = ref([])

// 画布相关
const canvasRef = ref(null)
const zoomLevel = ref(100)
const nodes = ref([])
const connections = ref([])
const selectedNode = ref(null)
const selectedConnection = ref(null)

// 拖拽相关
const draggedType = ref('')
const isDragging = ref(false)
const draggingNode = ref(null)
const dragOffset = ref({x: 0, y: 0})

// 连接相关
const connecting = ref({
  active: false,
  startNode: null,
  startPoint: '',
  path: '',
  endX: 0,
  endY: 0
})
const highlightedPoint = ref(null)

// 对话框
const showPreviewDialog = ref(false)
const templateDialogVisible = ref(false)
const templateListDialogVisible = ref(false)
const importXMLDialogVisible = ref(false)
const validationDialogVisible = ref(false)

// 模板相关数据
const templateForm = reactive({
  name: '',
  description: ''
})
const templateList = ref([])
const selectedTemplate = ref(null)
const xmlFile = ref(null)
const validationResult = ref(null)

// 节点ID计数器
let nodeIdCounter = 1
let connectionIdCounter = 1

// 获取节点表情符号
const getNodeEmoji = (type) => {
  const emojiMap = {
    start: '🚀',
    end: '🏁',
    task: '📋',
    condition: '🔀',
    parallel: '⚡',
    timer: '⏰',
    script: '📜'
  }
  return emojiMap[type] || '📄'
}

// 获取节点描述
const getNodeDescription = (type) => {
  const descMap = {
    start: '流程开始节点',
    end: '流程结束节点',
    task: '执行具体任务',
    condition: '条件判断分支',
    parallel: '并行执行任务',
    timer: '定时触发任务',
    script: '执行脚本代码'
  }
  return descMap[type] || '未知节点类型'
}

// 获取节点类型名称
const getNodeTypeName = (type) => {
  const nameMap = {
    start: '开始节点',
    end: '结束节点',
    task: '任务节点',
    condition: '条件节点',
    parallel: '并行节点',
    timer: '定时器',
    script: '脚本节点'
  }
  return nameMap[type] || '未知类型'
}

// 获取节点类型颜色
const getNodeTypeColor = (type) => {
  const colorMap = {
    start: 'success',
    end: 'danger',
    task: 'primary',
    condition: 'warning',
    parallel: 'info',
    timer: '',
    script: 'success'
  }
  return colorMap[type] || ''
}

// 获取节点状态类
const getNodeStatusClass = (status) => {
  const statusMap = {
    pending: 'status-pending',
    running: 'status-running',
    completed: 'status-completed',
    failed: 'status-failed'
  }
  return statusMap[status] || ''
}

// 拖拽开始
const onDragStart = (event, nodeType) => {
  draggedType.value = nodeType
  event.dataTransfer.effectAllowed = 'copy'
  event.dataTransfer.setData('text/plain', nodeType)
}

// 拖拽悬停
const onDragOver = (event) => {
  event.preventDefault()
  event.dataTransfer.dropEffect = 'copy'
}

// 拖拽放置
const onDrop = (event) => {
  event.preventDefault()

  if (!draggedType.value) return

  const rect = canvasRef.value.getBoundingClientRect()
  const x = (event.clientX - rect.left) / (zoomLevel.value / 100)
  const y = (event.clientY - rect.top) / (zoomLevel.value / 100)

  const newNode = {
    id: `node_${nodeIdCounter++}`,
    type: draggedType.value,
    name: getDefaultNodeName(draggedType.value),
    x: Math.max(0, x - 60),
    y: Math.max(0, y - 30),
    status: 'pending',
    ...getDefaultNodeProperties(draggedType.value)
  }

  nodes.value.push(newNode)
  draggedType.value = ''
}

// 获取默认节点名称
const getDefaultNodeName = (type) => {
  const nameMap = {
    start: '开始',
    end: '结束',
    task: '任务节点',
    condition: '条件判断',
    parallel: '并行处理',
    timer: '定时器',
    script: '脚本执行'
  }
  return nameMap[type] || '新节点'
}

// 获取默认节点属性
const getDefaultNodeProperties = (type) => {
  const defaultProps = {
    start: {},
    end: {},
    task: {
      assignee: '',
      candidateUsers: '',
      priority: 'medium'
    },
    condition: {
      condition: ''
    },
    parallel: {},
    timer: {
      timerCycle: '',
      timerDelay: ''
    },
    script: {
      scriptFormat: 'javascript',
      script: ''
    }
  }
  return defaultProps[type] || {}
}

// 节点拖拽相关方法
const startNodeDrag = (event, node) => {
  // 检查是否点击了连接点
  const isConnectionPoint = event.target.classList.contains('connection-point');

  // 如果正在连接或点击了连接点，则不触发拖拽
  if (connecting.value.active || isConnectionPoint) return;

  // 确保没有其他拖拽在进行
  if (draggingNode.value) return;

  draggingNode.value = node;
  const rect = canvasRef.value.getBoundingClientRect();
  const zoom = zoomLevel.value / 100;

  dragOffset.value = {
    x: event.clientX - rect.left - node.x * zoom,
    y: event.clientY - rect.top - node.y * zoom
  };

  document.addEventListener('mousemove', onNodeDrag);
  document.addEventListener('mouseup', endNodeDrag);
}

// 确保拖拽结束后清理状态
const endNodeDrag = () => {
  draggingNode.value = null;
  document.removeEventListener('mousemove', onNodeDrag);
  document.removeEventListener('mouseup', endNodeDrag);
}

const onNodeDrag = (event) => {
  if (!draggingNode.value) return;

  const rect = canvasRef.value.getBoundingClientRect();
  const zoom = zoomLevel.value / 100;

  const x = (event.clientX - rect.left - dragOffset.value.x) / zoom;
  const y = (event.clientY - rect.top - dragOffset.value.y) / zoom;

  draggingNode.value.x = Math.max(0, x);
  draggingNode.value.y = Math.max(0, y);

  // 使用 requestAnimationFrame 优化性能
  requestAnimationFrame(() => {
    updateNodeConnections(draggingNode.value.id);
  });
}

// 在创建连接时存储节点引用
const createConnection = (source, target, sourcePoint, targetPoint) => {
  const connection = {
    id: `connection_${connectionIdCounter++}`,
    sourceId: source.id,
    targetId: target.id,
    sourcePoint,
    targetPoint,
    name: '',
    condition: '',
    // 存储引用
    sourceRef: source,
    targetRef: target
  };

  connections.value.push(connection);
  return connection;
}

// 添加连接路径缓存
const connectionPathCache = new Map();

// 带缓存的连接路径获取
const getCachedConnectionPath = (connection) => {
  const cacheKey = `${connection.sourceId}-${connection.targetId}-${connection.sourcePoint}-${connection.targetPoint}`;

  if (connectionPathCache.has(cacheKey)) {
    return connectionPathCache.get(cacheKey);
  }

  const path = getConnectionPath(connection);
  connectionPathCache.set(cacheKey, path);
  return path;
}

// 在节点移动时清除相关缓存
const updateNodeConnections = (nodeId) => {
  // 清除与该节点相关的缓存
  for (const [key] of connectionPathCache.entries()) {
    if (key.includes(nodeId)) {
      connectionPathCache.delete(key);
    }
  }

  // 更新相关连线
  connections.value.forEach(connection => {
    if (connection.sourceId === nodeId || connection.targetId === nodeId) {
      // 触发响应式更新
      connection.updated = Date.now();
    }
  });
}

// // 更新节点相关的连线
// const updateNodeConnections = (nodeId) => {
//   connections.value.forEach(connection => {
//     if (connection.sourceId === nodeId || connection.targetId === nodeId) {
//       // 连线会通过响应式自动更新
//     }
//   })
// }

// 处理画布点击
const handleCanvasClick = (event) => {
  // 确保点击的是画布本身，而不是节点或连线
  if (event.target === canvasRef.value ||
      event.target.classList.contains('grid-background') ||
      event.target.classList.contains('canvas')) {

    // 清除所有选择
    selectedNode.value = null;
    selectedConnection.value = null;

    // 如果正在连接，取消连接
    if (connecting.value.active) {
      endConnection();
    }
  }
}

// 选择节点
const selectNode = (node, event) => {
  if (event) {
    event.stopPropagation();
  }

  // 如果点击的是当前已选中的节点，则取消选择
  if (selectedNode.value && selectedNode.value.id === node.id) {
    selectedNode.value = null;
    selectedConnection.value = null;
  } else {
    selectedNode.value = node;
    selectedConnection.value = null;
  }
}

// 选择连接线
const selectConnection = (connection, event) => {
  if (event) {
    event.stopPropagation();
  }

  // 如果点击的是当前已选中的连线，则取消选择
  if (selectedConnection.value && selectedConnection.value.id === connection.id) {
    selectedConnection.value = null;
    selectedNode.value = null;
  } else {
    selectedConnection.value = connection;
    selectedNode.value = null;
  }
}

// 删除选中的节点或连线
const deleteSelected = () => {
  if (selectedNode.value) {
    deleteNode(selectedNode.value.id)
  } else if (selectedConnection.value) {
    deleteConnection(selectedConnection.value.id)
  }
}

// 删除节点
const deleteNode = (nodeId) => {
  // 删除相关连接
  connections.value = connections.value.filter(
      conn => conn.sourceId !== nodeId && conn.targetId !== nodeId
  )

  // 删除节点
  nodes.value = nodes.value.filter(node => node.id !== nodeId)

  if (selectedNode.value && selectedNode.value.id === nodeId) {
    selectedNode.value = null
  }
}

// 删除连线
const deleteConnection = (connectionId) => {
  connections.value = connections.value.filter(conn => conn.id !== connectionId)

  if (selectedConnection.value && selectedConnection.value.id === connectionId) {
    selectedConnection.value = null
  }
}

// 开始连接
const startConnection = (event, node, point) => {
  if (event) {
    event.stopPropagation()
  }

  connecting.value = {
    active: true,
    startNode: node,
    startPoint: point,
    path: '',
    endX: 0,
    endY: 0
  }

  document.addEventListener('mousemove', updateTempConnection)
  document.addEventListener('mouseup', endConnection)
}

// 更新临时连接线
const updateTempConnection = (event) => {
  if (!connecting.value.active) return

  const rect = canvasRef.value.getBoundingClientRect()
  connecting.value.endX = (event.clientX - rect.left) / (zoomLevel.value / 100)
  connecting.value.endY = (event.clientY - rect.top) / (zoomLevel.value / 100)
}

// 修改 endConnection 函数
const endConnection = (event) => {
  if (!connecting.value.active) return;

  // 查找目标节点
  const targetNodeElement = event.target.closest('.flow-node');
  const targetNode = targetNodeElement
      ? nodes.value.find(node => node.id === targetNodeElement.dataset.nodeId)
      : null;

  if (targetNode && targetNode.id !== connecting.value.startNode.id) {
    // 获取目标连接点位置（需要实现）
    const targetPoint = getNearestConnectionPoint(targetNode, event);

    // 创建新连接
    const newConnection = {
      id: `connection_${connectionIdCounter++}`,
      sourceId: connecting.value.startNode.id,
      targetId: targetNode.id,
      sourcePoint: connecting.value.startPoint,
      targetPoint: targetPoint,
      name: '',
      condition: ''
    };
    connections.value.push(newConnection);
  }

  // 重置连接状态
  connecting.value = {
    active: false,
    startNode: null,
    startPoint: '',
    path: '',
    endX: 0,
    endY: 0
  };

  // 确保移除所有事件监听器
  document.removeEventListener('mousemove', updateTempConnection);
  document.removeEventListener('mouseup', endConnection);
}



// 辅助函数：找到最近的连接点
const getNearestConnectionPoint = (node, event) => {
  const points = ['top', 'right', 'bottom', 'left'];
  let minDistance = Infinity;
  let nearestPoint = 'right'; // 默认值

  const rect = canvasRef.value.getBoundingClientRect();
  const zoom = zoomLevel.value / 100;

  const mouseX = (event.clientX - rect.left) / zoom;
  const mouseY = (event.clientY - rect.top) / zoom;

  points.forEach(point => {
    const position = getConnectionPointPosition(node, point);
    const distance = Math.sqrt(
        Math.pow(mouseX - position.x, 2) +
        Math.pow(mouseY - position.y, 2)
    );

    if (distance < minDistance) {
      minDistance = distance;
      nearestPoint = point;
    }
  });

  return nearestPoint;
}


// 修改 getConnectionPointPosition 函数
const getConnectionPointPosition = (node, point) => {
  const width = node.width || 120; // 默认宽度
  const height = node.height || 60; // 默认高度

  const positions = {
    top: { x: node.x + width / 2, y: node.y },
    right: { x: node.x + width, y: node.y + height / 2 },
    bottom: { x: node.x + width / 2, y: node.y + height },
    left: { x: node.x, y: node.y + height / 2 }
  };

  return positions[point] || positions.top;
}

const createNode = (type, x, y) => {
  const sizeMap = {
    start: { width: 100, height: 60 },
    end: { width: 100, height: 60 },
    task: { width: 120, height: 60 },
    condition: { width: 100, height: 100 },
    parallel: { width: 120, height: 80 },
    timer: { width: 80, height: 80 },
    script: { width: 120, height: 70 }
  };

  return {
    id: `node_${nodeIdCounter++}`,
    type,
    name: getDefaultNodeName(type),
    x,
    y,
    width: sizeMap[type]?.width || 120,
    height: sizeMap[type]?.height || 60,
    status: 'pending',
    ...getDefaultNodeProperties(type)
  };
}

// 获取连接路径
const getConnectionPath = (connection) => {
  const sourceNode = nodes.value.find(n => n.id === connection.sourceId)
  const targetNode = nodes.value.find(n => n.id === connection.targetId)

  if (!sourceNode || !targetNode) return ''

  const start = getConnectionPointPosition(sourceNode, connection.sourcePoint)
  const end = getConnectionPointPosition(targetNode, connection.targetPoint)



  // 贝塞尔曲线
  const controlOffset = Math.abs(end.x - start.x) * 0.5
  const cp1x = start.x + controlOffset
  const cp1y = start.y
  const cp2x = end.x - controlOffset
  const cp2y = end.y

  return `M ${start.x} ${start.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${end.x} ${end.y}`
}

// 获取临时连接路径
const getTempConnectionPath = () => {
  if (!connecting.value.active || !connecting.value.startNode) return ''

  const start = getConnectionPointPosition(connecting.value.startNode, connecting.value.startPoint)
  const end = {x: connecting.value.endX, y: connecting.value.endY}

  const controlOffset = Math.abs(end.x - start.x) * 0.5
  const cp1x = start.x + controlOffset
  const cp1y = start.y
  const cp2x = end.x - controlOffset
  const cp2y = end.y

  return `M ${start.x} ${start.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${end.x} ${end.y}`
}

// 快速创建简单流程
const createSimpleProcess = () => {
  clearCanvas()

  const startNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'start',
    name: '开始',
    x: 100,
    y: 200,
    status: 'pending'
  }

  const taskNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '处理任务',
    x: 300,
    y: 200,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'medium'
  }

  const endNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'end',
    name: '结束',
    x: 500,
    y: 200,
    status: 'pending'
  }

  nodes.value = [startNode, taskNode, endNode]

  const connection1 = {
    id: `connection_${connectionIdCounter++}`,
    sourceId: startNode.id,
    targetId: taskNode.id,
    sourcePoint: 'right',
    targetPoint: 'left',
    name: '',
    condition: ''
  }

  const connection2 = {
    id: `connection_${connectionIdCounter++}`,
    sourceId: taskNode.id,
    targetId: endNode.id,
    sourcePoint: 'right',
    targetPoint: 'left',
    name: '',
    condition: ''
  }

  connections.value = [connection1, connection2]

  ElMessage.success('简单流程创建成功')
}

// 快速创建复杂流程
const createComplexProcess = () => {
  clearCanvas()

  const startNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'start',
    name: '开始',
    x: 100,
    y: 200,
    status: 'pending'
  }

  const taskNode1 = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '审核申请',
    x: 300,
    y: 200,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'high'
  }

  const conditionNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'condition',
    name: '审核结果',
    x: 500,
    y: 200,
    status: 'pending',
    condition: ''
  }

  const taskNode2 = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '通过处理',
    x: 700,
    y: 150,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'medium'
  }

  const taskNode3 = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '拒绝处理',
    x: 700,
    y: 250,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'medium'
  }

  const endNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'end',
    name: '结束',
    x: 900,
    y: 200,
    status: 'pending'
  }

  nodes.value = [startNode, taskNode1, conditionNode, taskNode2, taskNode3, endNode]

  const connections = [
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: startNode.id,
      targetId: taskNode1.id,
      sourcePoint: 'right',
      targetPoint: 'left',
      name: '',
      condition: ''
    },
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: taskNode1.id,
      targetId: conditionNode.id,
      sourcePoint: 'right',
      targetPoint: 'left',
      name: '',
      condition: ''
    },
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: conditionNode.id,
      targetId: taskNode2.id,
      sourcePoint: 'top',
      targetPoint: 'left',
      name: '通过',
      condition: 'approved == true'
    },
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: conditionNode.id,
      targetId: taskNode3.id,
      sourcePoint: 'bottom',
      targetPoint: 'left',
      name: '拒绝',
      condition: 'approved == false'
    },
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: taskNode2.id,
      targetId: endNode.id,
      sourcePoint: 'right',
      targetPoint: 'top',
      name: '',
      condition: ''
    },
    {
      id: `connection_${connectionIdCounter++}`,
      sourceId: taskNode3.id,
      targetId: endNode.id,
      sourcePoint: 'right',
      targetPoint: 'bottom',
      name: '',
      condition: ''
    }
  ]

  connections.value = connections

  ElMessage.success('复杂流程创建成功')
}

// 清空画布
const clearCanvas = () => {
  nodes.value = []
  connections.value = []
  selectedNode.value = null
  selectedConnection.value = null
  nodeIdCounter = 1
  connectionIdCounter = 1
  ElMessage.success('画布已清空')
}

// 自动布局
const autoLayout = () => {
  if (nodes.value.length === 0) {
    ElMessage.warning('画布为空，无需布局')
    return
  }

  // 简单的水平布局算法
  const startNodes = nodes.value.filter(node => node.type === 'start')
  const endNodes = nodes.value.filter(node => node.type === 'end')
  const otherNodes = nodes.value.filter(node => node.type !== 'start' && node.type !== 'end')

  let x = 100
  const y = 200
  const spacing = 200

  // 布局开始节点
  startNodes.forEach(node => {
    node.x = x
    node.y = y
    x += spacing
  })

  // 布局其他节点
  otherNodes.forEach(node => {
    node.x = x
    node.y = y
    x += spacing
  })

  // 布局结束节点
  endNodes.forEach(node => {
    node.x = x
    node.y = y
    x += spacing
  })

  ElMessage.success('自动布局完成')
}

// 保存流程
const saveProcess = async () => {
  if (!validateProcess()) {
    return
  }

  try {
    const processData = {
      name: processName.value,
      category: processCategory.value,
      nodes: nodes.value,
      connections: connections.value,
      version: '1.0',
      createTime: new Date().toISOString()
    }

    await saveModel(processData)
    ElMessage.success('流程保存成功')
  } catch (error) {
    console.error('保存流程失败:', error)
    ElMessage.error('保存流程失败')
  }
}

// 预览流程
const previewProcess = () => {
  if (!validateProcess()) {
    return
  }

  processInfo.name = processName.value
  processInfo.category = processCategory.value
  showPreviewDialog.value = true
}

// 验证流程
const validateProcess = () => {
  const errors = []

  // 检查是否有开始节点
  const startNodes = nodes.value.filter(node => node.type === 'start')
  if (startNodes.length === 0) {
    errors.push('流程必须包含至少一个开始节点')
  } else if (startNodes.length > 1) {
    errors.push('流程只能包含一个开始节点')
  }

  // 检查是否有结束节点
  const endNodes = nodes.value.filter(node => node.type === 'end')
  if (endNodes.length === 0) {
    errors.push('流程必须包含至少一个结束节点')
  }

  // 检查节点名称
  nodes.value.forEach(node => {
    if (!node.name || node.name.trim() === '') {
      errors.push(`节点 ${node.id} 缺少名称`)
    }
  })

  // 检查任务节点的必要属性
  const taskNodes = nodes.value.filter(node => node.type === 'task')
  taskNodes.forEach(node => {
    if (!node.assignee && !node.candidateUsers) {
      errors.push(`任务节点 ${node.name} 必须指定执行人或候选人`)
    }
  })

  if (errors.length > 0) {
    ElMessageBox.alert(
        errors.join('\n'),
        '流程验证失败',
        {
          type: 'error'
        }
    )
    return false
  }

  return true
}


// 其他方法继续保持不变...
const handleTemplateCommand = (command) => {
  switch (command) {
    case 'saveTemplate':
      templateDialogVisible.value = true
      break
    case 'loadTemplate':
      loadTemplateList()
      break
    case 'exportXML':
      exportToXML()
      break
    case 'importXML':
      importXMLDialogVisible.value = true
      break
    case 'validateWorkflow':
      validateCurrentWorkflow()
      break
  }
}

const saveTemplate = async () => {
  if (!templateForm.name) {
    ElMessage.warning('请输入模板名称')
    return
  }

  try {
    const templateData = {
      name: templateForm.name,
      description: templateForm.description,
      content: JSON.stringify({
        nodes: nodes.value,
        connections: connections.value
      })
    }

    await saveWorkflowTemplate(templateData)
    ElMessage.success('模板保存成功')
    templateDialogVisible.value = false
    templateForm.name = ''
    templateForm.description = ''
  } catch (error) {
    console.error('保存模板失败:', error)
    ElMessage.error('保存模板失败')
  }
}

const loadTemplateList = async () => {
  try {
    const response = await getWorkflowTemplates()
    templateList.value = response.data
    templateListDialogVisible.value = true
  } catch (error) {
    console.error('获取模板列表失败:', error)
    ElMessage.error('获取模板列表失败')
  }
}

const loadTemplate = async (templateId) => {
  if (!templateId) {
    ElMessage.warning('请选择模板')
    return
  }

  try {
    const response = await getWorkflowTemplateDetail(templateId)
    const templateData = JSON.parse(response.data.content)

    nodes.value = templateData.nodes || []
    connections.value = templateData.connections || []

    templateListDialogVisible.value = false
    ElMessage.success('模板加载成功')
  } catch (error) {
    console.error('加载模板失败:', error)
    ElMessage.error('加载模板失败')
  }
}

const exportToXML = async () => {
  try {
    const workflowData = {
      nodes: nodes.value,
      connections: connections.value
    }

    const response = await exportWorkflowXML(workflowData)
    const blob = new Blob([response.data], {type: 'application/xml'})
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `${processName.value || '工作流'}.xml`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    ElMessage.success('XML导出成功')
  } catch (error) {
    console.error('导出XML失败:', error)
    ElMessage.error('导出失败')
  }
}

const handleFileChange = (file) => {
  xmlFile.value = file.raw
}

const submitImportXML = async () => {
  if (!xmlFile.value) {
    ElMessage.warning('请选择文件')
    return
  }

  try {
    const formData = new FormData()
    formData.append('file', xmlFile.value)

    const response = await importWorkflowXML(formData)
    const workflowData = response.data

    nodes.value = workflowData.nodes || []
    connections.value = workflowData.connections || []

    importXMLDialogVisible.value = false
    xmlFile.value = null
    ElMessage.success('导入成功')
  } catch (error) {
    console.error('导入XML失败:', error)
    ElMessage.error('导入失败')
  }
}

const validateCurrentWorkflow = async () => {
  try {
    const workflowData = {
      nodes: nodes.value,
      connections: connections.value
    }

    const response = await validateWorkflow(workflowData)
    validationResult.value = response.data
    validationDialogVisible.value = true
  } catch (error) {
    console.error('验证工作流失败:', error)
    ElMessage.error('验证失败')
  }
}

// 流程信息
const processInfo = reactive({
  name: '',
  category: ''
})

const categoryOptions = ref([])

// 初始化
onMounted(async () => {
  try {
    const response = await getCategoryTree()
    categoryOptions.value = response.data || []
  } catch (error) {
    console.error('获取分类失败:', error)
  }
})


// 缩放操作
const zoomIn = () => {
  if (zoomLevel.value < 200) {
    zoomLevel.value += 25
  }
}

const zoomOut = () => {
  if (zoomLevel.value > 50) {
    zoomLevel.value -= 25
  }
}

const resetZoom = () => {
  zoomLevel.value = 100
}

// 撤销重做
const undoAction = () => {
  ElMessage.info('撤销功能开发中...')
}

const redoAction = () => {
  ElMessage.info('重做功能开发中...')
}


// 快速创建复杂流程
const createComplexFlow = () => {
  clearCanvas()

  const startNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'start',
    name: '开始',
    x: 50,
    y: 200,
    status: 'pending'
  }

  const task1Node = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '审核任务',
    x: 200,
    y: 200,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'high'
  }

  const conditionNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'condition',
    name: '审核结果',
    x: 400,
    y: 200,
    status: 'pending',
    condition: ''
  }

  const task2Node = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '通过处理',
    x: 600,
    y: 120,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'medium'
  }

  const task3Node = {
    id: `node_${nodeIdCounter++}`,
    type: 'task',
    name: '拒绝处理',
    x: 600,
    y: 280,
    status: 'pending',
    assignee: '',
    candidateUsers: '',
    priority: 'medium'
  }

  const endNode = {
    id: `node_${nodeIdCounter++}`,
    type: 'end',
    name: '结束',
    x: 800,
    y: 200,
    status: 'pending'
  }

  nodes.value = [startNode, task1Node, conditionNode, task2Node, task3Node, endNode]

  connections.value = [
    {
      id: `conn_${Date.now()}_1`,
      sourceId: startNode.id,
      targetId: task1Node.id,
      sourcePoint: 'right',
      targetPoint: 'left',
      name: ''
    },
    {
      id: `conn_${Date.now()}_2`,
      sourceId: task1Node.id,
      targetId: conditionNode.id,
      sourcePoint: 'right',
      targetPoint: 'left',
      name: ''
    },
    {
      id: `conn_${Date.now()}_3`,
      sourceId: conditionNode.id,
      targetId: task2Node.id,
      sourcePoint: 'top',
      targetPoint: 'left',
      name: '通过'
    },
    {
      id: `conn_${Date.now()}_4`,
      sourceId: conditionNode.id,
      targetId: task3Node.id,
      sourcePoint: 'bottom',
      targetPoint: 'left',
      name: '拒绝'
    },
    {
      id: `conn_${Date.now()}_5`,
      sourceId: task2Node.id,
      targetId: endNode.id,
      sourcePoint: 'right',
      targetPoint: 'top',
      name: ''
    },
    {
      id: `conn_${Date.now()}_6`,
      sourceId: task3Node.id,
      targetId: endNode.id,
      sourcePoint: 'right',
      targetPoint: 'bottom',
      name: ''
    }
  ]
}


// 导出流程
const exportProcess = () => {
  try {
    const processData = {
      name: processName.value,
      category: processCategory.value,
      nodes: nodes.value,
      connections: connections.value,
      version: '1.0',
      exportTime: new Date().toISOString()
    }

    const dataStr = JSON.stringify(processData, null, 2)
    const dataBlob = new Blob([dataStr], {type: 'application/json'})

    const link = document.createElement('a')
    link.href = URL.createObjectURL(dataBlob)
    link.download = `${processName.value || '流程设计'}.json`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    ElMessage.success('流程导出成功')
  } catch (error) {
    console.error('导出流程失败:', error)
    ElMessage.error('导出流程失败')
  }
}


// 初始化
onMounted(async () => {
  try {
    const response = await getCategoryTree()
    categoryOptions.value = response.data || []
  } catch (error) {
    console.error('获取分类失败:', error)
  }
})

// // 导出所有方法和数据
// return {
//   // 响应式数据
//   processName,
//   processCategory,
//   categories,
//   canvasRef,
//   zoomLevel,
//   nodes,
//   connections,
//   selectedNode,
//   selectedConnection,
//   draggedType,
//   isDragging,
//   draggingNode,
//   dragOffset,
//   connecting,
//   highlightedPoint,
//   showPreviewDialog,
//   templateDialogVisible,
//   templateListDialogVisible,
//   importXMLDialogVisible,
//   validationDialogVisible,
//   templateForm,
//   templateList,
//   selectedTemplate,
//   xmlFile,
//   validationResult,
//   processInfo,
//   categoryOptions,

//   // 计算属性和方法
//   getNodeEmoji,
//   getNodeDescription,
//   getNodeTypeName,
//   getNodeTypeColor,
//   getNodeStatusClass,
//   onDragStart,
//   onDragOver,
//   onDrop,
//   getDefaultNodeName,
//   getDefaultNodeProperties,
//   startNodeDrag,
//   onNodeDrag,
//   endNodeDrag,
//   updateNodeConnections,
//   handleCanvasClick,
//   selectNode,
//   selectConnection,
//   deleteSelected,
//   deleteNode,
//   deleteConnection,
//   startConnection,
//   updateTempConnection,
//   endConnection,
//   getConnectionPointPosition,
//   getConnectionPath,
//   getTempConnectionPath,
//   zoomIn,
//   zoomOut,
//   resetZoom,
//   undoAction,
//   redoAction,
//   clearCanvas,
//   createSimpleFlow,
//   createComplexFlow,
//   autoLayout,
//   validateProcess,
//   saveProcess,
//   deployProcess,
//   previewProcess,
//   exportProcess,
//   handleTemplateCommand,
//   saveTemplate,
//   loadTemplateList,
//   loadTemplate,
//   exportToXML,
//   handleFileChange,
//   submitImportXML,
//   validateCurrentWorkflow
// }

</script>

<style scoped>
.simplified-process-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 头部样式 */
.designer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

/* 添加GPU加速 */
.canvas {
  will-change: transform;
}

/* 优化SVG渲染 */
.connections-layer {
  transform: translateZ(0);
}

/* 拖动时简化连线样式 */
.dragging .connection-line {
  stroke-width: 1.5;
  stroke-dasharray: none;
}

.header-left h2 {
  margin: 0 0 4px 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}

.grid-background {
  pointer-events: auto !important;
}

/* 确保画布容器可点击 */
.canvas {
  pointer-events: auto !important;
}

.subtitle {
  margin: 0;
  color: #606266;
  font-size: 12px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

/* 设计器内容区域 */
.designer-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.designer-main {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧边栏 */
.designer-sidebar {
  width: 280px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
  padding: 15px;
}

.quick-start {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.quick-start h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 14px;
  font-weight: 600;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.node-category {
  margin-bottom: 25px;
}

.node-category h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 14px;
  font-weight: 600;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.node-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.node-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: grab;
  transition: all 0.2s;
  background: #fff;
}

.node-item:hover {
  border-color: #409EFF;
  background: #f0f9ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.node-item:active {
  cursor: grabbing;
}

.node-item .node-icon {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  background: #f0f9ff;
  border: 1px solid #d4edda;
}

.icon-emoji {
  font-size: 16px;
}

.node-info {
  flex: 1;
}

.node-name {
  display: block;
  font-size: 13px;
  color: #303133;
  font-weight: 500;
  margin-bottom: 2px;
}

.node-desc {
  display: block;
  font-size: 11px;
  color: #909399;
  line-height: 1.3;
}

.operation-tips {
  padding: 15px;
  background: #fff9e6;
  border-radius: 8px;
  border: 1px solid #ffeaa7;
}

.operation-tips h4 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 13px;
  font-weight: 600;
}

.tips-content p {
  margin: 4px 0;
  color: #606266;
  font-size: 11px;
  line-height: 1.4;
}

/* 画布容器 */
.designer-canvas-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
  overflow: hidden;
}

.canvas-main {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.canvas {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
  background: #fafbfc;
  cursor: default;
}

.grid-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(to right, #e1e5e9 1px, transparent 1px),
  linear-gradient(to bottom, #e1e5e9 1px, transparent 1px);
  background-size: 20px 20px;
  pointer-events: none;
  opacity: 0.6;
}

/* 流程节点样式 */
.flow-node {
  position: absolute;
  width: 120px;
  height: 60px;
  border: 2px solid #d1d5db;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  user-select: none;
}

.flow-node:hover {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transform: translateY(-1px);
}

.flow-node.selected {
  border-color: #409EFF;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}

.flow-node.dragging {
  opacity: 0.8;
  transform: rotate(2deg);
  z-index: 1000;
}

.node-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  position: relative;
}

.flow-node .node-icon {
  margin-bottom: 4px;
}

.flow-node .node-icon .icon-emoji {
  font-size: 18px;
}

.node-name {
  font-size: 12px;
  color: #374151;
  text-align: center;
  font-weight: 500;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.node-status {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #6b7280;
}

.node-status.pending {
  background: #f59e0b;
}

.node-status.running {
  background: #3b82f6;
}

.node-status.completed {
  background: #10b981;
}

.node-status.failed {
  background: #ef4444;
}

/* 连接点样式 */
.connection-point {
  position: absolute;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #409EFF;
  border: 2px solid #fff;
  cursor: crosshair;
  opacity: 0;
  transition: opacity 0.2s ease;
  z-index: 10;
}

/* 确保连接点不会阻止节点拖拽 */
.connection-point {
  pointer-events: auto !important; /* 允许连接点捕获事件 */
  transition: all 0.2s;
  transform: scale(0.8);
}

.connection-point:hover {
  transform: scale(1.2);
  background: #67c23a;
}

.flow-node > *:not(.connection-point) {
  pointer-events: none !important; /* 节点内容不捕获事件 */
}

.flow-node {
  pointer-events: auto !important; /* 整个节点捕获事件 */
}

.flow-node:hover .connection-point {
  opacity: 1 !important;
  transform: scale(1.3);
}

.connection-point.highlighted {
  opacity: 1;
  background: #67c23a;
  transform: scale(1.2);
}

.connection-point.top {
  top: -5px;
  left: 50%;
  transform: translateX(-50%);
}

.connection-point.right {
  right: -5px;
  top: 50%;
  transform: translateY(-50%);
}

.connection-point.bottom {
  bottom: -5px;
  left: 50%;
  transform: translateX(-50%);
}

.connection-point.left {
  left: -5px;
  top: 50%;
  transform: translateY(-50%);
}

/* 连接线样式 */
.connections-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.connection-line {
  pointer-events: stroke;
  stroke-width: 2;
  cursor: pointer;
  transition: stroke-width 0.2s ease;
}

.connection-line:hover {
  stroke-width: 3;
}

.connection-line.selected {
  stroke: #67c23a;
  stroke-width: 3;
}

.temp-connection {
  pointer-events: none;
  animation: dash 1s linear infinite;
}

@keyframes dash {
  to {
    stroke-dashoffset: -10;
  }
}

/* 节点类型样式 */
.node-start {
  border-color: #67c23a;
}

.node-end {
  border-color: #f56c6c;
}

.node-task {
  border-color: #409eff;
}

.node-condition {
  border-color: #e6a23c;
  border-radius: 50%;
  width: 80px;
  height: 80px;
}

.node-parallel {
  border-color: #909399;
}

.node-timer {
  border-color: #909399;
  border-radius: 50%;
}

.node-script {
  border-color: #606266;
}

/* 右侧属性面板 */
.designer-properties {
  width: 320px;
  background: #fff;
  border-left: 1px solid #e4e7ed;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.properties-header {
  padding: 15px 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
}

.properties-header h4 {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.properties-content {
  flex: 1;
  padding: 20px;
}

.no-selection {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.empty-state {
  text-align: center;
  color: #909399;
}

.empty-state p {
  margin: 12px 0 0 0;
  font-size: 14px;
}

/* 表单样式优化 */
.el-form-item {
  margin-bottom: 18px;
}

.el-form-item__label {
  font-weight: 500;
  color: #606266;
}

.el-input, .el-select, .el-textarea {
  width: 100%;
}

.el-tag {
  font-size: 12px;
}

/* 对话框样式 */
.preview-content {
  padding: 20px;
}

.preview-info {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.preview-info h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.preview-info p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.preview-canvas {
  height: 400px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.preview-placeholder {
  text-align: center;
  color: #909399;
}

.preview-placeholder p {
  margin: 10px 0 0 0;
  font-size: 14px;
}

/* 验证错误样式 */
.validation-errors {
  margin-top: 15px;
  padding: 15px;
  background-color: #fef0f0;
  border-radius: 6px;
  border-left: 4px solid #f56c6c;
}

.validation-errors h4 {
  color: #f56c6c;
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 600;
}

.validation-errors ul {
  margin: 0;
  padding-left: 20px;
}

.validation-errors li {
  margin-bottom: 6px;
  color: #606266;
  font-size: 13px;
  line-height: 1.4;
}

/* 工具提示 */
.dialog-footer {
  text-align: right;
  padding-top: 15px;
}

.upload-demo {
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .designer-sidebar {
    width: 240px;
  }

  .designer-properties {
    width: 280px;
  }
}

@media (max-width: 768px) {
  .designer-sidebar {
    width: 200px;
  }

  .designer-properties {
    width: 240px;
  }

  .flow-node {
    width: 100px;
    height: 50px;
  }

  .node-name {
    font-size: 11px;
  }
}

.preview-content {
  padding: 20px;
}

.preview-info {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.preview-info h3 {
  margin: 0 0 8px 0;
  color: #303133;
}

.preview-info p {
  margin: 0;
  color: #606266;
}

.preview-canvas {
  height: 400px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.preview-placeholder {
  text-align: center;
  color: #909399;
}

.preview-placeholder p {
  margin: 10px 0 0 0;
}

.validation-errors {
  margin-top: 15px;
  padding: 10px;
  background-color: #fff5f7;
  border-radius: 4px;
  border-left: 3px solid #f56c6c;
}

.validation-errors h4 {
  color: #f56c6c;
  margin-top: 0;
}

.validation-errors ul {
  padding-left: 20px;
}

.validation-errors li {
  margin-bottom: 5px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}

.upload-demo {
  width: 100%;
}

.el-form-item {
  margin-bottom: 15px;
}

.el-divider--vertical {
  height: 20px;
}
</style>