package com.nanawally.Auth_microservice.user.admin;

import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.CustomUserRepository;
import com.nanawally.Auth_microservice.user.mapper.CustomUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, PasswordEncoder passwordEncoder){
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomUser> getAllUsers() {
        return customUserRepository.findAll();
    }

    /*

    @GetMapping("/users")
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
    }*/
}
