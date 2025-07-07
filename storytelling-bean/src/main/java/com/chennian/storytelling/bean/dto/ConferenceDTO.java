package com.chennian.storytelling.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频会议相关DTO类
 * @author chen
 */
public class ConferenceDTO {

    /**
     * 创建会议房间请求DTO
     */
    @Data
    @ApiModel("创建会议房间请求")
    public static class CreateRoomRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "会议名称", required = true)
        @NotBlank(message = "会议名称不能为空")
        private String name;

        @ApiModelProperty("会议描述")
        private String description;

        @ApiModelProperty("是否私密会议")
        private Boolean isPrivate = false;

        @ApiModelProperty("私密会议密码")
        private String password;

        @ApiModelProperty("最大参与人数")
        private Integer maxParticipants = 50;

        @ApiModelProperty("会议开始时间")
        private LocalDateTime startTime;

        @ApiModelProperty("会议结束时间")
        private LocalDateTime endTime;

        @ApiModelProperty("会议标签")
        private List<String> tags;

        @ApiModelProperty("是否录制")
        private Boolean recordEnabled = false;
    }

    /**
     * 会议房间信息响应DTO
     */
    @Data
    @ApiModel("会议房间信息")
    public static class RoomInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("房间ID")
        private String roomId;

        @ApiModelProperty("会议名称")
        private String name;

        @ApiModelProperty("会议描述")
        private String description;

        @ApiModelProperty("是否私密会议")
        private Boolean isPrivate;

        @ApiModelProperty("当前参与人数")
        private Integer activeUsers;

        @ApiModelProperty("最大参与人数")
        private Integer maxParticipants;

        @ApiModelProperty("会议状态")
        private String status; // WAITING, ACTIVE, ENDED

        @ApiModelProperty("创建时间")
        private LocalDateTime createdAt;

        @ApiModelProperty("会议开始时间")
        private LocalDateTime startTime;

        @ApiModelProperty("会议结束时间")
        private LocalDateTime endTime;

        @ApiModelProperty("会议标签")
        private List<String> tags;

        @ApiModelProperty("是否录制")
        private Boolean recordEnabled;

        @ApiModelProperty("参与者列表")
        private List<ParticipantInfo> participants;
    }

    /**
     * 参与者信息DTO
     */
    @Data
    @ApiModel("参与者信息")
    public static class ParticipantInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("参与者ID")
        private String peerId;

        @ApiModelProperty("显示名称")
        private String displayName;

        @ApiModelProperty("用户ID")
        private String userId;

        @ApiModelProperty("用户名称")
        private String username;


        @ApiModelProperty("头像URL")
        private String avatarUrl;

        @ApiModelProperty("加入时间")
        private LocalDateTime joinTime;

        @ApiModelProperty("是否主持人")
        private Boolean isHost = false;

        @ApiModelProperty("音频状态")
        private Boolean audioEnabled = true;

        @ApiModelProperty("视频状态")
        private Boolean videoEnabled = true;

        @ApiModelProperty("屏幕共享状态")
        private Boolean screenShareEnabled = false;

        @ApiModelProperty("参与者角色")
        private String role = "PARTICIPANT"; // HOST, MODERATOR, PARTICIPANT
    }

    /**
     * 加入会议请求DTO
     */
    @Data
    @ApiModel("加入会议请求")
    public static class JoinRoomRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "房间ID", required = true)
        @NotBlank(message = "房间ID不能为空")
        private String roomId;

        @ApiModelProperty(value = "显示名称", required = true)
        @NotBlank(message = "显示名称不能为空")
        private String displayName;

        @ApiModelProperty("用户ID")
        private String userId;

        @ApiModelProperty("用户名称")
        private String username;

        @ApiModelProperty("头像URL")
        private String avatarUrl;

        @ApiModelProperty("私密会议密码")
        private String password;

        @ApiModelProperty("初始音频状态")
        private Boolean audioEnabled = true;

        @ApiModelProperty("初始视频状态")
        private Boolean videoEnabled = true;
    }

    /**
     * 验证密码请求DTO
     */
    @Data
    @ApiModel("验证密码请求")
    public static class VerifyPasswordRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "密码", required = true)
        @NotBlank(message = "密码不能为空")
        private String password;
    }

    /**
     * 会议列表查询请求DTO
     */
    @Data
    @ApiModel("会议列表查询请求")
    public static class RoomListRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("页码")
        private Integer page = 1;

        @ApiModelProperty("每页大小")
        private Integer size = 10;

        @ApiModelProperty("搜索关键词")
        private String keyword;

        @ApiModelProperty("会议状态")
        private String status;

        @ApiModelProperty("标签过滤")
        private List<String> tags;

        @ApiModelProperty("是否只显示我创建的")
        private Boolean onlyMine = false;

        @ApiModelProperty("是否只显示活跃的")
        private Boolean onlyActive = false;

        @ApiModelProperty("排序字段")
        private String sortBy = "createdAt"; // createdAt, activeUsers, title

        @ApiModelProperty("是否升序排列")
        private Boolean ascending = false;
    }

    /**
     * 会议统计信息DTO
     */
    @Data
    @ApiModel("会议统计信息")
    public static class ConferenceStats implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("总会议数")
        private Long totalRooms;

        @ApiModelProperty("活跃会议数")
        private Long activeRooms;

        @ApiModelProperty("总参与人数")
        private Long totalParticipants;

        @ApiModelProperty("今日新建会议数")
        private Long todayNewRooms;

        @ApiModelProperty("平均会议时长（分钟）")
        private Double avgDuration;

        @ApiModelProperty("最受欢迎的标签")
        private List<String> popularTags;
    }

    /**
     * 会议操作请求DTO
     */
    @Data
    @ApiModel("会议操作请求")
    public static class RoomOperationRequest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "操作类型", required = true)
        @NotBlank(message = "操作类型不能为空")
        private String operation; // START, END, PAUSE, RESUME, KICK_USER, MUTE_USER, etc.

        @ApiModelProperty("目标用户ID（踢出用户、静音用户时使用）")
        private String targetUserId;

        @ApiModelProperty("操作原因")
        private String reason;
    }
}