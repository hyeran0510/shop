package com.shop.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
public class memberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Optional<Member> _member = this.memberRepository.findByEmail(email);
        Member member = this.memberRepository.findByEmail(email);// findByEmail로 수정
        // 사용자를 찾지 못한 경우 예외 발생
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Role에 따라 권한 부여
        authorities.add(new SimpleGrantedAuthority(member.getRole().name()));

        // User 객체 반환 시 member.getEmail()과 member.getPassword()를 사용
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
