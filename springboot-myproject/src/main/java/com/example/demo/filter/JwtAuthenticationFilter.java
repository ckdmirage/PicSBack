package com.example.demo.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
@Order(1) // 優先順序：數字越小越先
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	private static final String[] WHITELIST = { "/user/login", "/user/register", "/public" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		for (String free : WHITELIST) {
			if (path.startsWith(free)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				Claims claims = jwtUtil.extractClaims(token);
				String username = claims.get("username", String.class);
				String role = claims.get("role", String.class);
				UserCertDto userCertDto = new UserCertDto(username, role, token);

				request.setAttribute("userCertDto", userCertDto);
				filterChain.doFilter(request, response);
				return;

			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("{\"message\":\"Token 無效或過期\"}");
				return;
			}
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write("{\"message\":\"未提供有效授權\"}");
	}
}
