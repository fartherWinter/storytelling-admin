package com.chennian.storytelling.admin.controller.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> handleWorkflowException(WorkflowException e) {
        log.error("工作流业务异常: {}", e.getMessage(), e);
        return ResponseEntity.ok(WorkflowResponse.error(e.getMessage()));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception e) {
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
        return ResponseEntity.ok(WorkflowResponse.error("参数验证失败: " + errorMessage));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常: {}", e.getMessage());
        return ResponseEntity.ok(WorkflowResponse.error("参数错误: " + e.getMessage()));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return ResponseEntity.ok(WorkflowResponse.error("系统内部错误，请联系管理员"));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return ResponseEntity.ok(WorkflowResponse.error("操作失败: " + e.getMessage()));
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.ok(WorkflowResponse.error("系统异常，请联系管理员"));
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