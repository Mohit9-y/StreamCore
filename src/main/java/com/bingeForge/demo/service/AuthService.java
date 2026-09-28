package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.auth.AuthResponse;
import com.bingeForge.demo.dto.auth.LoginRequest;
import com.bingeForge.demo.dto.auth.UserRegisterRequest;
import com.bingeForge.demo.dto.user.UserResponse;
import com.bingeForge.demo.entity.User;
import com.bingeForge.demo.mapper.UserMapper;
import com.bingeForge.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService,
            UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public AuthResponse register(UserRegisterRequest request) {
        User user = userService.registerUser(request);

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.identifier(), // Corrected for Java record
                        request.password()    // Corrected for Java record
                )
        );

        User user = (User) authentication.getPrincipal();

        // Null check to satisfy the IDE's safety warning
        if (user == null) {
            throw new IllegalStateException("Authentication failed: User principal is null");
        }

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
}