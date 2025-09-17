package org.lhh.spongehome.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.lhh.spongehome.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenprovider {

    @Value("${jwt.secret}") // 从配置读取密钥
    private String jwtSecret;

    @Value("${jwt.expiration}") // Token有效期（毫秒）
    private long jwtExpiration;

    // 生成Token
    public String generateToken(User user) {
        // 1. 创建Token声明(Claims)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
//        claims.put("role", user.getRole());

        // 2. 设置签发时间
        Date now = new Date();

        // 3. 设置过期时间
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // 4. 鐢熸垚绛惧悕瀵嗛挜
        // SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256 算娉曢渶瑕佸皯浜 256 浣嶅瘑閽

        // 5. 鏋勫缓JWT
        return Jwts.builder()
                .setClaims(claims)            // 自定义声明
                .setSubject(user.getUserId().toString()) // 主体标识
                .setIssuedAt(now)              // 签发时间
                .setExpiration(expiryDate)      // 过期时间
                .signWith(key)                 // 签名算法
                .compact();                    // 生成字符串
    }
}