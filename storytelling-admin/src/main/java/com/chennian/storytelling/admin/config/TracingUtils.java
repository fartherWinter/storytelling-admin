package com.chennian.storytelling.admin.config;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 追踪工具类
 * 提供便捷的追踪操作方法
 */
@Component
class TracingUtils {

    private static final Logger log = LoggerFactory.getLogger(TracingUtils.class);

    /**
     * 开始一个新的追踪span
     */
    public Span startSpan(String operationName) {
        return tracer.nextSpan().name(operationName).start();
    }

    /**
     * 为当前span添加标签
     */
    public void addTag(String key, String value) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag(key, value);
        }
    }

    /**
     * 为当前span添加事件
     */
    public void addEvent(String event) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.annotate(event);
        }
    }

    /**
     * 记录错误信息
     */
    public void recordError(Throwable throwable) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag("error", throwable.getMessage());
            currentSpan.tag("error.type", throwable.getClass().getSimpleName());
        }
    }

    /**
     * 完成当前span
     */
    public void finishSpan() {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.finish();
        }
    }

    /**
     * 在指定span上下文中执行操作
     */
    public <T> T executeInSpan(String spanName, Supplier<T> operation) {
        Span span = startSpan(spanName);
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return operation.get();
        } catch (Exception e) {
            recordError(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    /**
     * 异步执行操作并传播追踪上下文
     */
    public CompletableFuture<Void> executeAsync(String spanName, Runnable operation) {
        Span span = startSpan(spanName);
        TraceContext traceContext = span.context();

        return CompletableFuture.runAsync(() -> {
            try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
                operation.run();
            } catch (Exception e) {
                recordError(e);
                throw e;
            } finally {
                span.finish();
            }
        });
    }

    @Autowired
    private Tracer tracer;
}
