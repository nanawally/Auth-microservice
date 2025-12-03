package com.nanawally.Auth_microservice.user.admin;

import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<CustomUser>> getAllUsers() {

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

        if (adminService.deleteUserById(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
