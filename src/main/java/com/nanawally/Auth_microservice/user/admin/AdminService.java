package com.nanawally.Auth_microservice.user.admin;

import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.CustomUserRepository;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import com.nanawally.Auth_microservice.user.mapper.CustomUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomUser> getAllUsers() {
        if (customUserRepository.findAll().isEmpty()) {
            logger.info("No users found");
        }
        return customUserRepository.findAll();
    }

    public CustomUser registerUser(CustomUserCreationDTO dto) {
        CustomUser user = customUserMapper.toEntity(dto);
        user.setPassword(user.getPassword(), passwordEncoder);
        user.setAccountNonExpired(dto.isAccountNonExpired());
        user.setAccountNonLocked(dto.isAccountNonLocked());
        user.setCredentialsNonExpired(dto.isCredentialsNonExpired());
        user.setEnabled(dto.isEnabled());
        user.setRoles(dto.roles());

        customUserRepository.save(user);

        logger.info("New CustomUser with id {} was created successfully", user.getId());

        return user;
    }

    public boolean deleteUserById(UUID id) {
        Optional<CustomUser> userToDelete = customUserRepository.findById(id);

        if (userToDelete.isPresent()) {
            customUserRepository.deleteById(id);
            logger.info("Delete user with id: {}", id);
            return true;
        }

        logger.warn("User with id {} was not found", id);
        return false;
    }
}
