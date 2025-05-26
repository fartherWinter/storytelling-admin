<template>
  <div class="custom-workflow-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>{{ $t('workflow.custom.title') }}</span>
        </div>
      </template>
      
      <!-- 工作流自定义区域 -->
      <div class="workflow-designer">
        <div class="toolbar">
          <el-button-group>
            <el-button type="primary" size="small" @click="saveWorkflow">{{ $t('common.save') }}</el-button>
            <el-button type="success" size="small" @click="deployWorkflow">{{ $t('workflow.deploy') }}</el-button>
            <el-button type="warning" size="small" @click="clearDesigner">{{ $t('workflow.clear') }}</el-button>
          </el-button-group>
          
          <el-dropdown @command="handleTemplateCommand" style="margin-left: 10px;">
            <el-button type="info" size="small">
              {{ $t('workflow.custom.saveTemplate') }}<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="saveTemplate">{{ $t('workflow.custom.saveTemplate') }}</el-dropdown-item>
                <el-dropdown-item command="loadTemplate">{{ $t('workflow.custom.loadTemplate') }}</el-dropdown-item>
                <el-dropdown-item divided command="exportXML">{{ $t('workflow.custom.exportXML') }}</el-dropdown-item>
                <el-dropdown-item command="importXML">{{ $t('workflow.custom.importXML') }}</el-dropdown-item>
                <el-dropdown-item divided command="validateWorkflow">{{ $t('workflow.custom.validation') }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <div class="designer-container">
          <div class="node-panel">
            <h4>{{ $t('workflow.nodeTypes') }}</h4>
            <div class="node-list">
              <div 
                v-for="node in nodeTypes" 
                :key="node.type" 
                class="node-item"
                draggable="true"
                @dragstart="handleDragStart($event, node)"
              >
                <i :class="node.icon"></i>
                <span>{{ node.name }}</span>
              </div>
            </div>
          </div>
          
          <div 
            class="canvas" 
            @dragover="handleDragOver"
            @drop="handleDrop"
            ref="canvas"
          >
            <div 
              v-for="(node, index) in workflowNodes" 
              :key="index"
              class="workflow-node"
              :style="{left: node.x + 'px', top: node.y + 'px'}"
              @mousedown="startDrag($event, index)"
              @click="selectNode(index)"
              :class="{selected: selectedNodeIndex === index}"
            >
              <div class="node-header" :style="{backgroundColor: node.color}">
                <i :class="node.icon"></i>
                <span>{{ node.name }}</span>
              </div>
              <div class="node-body">
                <p>{{ node.description }}</p>
              </div>
              <div class="node-ports">
                <div class="port output" @mousedown="startConnection(index)"></div>
              </div>
            </div>
            
            <!-- 连接线 -->
            <svg class="connections" ref="connections">
              <path 
                v-for="(connection, index) in connections" 
                :key="index"
                :d="connection.path"
                :stroke="connection.color"
                stroke-width="2"
                fill="none"
              ></path>
            </svg>
          </div>
          
          <div class="properties-panel" v-if="selectedNodeIndex !== null">
            <h4>{{ $t('workflow.properties') }}</h4>
            <el-form label-position="top">
              <el-form-item :label="$t('workflow.nodeName')">
                <el-input v-model="workflowNodes[selectedNodeIndex].name"></el-input>
              </el-form-item>
              <el-form-item :label="$t('workflow.nodeDescription')">
                <el-input type="textarea" v-model="workflowNodes[selectedNodeIndex].description"></el-input>
              </el-form-item>
              <el-form-item :label="$t('workflow.nodeColor')">
                <el-color-picker v-model="workflowNodes[selectedNodeIndex].color"></el-color-picker>
              </el-form-item>
              
              <!-- 动态属性 -->
              <template v-for="(prop, key) in workflowNodes[selectedNodeIndex].properties" :key="key">
                <el-form-item :label="key">
                  <el-input v-model="workflowNodes[selectedNodeIndex].properties[key]"></el-input>
                </el-form-item>
              </template>
              
              <el-form-item>
                <el-button type="danger" size="small" @click="removeNode(selectedNodeIndex)">{{ $t('common.delete') }}</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </el-card>
  </div>
  <!-- 模板保存对话框 -->
  <el-dialog
    :title="$t('workflow.custom.saveTemplate')"
    v-model="templateDialogVisible"
    width="30%">
    <el-form :model="templateForm" label-width="100px">
      <el-form-item :label="$t('workflow.custom.templateName')" required>
        <el-input v-model="templateForm.name"></el-input>
      </el-form-item>
      <el-form-item :label="$t('workflow.custom.templateDesc')">
        <el-input type="textarea" v-model="templateForm.description"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="templateDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="saveTemplate">{{ $t('common.confirm') }}</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 模板列表对话框 -->
  <el-dialog
    :title="$t('workflow.custom.loadTemplate')"
    v-model="templateListDialogVisible"
    width="50%">
    <el-table :data="templateList" style="width: 100%" @row-click="row => selectedTemplate = row.id">
      <el-table-column prop="name" :label="$t('workflow.custom.templateName')" width="180"></el-table-column>
      <el-table-column prop="description" :label="$t('workflow.custom.templateDesc')"></el-table-column>
      <el-table-column prop="createTime" :label="$t('workflow.createTime')" width="180"></el-table-column>
    </el-table>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="templateListDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="loadTemplate(selectedTemplate)" :disabled="!selectedTemplate">{{ $t('common.confirm') }}</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- XML导入对话框 -->
  <el-dialog
    :title="$t('workflow.custom.importXML')"
    v-model="importXMLDialogVisible"
    width="30%">
    <el-upload
      class="upload-demo"
      action="#"
      :auto-upload="false"
      :on-change="handleFileChange"
      :limit="1"
      accept=".xml,.bpmn">
      <template #trigger>
        <el-button type="primary">{{ $t('common.select') }}</el-button>
      </template>
      <template #tip>
        <div class="el-upload__tip">{{ $t('common.pleaseSelect') }} XML {{ $t('common.file') }}</div>
      </template>
    </el-upload>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="importXMLDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="submitImportXML">{{ $t('common.import') }}</el-button>
      </span>
    </template>
  </el-dialog>
  
  <!-- 工作流验证结果对话框 -->
  <el-dialog
    :title="$t('workflow.custom.validation')"
    v-model="validationDialogVisible"
    width="50%">
    <div v-if="validationResult">
      <el-alert
        :title="validationResult.valid ? $t('workflow.custom.validationSuccess') : $t('workflow.custom.validationFailed')"
        :type="validationResult.valid ? 'success' : 'error'"
        :description="validationResult.message"
        show-icon>
      </el-alert>
      <div v-if="!validationResult.valid && validationResult.errors" class="validation-errors">
        <h4>{{ $t('common.errors') }}:</h4>
        <ul>
          <li v-for="(error, index) in validationResult.errors" :key="index">
            {{ error.message }}
          </li>
        </ul>
      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="validationDialogVisible = false">{{ $t('common.close') }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { saveWorkflowTemplate, getWorkflowTemplates, getWorkflowTemplateDetail, exportWorkflowXML, importWorkflowXML, validateWorkflow } from '@/api/workflow'

export default {
  name: 'CustomWorkflow',
  data() {
    return {
      nodeTypes: [
        { type: 'start', name: '开始节点', icon: 'el-icon-video-play', color: '#67C23A', properties: {} },
        { type: 'task', name: '任务节点', icon: 'el-icon-s-order', color: '#409EFF', properties: { assignee: '', dueDate: '', priority: 'medium' } },
        { type: 'gateway', name: '网关节点', icon: 'el-icon-share', color: '#E6A23C', properties: { condition: '', defaultFlow: false } },
        { type: 'end', name: '结束节点', icon: 'el-icon-video-pause', color: '#F56C6C', properties: {} },
        { type: 'userTask', name: '用户任务', icon: 'el-icon-user', color: '#409EFF', properties: { assignee: '', candidateUsers: '', candidateGroups: '', dueDate: '' } },
        { type: 'serviceTask', name: '服务任务', icon: 'el-icon-s-platform', color: '#409EFF', properties: { class: '', expression: '', delegateExpression: '' } },
        { type: 'scriptTask', name: '脚本任务', icon: 'el-icon-s-operation', color: '#409EFF', properties: { scriptFormat: 'groovy', script: '' } },
        { type: 'exclusiveGateway', name: '排他网关', icon: 'el-icon-share', color: '#E6A23C', properties: { defaultFlow: '' } },
        { type: 'parallelGateway', name: '并行网关', icon: 'el-icon-s-grid', color: '#E6A23C', properties: {} },
        { type: 'inclusiveGateway', name: '包容网关', icon: 'el-icon-s-fold', color: '#E6A23C', properties: { defaultFlow: '' } },
        { type: 'timerEvent', name: '定时事件', icon: 'el-icon-time', color: '#909399', properties: { timeDuration: '', timeCycle: '', timeDate: '' } }
      ],
      workflowNodes: [],
      connections: [],
      selectedNodeIndex: null,
      isDragging: false,
      draggedNode: null,
      startConnectionNode: null,
      tempConnection: null,
      // 模板相关数据
      templateDialogVisible: false,
      templateListDialogVisible: false,
      templateForm: {
        name: '',
        description: ''
      },
      templateList: [],
      selectedTemplate: null,
      // XML导入相关
      importXMLDialogVisible: false,
      xmlFile: null,
      // 验证结果
      validationResult: null,
      validationDialogVisible: false
    }
  },
  methods: {
    // 模板和XML相关方法
    handleTemplateCommand(command) {
      switch(command) {
        case 'saveTemplate':
          this.templateDialogVisible = true;
          break;
        case 'loadTemplate':
          this.loadTemplateList();
          break;
        case 'exportXML':
          this.exportToXML();
          break;
        case 'importXML':
          this.importXMLDialogVisible = true;
          break;
        case 'validateWorkflow':
          this.validateCurrentWorkflow();
          break;
      }
    },
    
    // 保存为模板
    saveTemplate() {
      if (!this.templateForm.name) {
        this.$message.warning(this.$t('common.pleaseInput') + this.$t('workflow.custom.templateName'));
        return;
      }
      
      const templateData = {
        name: this.templateForm.name,
        description: this.templateForm.description,
        content: JSON.stringify({
          nodes: this.workflowNodes,
          connections: this.connections
        })
      };
      
      saveWorkflowTemplate(templateData).then(response => {
        this.$message.success(this.$t('workflow.custom.saveSuccess'));
        this.templateDialogVisible = false;
        this.templateForm = { name: '', description: '' };
      }).catch(error => {
        console.error('保存模板失败:', error);
      });
    },
    
    // 加载模板列表
    loadTemplateList() {
      getWorkflowTemplates().then(response => {
        this.templateList = response.data;
        this.templateListDialogVisible = true;
      }).catch(error => {
        console.error('获取模板列表失败:', error);
      });
    },
    
    // 加载选中的模板
    loadTemplate(templateId) {
      getWorkflowTemplateDetail(templateId).then(response => {
        const templateData = response.data;
        if (templateData && templateData.content) {
          try {
            const workflowData = JSON.parse(templateData.content);
            this.workflowNodes = workflowData.nodes;
            this.connections = workflowData.connections;
            this.$message.success(this.$t('workflow.custom.loadSuccess'));
            this.templateListDialogVisible = false;
            this.selectedTemplate = null;
            this.$nextTick(() => {
              this.updateConnections();
            });
          } catch (e) {
            console.error('解析模板内容失败:', e);
            this.$message.error(this.$t('common.failed'));
          }
        }
      }).catch(error => {
        console.error('加载模板失败:', error);
      });
    },
    
    // 导出为XML
    exportToXML() {
      const workflowData = {
        nodes: this.workflowNodes,
        connections: this.connections,
        name: 'Custom Workflow',
        version: 1
      };
      
      exportWorkflowXML(workflowData).then(response => {
        // 创建下载链接
        const blob = new Blob([response.data], { type: 'application/xml' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'workflow.bpmn20.xml';
        link.click();
        URL.revokeObjectURL(link.href);
        this.$message.success(this.$t('workflow.custom.exportSuccess'));
      }).catch(error => {
        console.error('导出XML失败:', error);
      });
    },
    
    // 导入XML文件
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.xmlFile = file;
      }
    },
    
    // 提交XML导入
    submitImportXML() {
      if (!this.xmlFile) {
        this.$message.warning(this.$t('common.pleaseSelect') + ' XML ' + this.$t('common.file'));
        return;
      }
      
      const formData = new FormData();
      formData.append('file', this.xmlFile);
      
      importWorkflowXML(formData).then(response => {
        const workflowData = response.data;
        if (workflowData) {
          this.workflowNodes = workflowData.nodes;
          this.connections = workflowData.connections;
          this.$message.success(this.$t('workflow.custom.importSuccess'));
          this.importXMLDialogVisible = false;
          this.xmlFile = null;
          this.$nextTick(() => {
            this.updateConnections();
          });
        }
      }).catch(error => {
        console.error('导入XML失败:', error);
      });
    },
    
    // 验证当前工作流
    validateCurrentWorkflow() {
      const workflowData = {
        nodes: this.workflowNodes,
        connections: this.connections
      };
      
      validateWorkflow(workflowData).then(response => {
        this.validationResult = response.data;
        this.validationDialogVisible = true;
      }).catch(error => {
        console.error('验证工作流失败:', error);
      });
    },
    
    // 节点拖拽相关方法
    handleDragStart(event, node) {
      this.draggedNode = { ...node, x: 0, y: 0, id: Date.now() };
    },
    handleDragOver(event) {
      event.preventDefault();
    },
    handleDrop(event) {
      event.preventDefault();
      if (this.draggedNode) {
        const rect = this.$refs.canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        
        this.workflowNodes.push({
          ...this.draggedNode,
          x,
          y,
          properties: { ...this.draggedNode.properties }
        });
        
        this.draggedNode = null;
      }
    },
    startDrag(event, index) {
      if (event.target.classList.contains('port')) return;
      
      this.isDragging = true;
      this.selectedNodeIndex = index;
      
      const node = this.workflowNodes[index];
      const startX = event.clientX;
      const startY = event.clientY;
      const startNodeX = node.x;
      const startNodeY = node.y;
      
      const handleMouseMove = (moveEvent) => {
        const dx = moveEvent.clientX - startX;
        const dy = moveEvent.clientY - startY;
        
        this.workflowNodes[index].x = startNodeX + dx;
        this.workflowNodes[index].y = startNodeY + dy;
        
        this.updateConnections();
      };
      
      const handleMouseUp = () => {
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
        this.isDragging = false;
      };
      
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
    },
    selectNode(index) {
      if (!this.isDragging) {
        this.selectedNodeIndex = index;
      }
    },
    removeNode(index) {
      // 删除与该节点相关的连接
      this.connections = this.connections.filter(conn => 
        conn.source !== index && conn.target !== index
      );
      
      // 更新连接中大于被删除索引的索引值
      this.connections.forEach(conn => {
        if (conn.source > index) conn.source--;
        if (conn.target > index) conn.target--;
      });
      
      this.workflowNodes.splice(index, 1);
      this.selectedNodeIndex = null;
      this.updateConnections();
    },
    startConnection(sourceIndex) {
      this.startConnectionNode = sourceIndex;
      
      const sourceNode = this.workflowNodes[sourceIndex];
      const sourcePort = this.$el.querySelectorAll('.workflow-node')[sourceIndex].querySelector('.port.output');
      const sourcePortRect = sourcePort.getBoundingClientRect();
      const canvasRect = this.$refs.canvas.getBoundingClientRect();
      
      const startX = sourcePortRect.left + sourcePortRect.width / 2 - canvasRect.left;
      const startY = sourcePortRect.top + sourcePortRect.height / 2 - canvasRect.top;
      
      const handleMouseMove = (moveEvent) => {
        const endX = moveEvent.clientX - canvasRect.left;
        const endY = moveEvent.clientY - canvasRect.top;
        
        // 创建贝塞尔曲线路径
        const path = `M ${startX} ${startY} C ${startX + 50} ${startY}, ${endX - 50} ${endY}, ${endX} ${endY}`;
        
        if (!this.tempConnection) {
          const svg = this.$refs.connections;
          const newPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
          newPath.setAttribute('d', path);
          newPath.setAttribute('stroke', '#999');
          newPath.setAttribute('stroke-width', '2');
          newPath.setAttribute('fill', 'none');
          newPath.setAttribute('stroke-dasharray', '5,5');
          svg.appendChild(newPath);
          this.tempConnection = newPath;
        } else {
          this.tempConnection.setAttribute('d', path);
        }
      };
      
      const handleMouseUp = (upEvent) => {
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
        
        if (this.tempConnection) {
          this.tempConnection.remove();
          this.tempConnection = null;
        }
        
        // 检查是否放在了另一个节点上
        const targetElement = document.elementFromPoint(upEvent.clientX, upEvent.clientY);
        if (targetElement) {
          const targetNode = targetElement.closest('.workflow-node');
          if (targetNode) {
            const targetIndex = Array.from(this.$el.querySelectorAll('.workflow-node')).indexOf(targetNode);
            if (targetIndex !== -1 && targetIndex !== sourceIndex) {
              // 创建新连接
              this.connections.push({
                source: sourceIndex,
                target: targetIndex,
                color: '#409EFF',
                path: '',
                id: `connection_${Date.now()}`,
                label: ''
              });
              this.updateConnections();
            }
          }
        }
        
        this.startConnectionNode = null;
      };
      
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
    },
    updateConnections() {
      this.$nextTick(() => {
        const nodeElements = this.$el.querySelectorAll('.workflow-node');
        const canvasRect = this.$refs.canvas.getBoundingClientRect();
        
        this.connections.forEach((connection, index) => {
          const sourceNode = nodeElements[connection.source];
          const targetNode = nodeElements[connection.target];
          
          if (sourceNode && targetNode) {
            const sourcePort = sourceNode.querySelector('.port.output');
            const sourcePortRect = sourcePort.getBoundingClientRect();
            
            const startX = sourcePortRect.left + sourcePortRect.width / 2 - canvasRect.left;
            const startY = sourcePortRect.top + sourcePortRect.height / 2 - canvasRect.top;
            
            const targetRect = targetNode.getBoundingClientRect();
            const endX = targetRect.left + targetRect.width / 2 - canvasRect.left;
            const endY = targetRect.top - canvasRect.top;
            
            // 创建贝塞尔曲线路径，优化曲线形状
            const dx = Math.abs(endX - startX);
            const dy = Math.abs(endY - startY);
            const controlPointOffset = Math.max(50, dx * 0.3);
            
            const path = `M ${startX} ${startY} C ${startX + controlPointOffset} ${startY}, ${endX - controlPointOffset} ${endY}, ${endX} ${endY}`;
            
            this.connections[index].path = path;
          }
        });
      });
    },
    clearDesigner() {
      this.$confirm(this.$t('workflow.confirmClear'), this.$t('common.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.workflowNodes = [];
        this.connections = [];
        this.selectedNodeIndex = null;
      }).catch(() => {});
    },
    saveWorkflow() {
      const workflow = {
        nodes: this.workflowNodes,
        connections: this.connections,
        name: 'Custom Workflow',
        version: 1,
        createTime: new Date().toISOString()
      };
      
      // 这里可以调用API保存工作流
      console.log('保存工作流:', workflow);
      this.$message.success(this.$t('workflow.saveSuccess'));
    },
    deployWorkflow() {
      // 先验证工作流
      const workflowData = {
        nodes: this.workflowNodes,
        connections: this.connections
      };
      
      validateWorkflow(workflowData).then(response => {
        const validationResult = response.data;
        if (validationResult.valid) {
          // 验证通过，可以部署
          this.$message.success(this.$t('workflow.deploySuccess'));
        } else {
          // 验证失败，显示错误信息
          this.$message.error(this.$t('workflow.custom.validationFailed') + ': ' + validationResult.message);
        }
      }).catch(error => {
        console.error('验证工作流失败:', error);
        this.$message.error(this.$t('common.failed'));
      });
    }
  },
  mounted() {
    window.addEventListener('resize', this.updateConnections);
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.updateConnections);
  }
}
</script>

<style scoped>
.custom-workflow-container {
  padding: 20px;
}

.workflow-designer {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 200px);
}

