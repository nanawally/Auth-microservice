package com.nanawally.Auth_microservice.view;

import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.authority.UserRole;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import com.nanawally.Auth_microservice.user.CustomUserRepository;
import com.nanawally.Auth_microservice.user.mapper.CustomUserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class CustomViewController {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;

    @Autowired
    public CustomViewController(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder, CustomUserMapper customUserMapper) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "logout";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminpage";
    }

    @GetMapping("/user")
    public String userPage() {
        return "userpage";
    }

    // Responsible for inserting CustomUser Entity (otherwise DTO)
    @GetMapping("/register")
    public String registerPage(Model model) {
        // Best practice: id aka Attribute name, should be the same as object name
        model.addAttribute("customUser", new CustomUser());
        return "registerpage";
    }

    // Handles business logic coming from SUBMIT FORM
    @PostMapping("/register")
    public String registerUser(
            @Valid CustomUserCreationDTO customUserCreationDTO, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "registerpage";
        }

        CustomUser customUser = customUserMapper.toEntity(customUserCreationDTO);

        customUser.setPassword(customUser.getPassword(), passwordEncoder);

        customUser.setAccountNonExpired(true);
        customUser.setAccountNonLocked(true);
        customUser.setCredentialsNonExpired(true);
        customUser.setEnabled(true);

        customUser.setRoles(
                Set.of(UserRole.USER)
        );

        System.out.println("Saving user...");
        customUserRepository.save(customUser);

        return "redirect:/login";
    }

}
