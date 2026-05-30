package com.blog.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        byte[] keyBytes = new byte[32];
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(secretBytes, 0, keyBytes, 0, Math.min(secretBytes.length, 32));
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    // 1. 创建 token（用户ID + 角色 + 用户名）
    public String createToken(Long userId, String role, String username) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("role", role)
                .claim("username", username)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // 2. 取出用户ID
    public Long getUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(subject);
    }

    // 3. 取出角色
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    // 4. 取出用户名
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token).getPayload()
                .get("username", String.class);
    }
}
