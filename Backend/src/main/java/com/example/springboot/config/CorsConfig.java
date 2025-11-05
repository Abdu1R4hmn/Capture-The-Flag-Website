package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173",
                    "https://localhost:5173",
                    "https://ctf.jibna.live"
                )
                .allowedOriginPatterns(
                    "https://*.ctf.jibna.live",
                    "https://*.cloudflareaccess.com",
                    "https://*.trycloudflare.com",
                    "https://*.ngrok.io",
                    "https://*.ngrok-free.app",
                    "https://*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        
        // Allow multiple origins for development and production
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("https://localhost:5173");
        configuration.addAllowedOrigin("https://ctf.jibna.live");
        configuration.addAllowedOriginPattern("https://*.ctf.jibna.live");
        configuration.addAllowedOriginPattern("https://*.cloudflareaccess.com");
        configuration.addAllowedOriginPattern("https://*.trycloudflare.com");
        configuration.addAllowedOriginPattern("https://*.ngrok.io");
        configuration.addAllowedOriginPattern("https://*.ngrok-free.app");
        // Allow any HTTPS origin for development (remove in production)
        configuration.addAllowedOriginPattern("https://*");
        
        // Allow all headers and methods
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        
        // Expose headers that might be needed
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Content-Type");
        configuration.addExposedHeader("X-Requested-With");
        configuration.addExposedHeader("Accept");
        configuration.addExposedHeader("Origin");
        configuration.addExposedHeader("Access-Control-Request-Method");
        configuration.addExposedHeader("Access-Control-Request-Headers");
        
        // Set max age for preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 