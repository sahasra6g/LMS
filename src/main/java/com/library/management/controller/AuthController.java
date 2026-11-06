package com.library.management.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.library.management.dto.*;
import com.library.management.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return "Member registered successfully. You can now log in.";
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
