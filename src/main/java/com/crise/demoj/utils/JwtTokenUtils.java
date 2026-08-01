package com.crise.demoj.utils;

import com.crise.demoj.dao.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenUtils {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.refreshThreshold:300}")
    private Long refreshThreshold;

    private String generateToken(Map<String, Object> claims, long expirationSeconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    public String getUserNameFromToken(String token) {
        if (token == null)
            return null;
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getUserNameFromTokenAllowExpired(String token) {
        if (token == null) return null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public long getTokenRemainingSeconds(String token) {
        if (token == null) return -1;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            log.info("=== claims.getExpiration().getTime(): {}", claims.getExpiration().getTime());

            long subRes = (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
            log.info("sub result: {}", subRes);
            return subRes;

        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isTokenAboutToExpire(String token) {
        return getTokenRemainingSeconds(token) < refreshThreshold;
    }

    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userEntity.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, accessExpiration);
    }
}
