package com.chennian.storytelling.admin.controller.conference;

import com.chennian.storytelling.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 视频会议控制器
 * 处理会议房间的创建、加入和管理
 * @author chen
 */
@RestController
@RequestMapping("/api/conference")
public class ConferenceController {

    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    /**
     * 创建新会议房间
     */
    @PostMapping("/rooms")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody Map<String, Object> request) {
        String name = (String) request.getOrDefault("name", "新会议");
        String description = (String) request.getOrDefault("description", "");
        boolean isPrivate = Boolean.TRUE.equals(request.get("isPrivate"));
        String password = isPrivate ? (String) request.get("password") : null;

        // 生成房间ID或使用服务创建
        String roomId = conferenceService.createRoom(name, isPrivate, password);
        if (roomId == null) {
            roomId = UUID.randomUUID().toString();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("roomId", roomId);
        response.put("name", name);
        response.put("isPrivate", isPrivate);
        return ResponseEntity.ok(response);
    }

    /**
     * 检查房间是否存在
     */
    @GetMapping("/rooms/{roomId}/check")
    public ResponseEntity<Map<String, Object>> checkRoom(@PathVariable String roomId) {
        Object roomInfo = conferenceService.getRoomInfo(roomId);
        boolean exists = roomInfo != null;

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        
        if (exists && roomInfo instanceof Map) {
            Map<String, Object> info = (Map<String, Object>) roomInfo;
            response.put("name", info.get("name"));
            response.put("isPrivate", info.get("isPrivate"));
        } else {
            // 模拟响应，实际应从数据库或缓存获取
            response.put("name", "会议 " + roomId);
            response.put("isPrivate", false);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 验证房间密码
     */
    @PostMapping("/rooms/{roomId}/verify-password")
    public ResponseEntity<Map<String, Object>> verifyPassword(
            @PathVariable String roomId,
            @RequestBody Map<String, String> request) {
        
        String password = request.get("password");
        boolean valid = conferenceService.verifyRoomPassword(roomId, password);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", valid);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取最近的房间列表
     */
    @GetMapping("/rooms/recent")
    public ResponseEntity<?> getRecentRooms() {
        Object rooms = conferenceService.getRecentRooms(10);
        if (rooms != null) {
            return ResponseEntity.ok(rooms);
        }
        
        // 如果服务未实现，返回空列表
        return ResponseEntity.ok(new Object[0]);
    }
}