.toolbar {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.designer-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.node-panel {
  width: 200px;
  border-right: 1px solid #eee;
  padding: 10px;
  overflow-y: auto;
  max-height: calc(100vh - 250px);
}

.node-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.node-item {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: move;
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #f9f9f9;
  transition: all 0.3s;
}

.node-item:hover {
  background-color: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.node-item i {
  font-size: 18px;
}

.canvas {
  flex: 1;
  position: relative;
  background-color: #f5f7fa;
  background-image: radial-gradient(#e0e0e0 1px, transparent 1px);
  background-size: 20px 20px;
  overflow: auto;
  min-height: 500px;
}

.workflow-node {
  position: absolute;
  width: 150px;
  border-radius: 4px;
  background-color: white;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1;
  user-select: none;
  transition: box-shadow 0.3s, transform 0.2s;
}

.workflow-node:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.workflow-node.selected {
  box-shadow: 0 0 0 2px #409EFF, 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.node-header {
  padding: 8px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.node-body {
  padding: 8px;
}

.node-ports {
  display: flex;
  justify-content: flex-end;
  padding: 4px;
}

.port {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #409EFF;
  cursor: crosshair;
  transition: transform 0.2s, background-color 0.2s;
}

.port:hover {
  transform: scale(1.2);
  background-color: #66b1ff;
}

.connections {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.properties-panel {
  width: 280px;
  border-left: 1px solid #eee;
  padding: 10px;
  overflow-y: auto;
  max-height: calc(100vh - 250px);
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
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.el-upload__tip {
  line-height: 1.5;
  margin-top: 8px;
  color: #909399;
}
</style>