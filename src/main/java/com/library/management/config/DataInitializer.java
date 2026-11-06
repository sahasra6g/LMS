package com.library.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.library.management.entity.Role;
import com.library.management.entity.User;
import com.library.management.repository.UserRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner createConfiguredLibrarian(
            UserRepository users, PasswordEncoder encoder,
            @Value("${app.librarian.username}") String username,
            @Value("${app.librarian.password}") String password) {
        return args -> {
            if (!users.existsByUsername(username)) {
                User librarian = new User();
                librarian.setUsername(username);
                librarian.setPassword(encoder.encode(password));
                librarian.setRole(Role.LIBRARIAN);
                users.save(librarian);
            }
        };
    }
}
