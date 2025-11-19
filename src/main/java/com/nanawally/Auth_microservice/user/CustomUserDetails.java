package com.nanawally.Auth_microservice.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final CustomUser customUser;

    public CustomUserDetails(CustomUser customUser) {
        this.customUser = customUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        customUser.getRoles().forEach(
                userRole -> authorities.addAll(userRole.getUserAuthorities())
        );

        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customUser.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customUser.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return customUser.isEnabled();
    }

    public CustomUser getCustomUser() {
        return customUser;
    }
}