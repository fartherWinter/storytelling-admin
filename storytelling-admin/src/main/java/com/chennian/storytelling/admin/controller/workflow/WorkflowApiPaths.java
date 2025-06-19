package com.chennian.storytelling.admin.controller.workflow;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 工作流API路径常量（增强版）
 * 统一管理所有工作流相关的API路径，提供路径验证、类型安全构建等功能
 * 
 * @author chennian
 */
public final class WorkflowApiPaths {

    private WorkflowApiPaths() {
        // 工具类，禁止实例化
    }

    // 版本管理
    public static final String API_VERSION = "v1";
    public static final String BASE = "/sys/" + API_VERSION + "/workflow";

    // 参数验证正则表达式
    private static final Pattern TASK_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]{1,64}$");
    private static final Pattern PROCESS_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]{1,64}$");
    private static final Pattern MODEL_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]{1,64}$");
    private static final Pattern DEFINITION_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9-_:]{1,64}$");

    /**
     * 任务相关路径
     */
    public static final class TaskPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/tasks";
        public static final String TODO = BASE + "/todo";
        public static final String BY_PROCESS_INSTANCE = BASE + "/process/{processInstanceId}";
        public static final String COMPLETE = BASE + "/{taskId}/complete";
        public static final String APPROVE = BASE + "/{taskId}/approve";
        public static final String REJECT = BASE + "/{taskId}/reject";
        public static final String SEARCH = BASE + "/search";
        public static final String DETAIL = BASE + "/{taskId}/detail";
        public static final String DELEGATE = BASE + "/{taskId}/delegate";
        public static final String CLAIM = BASE + "/{taskId}/claim";
        public static final String TRANSFER = BASE + "/{taskId}/transfer";
        public static final String HISTORY = BASE + "/history";
        public static final String STATISTICS = BASE + "/statistics";
        public static final String BATCH = BASE + "/batch";
    }

    /**
     * 流程相关路径
     */
    public static final class ProcessPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/processes";
        public static final String SEARCH = BASE + "/search";
        public static final String START = BASE + "/start";
        public static final String DETAIL = BASE + "/{processInstanceId}";
        public static final String DIAGRAM = BASE + "/{processInstanceId}/diagram";
        public static final String TERMINATE = BASE + "/{processInstanceId}/terminate";
        public static final String SUSPEND = BASE + "/{processInstanceId}/suspend";
        public static final String ACTIVATE = BASE + "/{processInstanceId}/activate";
        public static final String BATCH = BASE + "/batch";
        public static final String VARIABLES = BASE + "/{processInstanceId}/variables";
        public static final String VARIABLE_GET = BASE + "/{processInstanceId}/variables/{variableName}";
        public static final String VARIABLE_SET = BASE + "/{processInstanceId}/variables/{variableName}";
        public static final String HISTORY = BASE + "/{processInstanceId}/history";
        public static final String JUMP = BASE + "/{processInstanceId}/jump";
        public static final String ROLLBACK = BASE + "/{processInstanceId}/rollback";
    }

    /**
     * 核心工作流操作路径
     */
    public static final class CorePaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/core";
        public static final String DEPLOY = BASE + "/deploy";
        public static final String START = BASE + "/start";
        public static final String DEPLOY_PROCESS = BASE + "/deploy";
        public static final String START_PROCESS = BASE + "/start";
        public static final String COMPLETE_TASK = BASE + "/tasks/{taskId}/complete";
        public static final String TODO_TASKS = BASE + "/tasks/todo";
        public static final String PROCESS_TASKS = BASE + "/processes/{processInstanceId}/tasks";
        public static final String PROCESS_HISTORY = BASE + "/processes/{processInstanceId}/history";
        public static final String USER_HISTORY_TASKS = BASE + "/tasks/history";
        public static final String WORKFLOW_STATISTICS = BASE + "/statistics";
    }

    /**
     * 流程定义管理路径
     */
    public static final class DefinitionPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/definitions";
        public static final String LIST = BASE;
        public static final String PAGE = BASE + "/page";
        public static final String ENHANCED = BASE + "/enhanced";
        public static final String DETAIL = BASE + "/{definitionId}";
        public static final String RESOURCE = BASE + "/{processDefinitionId}/resource";
        public static final String DEPLOY = BASE + "/deploy";
        public static final String DEPLOY_FROM_MODEL = BASE + "/deploy-from-model";
        public static final String SUSPEND = BASE + "/{definitionId}/suspend";
        public static final String ACTIVATE = BASE + "/{definitionId}/activate";
        public static final String DELETE = BASE + "/{definitionId}";
    }

    /**
     * 监控统计路径
     */
    public static final class MonitorPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/monitor";
        public static final String DASHBOARD = BASE + "/dashboard";
        public static final String STATISTICS = BASE + "/statistics";
        public static final String PERFORMANCE = BASE + "/performance";
        public static final String WORKLOAD = BASE + "/workload";
        public static final String EXCEPTIONS = BASE + "/exceptions";
        public static final String EXPORT = BASE + "/export";
    }

    /**
     * 流程设计器路径
     */
    public static final class DesignerPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/designer";
        public static final String MODELS = BASE + "/models";
        public static final String MODEL_DATA = BASE + "/model/{modelId}";
        public static final String MODEL_XML = BASE + "/model/{modelId}/xml";
        public static final String DEPLOY = BASE + "/deploy/{modelId}";
    }

    /**
     * 动态表单路径
     */
    public static final class FormPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/forms";
        public static final String TEMPLATES = BASE + "/templates";
        public static final String TEMPLATE_DETAIL = BASE + "/templates/{templateId}";
        public static final String TEMPLATE_CREATE = BASE + "/templates";
        public static final String TEMPLATE_UPDATE = BASE + "/templates/{templateId}";
        public static final String TEMPLATE_DELETE = BASE + "/templates/{templateId}";
    }

    /**
     * 工作流模型路径
     */
    public static final class ModelPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/models";
        public static final String CREATE = BASE;
        public static final String UPDATE = BASE + "/{modelId}";
        public static final String LIST = BASE;
        public static final String DETAIL = BASE + "/{modelId}";
        public static final String XML = BASE + "/{modelId}/xml";
        public static final String DEPLOY = BASE + "/{modelId}/deploy";
    }

    /**
     * 报表分析路径
     */
    public static final class ReportPaths {
        public static final String BASE = WorkflowApiPaths.BASE + "/reports";
        public static final String EFFICIENCY = BASE + "/process-efficiency";
        public static final String WORKLOAD = BASE + "/workload";
        public static final String QUALITY = BASE + "/quality";
        public static final String COST = BASE + "/cost";
        public static final String COMPREHENSIVE = BASE + "/comprehensive";
    }

    // 保持向后兼容的原有路径常量
    public static final String TASKS = TaskPaths.BASE;
    public static final String TASK_DELEGATE = TaskPaths.DELEGATE;
    public static final String TASK_CLAIM = TaskPaths.CLAIM;
    public static final String TASK_TRANSFER = TaskPaths.TRANSFER;
    public static final String PROCESSES = ProcessPaths.BASE;
    public static final String DEFINITIONS = DefinitionPaths.BASE;
    public static final String MONITOR = MonitorPaths.BASE;
    public static final String DESIGNER = DesignerPaths.BASE;
    public static final String FORMS = FormPaths.BASE;
    public static final String MODELS = ModelPaths.BASE;
    public static final String REPORTS = ReportPaths.BASE;

    /**
     * 参数验证工具
     */
    public static final class Validator {
        
        public static boolean isValidTaskId(String taskId) {
            return taskId != null && TASK_ID_PATTERN.matcher(taskId).matches();
        }

        public static boolean isValidProcessInstanceId(String processInstanceId) {
            return processInstanceId != null && PROCESS_ID_PATTERN.matcher(processInstanceId).matches();
        }

        public static boolean isValidModelId(String modelId) {
            return modelId != null && MODEL_ID_PATTERN.matcher(modelId).matches();
        }

        public static boolean isValidDefinitionId(String definitionId) {
            return definitionId != null && DEFINITION_ID_PATTERN.matcher(definitionId).matches();
        }

        public static void validateTaskId(String taskId) {
            if (!isValidTaskId(taskId)) {
                throw new IllegalArgumentException("Invalid task ID: " + taskId);
            }
        }

        public static void validateProcessInstanceId(String processInstanceId) {
            if (!isValidProcessInstanceId(processInstanceId)) {
                throw new IllegalArgumentException("Invalid process instance ID: " + processInstanceId);
            }
        }

        public static void validateModelId(String modelId) {
            if (!isValidModelId(modelId)) {
                throw new IllegalArgumentException("Invalid model ID: " + modelId);
            }
        }

        public static void validateDefinitionId(String definitionId) {
            if (!isValidDefinitionId(definitionId)) {
                throw new IllegalArgumentException("Invalid definition ID: " + definitionId);
            }
        }
    }

    /**
     * 类型安全的路径构建工具
     */
    public static final class Builder {
        
        public static String buildTaskPath(String taskId, String action) {
            Validator.validateTaskId(taskId);
            return TaskPaths.BASE + "/" + taskId + "/" + action;
        }

        public static String buildProcessPath(String processInstanceId, String action) {
            Validator.validateProcessInstanceId(processInstanceId);
            return ProcessPaths.BASE + "/" + processInstanceId + "/" + action;
        }

        public static String buildModelPath(String modelId, String action) {
            Validator.validateModelId(modelId);
            return ModelPaths.BASE + "/" + modelId + "/" + action;
        }

        public static String buildDefinitionPath(String definitionId, String action) {
            Validator.validateDefinitionId(definitionId);
            return DefinitionPaths.BASE + "/" + definitionId + "/" + action;
        }

        /**
         * 带查询参数的路径构建
         */
        public static String buildPathWithQuery(String basePath, Map<String, String> queryParams) {
            if (queryParams == null || queryParams.isEmpty()) {
                return basePath;
            }
            
            StringBuilder sb = new StringBuilder(basePath).append("?");
            queryParams.forEach((key, value) -> {
                if (key != null && value != null) {
                    sb.append(key).append("=").append(value).append("&");
                }
            });
            
            String result = sb.toString();
            return result.endsWith("&") ? result.substring(0, result.length() - 1) : result;
        }

        /**
         * 安全的路径参数替换
         */
        public static String replacePath(String path, String paramName, String paramValue) {
            if (path == null || paramName == null || paramValue == null) {
                throw new IllegalArgumentException("Path, parameter name and value cannot be null");
            }
            return path.replace("{" + paramName + "}", paramValue);
        }

        /**
         * 构建带参数的路径（按顺序替换）
         */
        public static String buildPath(String basePath, Object... params) {
            if (basePath == null) {
                throw new IllegalArgumentException("Base path cannot be null");
            }
            
            String result = basePath;
            int paramIndex = 0;
            
            while (result.contains("{") && paramIndex < params.length) {
                int start = result.indexOf("{");
                int end = result.indexOf("}", start);
                if (end > start) {
                    String paramValue = params[paramIndex] != null ? params[paramIndex].toString() : "";
                    result = result.substring(0, start) + paramValue + result.substring(end + 1);
                    paramIndex++;
                } else {
                    break;
                }
            }
            
            return result;
        }
    }

    /**
     * API路径元数据
     */
    public static final class ApiMetadata {
        
        public static final Map<String, String> PATH_DESCRIPTIONS = new HashMap<>();
        public static final Map<String, String[]> PATH_METHODS = new HashMap<>();
        
        static {
            // 任务相关描述
            PATH_DESCRIPTIONS.put(TaskPaths.DELEGATE, "委派任务给其他用户");
            PATH_DESCRIPTIONS.put(TaskPaths.CLAIM, "认领任务");
            PATH_DESCRIPTIONS.put(TaskPaths.TRANSFER, "转办任务");
            PATH_DESCRIPTIONS.put(TaskPaths.SEARCH, "搜索任务");
            PATH_DESCRIPTIONS.put(TaskPaths.BATCH, "批量操作任务");
            
            // 流程相关描述
            PATH_DESCRIPTIONS.put(ProcessPaths.TERMINATE, "终止流程实例");
            PATH_DESCRIPTIONS.put(ProcessPaths.SUSPEND, "挂起流程实例");
            PATH_DESCRIPTIONS.put(ProcessPaths.ACTIVATE, "激活流程实例");
            PATH_DESCRIPTIONS.put(ProcessPaths.START, "启动流程实例");
            PATH_DESCRIPTIONS.put(ProcessPaths.BATCH, "批量操作流程实例");
            
            // HTTP方法映射
            PATH_METHODS.put(TaskPaths.DELEGATE, new String[]{"POST"});
            PATH_METHODS.put(TaskPaths.CLAIM, new String[]{"POST"});
            PATH_METHODS.put(TaskPaths.TRANSFER, new String[]{"POST"});
            PATH_METHODS.put(TaskPaths.SEARCH, new String[]{"GET", "POST"});
            PATH_METHODS.put(TaskPaths.BASE, new String[]{"GET", "POST"});
            PATH_METHODS.put(ProcessPaths.START, new String[]{"POST"});
            PATH_METHODS.put(ProcessPaths.TERMINATE, new String[]{"POST"});
            PATH_METHODS.put(ProcessPaths.BASE, new String[]{"GET"});
        }
        
        public static String getDescription(String path) {
            return PATH_DESCRIPTIONS.getOrDefault(path, "工作流操作");
        }
        
        public static String[] getMethods(String path) {
            return PATH_METHODS.getOrDefault(path, new String[]{"GET"});
        }
    }
}