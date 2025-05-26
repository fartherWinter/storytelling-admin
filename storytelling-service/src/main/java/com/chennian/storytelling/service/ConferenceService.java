package com.chennian.storytelling.service;



/**
 * 视频会议服务接口
 * 处理WebRTC视频会议的信令和房间管理
 * @author chen
 */
public interface ConferenceService {
    
    /**
     * 获取房间信息
     * @param roomId 房间ID
     * @return 房间信息
     */
    default Object getRoomInfo(String roomId) {
        return null;
    }
    
    /**
     * 创建新房间
     * @param roomName 房间名称
     * @param isPrivate 是否私密房间
     * @param password 私密房间密码
     * @return 房间ID
     */
    default String createRoom(String roomName, boolean isPrivate, String password) {
        return null;
    }
    
    /**
     * 验证房间密码
     * @param roomId 房间ID
     * @param password 密码
     * @return 是否验证通过
     */
    default boolean verifyRoomPassword(String roomId, String password) {
        return false;
    }
    
    /**
     * 获取最近活跃的房间列表
     * @param limit 限制数量
     * @return 房间列表
     */
    default Object getRecentRooms(int limit) {
        return null;
    }
}