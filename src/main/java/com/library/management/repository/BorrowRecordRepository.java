package com.library.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.library.management.entity.BorrowRecord;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    boolean existsByBookIdAndStatus(Integer bookId, String status);
    List<BorrowRecord> findByStudentIdOrderByIssueDateDesc(Integer studentId);
    List<BorrowRecord> findByStudentIdAndStatus(Integer studentId, String status);
}
