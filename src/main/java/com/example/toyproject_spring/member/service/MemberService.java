package com.example.toyproject_spring.member.service;

import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 사용자 저장 (비밀번호 암호화 제거)
    public Member saveUser(Member member) {
        if (member.getRole() == null) {
            member.setRole(Member.Role.USER);
        }
        return memberRepository.save(member);
    }

    // 사용자명으로 사용자 찾기
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

}
