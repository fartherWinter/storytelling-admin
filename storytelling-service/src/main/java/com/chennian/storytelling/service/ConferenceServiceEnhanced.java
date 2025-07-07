package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.ConferenceDTO;
import com.chennian.storytelling.bean.enums.ConferenceEnum;

import java.util.List;
import java.util.Map;

/**
 * 增强的视频会议服务接口
 * 提供完整的会议管理功能
 * @author chen
 */
public interface ConferenceServiceEnhanced extends ConferenceService {

    /**
     * 创建会议房间（增强版）
     * @param request 创建房间请求
     * @return 房间信息
     */
    ConferenceDTO.RoomInfo createRoomEnhanced(ConferenceDTO.CreateRoomRequest request);

    /**
     * 更新房间信息
     * @param roomId 房间ID
     * @param request 更新请求
     * @return 更新结果
     */
    boolean updateRoom(String roomId, ConferenceDTO.RoomOperationRequest request);

    /**
     * 关闭房间
     * @param roomId 房间ID
     * @param operatorId 操作者ID
     * @return 关闭结果
     */
    boolean closeRoom(String roomId, String operatorId);

    /**
     * 用户加入房间
     * @param roomId 房间ID
     * @param request 加入请求
     * @return 参与者信息
     */
    ConferenceDTO.ParticipantInfo joinRoom(String roomId, ConferenceDTO.JoinRoomRequest request);

    /**
     * 用户离开房间
     * @param roomId 房间ID
     * @param userId 用户ID
     * @return 离开结果
     */
    boolean leaveRoom(String roomId, String userId);

    /**
     * 踢出参与者
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param operatorId 操作者ID
     * @return 踢出结果
     */
    boolean kickParticipant(String roomId, String userId, String operatorId);

    /**
     * 获取房间参与者列表
     * @param roomId 房间ID
     * @return 参与者列表
     */
    List<ConferenceDTO.ParticipantInfo> getRoomParticipants(String roomId);

    /**
     * 获取房间详细信息
     * @param roomId 房间ID
     * @return 房间详细信息
     */
    ConferenceDTO.RoomInfo getRoomDetails(String roomId);

    /**
     * 获取分页房间列表
     * @param request 查询请求
     * @return 房间列表
     */
    List<ConferenceDTO.RoomInfo> getRoomList(ConferenceDTO.RoomListRequest request);

    /**
     * 获取会议统计信息
     * @return 统计信息
     */
    ConferenceDTO.ConferenceStats getConferenceStats();

    /**
     * 更新参与者媒体状态
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param mediaType 媒体类型
     * @param enabled 是否启用
     * @return 更新结果
     */
    boolean updateParticipantMediaStatus(String roomId, String userId, 
                                       ConferenceEnum.MediaType mediaType, boolean enabled);

    /**
     * 设置参与者角色
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param role 角色
     * @param operatorId 操作者ID
     * @return 设置结果
     */
    boolean setParticipantRole(String roomId, String userId, 
                             ConferenceEnum.ParticipantRole role, String operatorId);

    /**
     * 检查房间权限
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean checkRoomPermission(String roomId, String userId, ConferenceEnum.OperationType operation);

    /**
     * 获取用户参与的房间列表
     * @param userId 用户ID
     * @return 房间列表
     */
    List<ConferenceDTO.RoomInfo> getUserRooms(String userId);

    /**
     * 开始录制
     * @param roomId 房间ID
     * @param operatorId 操作者ID
     * @return 录制结果
     */
    boolean startRecording(String roomId, String operatorId);

    /**
     * 停止录制
     * @param roomId 房间ID
     * @param operatorId 操作者ID
     * @return 停止结果
     */
    boolean stopRecording(String roomId, String operatorId);

    /**
     * 获取录制状态
     * @param roomId 房间ID
     * @return 录制状态
     */
    boolean getRecordingStatus(String roomId);

    /**
     * 发送房间消息
     * @param roomId 房间ID
     * @param senderId 发送者ID
     * @param message 消息内容
     * @return 发送结果
     */
    boolean sendRoomMessage(String roomId, String senderId, String message);

    /**
     * 获取房间消息历史
     * @param roomId 房间ID
     * @param limit 消息数量限制
     * @return 消息列表
     */
    List<Map<String, Object>> getRoomMessageHistory(String roomId, int limit);

    /**
     * 设置房间质量等级
     * @param roomId 房间ID
     * @param qualityLevel 质量等级
     * @param operatorId 操作者ID
     * @return 设置结果
     */
    boolean setRoomQuality(String roomId, ConferenceEnum.QualityLevel qualityLevel, String operatorId);

    /**
     * 获取房间质量统计
     * @param roomId 房间ID
     * @return 质量统计
     */
    Map<String, Object> getRoomQualityStats(String roomId);
}