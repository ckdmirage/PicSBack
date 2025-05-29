package com.example.demo.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//用於生成用戶在線權限驗證
public class JwtUtil {
	private final String SECRET_KEY = "mySecretKey";// 登入權限的驗證,應該單獨寫出來,因為是專案無所謂了直接放在這裡

	public String generateToken(String username, String role) {
		return Jwts.builder().claim("username", username).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1天有效
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Claims extractClaims(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
}
