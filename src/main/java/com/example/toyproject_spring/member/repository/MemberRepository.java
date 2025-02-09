package com.example.toyproject_spring.member.repository;

import com.example.toyproject_spring.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByApiKey(String apiKey);  // API 키를 찾는 메서드 추가
}
