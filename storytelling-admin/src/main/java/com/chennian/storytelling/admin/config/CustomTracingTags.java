package com.chennian.storytelling.admin.config;

import org.springframework.context.annotation.Configuration;

/**
 * 自定义追踪标签配置
 */
@Configuration
class CustomTracingTags {

    /**
     * 添加自定义标签到追踪信息中
     */
    public static final String CONFERENCE_ROOM_ID = "conference.room.id";
    public static final String CONFERENCE_USER_ID = "conference.user.id";
    public static final String CONFERENCE_OPERATION = "conference.operation";
    public static final String CONFERENCE_PARTICIPANT_COUNT = "conference.participant.count";
    public static final String CONFERENCE_ROOM_TYPE = "conference.room.type";
    public static final String CONFERENCE_ERROR_TYPE = "conference.error.type";
    public static final String CONFERENCE_PERFORMANCE_METRIC = "conference.performance.metric";
    public static final String CONFERENCE_CACHE_HIT = "conference.cache.hit";
    public static final String CONFERENCE_ASYNC_TASK_ID = "conference.async.task.id";
    public static final String CONFERENCE_REQUEST_SOURCE = "conference.request.source";
}
