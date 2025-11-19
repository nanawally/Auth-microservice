package com.nanawally.Auth_microservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomUserResponseDTO(
        @Size(min = 2, max = 50, message = "Username length must be between 2-50 chars")
        @NotBlank(message = "Username may not contain whitespace")
        String username
) {
}
