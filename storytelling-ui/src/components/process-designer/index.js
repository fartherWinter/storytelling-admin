// 流程设计器组件库入口文件

// 导入所有流程节点组件
import ProcessStartNode from './ProcessStartNode.vue'
import ProcessEndNode from './ProcessEndNode.vue'
import ProcessUserTask from './ProcessUserTask.vue'
import ProcessServiceTask from './ProcessServiceTask.vue'
import ProcessScriptTask from './ProcessScriptTask.vue'
import ProcessExclusiveGateway from './ProcessExclusiveGateway.vue'
import ProcessParallelGateway from './ProcessParallelGateway.vue'
import ProcessTimerEvent from './ProcessTimerEvent.vue'
import ProcessConnection from './ProcessConnection.vue'

// 导入图标组件
import {
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
  Bell
} from '@element-plus/icons-vue'

// 组件映射表
export const componentMap = {
  'start-node': ProcessStartNode,
  'end-node': ProcessEndNode,
  'user-task': ProcessUserTask,
  'service-task': ProcessServiceTask,
  'script-task': ProcessScriptTask,
  'exclusive-gateway': ProcessExclusiveGateway,
  'parallel-gateway': ProcessParallelGateway,
  'timer-event': ProcessTimerEvent,
  'connection': ProcessConnection
}

// 流程节点配置
export const nodeConfig = {
  // 开始/结束节点
  startEnd: [
    {
      type: 'start',
      label: '开始',
      icon: VideoPlay,
      component: ProcessStartNode,
      category: 'startEnd',
      defaultConfig: {
        id: '',
        label: '开始',
        x: 0,
        y: 0,
        width: 100,
        height: 50,
        properties: {
          name: '开始',
          documentation: ''
        }
      }
    },
    {
      type: 'end',
      label: '结束',
      icon: VideoPause,
      component: ProcessEndNode,
      category: 'startEnd',
      defaultConfig: {
        id: '',
        label: '结束',
        x: 0,
        y: 0,
        width: 100,
        height: 50,
        properties: {
          name: '结束',
          documentation: ''
        }
      }
    }
  ],
  
  // 任务节点
  tasks: [
    {
      type: 'userTask',
      label: '用户任务',
      icon: User,
      component: ProcessUserTask,
      category: 'tasks',
      defaultConfig: {
        id: '',
        label: '用户任务',
        x: 0,
        y: 0,
        width: 120,
        height: 80,
        properties: {
          name: '用户任务',
          assignee: '',
          candidateUsers: '',
          candidateGroups: '',
          formKey: '',
          priority: 50,
          dueDate: '',
          documentation: ''
        }
      }
    },
    {
      type: 'serviceTask',
      label: '服务任务',
      icon: Setting,
      component: ProcessServiceTask,
      category: 'tasks',
      defaultConfig: {
        id: '',
        label: '服务任务',
        x: 0,
        y: 0,
        width: 120,
        height: 80,
        properties: {
          name: '服务任务',
          implementation: '',
          expression: '',
          delegateExpression: '',
          documentation: ''
        }
      }
    },
    {
      type: 'scriptTask',
      label: '脚本任务',
      icon: Document,
      component: ProcessScriptTask,
      category: 'tasks',
      defaultConfig: {
        id: '',
        label: '脚本任务',
        x: 0,
        y: 0,
        width: 120,
        height: 80,
        properties: {
          name: '脚本任务',
          scriptFormat: 'javascript',
          script: '',
          resultVariable: '',
          documentation: ''
        }
      }
    }
  ],
  
  // 网关节点
  gateways: [
    {
      type: 'exclusiveGateway',
      label: '排他网关',
      icon: Switch,
      component: ProcessExclusiveGateway,
      category: 'gateways',
      defaultConfig: {
        id: '',
        label: '排他网关',
        x: 0,
        y: 0,
        width: 80,
        height: 80,
        properties: {
          name: '排他网关',
          defaultFlow: '',
          documentation: ''
        }
      }
    },
    {
      type: 'parallelGateway',
      label: '并行网关',
      icon: Grid,
      component: ProcessParallelGateway,
      category: 'gateways',
      defaultConfig: {
        id: '',
        label: '并行网关',
        x: 0,
        y: 0,
        width: 80,
        height: 80,
        properties: {
          name: '并行网关',
          documentation: ''
        }
      }
    },
    {
      type: 'inclusiveGateway',
      label: '包容网关',
      icon: Connection,
      component: ProcessInclusiveGateway,
      category: 'gateways',
      defaultConfig: {
        id: '',
        label: '包容网关',
        x: 0,
        y: 0,
        width: 80,
        height: 80,
        properties: {
          name: '包容网关',
          defaultFlow: '',
          documentation: ''
        }
      }
    }
  ],
  
  // 事件节点
  events: [
    {
      type: 'timerEvent',
      label: '定时事件',
      icon: Timer,
      component: ProcessTimerEvent,
      category: 'events',
      defaultConfig: {
        id: '',
        label: '定时事件',
        x: 0,
        y: 0,
        width: 60,
        height: 60,
        properties: {
          name: '定时事件',
          timerType: 'timeDate',
          timerExpression: '',
          documentation: ''
        }
      }
    },
    {
      type: 'messageEvent',
      label: '消息事件',
      icon: Message,
      component: ProcessMessageEvent,
      category: 'events',
      defaultConfig: {
        id: '',
        label: '消息事件',
        x: 0,
        y: 0,
        width: 60,
        height: 60,
        properties: {
          name: '消息事件',
          messageRef: '',
          documentation: ''
        }
      }
    },
    {
      type: 'signalEvent',
      label: '信号事件',
      icon: Bell,
      component: ProcessSignalEvent,
      category: 'events',
      defaultConfig: {
        id: '',
        label: '信号事件',
        x: 0,
        y: 0,
        width: 60,
        height: 60,
        properties: {
          name: '信号事件',
          signalRef: '',
          documentation: ''
        }
      }
    }
  ]
}

// 获取节点配置
export function getNodeConfig(nodeType) {
  for (const category in nodeConfig) {
    const node = nodeConfig[category].find(item => item.type === nodeType)
    if (node) {
      return { ...node }
    }
  }
  return null
}

// 获取节点组件
export function getNodeComponent(nodeType) {
  const config = getNodeConfig(nodeType)
  return config ? config.component : null
}

// 获取所有节点类型
export function getAllNodeTypes() {
  const types = []
  for (const category in nodeConfig) {
    nodeConfig[category].forEach(node => {
      types.push(node.type)
    })
  }
  return types
}

// 生成节点ID
let nodeIdCounter = 1
export function generateNodeId(nodeType) {
  return `${nodeType}_${nodeIdCounter++}`
}

// 生成连接线ID
let connectionIdCounter = 1
export function generateConnectionId() {
  return `connection_${connectionIdCounter++}`
}

// 重置ID计数器
export function resetIdCounters() {
  nodeIdCounter = 1
  connectionIdCounter = 1
}

// 导出所有组件
export {
  ProcessStartNode,
  ProcessEndNode,
  ProcessUserTask,
  ProcessServiceTask,
  ProcessScriptTask,
  ProcessExclusiveGateway,
  ProcessParallelGateway,
  ProcessInclusiveGateway,
  ProcessTimerEvent,
  ProcessMessageEvent,
  ProcessSignalEvent
}

// 默认导出
export default {
  componentMap,
  nodeConfig,
  getNodeConfig,
  getNodeComponent,
  getAllNodeTypes,
  generateNodeId,
  generateConnectionId,
  resetIdCounters
}