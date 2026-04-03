package com.eventhub.api.service;

import com.eventhub.api.dto.auth.LoginRequest;
import com.eventhub.api.dto.auth.LoginResponse;
import com.eventhub.api.dto.auth.PasswordResetRequest;
import com.eventhub.api.dto.auth.RegisterRequest;
import com.eventhub.api.exception.DuplicateResourceException;
import com.eventhub.api.exception.ResourceNotFoundException;
import com.eventhub.api.model.PasswordResetToken;
import com.eventhub.api.model.Role;
import com.eventhub.api.model.User;
import com.eventhub.api.repository.PasswordResetTokenRepository;
import com.eventhub.api.repository.RoleRepository;
import com.eventhub.api.repository.UserRepository;
import com.eventhub.api.security.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordResetTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;

    }

    public LoginResponse register(RegisterRequest request) {
        // check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists" + request.getEmail()
            );
        }

        // Get default USER role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found"));

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User saved = userRepository.save(user);

        // Generate token
        String token = jwtUtils.generateToken(
                saved.getEmail(), "ROLE_USER");

        return new LoginResponse(token, saved.getEmail(),
                saved.getName(), "ROLE_USER");
    }

    public LoginResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(
                        "Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException(
                    "Invalid email or password");
        }

        // Check enabled
        if (!user.getEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }

        // Get primary role
        String role = user.getRoles().stream()
                .map(Role::getName)
                .findFirst()
                .orElse("ROLE_USER");

        // Generate token
        String token = jwtUtils.generateToken(user.getEmail(), role);

        return new LoginResponse(token, user.getEmail(),
                user.getName(), role);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));

        // Delete old tokens
        tokenRepository.deleteByUserId(user.getId());

        // Create new token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiry(LocalDateTime.now().plusHours(1));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        // In real app send email - for now just log it
        System.out.println("Password reset token: "
                + resetToken.getToken());
    }

    public void resetPassword(PasswordResetRequest request) {
        PasswordResetToken resetToken = tokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invalid reset token"));

        // Check if expired
        if (resetToken.isExpired()) {
            throw new BadCredentialsException("Reset token has expired");
        }

        // Check if already used
        if (resetToken.getUsed()) {
            throw new BadCredentialsException(
                    "Reset token has already been used");
        }

        // Update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

}
