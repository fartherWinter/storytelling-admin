/**
 * 工作流模型设计器配置
 */
const WorkflowDesignerConfig = {
    // API基础路径
    baseUrl: '/workflow',
    
    // 模型API路径
    modelApi: {
        list: '/workflow/model',                      // 获取模型列表
        create: '/workflow/model',                    // 创建模型
        get: '/workflow/model/{modelId}',            // 获取模型详情
        update: '/workflow/model/{modelId}',         // 更新模型
        delete: '/workflow/model/{modelId}',         // 删除模型
        deploy: '/workflow/model/{modelId}/deploy',  // 部署模型
        exportXml: '/workflow/model/{modelId}/export-xml', // 导出XML
        exportPng: '/workflow/model/{modelId}/export-png'  // 导出PNG
    },
    
    // 设计器API路径
    designerApi: {
        stencilset: '/workflow/designer/editor/stencilset',       // 获取模型集合
        modelJson: '/workflow/designer/model/{modelId}/json',      // 获取模型JSON
        saveModel: '/workflow/designer/model/{modelId}/save',      // 保存模型
        convertToXml: '/workflow/designer/model/{modelId}/convert-to-xml', // 转换为XML
        deployModel: '/workflow/designer/model/{modelId}/deploy'   // 部署模型
    },
    
    // 流程定义API路径
    processDefinitionApi: {
        list: '/workflow/process-definitions',                      // 获取流程定义列表
        get: '/workflow/process-definitions/{processDefinitionId}', // 获取流程定义详情
        resource: '/workflow/process-definitions/{processDefinitionId}/resource' // 获取流程定义资源
    },
    
    // 流程实例API路径
    processInstanceApi: {
        start: '/workflow/start',                                  // 启动流程实例
        tasks: '/workflow/process/{processInstanceId}/tasks',      // 获取流程任务
        diagram: '/workflow/process/{processInstanceId}/diagram',  // 获取流程图
        variable: '/workflow/process/{processInstanceId}/variables/{variableName}', // 获取流程变量
        terminate: '/workflow/process/{processInstanceId}'         // 终止流程实例
    },
    
    // 任务API路径
    taskApi: {
        list: '/workflow/tasks',                                  // 获取任务列表
        complete: '/workflow/tasks/{taskId}/complete',            // 完成任务
        approve: '/workflow/tasks/{taskId}/approve',              // 审批通过
        reject: '/workflow/tasks/{taskId}/reject'                 // 拒绝任务
    },
    
    // 设计器配置
    designer: {
        width: '100%',
        height: '600px',
        theme: 'default',
        palette: {
            width: '200px'
        }
    }
};