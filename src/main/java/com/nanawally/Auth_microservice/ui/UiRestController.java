package com.nanawally.Auth_microservice.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UiRestController {

    /*
    @GetMapping("/")
    public ResponseEntity<String> homepage() {

    }*/

    @GetMapping("/about")
    public ResponseEntity<String> about(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
        } else {
            return ResponseEntity.ok("Request granted");
        }
    }
}


/*
 * have if (isOk) for all endpoints
 * return responseentitys
 * */
