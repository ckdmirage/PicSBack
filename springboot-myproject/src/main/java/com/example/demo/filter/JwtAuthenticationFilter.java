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

	private static final Set<String> WHITELIST = Set.of("/user/login", "/user/register", "/public", "/myprojectImg",
		    "/static", "/css", "/js", "/images");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		if (WHITELIST.stream().anyMatch(path::startsWith)) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				Claims claims = jwtUtil.extractClaims(token);
				String username = claims.get("username", String.class);
				String role = claims.get("role", String.class);

				// 設定使用者資訊到 SecurityContext
				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role)); // 必須加 "ROLE_"
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);

				// 若你還需要給 Controller 傳 userCertDto 也可以保留這行
				request.setAttribute("userCertDto", new UserCertDto(username, role, token));

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

		// 沒有 Token
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write("{\"status\":401,\"message\":\"未提供授權資訊\"}");
	}
}
