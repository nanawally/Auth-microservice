package com.nanawally.Auth_microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AppCorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://172.0.0.1:3000",
                "https://todo-application-git-master-anna-wallstroms-projects.vercel.app",
                "https://todo-application-dolz4x1kd-anna-wallstroms-projects.vercel.app",
                "https://nalix.vercel.app"
        ));
        corsConfiguration.setAllowedHeaders(List.of("*"));          // new, trying out
        corsConfiguration.setExposedHeaders(List.of("Set-Cookie")); // new, trying out
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));
        corsConfiguration.setAllowCredentials(true); // Send Cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/register", corsConfiguration);
        source.registerCorsConfiguration("/api/v1/who-am-i", corsConfiguration);
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
