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
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    @Autowired
    public AdminController(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, PasswordEncoder passwordEncoder, AdminService adminService) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<CustomUser>> getAllUsers(){

        if (adminService.getAllUsers().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(adminService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUser> registerUser(@Valid @RequestBody CustomUserCreationDTO dto) {
         CustomUser newUser = adminService.registerUser(dto);
         return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {

        Optional<CustomUser> userToDelete = customUserRepository.findById(id);

        if(userToDelete.isPresent()) {
            customUserRepository.deleteById(id);
            logger.info("Delete user with id: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
