package com.example.FinalRing.websocket;

import com.example.FinalRing.Exception.JwtInvalid;
import com.example.FinalRing.Service.JwtService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        if(!(request instanceof ServletServerHttpRequest serverHttpRequest)){
            return false;
        }
        Cookie[] cookies=serverHttpRequest.getServletRequest().getCookies();
        if(cookies==null){
            return false;
        }
        String jwt=null;
        for(Cookie cookie:cookies){
            if("access_token".equals(cookie.getName())){
                jwt=cookie.getValue();
                break;
            }
        }
        if(jwt==null){
            return false;
        }
        try{
            String email=jwtService.validateToken(jwt);
            attributes.put("email",email);

        }
        catch(JwtInvalid e){
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}

