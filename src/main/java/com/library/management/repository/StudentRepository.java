package com.library.management.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.library.management.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUserId(Integer userId);
    boolean existsByEmail(String email);
}
