package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.HttpServletResponse; // For Spring Boot 3+ (Jakarta EE)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // private final CustomOAuth2UserService customOAuth2UserService;

    // public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
    //     this.customOAuth2UserService = customOAuth2UserService;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(org.springframework.security.config.Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/", "/login", "/register", "/css/**", "/js/**", "/images/**",
                    "/api/public/**", "/api/user/register",
                    "/oauth2/**", "/login/**",
                    "/api/auth/login"
                ).permitAll()
                .requestMatchers("/api/user/auth/forgot-password").permitAll()
                .requestMatchers("/api/user/test-mailtrap").permitAll()

                // Allow USER, LECTURER, ADMIN to access these two endpoints
                .requestMatchers("/api/challenge/**").hasAnyRole("USER", "LECTURER", "ADMIN")

                .requestMatchers("/api/challenge/solve/**").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/challenge/get/public/all").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/category/get/all/with-challenge-counts").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/challenge/get/category/public/**").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/challenge/get/difficulty/**").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/challenge/get/name/**").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/user/delete").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/user/me", "/api/progress/**").hasAnyRole("USER", "LECTURER", "ADMIN")


                // LECTURER, ADMIN: can access category and feedback admin endpoints
                .requestMatchers("/api/feedback/post/**").hasAnyRole("USER", "LECTURER", "ADMIN")
                .requestMatchers("/api/category/**", "/api/feedback/**").hasAnyRole("LECTURER", "ADMIN")
                .requestMatchers("/api/challenge/**").hasAnyRole("LECTURER", "ADMIN")

                // ADMIN only: user management endpoints
                .requestMatchers(
                    "/api/user/get/all",
                    "/api/user/edit/**",
                    "/api/user/get/**"
                ).hasRole("ADMIN")
                .requestMatchers(
                    "/api/user/total"
                ).hasAnyRole("ADMIN", "LECTURER")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable()) // Use your custom /api/auth/login endpoint
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\"}");
                })
            );

        return http.build();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // This allows PATCH, PUT, DELETE, etc.
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}