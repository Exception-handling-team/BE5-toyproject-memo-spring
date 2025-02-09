package com.example.toyproject_spring.global.security;

import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new User(member.getUsername(), member.getPassword(), List.of());
    }

    // API 키로 사용자 찾기
    public UserDetails loadUserByApiKey(String apiKey) throws UsernameNotFoundException {
        Member member = memberRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new UsernameNotFoundException("API 키로 사용자를 찾을 수 없습니다."));
        return new User(member.getUsername(), member.getPassword(), List.of());
    }
}
