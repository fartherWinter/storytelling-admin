package com.chennian.storytelling.common.response;


import com.chennian.storytelling.common.enums.InventoryErrorCode;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

import com.chennian.storytelling.common.enums.WorkflowResponseEnum;

/**
 * 响应实体类
 *
 * @author by chennian
 * @date 2023/3/14 14:29
 */
@Slf4j
public class ServerResponseEntity<T> implements Serializable {
    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 版本
     */
    private String version;

    /**
     * 时间
     */
    private Long timestamp;

    private String sign;



    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public ServerResponseEntity setData(T data) {
        this.data = data;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return Objects.equals(ResponseEnum.OK.value(), this.code);
    }

    public boolean isFail() {
        return !Objects.equals(ResponseEnum.OK.value(), this.code);
    }

    public ServerResponseEntity() {
        // 版本号
        this.version = "storytelling";
    }

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(ResponseEnum.OK.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success(String code, String msg, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(code);
        serverResponseEntity.setMsg(msg);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseEnum.OK.value());
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success(Integer code, T data) {
        return success(String.valueOf(code), data);
    }

    public static <T> ServerResponseEntity<T> success(String code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success(String code, String msg) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setMsg(msg);
        return serverResponseEntity;
    }

    /**
     * 前端显示失败消息
     *
     * @param msg 失败消息
     * @return
     */
    public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.value());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(MallResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMessage());
        serverResponseEntity.setCode(String.valueOf(responseEnum.getCode()));
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(WorkflowResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMessage());
        serverResponseEntity.setCode(String.valueOf(responseEnum.getCode()));
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(InventoryErrorCode responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMessage());
        serverResponseEntity.setCode(String.valueOf(responseEnum.getCode()));
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.value());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg, T data) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> ServerResponseEntity<T> fail(Integer code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(String.valueOf(code));
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(int code, String msg) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(String.valueOf(code));
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(int code, String msg, T data) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(String.valueOf(code));
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    @Override
    public String toString() {
        return "ServerResponseEntity{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }
}
