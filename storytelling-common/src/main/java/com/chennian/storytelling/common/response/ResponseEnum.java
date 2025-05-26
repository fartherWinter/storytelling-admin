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
    METHOD_ARGUMENT_NOT_VALID("A00014", "方法参数没有校验");

    private final String code;

    private final String msg;

    public String value() {
        return code;
    }

    public String getMsg() {
        return msg;
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
