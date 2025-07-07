package com.chennian.storytelling.admin.config;


import brave.Span;
import brave.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会议系统专用的追踪拦截器
 */
@Component
class ConferenceTracingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ConferenceTracingInterceptor.class);

    @Autowired
    private Tracer tracer;

    @Autowired
    private TracingUtils tracingUtils;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();

        // 只对会议相关的请求进行特殊处理
        if (requestURI.contains("/conference/")) {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                // 添加会议系统特有的标签
                currentSpan.tag("http.url", requestURI);
                currentSpan.tag("http.method", request.getMethod());
                currentSpan.tag("service.name", "conference-service");
                currentSpan.tag("service.version", "1.0.0");

                // 提取房间ID（如果存在）
                String roomId = extractRoomId(requestURI);
                if (roomId != null) {
                    currentSpan.tag(CustomTracingTags.CONFERENCE_ROOM_ID, roomId);
                }

                // 提取用户ID（如果存在）
                String userId = request.getParameter("userId");
                if (userId != null) {
                    currentSpan.tag(CustomTracingTags.CONFERENCE_USER_ID, userId);
                }

                // 记录请求开始事件
                currentSpan.annotate("conference.request.start");
            }
        }

        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String requestURI = request.getRequestURI();

        if (requestURI.contains("/conference/")) {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                // 记录响应状态
                currentSpan.tag("http.status_code", String.valueOf(response.getStatus()));

                // 记录错误信息（如果有）
                if (ex != null) {
                    tracingUtils.recordError(ex);
                    currentSpan.tag(CustomTracingTags.CONFERENCE_ERROR_TYPE, ex.getClass().getSimpleName());
                }

                // 记录请求完成事件
                currentSpan.annotate("conference.request.complete");
            }
        }
    }

    /**
     * 从URI中提取房间ID
     */
    private String extractRoomId(String uri) {
        // 匹配 /conference/room/{roomId} 模式
        String pattern = "/conference/room/([^/]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(uri);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
