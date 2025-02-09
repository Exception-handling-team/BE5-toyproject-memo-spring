package com.example.toyproject_spring.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;  // Role 필드 추가

    private String apiKey;  // apiKey 필드 추가

    // Enum 역할 정의
    public enum Role {
        USER, ADMIN
    }

    // Role을 GrantedAuthority로 변환
    public GrantedAuthority getAuthority() {
        return () -> "ROLE_" + this.role.name();
    }
}
