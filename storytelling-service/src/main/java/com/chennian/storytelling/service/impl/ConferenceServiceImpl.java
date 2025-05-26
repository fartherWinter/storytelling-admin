package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.service.ConferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebRTC会议服务实现类
 * 处理视频会议的信令服务和房间管理
 * @author chen
 */
@Service
public class ConferenceServiceImpl extends TextWebSocketHandler implements ConferenceService {

    private static final Logger logger = LoggerFactory.getLogger(ConferenceServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 房间管理：roomId -> Map<peerId, session>
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    // 会话管理：sessionId -> roomId
    private final Map<String, String> sessionRooms = new ConcurrentHashMap<>();
    // 用户信息：sessionId -> displayName
    private final Map<String, String> userDisplayNames = new ConcurrentHashMap<>();
    // 房间元数据：roomId -> 房间信息
    private final Map<String, Map<String, Object>> roomMetadata = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("WebSocket连接已建立: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // 解析消息
            Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
            String type = (String) payload.get("type");

            switch (type) {
                case "join":
                    handleJoinRoom(session, payload);
                    break;
                case "leave":
                    handleLeaveRoom(session);
                    break;
                case "offer":
                case "answer":
                case "ice_candidate":
                    handleSignalingMessage(session, payload, type);
                    break;
                case "chat_message":
                    handleChatMessage(session, payload);
                    break;
                case "media_state_change":
                    handleMediaStateChange(session, payload);
                    break;
                default:
                    logger.warn("未知的消息类型: {}", type);
            }
        } catch (Exception e) {
            logger.error("处理WebSocket消息时出错", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        handleLeaveRoom(session);
        logger.info("WebSocket连接已关闭: {}, 状态: {}", session.getId(), status);
    }

    /**
     * 处理加入房间请求
     */
    private void handleJoinRoom(WebSocketSession session, Map<String, Object> payload) throws IOException {
        String roomId = (String) payload.get("roomId");
        String displayName = (String) payload.get("displayName");
        String peerId = session.getId();

        // 存储用户信息
        userDisplayNames.put(peerId, displayName);
        sessionRooms.put(peerId, roomId);

        // 创建房间（如果不存在）
        rooms.putIfAbsent(roomId, new ConcurrentHashMap<>());
        Map<String, WebSocketSession> peers = rooms.get(roomId);

        // 通知房间内的其他用户有新用户加入
        for (Map.Entry<String, WebSocketSession> entry : peers.entrySet()) {
            String existingPeerId = entry.getKey();
            WebSocketSession peerSession = entry.getValue();

            // 通知现有用户有新用户加入
            if (peerSession.isOpen()) {
                Map<String, Object> newPeerMsg = Map.of(
                        "type", "new_peer",
                        "peerId", peerId,
                        "displayName", displayName
                );
                peerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(newPeerMsg)));

                // 通知新用户有现有用户
                Map<String, Object> existingPeerMsg = Map.of(
                        "type", "new_peer",
                        "peerId", existingPeerId,
                        "displayName", userDisplayNames.getOrDefault(existingPeerId, "未知用户")
                );
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(existingPeerMsg)));
            }
        }

        // 将新用户添加到房间
        peers.put(peerId, session);

        // 通知用户已成功加入房间
        Map<String, Object> joinedMsg = Map.of(
                "type", "room_joined",
                "roomId", roomId,
                "peerId", peerId,
                "peers", peers.size()
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(joinedMsg)));

        logger.info("用户 {} ({}) 加入房间 {}, 当前房间人数: {}", peerId, displayName, roomId, peers.size());
    }

    /**
     * 处理离开房间请求
     */
    private void handleLeaveRoom(WebSocketSession session) {
        String peerId = session.getId();
        String roomId = sessionRooms.remove(peerId);

        if (roomId != null) {
            Map<String, WebSocketSession> peers = rooms.get(roomId);
            if (peers != null) {
                // 从房间中移除用户
                peers.remove(peerId);

                // 通知房间内的其他用户该用户已离开
                peers.forEach((id, peerSession) -> {
                    if (peerSession.isOpen()) {
                        try {
                            Map<String, Object> msg = Map.of(
                                    "type", "peer_left",
                                    "peerId", peerId
                            );
                            peerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                        } catch (IOException e) {
                            logger.error("发送用户离开通知失败", e);
                        }
                    }
                });

                // 如果房间为空，则删除房间
                if (peers.isEmpty()) {
                    rooms.remove(roomId);
                    logger.info("房间 {} 已空，已删除", roomId);
                }

                String displayName = userDisplayNames.remove(peerId);
                logger.info("用户 {} ({}) 离开房间 {}, 当前房间人数: {}", peerId, displayName, roomId, peers.size());
            }
        }
    }

    /**
     * 处理信令消息（offer, answer, ice_candidate）
     */
    private void handleSignalingMessage(WebSocketSession session, Map<String, Object> payload, String type) throws IOException {
        String peerId = session.getId();
        String roomId = sessionRooms.get(peerId);
        String targetPeerId = (String) payload.get("targetPeerId");

        if (roomId != null && targetPeerId != null) {
            Map<String, WebSocketSession> peers = rooms.get(roomId);
            WebSocketSession targetSession = peers.get(targetPeerId);

            if (targetSession != null && targetSession.isOpen()) {
                // 构建转发消息
                Map<String, Object> forwardMsg = Map.of(
                        "type", type,
                        "peerId", peerId,
                        "displayName", userDisplayNames.getOrDefault(peerId, "未知用户")
                );

                // 添加特定类型的数据
                if ("offer".equals(type)) {
                    forwardMsg = Map.of(
                            "type", type,
                            "peerId", peerId,
                            "displayName", userDisplayNames.getOrDefault(peerId, "未知用户"),
                            "offer", payload.get("offer")
                    );
                } else if ("answer".equals(type)) {
                    forwardMsg = Map.of(
                            "type", type,
                            "peerId", peerId,
                            "answer", payload.get("answer")
                    );
                } else if ("ice_candidate".equals(type)) {
                    forwardMsg = Map.of(
                            "type", type,
                            "peerId", peerId,
                            "candidate", payload.get("candidate")
                    );
                }

                // 转发消息
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(forwardMsg)));
            }
        }
    }

    /**
     * 处理聊天消息
     */
    private void handleChatMessage(WebSocketSession session, Map<String, Object> payload) throws IOException {
        String peerId = session.getId();
        String roomId = sessionRooms.get(peerId);
        String content = (String) payload.get("content");

        if (roomId != null && content != null) {
            Map<String, WebSocketSession> peers = rooms.get(roomId);

            // 构建聊天消息
            Map<String, Object> chatMsg = Map.of(
                    "type", "chat_message",
                    "senderId", peerId,
                    "senderName", userDisplayNames.getOrDefault(peerId, "未知用户"),
                    "content", content,
                    "timestamp", System.currentTimeMillis()
            );

            String chatMsgStr = objectMapper.writeValueAsString(chatMsg);

            // 广播给房间内所有用户（包括发送者）
            for (WebSocketSession peerSession : peers.values()) {
                if (peerSession.isOpen()) {
                    peerSession.sendMessage(new TextMessage(chatMsgStr));
                }
            }
        }
    }

    /**
     * 处理媒体状态变更（音频/视频开关）
     */
    private void handleMediaStateChange(WebSocketSession session, Map<String, Object> payload) throws IOException {
        String peerId = session.getId();
        String roomId = sessionRooms.get(peerId);
        // "audio" 或 "video"
        String mediaType = (String) payload.get("mediaType");
        boolean enabled = (boolean) payload.get("enabled");

        if (roomId != null) {
            Map<String, WebSocketSession> peers = rooms.get(roomId);

            // 构建媒体状态变更消息
            Map<String, Object> stateMsg = Map.of(
                    "type", "media_state_change",
                    "peerId", peerId,
                    "mediaType", mediaType,
                    "enabled", enabled
            );

            String stateMsgStr = objectMapper.writeValueAsString(stateMsg);

            // 广播给房间内其他用户
            for (Map.Entry<String, WebSocketSession> entry : peers.entrySet()) {
                if (!entry.getKey().equals(peerId) && entry.getValue().isOpen()) {
                    entry.getValue().sendMessage(new TextMessage(stateMsgStr));
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("WebSocket传输错误", exception);
    }
    
    /**
     * 获取房间信息
     * @param roomId 房间ID
     * @return 房间信息
     */
    @Override
    public Object getRoomInfo(String roomId) {
        // 检查房间是否存在
        if (!rooms.containsKey(roomId)) {
            return null;
        }
        
        // 获取房间元数据，如果不存在则创建一个基本信息
        Map<String, Object> metadata = roomMetadata.getOrDefault(roomId, new HashMap<>());
        Map<String, Object> roomInfo = new HashMap<>(metadata);
        
        // 添加当前房间人数信息
        Map<String, WebSocketSession> peers = rooms.get(roomId);
        roomInfo.put("activeUsers", peers.size());
        roomInfo.put("roomId", roomId);
        
        // 如果没有名称，设置一个默认名称
        if (!roomInfo.containsKey("name")) {
            roomInfo.put("name", "会议 " + roomId);
        }
        
        return roomInfo;
    }
    
    /**
     * 创建新房间
     * @param roomName 房间名称
     * @param isPrivate 是否私密房间
     * @param password 私密房间密码
     * @return 房间ID
     */
    @Override
    public String createRoom(String roomName, boolean isPrivate, String password) {
        // 生成唯一的房间ID
        String roomId = UUID.randomUUID().toString();
        
        // 创建房间元数据
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", roomName);
        metadata.put("isPrivate", isPrivate);
        metadata.put("createdAt", System.currentTimeMillis());
        
        // 如果是私密房间，存储密码（实际应用中应加密存储）
        if (isPrivate && password != null && !password.isEmpty()) {
            metadata.put("password", password);
        }
        
        // 初始化房间
        rooms.put(roomId, new ConcurrentHashMap<>());
        roomMetadata.put(roomId, metadata);
        
        logger.info("创建新房间: {}, 名称: {}, 私密: {}", roomId, roomName, isPrivate);
        return roomId;
    }
    
    /**
     * 验证房间密码
     * @param roomId 房间ID
     * @param password 密码
     * @return 是否验证通过
     */
    @Override
    public boolean verifyRoomPassword(String roomId, String password) {
        // 检查房间是否存在
        Map<String, Object> metadata = roomMetadata.get(roomId);
        if (metadata == null) {
            return false;
        }
        
        // 检查房间是否是私密房间
        Boolean isPrivate = (Boolean) metadata.getOrDefault("isPrivate", false);
        if (!isPrivate) {
            // 非私密房间不需要密码
            return true;
        }
        
        // 验证密码
        String storedPassword = (String) metadata.get("password");
        return storedPassword != null && storedPassword.equals(password);
    }
    
    /**
     * 获取最近活跃的房间列表
     * @param limit 限制数量
     * @return 房间列表
     */
    @Override
    public Object getRecentRooms(int limit) {
        List<Map<String, Object>> recentRooms = new ArrayList<>();
        
        // 获取所有房间，并按活跃度排序
        rooms.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty()) // 只包含有用户的房间
            .sorted((a, b) -> {
                // 按房间人数降序排序
                return Integer.compare(b.getValue().size(), a.getValue().size());
            })
            .limit(limit)
            .forEach(entry -> {
                String roomId = entry.getKey();
                Map<String, WebSocketSession> peers = entry.getValue();
                
                // 获取房间元数据
                Map<String, Object> metadata = roomMetadata.getOrDefault(roomId, new HashMap<>());
                Map<String, Object> roomInfo = new HashMap<>(metadata);
                
                // 添加房间基本信息
                roomInfo.put("roomId", roomId);
                roomInfo.put("activeUsers", peers.size());
                
                // 如果是私密房间，移除密码信息
                roomInfo.remove("password");
                
                // 如果没有名称，设置一个默认名称
                if (!roomInfo.containsKey("name")) {
                    roomInfo.put("name", "会议 " + roomId);
                }
                
                recentRooms.add(roomInfo);
            });
        
        return recentRooms;
    }
}