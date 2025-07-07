package com.chennian.storytelling.admin.config;

import com.chennian.storytelling.service.impl.ConferenceServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
* WebSocket配置类
* 用于注册WebRTC信令服务的WebSocket处理器
* @author chen
*/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

   private final ConferenceServiceImpl conferenceService;

   public WebSocketConfig(ConferenceServiceImpl conferenceService) {
       this.conferenceService = conferenceService;
   }

   @Override
   public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
       // 注册WebRTC信令服务的WebSocket处理器
       registry.addHandler(conferenceService, "/api/conference/ws/{roomId}")
               // 在生产环境中应该限制允许的源
               .setAllowedOrigins("*");
   }
}