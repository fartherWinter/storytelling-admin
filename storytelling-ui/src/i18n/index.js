import { createI18n } from 'vue-i18n'

// 中文语言包
const zh = {
  common: {
    confirm: '确认',
    cancel: '取消',
    save: '保存',
    delete: '删除',
    edit: '编辑',
    add: '添加',
    search: '搜索',
    reset: '重置',
    back: '返回',
    export: '导出',
    import: '导入',
    refresh: '刷新',
    more: '更多',
    loading: '加载中...',
    noData: '暂无数据',
    success: '操作成功',
    failed: '操作失败',
    pleaseInput: '请输入',
    pleaseSelect: '请选择',
    view: '查看',
    contact: '联系',
    type: '类型',
    operation: '操作'
  },
  login: {
    title: '用户登录',
    username: '用户名',
    password: '密码',
    login: '登录',
    rememberMe: '记住我',
    forgotPassword: '忘记密码'
  },
  menu: {
    dashboard: '仪表盘',
    dataVisualization: '数据可视化',
    report: '报表中心',
    reportDesign: '报表设计',
    workflow: '工作流程',
    supplyChain: '供应链协同',
    settings: '系统设置'
  },
  dashboard: {
    title: '数据可视化仪表盘',
    enhancedTitle: '增强型数据仪表盘',
    salesTrend: '销售趋势',
    productDistribution: '产品分布',
    customerAnalysis: '客户分析',
    supplyChainStatus: '供应链状态',
    aiAnalysis: 'AI分析预测',
    insights: '数据洞察',
    increase: '增长',
    decrease: '下降',
    chartType: '图表类型',
    lineChart: '折线图',
    barChart: '柱状图',
    salesPrediction: '销售预测',
    actualSales: '实际销售',
    predictedSales: '预测销售',
    productCategory: '产品类别',
    dataRefreshed: '数据已刷新',
    refreshFailed: '刷新失败',
    aiAnalysisRefreshed: 'AI分析已更新',
    aiInsight1: '预计下个月销售额将增长15%，建议增加库存',
    aiInsight2: '产品A库存过高，建议促销清理',
    aiInsight3: '新客户增长率下降，建议加强营销活动',
    sales: '销售额',
    orders: '订单数',
    predictions: '未来预测',
    totalSales: '总销售额',
    totalOrders: '总订单数',
    totalCustomers: '总客户数',
    inventoryValue: '库存价值'
  },
  report: {
    title: '报表中心',
    createReport: '创建报表',
    reportName: '报表名称',
    reportType: '报表类型',
    createTime: '创建时间',
    updateTime: '更新时间',
    operation: '操作',
    preview: '预览',
    design: '设计',
    delete: '删除',
    confirmDelete: '确认删除该报表吗？',
    exportFormat: '导出格式',
    generateReport: '生成报表',
    paramConfig: '参数配置',
    columnConfig: '列配置',
    styleConfig: '样式配置',
    visualConfig: '可视化配置'
  },
  workflow: {
    title: '工作流程管理',
    createWorkflow: '创建工作流',
    workflowName: '工作流名称',
    workflowType: '工作流类型',
    status: '状态',
    createTime: '创建时间',
    updateTime: '更新时间',
    operation: '操作',
    edit: '编辑',
    delete: '删除',
    confirmDelete: '确认删除该工作流吗？',
    design: '设计',
    deploy: '部署',
    undeploy: '下线',
    customNode: '自定义节点',
    approvalNode: '审批节点',
    conditionNode: '条件节点',
    startNode: '开始节点',
    endNode: '结束节点',
    custom: {
      title: '自定义工作流',
      saveTemplate: '保存为模板',
      loadTemplate: '加载模板',
      exportXML: '导出XML',
      importXML: '导入XML',
      templateName: '模板名称',
      templateDesc: '模板描述',
      saveSuccess: '模板保存成功',
      loadSuccess: '模板加载成功',
      exportSuccess: 'XML导出成功',
      importSuccess: 'XML导入成功',
      validation: '工作流验证',
      validationSuccess: '工作流验证通过',
      validationFailed: '工作流验证失败'
    },
    nodeTypes: '节点类型',
    properties: '属性',
    nodeName: '节点名称',
    nodeDescription: '节点描述',
    nodeColor: '节点颜色',
    clear: '清空',
    confirmClear: '确认清空当前工作流？',
    saveSuccess: '工作流保存成功',
    deploySuccess: '工作流部署成功'
  },
  supplyChain: {
    title: '供应链协同平台',
    supplier: '供应商',
    inventory: '库存',
    logistics: '物流',
    procurement: '采购',
    production: '生产',
    distribution: '配送',
    tracking: '追踪',
    forecast: '预测',
    alert: '预警',
    collaboration: '协作',
    message: '消息',
    partnerManagement: '合作伙伴管理',
    partnerStatus: '合作伙伴状态',
    inventoryStatus: '库存状态',
    orderStatus: '订单状态',
    active: '活跃',
    inactive: '非活跃',
    pending: '待处理',
    completed: '已完成',
    processing: '处理中',
    cancelled: '已取消',
    inventoryValue: '库存价值'
  },
  settings: {
    title: '系统设置',
    userManagement: '用户管理',
    roleManagement: '角色管理',
    permissionManagement: '权限管理',
    systemConfig: '系统配置',
    logManagement: '日志管理',
    dataBackup: '数据备份',
    languageSettings: '语言设置'
  }
}

