package com.nanawally.Auth_microservice.user;

import com.nanawally.Auth_microservice.user.authority.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    @Setter
    private boolean isAccountNonExpired;
    @Setter
    private boolean isAccountNonLocked;
    @Setter
    private boolean isCredentialsNonExpired;
    @Setter
    private boolean isEnabled;

    @Setter
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER) // fetch immediately
    @Enumerated(value = EnumType.STRING)
    private Set<UserRole> roles;

    public CustomUser() {
    }

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CustomUser(String username, String password, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    public void setPassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
