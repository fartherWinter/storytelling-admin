package com.chennian.storytelling.admin.controller.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.response.ResponseEnum;
import java.util.stream.Collectors;

/**
 * 工作流统一异常处理器
 * 提供标准化的异常处理和响应格式
 * 
 * @author chennian
 */
@Slf4j
@ControllerAdvice(basePackages = "com.chennian.storytelling.admin.controller.workflow")
public class WorkflowExceptionHandler {

    /**
     * 处理工作流业务异常
     */
    @ExceptionHandler(WorkflowException.class)
    public ServerResponseEntity<Void> handleWorkflowException(WorkflowException e) {
        log.error("工作流异常: {}", e.getMessage(), e);
        return ServerResponseEntity.fail(ResponseEnum.WORKFLOW_ERROR);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ServerResponseEntity<Void> handleValidationException(Exception e) {
        String errorMessage;
        
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            errorMessage = validException.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            errorMessage = bindException.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        } else {
            errorMessage = "参数验证失败";
        }
        
        log.warn("参数验证异常: {}", errorMessage);
        return ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ServerResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage());
        return ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ServerResponseEntity<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ServerResponseEntity<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return ServerResponseEntity.fail(ResponseEnum.WORKFLOW_ERROR);
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ServerResponseEntity<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
    }

    /**
     * 工作流业务异常类
     */
    public static class WorkflowException extends RuntimeException {
        public WorkflowException(String message) {
            super(message);
        }
        
        public WorkflowException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}