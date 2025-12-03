package com.nanawally.Auth_microservice.ui;

import com.nanawally.Auth_microservice.config.RabbitConfig;
import com.nanawally.Auth_microservice.rabbitmq.EmailMessageProducer;
import com.nanawally.Auth_microservice.user.CustomUser;
import com.nanawally.Auth_microservice.user.CustomUserRepository;
import com.nanawally.Auth_microservice.user.authority.UserRole;
import com.nanawally.Auth_microservice.user.dto.CustomUserCreationDTO;
import com.nanawally.Auth_microservice.user.dto.CustomUserSelfRegisterDTO;
import com.nanawally.Auth_microservice.user.mapper.CustomUserMapper;
import jakarta.validation.Valid;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UiRestController {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;
    private final EmailMessageProducer emailMessageProducer;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public UiRestController(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder, CustomUserMapper customUserMapper, EmailMessageProducer emailMessageProducer, AmqpTemplate amqpTemplate) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
        this.emailMessageProducer = emailMessageProducer;
        this.amqpTemplate = amqpTemplate;
    }

    @GetMapping("/about")
    public ResponseEntity<String> about(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
        } else {
            return ResponseEntity.ok("Request granted");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutPage(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/login")
                    .build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody CustomUserSelfRegisterDTO dto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("ok", false);
            response.put("message", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return ResponseEntity.badRequest().body(response);
        }

        CustomUser user = customUserMapper.toRegisterEntity(dto);
        user.setPassword(user.getPassword(), passwordEncoder);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRoles(Set.of(UserRole.USER));

        customUserRepository.save(user);

        // Publish RabbitMQ message AFTER successful registration
        /*amqpTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                user.getUsername()  // send username to Email microservice
        );*/

        // Custom message producer class so to not call AmqpTemplate directly.
        emailMessageProducer.sendRegistrationEmailMessage(user.getUsername());

        response.put("ok", true);
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response);
    }

}

