package com.example.toyproject_spring.global;

import com.example.toyproject_spring.global.jwt.JwtUtil;
import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@RequestScope
public class Rq {

    private final HttpServletRequest request;
    private final MemberService memberService;

    public Member getAuthenticatedActor() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ServiceException("401-1"); // Authorization 헤더가 없거나 잘못된 경우
        }

        String token = authorizationHeader.substring("Bearer ".length());

        if (!JwtUtil.isValidToken(JwtUtil.getSecretKey(), token)) {
            throw new ServiceException("401-1");
        }

        Map<String, Object> payload = JwtUtil.getPayload(JwtUtil.getSecretKey(), token);
        String username = (String) payload.get("username");

        Optional<Member> opUser = memberService.findByUsername(username);
        if (opUser.isEmpty()) {
            throw new ServiceException("401-1");
        }

        return opUser.get();
    }

    public void setLogin(String username) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", List.of());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public Member getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberService.findByUsername(userDetails.getUsername()).orElseThrow(() -> new ServiceException("401-2"));
    }
}
