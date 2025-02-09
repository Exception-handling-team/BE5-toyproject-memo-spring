package com.example.toyproject_spring.global.jwt;

import com.example.toyproject_spring.global.security.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;

    public JwtFilter(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        token = token.substring(7); // "Bearer " 제거

        if (!JwtUtil.isValidToken(JwtUtil.getSecretKey(), token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // JWT에서 사용자 정보 추출
        Map<String, Object> claims = JwtUtil.getPayload(JwtUtil.getSecretKey(), token);
        String username = (String) claims.get("username");

        // 사용자 인증 처리
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        // 인증된 사용자 정보를 SecurityContext에 설정
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()));

        filterChain.doFilter(request, response);
    }
}
