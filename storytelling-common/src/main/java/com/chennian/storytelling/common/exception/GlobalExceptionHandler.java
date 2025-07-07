package com.chennian.storytelling.common.exception;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

/**
 * 全局异常处理器
 *
 * @author by chennian
 * @date 2023/3/25 15:21
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ServerResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                    HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return ServerResponseEntity.showFailMsg(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ServerResponseEntity handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return ServerResponseEntity.showFailMsg(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ServerResponseEntity handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error(e.getMessage(), e);
        return ServerResponseEntity.showFailMsg(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ServerResponseEntity handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return ServerResponseEntity.showFailMsg(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ServerResponseEntity handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ServerResponseEntity.showFailMsg(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServerResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ServerResponseEntity.showFailMsg(message);
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e) {
        e.printStackTrace();
        return SaResult.error("您没有权限访问");
    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerException(NotRoleException e) {
        e.printStackTrace();
        return SaResult.error("您没有权限访问");
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ServerResponseEntity handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生空指针异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("系统繁忙，请重试");
    }

    /**
     * 数据库异常
     */
    @ExceptionHandler(DataAccessException.class)
    public ServerResponseEntity handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生数据库访问异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("数据库操作失败，请稍后重试");
    }

    /**
     * SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public ServerResponseEntity handleSQLException(SQLException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生SQL异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("数据库操作异常");
    }

    /**
     * 重复键异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ServerResponseEntity handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生重复键异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("数据已存在，请检查后重试");
    }

    /**
     * 缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ServerResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数异常", e);
        return ServerResponseEntity.showFailMsg("缺少必要的请求参数: " + e.getParameterName());
    }

    /**
     * 参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ServerResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配异常", e);
        return ServerResponseEntity.showFailMsg("参数类型错误: " + e.getName());
    }

    /**
     * HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ServerResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HTTP消息不可读异常", e);
        return ServerResponseEntity.showFailMsg("请求参数格式错误");
    }

    /**
     * 文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ServerResponseEntity handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传大小超限异常", e);
        return ServerResponseEntity.showFailMsg("上传文件大小超出限制");
    }

    /**
     * 约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ServerResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        log.error("约束违反异常", e);
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return ServerResponseEntity.showFailMsg(message);
    }

    /**
     * 网络连接异常
     */
    @ExceptionHandler(ConnectException.class)
    public ServerResponseEntity handleConnectException(ConnectException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生网络连接异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("网络连接失败，请检查网络状态");
    }

    /**
     * 网络超时异常
     */
    @ExceptionHandler(SocketTimeoutException.class)
    public ServerResponseEntity handleSocketTimeoutException(SocketTimeoutException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生网络超时异常", requestURI, e);
        return ServerResponseEntity.showFailMsg("网络请求超时，请稍后重试");
    }
}
