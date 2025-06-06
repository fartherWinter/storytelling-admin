package com.chennian.storytelling.common.utils.alisignsdk;

import com.chennian.storytelling.common.utils.alisignsdk.enums.Method;

import java.util.List;
import java.util.Map;

/**
 * @author by chennian
 * @date 2023/5/18 15:34
 */
public class Request {
    public Request() {
    }

    public Request(Method method, String host, String path, String appKey, String appSecret, int timeout) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.timeout = timeout;
    }

    /**
     * （必选）请求方法
     */
    private Method method;

    /**
     * （必选）Host
     */
    private String host;

    /**
     * （必选）Path
     */
    private String path;

    /**
     * （必选)APP KEY
     */
    private String appKey;

    /**
     * （必选）APP密钥
     */
    private String appSecret;

    /**
     * （必选）超时时间,单位毫秒,设置零默认使用com.aliyun.apigateway.demo.constant.Constants.DEFAULT_TIMEOUT
     */
    private int timeout;

    /**
     * （可选） HTTP头
     */
    private Map<String, String> headers;

    /**
     * （可选） Querys
     */
    private Map<String, String> querys;

    /**
     * （可选）表单参数
     */
    private Map<String, String> bodys;

    /**
     * （可选）字符串Body体
     */
    private String stringBody;

    /**
     * （可选）字节数组类型Body体
     */
    private byte[] bytesBody;

    /**
     * （可选）自定义参与签名Header前缀
     */
    private List<String> signHeaderPrefixList;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getQuerys() {
        return querys;
    }

    public void setQuerys(Map<String, String> querys) {
        this.querys = querys;
    }

    public Map<String, String> getBodys() {
        return bodys;
    }

    public void setBodys(Map<String, String> bodys) {
        this.bodys = bodys;
    }

    public String getStringBody() {
        return stringBody;
    }

    public void setStringBody(String stringBody) {
        this.stringBody = stringBody;
    }

    public byte[] getBytesBody() {
        return bytesBody;
    }

    public void setBytesBody(byte[] bytesBody) {
        this.bytesBody = bytesBody;
    }

    public List<String> getSignHeaderPrefixList() {
        return signHeaderPrefixList;
    }

    public void setSignHeaderPrefixList(List<String> signHeaderPrefixList) {
        this.signHeaderPrefixList = signHeaderPrefixList;
    }
}
