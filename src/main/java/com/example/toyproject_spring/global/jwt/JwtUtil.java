package com.example.toyproject_spring.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "YourSecretKeyYourSecretKeyYourSecretKeyYourSecretKey";

    // SECRET_KEY를 SecretKey 객체로 변환
    private static final SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");

    private static final int EXPIRATION_TIME = 3600 * 1000; // 1시간 (밀리초 단위)

    // JWT 생성
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean isValidToken(SecretKey secretKey, String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(token);

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public static Map<String, Object> getPayload(SecretKey secretKey, String jwtStr) {
        return (Map<String, Object>) Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(jwtStr)
                .getPayload();

    }

    public static SecretKey getSecretKey() {
        return key;
    }
}
