package com.shop.config;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 관리자 계정이 없으면 생성
        if (memberRepository.findByEmail("admin@naver.com") == null) {
            Member admin = new Member();
            admin.setEmail("admin@naver.com");
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("admin12"));  // 패스워드는 암호화해서 저장
            admin.setRole(Role.ADMIN);  // 관리자 권한 설정
            memberRepository.save(admin);
        }
    }
}
