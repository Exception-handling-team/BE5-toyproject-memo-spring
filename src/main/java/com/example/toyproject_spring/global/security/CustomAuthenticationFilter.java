package com.example.toyproject_spring.global.security;

import com.example.toyproject_spring.global.Rq;
import com.example.toyproject_spring.global.jwt.JwtUtil;
import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());

        // JWT 유효성 검사
        if (!JwtUtil.isValidToken(JwtUtil.getSecretKey(), token)) {
            System.out.println("Invalid JWT Token: " + token);  // JWT 유효성 검사 실패 시 로그
            filterChain.doFilter(request, response);
            return;
        }

        // JWT에서 사용자 정보 추출
        Map<String, Object> payload = JwtUtil.getPayload(JwtUtil.getSecretKey(), token);
        String username = (String) payload.get("username");

        System.out.println("Extracted Username from Token: " + username);  // 디버깅을 위한 로그

        Optional<Member> userOptional = memberService.findByUsername(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + username);  // 유저가 없을 경우 로그
            filterChain.doFilter(request, response);
            return;
        }

        Member member = userOptional.get();
        rq.setLogin(member.getUsername());

        // 인증 정보 SecurityContext에 저장
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                member.getUsername(), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
