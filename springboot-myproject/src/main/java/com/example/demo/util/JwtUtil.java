package com.example.demo.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

//用於生成用戶在線權限驗證
@Component
public class JwtUtil {
	private final Key key = Keys.hmacShaKeyFor("mySecretKeymySecretKeymySecretKey".getBytes()); // 正常應當寫在私密的配置文件中

	public String generateToken(Integer userId, String username, String role) {
		return Jwts.builder().claim("userId", userId).claim("role", role)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	public Integer extractUserId(String token) {
		return extractClaims(token).get("userId", Integer.class);
	}

	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}
}
