package com.example.toyproject_spring.global;

import com.example.toyproject_spring.member.entity.Member;
import com.example.toyproject_spring.member.entity.Member.Role;
import com.example.toyproject_spring.member.service.MemberService;
import com.example.toyproject_spring.memo.entity.Memo;
import com.example.toyproject_spring.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final MemberService memberService;
    private final MemoService memoService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            initData();  // BaseInitData의 initData 메서드 호출
        };
    }

    public void initData() {
        System.out.println("BaseInitData is running...");

        // 어드민 계정 생성
        createAdminAccount();

        // 일반 계정 생성
        createUserAccount();
    }

    private void createAdminAccount() {
        Member admin = memberService.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new Member();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole(Role.ADMIN);
            memberService.saveUser(admin);
            createMemoForUser(admin); // 어드민 메모 생성
            System.out.println("Admin account created");
        } else {
            System.out.println("Admin account already exists");
        }
    }

    private void createUserAccount() {
        Member user = memberService.findByUsername("user").orElse(null);
        if (user == null) {
            user = new Member();
            user.setUsername("user");
            user.setPassword("user123");
            user.setRole(Role.USER);
            memberService.saveUser(user);
            createMemoForUser(user); // 일반 계정 메모 생성
            System.out.println("User account created");
        } else {
            System.out.println("User account already exists");
        }
    }

    private void createMemoForUser(Member member) {
        for (int i = 1; i <= 3; i++) {
            Memo memo = new Memo();
            memo.setTitle("Memo " + i);
            memo.setContent("This is content for memo " + i);
            memo.setMember(member);
            memoService.write(memo);  // 메모 저장
            System.out.println("Memo " + i + " created for " + member.getUsername());
        }
    }
}
