package com.shop.constant;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import com.shop.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public String getName() {
        return member.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRole().getAuthorities(); // 역할에 따른 권한 설정
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명이 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용 가능
    }

    public Member getMember() {
        return member;
    }

}
