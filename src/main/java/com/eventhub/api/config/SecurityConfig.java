package com.eventhub.api.config;

import com.eventhub.api.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //  Swagger - must be first
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/events/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/categories/**").permitAll()

                        .requestMatchers("/api/ai/**").permitAll()

                         //  USER role
                        .requestMatchers("/api/registrations/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        //  ADMIN role
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/events/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/events/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/events/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/users/**")         .hasAuthority("ROLE_ADMIN")
                                        .anyRequest().authenticated()
                        )
                        .addFilterBefore(jwtAuthFilter,
                                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





}