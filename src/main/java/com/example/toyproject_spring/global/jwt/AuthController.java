package com.example.toyproject_spring.global.jwt;

import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<Member> member = memberService.findByUsername(username);

        if (member.isEmpty() || !member.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // JWT 발급
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", member.get().getRole());

        String token = JwtUtil.createToken(claims);

        return ResponseEntity.ok(Map.of("token", token));
    }

    // 사용자 등록
    @PostMapping("/register")
    public Member register(@RequestBody Member member) {
        return memberService.saveUser(member);
    }
}
