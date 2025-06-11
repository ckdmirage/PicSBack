package com.example.demo.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("Request path: " + path);

        // ✅ 白名單條件判斷（包含靜態路徑 + 正則處理動態路徑）
        if (
        	// 用戶
            path.startsWith("/user/login") ||
            path.startsWith("/user/register") ||
            path.startsWith("/user/verify") ||
            path.matches("^/user/homepage/\\d+$") ||
            
            // 作品
            path.matches("^/artwork(/.*)?$") ||
            path.matches("^/artwork/user/\\d+$") ||
            
            // 點讚數
            path.matches("^/like/count/\\d+$")||
            
            // 圖片上傳
            path.startsWith("/public") ||
            path.startsWith("/myprojectImg") ||
            path.startsWith("/static") ||
            path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 進入 token 驗證區
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.extractClaims(token);
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);
                Integer userId = claims.get("userId", Integer.class);

                // 設定使用者資訊到 SecurityContext
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 提供給 Controller 使用
                request.setAttribute("userCertDto", new UserCertDto(userId, username, role, token));

                filterChain.doFilter(request, response);
                return;

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"status\":401,\"message\":\"Token 無效或過期\"}");
                return;
            }
        }

        // ❌ 沒有帶 Authorization
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":401,\"message\":\"未提供授權資訊\"}");
    }
}