// 英文语言包
const en = {
  common: {
    confirm: 'Confirm',
    cancel: 'Cancel',
    save: 'Save',
    delete: 'Delete',
    edit: 'Edit',
    add: 'Add',
    search: 'Search',
    reset: 'Reset',
    back: 'Back',
    export: 'Export',
    import: 'Import',
    refresh: 'Refresh',
    more: 'More',
    loading: 'Loading...',
    noData: 'No Data',
    success: 'Operation Successful',
    failed: 'Operation Failed',
    pleaseInput: 'Please Input',
    pleaseSelect: 'Please Select',
    view: 'View',
    contact: 'Contact',
    type: 'Type',
    operation: 'Operation'
  },
  login: {
    title: 'User Login',
    username: 'Username',
    password: 'Password',
    login: 'Login',
    rememberMe: 'Remember Me',
    forgotPassword: 'Forgot Password'
  },
  menu: {
    dashboard: 'Dashboard',
    dataVisualization: 'Data Visualization',
    report: 'Report Center',
    reportDesign: 'Report Design',
    workflow: 'Workflow',
    supplyChain: 'Supply Chain',
    settings: 'Settings'
  },
  dashboard: {
    title: 'Data Visualization Dashboard',
    enhancedTitle: 'Enhanced Data Dashboard',
    salesTrend: 'Sales Trend',
    productDistribution: 'Product Distribution',
    customerAnalysis: 'Customer Analysis',
    supplyChainStatus: 'Supply Chain Status',
    aiAnalysis: 'AI Analysis & Prediction',
    insights: 'Data Insights',
    increase: 'Increase',
    decrease: 'Decrease',
    chartType: 'Chart Type',
    lineChart: 'Line Chart',
    barChart: 'Bar Chart',
    totalSales: 'Total Sales',
    totalOrders: 'Total Orders',
    totalCustomers: 'Total Customers',
    inventoryValue: 'Inventory Value',
    salesPrediction: 'Sales Prediction',
    actualSales: 'Actual Sales',
    predictedSales: 'Predicted Sales',
    productCategory: 'Product Category',
    dataRefreshed: 'Data Refreshed',
    refreshFailed: 'Refresh Failed',
    aiAnalysisRefreshed: 'AI Analysis Updated',
    aiInsight1: 'Sales expected to increase by 15% next month, consider increasing inventory',
    aiInsight2: 'Product A inventory is too high, consider promotions',
    aiInsight3: 'New customer growth rate is declining, strengthen marketing activities',
    sales: 'Sales',
    orders: 'Orders',
    predictions: 'Future Predictions'
  },
  report: {
    title: 'Report Center',
    createReport: 'Create Report',
    reportName: 'Report Name',
    reportType: 'Report Type',
    createTime: 'Create Time',
    updateTime: 'Update Time',
    operation: 'Operation',
    preview: 'Preview',
    design: 'Design',
    delete: 'Delete',
    confirmDelete: 'Confirm to delete this report?',
    exportFormat: 'Export Format',
    generateReport: 'Generate Report',
    paramConfig: 'Parameter Config',
    columnConfig: 'Column Config',
    styleConfig: 'Style Config',
    visualConfig: 'Visual Config'
  },
  workflow: {
    title: 'Workflow Management',
    createWorkflow: 'Create Workflow',
    workflowName: 'Workflow Name',
    workflowType: 'Workflow Type',
    status: 'Status',
    createTime: 'Create Time',
    updateTime: 'Update Time',
    operation: 'Operation',
    edit: 'Edit',
    delete: 'Delete',
    confirmDelete: 'Confirm to delete this workflow?',
    design: 'Design',
    deploy: 'Deploy',
    undeploy: 'Undeploy',
    customNode: 'Custom Node',
    approvalNode: 'Approval Node',
    conditionNode: 'Condition Node',
    startNode: 'Start Node',
    endNode: 'End Node',
    custom: {
      title: 'Custom Workflow',
      saveTemplate: 'Save as Template',
      loadTemplate: 'Load Template',
      exportXML: 'Export XML',
      importXML: 'Import XML',
      templateName: 'Template Name',
      templateDesc: 'Template Description',
      saveSuccess: 'Template saved successfully',
      loadSuccess: 'Template loaded successfully',
      exportSuccess: 'XML exported successfully',
      importSuccess: 'XML imported successfully',
      validation: 'Workflow Validation',
      validationSuccess: 'Workflow validation passed',
      validationFailed: 'Workflow validation failed'
    },
    nodeTypes: 'Node Types',
    properties: 'Properties',
    nodeName: 'Node Name',
    nodeDescription: 'Node Description',
    nodeColor: 'Node Color',
    clear: 'Clear',
    confirmClear: 'Confirm to clear current workflow?',
    saveSuccess: 'Workflow saved successfully',
    deploySuccess: 'Workflow deployed successfully'
  },
  supplyChain: {
    title: 'Supply Chain Collaboration Platform',
    supplier: 'Supplier',
    inventory: 'Inventory',
    logistics: 'Logistics',
    procurement: 'Procurement',
    production: 'Production',
    distribution: 'Distribution',
    tracking: 'Tracking',
    forecast: 'Forecast',
    alert: 'Alert',
    collaboration: 'Supply Chain Collaboration',
    message: 'Message',
    partnerManagement: 'Partner Management',
    partnerStatus: 'Partner Status',
    inventoryStatus: 'Inventory Status',
    orderStatus: 'Order Status',
    active: 'Active',
    inactive: 'Inactive',
    pending: 'Pending',
    completed: 'Completed',
    processing: 'Processing',
    cancelled: 'Cancelled',
    inventoryValue: 'Inventory Value'
  },
  settings: {
    title: 'System Settings',
    userManagement: 'User Management',
    roleManagement: 'Role Management',
    permissionManagement: 'Permission Management',
    systemConfig: 'System Config',
    logManagement: 'Log Management',
    dataBackup: 'Data Backup',
    languageSettings: 'Language Settings'
  }
}

export default createI18n({
  legacy: false,
  locale: 'zh',
  messages: {
    zh,
    en
  }
})