package com.yummypet.config;

import com.yummypet.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {        
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Auth endpoints - Public access
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/auth/forgot-password",
                    "/api/auth/reset-password",
                    "/api/auth/reset-password/**"
                ).permitAll()
                .requestMatchers("/api/auth/create-user").hasAuthority("admin")
                
                // Public product/pet/service endpoints
                .requestMatchers(
                    "/api/products/**",
                    "/api/pets/**",
                    "/api/services/**",
                    "/api/categories/**"
                ).permitAll()
                
                // Voucher endpoints
                .requestMatchers(
                    "/api/vouchers/active",
                    "/api/vouchers/valid",
                    "/api/vouchers/validate"
                ).permitAll()
                .requestMatchers("/api/vouchers/**").hasAuthority("admin")
                
                // Employee management
                .requestMatchers("/api/employees").hasAuthority("admin")
                .requestMatchers("/api/employees/{id}/**", "/api/employees/code/**")
                    .hasAnyAuthority("admin", "staff")
                
                // Customer management
                .requestMatchers("/api/customers").hasAnyAuthority("admin", "staff")
                .requestMatchers("/api/customers/**").hasAnyAuthority("customer", "staff", "admin")
                
                // Other endpoints
                .requestMatchers("/api/loyalty-points/**", "/api/orders/**", "/api/cart/**")
                    .hasAnyAuthority("customer", "staff", "admin")
                
                // Admin specific endpoints
                .requestMatchers("/api/admin/**", "/api/statistics/**").hasAuthority("admin")
                
                // Default - require authentication
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
