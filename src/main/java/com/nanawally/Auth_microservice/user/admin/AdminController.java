package com.nanawally.Auth_microservice.user.admin;

import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.CustomUserRepository;
import com.nanawally.Auth_microservice.user.authority.UserRole;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import com.nanawally.Auth_microservice.user.dto.CustomUserSelfRegisterDTO;
import com.nanawally.Auth_microservice.user.mapper.CustomUserMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public ResponseEntity<List<CustomUser>> getAllUsers(){
        return ResponseEntity.ok().body(customUserRepository.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUser> registerUser(@Valid @RequestBody CustomUserCreationDTO dto) {

        CustomUser user = customUserMapper.toEntity(dto);
        user.setPassword(user.getPassword(), passwordEncoder);
        user.setAccountNonExpired(dto.isAccountNonExpired());
        user.setAccountNonLocked(dto.isAccountNonLocked());
        user.setCredentialsNonExpired(dto.isCredentialsNonExpired());
        user.setEnabled(dto.isEnabled());
        user.setRoles(dto.roles());

        customUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
