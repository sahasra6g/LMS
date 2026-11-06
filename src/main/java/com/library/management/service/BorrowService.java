package com.library.management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.library.management.dto.*;
import com.library.management.entity.*;
import com.library.management.repository.*;

@Service
public class BorrowService {
    private final BookRepository books;
    private final StudentRepository students;
    private final UserRepository users;
    private final BorrowRecordRepository borrows;

    public BorrowService(BookRepository books, StudentRepository students,
                         UserRepository users, BorrowRecordRepository borrows) {
        this.books = books;
        this.students = students;
        this.users = users;
        this.borrows = borrows;
    }

    @Transactional
    public BorrowedBookResponse issue(IssueBookRequest request, String librarianUsername) {
        Student student = students.findById(request.studentId())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Book book = books.findById(request.bookId())
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0)
            throw new IllegalStateException("No available copies of this book");
        if (request.dueDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Due date cannot be in the past");

        User librarian = users.findByUsername(librarianUsername)
            .orElseThrow(() -> new IllegalStateException("Configured librarian account not found"));

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        books.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setStudentId(student.getStudentId());
        record.setBookId(book.getId());
        record.setIssuedBy(librarian.getUserId());
        record.setIssueDate(LocalDate.now());
        record.setDueDate(request.dueDate());
        record.setStatus("BORROWED");
        BorrowRecord saved = borrows.save(record);
        return toResponse(saved, book.getName());
    }

    @Transactional
    public BorrowedBookResponse returnBook(Integer borrowId) {
        BorrowRecord record = borrows.findById(borrowId)
            .orElseThrow(() -> new IllegalArgumentException("Borrow record not found"));
        if (!"BORROWED".equals(record.getStatus()))
            throw new IllegalStateException("This book has already been returned");
        Book book = books.findById(record.getBookId())
            .orElseThrow(() -> new IllegalStateException("Book record not found"));

        record.setReturnDate(LocalDate.now());
        record.setStatus("RETURNED");
        book.setAvailableCopies(Math.min(book.getTotalCopies(), book.getAvailableCopies() + 1));
        borrows.save(record);
        books.save(book);
        return toResponse(record, book.getName());
    }

    public List<BorrowedBookResponse> memberHistory(String username) {
        User user = users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Student student = students.findByUserId(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Member profile not found"));
        return borrows.findByStudentIdOrderByIssueDateDesc(student.getStudentId()).stream()
            .map(record -> {
                String bookName = books.findById(record.getBookId())
                    .map(Book::getName).orElse("Book no longer exists");
                return toResponse(record, bookName);
            }).toList();
    }

    private BorrowedBookResponse toResponse(BorrowRecord record, String bookName) {
        return new BorrowedBookResponse(record.getBorrowId(), record.getBookId(), bookName,
            record.getIssueDate(), record.getDueDate(), record.getReturnDate(), record.getStatus());
    }
}
