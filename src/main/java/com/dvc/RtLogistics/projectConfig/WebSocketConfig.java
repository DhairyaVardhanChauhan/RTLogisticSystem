package com.dvc.RtLogistics.projectConfig;

import com.dvc.RtLogistics.document.interceptor.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic"); // it will listen on anypoint starting with /topic
        config.setApplicationDestinationPrefixes("/app"); // will now receive message at /app/doc.chat where doc.chat is @MessageMapping("/doc.chat")
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*").withSockJS();  // clients connect on this endpoint
    }
}