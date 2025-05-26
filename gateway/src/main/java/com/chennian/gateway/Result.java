package com.chennian.gateway;


import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 导入错误
     */
    public static final int IMPORT_ERROR = 600;

    public Result() {
        put("code", 200);
        put("msg", "success");
    }

    public static Result error201(String message) {
        return error(201, message);
    }

    public static Result error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }

    public static Result okData(Object data) {
        Result r = new Result();
        r.put("data", data);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    public static Result fail(SystemErrorType gatewayError) {
        Result r = new Result();
        r.put("code", gatewayError.getCode());
        r.put("msg", gatewayError.getMesg());
        return r;
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
