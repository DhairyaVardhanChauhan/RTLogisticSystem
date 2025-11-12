package com.dvc.RtLogistics.document.interceptor;

import com.dvc.RtLogistics.filters.JwtFilter;
import com.dvc.RtLogistics.service.authServices.CustomUserDetailsService;
import com.dvc.RtLogistics.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");
            String username = null;
            if(token != null){
                username = jwtUtils.getUserNameFromToken(token);
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (token != null && jwtUtils.validateToken(userDetails,token)) {
                attributes.put("user", jwtUtils.getUserNameFromToken(token));
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
