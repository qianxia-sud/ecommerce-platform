package com.ecommerce.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtil {
    
    /** 默认密钥（生产环境应从配置中读取） */
    private static final String SECRET = "ecommerce-platform-jwt-secret-key-2024-must-be-at-least-256-bits";
    
    /** 默认过期时间：24小时（毫秒） */
    private static final long EXPIRATION = 24 * 60 * 60 * 1000;
    
    /** 密钥 */
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    
    /**
     * 生成Token
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT Token
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, EXPIRATION);
    }
    
    /**
     * 生成Token
     * @param userId 用户ID
     * @param username 用户名
     * @param expiration 过期时间（毫秒）
     * @return JWT Token
     */
    public static String generateToken(Long userId, String username, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 解析Token
     * @param token JWT Token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 获取用户ID
     * @param token JWT Token
     * @return 用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }
    
    /**
     * 获取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("username");
    }
    
    /**
     * 验证Token是否有效
     * @param token JWT Token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * 检查Token是否过期
     * @param token JWT Token
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
    
    /**
     * 获取Token的过期时间
     * @param token JWT Token
     * @return 过期时间
     */
    public static Date getExpiration(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }
    
    /**
     * 获取默认过期时间（秒）
     * @return 过期时间（秒）
     */
    public static long getExpirationSeconds() {
        return EXPIRATION / 1000;
    }
}


