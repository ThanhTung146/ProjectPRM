package com.bookstore.service;

import com.bookstore.dto.AuthResponse;
import com.bookstore.dto.LoginRequest;
import com.bookstore.dto.RegisterRequest;
import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(User.UserRole.CUSTOMER);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getEmail());

        return new AuthResponse(
                token,
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
        );
    }
}
