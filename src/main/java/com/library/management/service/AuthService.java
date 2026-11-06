package com.library.management.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.library.management.dto.*;
import com.library.management.entity.*;
import com.library.management.repository.*;

@Service
public class AuthService {
    private final UserRepository users;
    private final StudentRepository students;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(UserRepository users, StudentRepository students, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       JwtService jwtService) {
        this.users = users;
        this.students = students;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public void signup(SignupRequest request) {
        if (users.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (students.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(Role.STUDENT);
        users.save(user);

        Student student = new Student();
        student.setUserId(user.getUserId());
        student.setFullName(request.fullName());
        student.setEmail(request.email());
        student.setPhone(request.phone());
        students.save(student);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails details = userDetailsService.loadUserByUsername(request.username());
        String role = details.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return new AuthResponse(jwtService.generateToken(details), "Bearer", request.username(), role);
    }
}
