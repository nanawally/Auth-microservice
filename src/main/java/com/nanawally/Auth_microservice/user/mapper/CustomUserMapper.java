package com.nanawally.Auth_microservice.user.mapper;


import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import com.nanawally.Auth_microservice.user.dto.CustomUserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomUserMapper {

    public CustomUser toEntity(CustomUserCreationDTO customUserCreationDTO) {
        return new CustomUser(
                customUserCreationDTO.username(),
                customUserCreationDTO.password(),
                customUserCreationDTO.isAccountNonExpired(),
                customUserCreationDTO.isAccountNonLocked(),
                customUserCreationDTO.isCredentialsNonExpired(),
                customUserCreationDTO.isEnabled(),
                customUserCreationDTO.roles()
        );
    }

    public CustomUserResponseDTO toUsernameDTO(CustomUser customUser) {
        return new CustomUserResponseDTO(customUser.getUsername());
    }

}

