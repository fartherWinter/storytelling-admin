package com.chennian.storytelling.common.response;

/**
 * 响应枚举
 *
 * @author by chennian
 * @date 2023/3/14 14:28
 */
public enum ResponseEnum {
    /**
     * ok
     */
    OK("200", "ok"),

    SHOW_FAIL("503", ""),

    /**
     * 用于直接显示提示用户的错误，内容由输入内容决定
     */

    /**
     * 用于直接显示提示系统的成功，内容由输入内容决定
     */
    SHOW_SUCCESS("A00002", ""),

    /**
     * 未授权
     */
    UNAUTHORIZED("A00004", "Unauthorized"),

    /**
     * 服务器出了点小差
     */
    EXCEPTION("A00005", "服务器出了点小差"),

    /**
     * 方法参数没有校验，内容由输入内容决定
     */
    METHOD_ARGUMENT_NOT_VALID("A00014", "方法参数没有校验"),

    /**
     * 工作流相关错误
     */
    WORKFLOW_ERROR("W00001", "工作流操作失败"),

    /**
     * 流程实例不存在
     */
    PROCESS_INSTANCE_NOT_FOUND("W00002", "流程实例不存在"),

    /**
     * 流程定义不存在
     */
    PROCESS_DEFINITION_NOT_FOUND("W00003", "流程定义不存在"),

    /**
     * 任务不存在
     */
    TASK_NOT_FOUND("W00004", "任务不存在"),

    /**
     * 流程模型不存在
     */
    WORKFLOW_MODEL_NOT_FOUND("W00005", "流程模型不存在"),

    /**
     * 流程部署失败
     */
    WORKFLOW_DEPLOY_FAILED("W00006", "流程部署失败"),

    /**
     * 流程启动失败
     */
    WORKFLOW_START_FAILED("W00007", "流程启动失败"),

    /**
     * 任务完成失败
     */
    TASK_COMPLETE_FAILED("W00008", "任务完成失败"),

    /**
     * 任务认领失败
     */
    TASK_CLAIM_FAILED("W00009", "任务认领失败"),

    /**
     * 流程变量操作失败
     */
    PROCESS_VARIABLE_FAILED("W00010", "流程变量操作失败"),

    /**
     * 流程图生成失败
     */
    PROCESS_DIAGRAM_FAILED("W00011", "流程图生成失败"),

    /**
     * 流程资源不存在
     */
    PROCESS_RESOURCE_NOT_FOUND("W00012", "流程资源不存在"),

    /**
     * 流程模型转换失败
     */
    WORKFLOW_MODEL_CONVERT_FAILED("W00013", "流程模型转换失败"),

    /**
     * 流程模型保存失败
     */
    WORKFLOW_MODEL_SAVE_FAILED("W00014", "流程模型保存失败"),

    /**
     * 流程权限不足
     */
    WORKFLOW_PERMISSION_DENIED("W00015", "流程权限不足"),

    /**
     * 流程状态异常
     */
    WORKFLOW_STATUS_ERROR("W00016", "流程状态异常");

    private final String code;

    private final String msg;

    public String value() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseEnum{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + "} " + super.toString();
    }
}
