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
    
    private boolean isWhiteListPath(String path, String method) {
        return
        	// 用戶
            path.startsWith("/user/login") ||				// 登入
            path.startsWith("/user/register") ||			// 註冊
            path.startsWith("/user/verify") ||				// 郵箱驗證
            path.matches("^/user/homepage/\\d+$") ||		// 個人主頁
            
            // 作品
            path.equals("/artwork/all") ||					// 主頁獲取所有作品
            path.matches("^/artwork/\\d+$") ||				// 瀏覽作品
            path.matches("^/artwork/user/\\d+$") ||			// 根據作者獲取作品列表
            path.startsWith("/artwork/tag/") ||				// 標籤獲得作品列表

            //標籤
            (path.equals("/artwork/tag/search") && method.equals("GET")) ||						// 搜索標籤

            // 點讚數
            path.matches("^/like/count/\\d+$") ||
            
            // 圖片上傳
            path.startsWith("/public") ||
            path.startsWith("/myprojectImg") ||
            path.startsWith("/static") ||
            path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String path = request.getRequestURI();
    	String method = request.getMethod();
    	
    	//System.out.println("🛠️ Path: " + path + ", Method: " + method);
        //System.out.println("🧪 isWhiteListPath 判斷結果: " + isWhiteListPath(path, method));
    	
    	
    	if ("OPTIONS".equalsIgnoreCase(method) || isWhiteListPath(path, method)) {
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
