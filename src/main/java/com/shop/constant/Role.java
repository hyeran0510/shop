package com.shop.constant;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;

public enum Role {
    USER, ADMIN;

    public List<SimpleGrantedAuthority> getAuthorities() {
        // 역할에 따른 권한 설정
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
