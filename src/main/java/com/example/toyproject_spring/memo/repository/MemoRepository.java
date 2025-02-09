package com.example.toyproject_spring.memo.repository;

import com.example.toyproject_spring.memo.entity.Memo;
import com.example.toyproject_spring.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // Member 객체를 사용하여 메모를 찾는 메서드
    List<Memo> findByMember(Member member);

    Memo findByMemberAndId(Member member, Long id);
}
