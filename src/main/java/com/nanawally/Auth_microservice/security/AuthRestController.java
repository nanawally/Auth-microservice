package com.nanawally.Auth_microservice.security;

import com.nanawally.Auth_microservice.config.RabbitConfig;
import com.nanawally.Auth_microservice.user.CustomUserDetails;
import com.nanawally.Auth_microservice.user.dto.CustomUserLoginDTO;
import com.nanawally.Auth_microservice.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, AmqpTemplate amqpTemplate) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.amqpTemplate = amqpTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody CustomUserLoginDTO customUserLoginDTO,
            HttpServletResponse response
    ) {
        logger.debug("Attempting authentication for user: {}", customUserLoginDTO.username());

        // Step 1: Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserLoginDTO.username(),
                        customUserLoginDTO.password())
        );

        // 🧩 DEBUG: Print full Authentication result
        System.out.println("\n========= AUTHENTICATION RESULT =========");
        System.out.println("Class: " + authentication.getClass().getSimpleName());
        System.out.println("Authenticated: " + authentication.isAuthenticated());

        Object principal = authentication.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getSimpleName());
        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("  Username: " + userDetails.getUsername());
            System.out.println("  Authorities: " + userDetails.getAuthorities());
            System.out.println("  Account Non Locked: " + userDetails.isAccountNonLocked());
            System.out.println("  Account Enabled: " + userDetails.isEnabled());
            System.out.println("  Password (hashed): " + userDetails.getPassword());
        } else {
            System.out.println("Principal value: " + principal);
        }

        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("=========================================\n");

        // Step 2: Extract your custom principal
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // Step 3: Generate JWT using your domain model (now includes roles)
        String token = jwtUtils.generateJwtToken(customUserDetails.getCustomUser());

        // Step 4: Set cookie
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ✅ change to true in production (HTTPS only)
        cookie.setAttribute("SameSite", "Lax"); // CSRF protection
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hour
        response.addCookie(cookie);

        logger.info("Authentication successful for user: {}", customUserLoginDTO.username());

        // RabbitMQ
        amqpTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                "User Logged in, todo: send email to user to alert them of login from weird IP addresses"
        );

        // Step 5: Return token - Optional
        return ResponseEntity.ok(Map.of(
                "username", customUserLoginDTO.username(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }
}
