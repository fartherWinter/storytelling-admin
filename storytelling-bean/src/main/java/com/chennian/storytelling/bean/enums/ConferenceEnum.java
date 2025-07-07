package com.chennian.storytelling.bean.enums;

/**
 * 会议相关枚举类
 * @author chen
 */
public class ConferenceEnum {

    /**
     * 会议房间状态枚举
     */
    public enum RoomStatus {
        WAITING("0", "等待中"),
        ACTIVE("1", "进行中"),
        PAUSED("2", "已暂停"),
        ENDED("3", "已结束"),
        CANCELLED("4", "已取消"),
        CLOSED("5", "已关闭");

        private final String code;
        private final String name;

        RoomStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static RoomStatus getByCode(String code) {
            for (RoomStatus status : values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static RoomStatus getByCode(Integer code) {
            return code != null ? getByCode(code.toString()) : null;
        }

        public static String getNameByCode(String code) {
            RoomStatus status = getByCode(code);
            return status != null ? status.getName() : null;
        }

        public static String getNameByCode(Integer code) {
            return getNameByCode(code != null ? code.toString() : null);
        }

        public boolean isActive() {
            return this == ACTIVE;
        }

        public boolean isEnded() {
            return this == ENDED || this == CANCELLED;
        }
    }

    /**
     * 会议操作类型枚举
     */
    public enum OperationType {
        START("0", "开始会议"),
        END("1", "结束会议"),
        PAUSE("2", "暂停会议"),
        RESUME("3", "恢复会议"),
        KICK_USER("4", "踢出用户"),
        MUTE_USER("5", "静音用户"),
        UNMUTE_USER("6", "取消静音"),
        DISABLE_VIDEO("7", "禁用视频"),
        ENABLE_VIDEO("8", "启用视频"),
        START_RECORDING("9", "开始录制"),
        STOP_RECORDING("10", "停止录制"),
        LOCK_ROOM("11", "锁定房间"),
        UNLOCK_ROOM("12", "解锁房间"),
        SET_ROLE("13", "设置角色"),
        TOGGLE_MEDIA("14", "切换媒体状态"),
        SEND_MESSAGE("15", "发送消息"),
        CLOSE_ROOM("16", "关闭房间"),
        TOGGLE_RECORDING("17", "切换录制状态"),
        SET_QUALITY("18", "设置质量");

        private final String code;
        private final String name;

        OperationType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static OperationType getByCode(String code) {
            for (OperationType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }

        public static OperationType getByCode(Integer code) {
            return code != null ? getByCode(code.toString()) : null;
        }

        public static String getNameByCode(String code) {
            OperationType type = getByCode(code);
            return type != null ? type.getName() : null;
        }

        public static String getNameByCode(Integer code) {
            return getNameByCode(code != null ? code.toString() : null);
        }
    }

    /**
     * 参与者角色枚举
     */
    public enum ParticipantRole {
        HOST("0", "主持人"),
        CO_HOST("1", "联合主持人"),
        MODERATOR("2", "管理员"),
        PRESENTER("3", "演示者"),
        PARTICIPANT("4", "参与者"),
        OBSERVER("5", "观察者");

        private final String code;
        private final String name;

        ParticipantRole(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static ParticipantRole getByCode(String code) {
            for (ParticipantRole role : values()) {
                if (role.getCode().equals(code)) {
                    return role;
                }
            }
            return null;
        }

        public static ParticipantRole getByCode(Integer code) {
            return code != null ? getByCode(code.toString()) : null;
        }

        public static String getNameByCode(String code) {
            ParticipantRole role = getByCode(code);
            return role != null ? role.getName() : null;
        }

        public static String getNameByCode(Integer code) {
            return getNameByCode(code != null ? code.toString() : null);
        }

        public boolean isHost() {
            return this == HOST || this == CO_HOST;
        }

        public boolean canManageRoom() {
            return this == HOST || this == CO_HOST || this == MODERATOR;
        }

        public boolean canPresent() {
            return this == HOST || this == CO_HOST || this == MODERATOR || this == PRESENTER;
        }
    }

    /**
     * 媒体类型枚举
     */
    public enum MediaType {
        AUDIO("0", "音频"),
        VIDEO("1", "视频"),
        SCREEN_SHARE("2", "屏幕共享"),
        WHITEBOARD("3", "白板"),
        FILE_SHARE("4", "文件共享");

        private final String code;
        private final String name;

        MediaType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static MediaType getByCode(String code) {
            for (MediaType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }

        public static MediaType getByCode(Integer code) {
            return code != null ? getByCode(code.toString()) : null;
        }

        public static String getNameByCode(String code) {
            MediaType type = getByCode(code);
            return type != null ? type.getName() : null;
        }

        public static String getNameByCode(Integer code) {
            return getNameByCode(code != null ? code.toString() : null);
        }
    }

    /**
     * 会议质量等级枚举
     */
    public enum QualityLevel {
        LOW("0", "低质量"),
        STANDARD("1", "标准质量"),
        MEDIUM("2", "中等质量"),
        HIGH("3", "高质量"),
        HD("4", "高清"),
        FULL_HD("5", "全高清");

        private final String code;
        private final String name;

        QualityLevel(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static QualityLevel getByCode(String code) {
            for (QualityLevel level : values()) {
                if (level.getCode().equals(code)) {
                    return level;
                }
            }
            return null;
        }

        public static QualityLevel getByCode(Integer code) {
            return code != null ? getByCode(code.toString()) : null;
        }

        public static String getNameByCode(String code) {
            QualityLevel level = getByCode(code);
            return level != null ? level.getName() : null;
        }

        public static String getNameByCode(Integer code) {
            return getNameByCode(code != null ? code.toString() : null);
        }
    }
}