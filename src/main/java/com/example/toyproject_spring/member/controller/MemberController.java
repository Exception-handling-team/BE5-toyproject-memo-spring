package com.example.toyproject_spring.member.controller;

import com.example.toyproject_spring.global.Rq;
import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final Rq rq; // Rq 주입

    @Autowired
    public MemberController(MemberService memberService, Rq rq) {
        this.memberService = memberService;
        this.rq = rq;
    }


    // 인증된 사용자 정보 가져오기 (Rq 사용)
    @GetMapping("/me")
    public Member getAuthenticatedUser() {
        return rq.getAuthenticatedActor(); // Rq에서 인증된 사용자 정보 가져오기
    }
}
