package com.library.management.dto;

import java.time.LocalDate;

public record BorrowedBookResponse(Integer borrowId, Integer bookId, String bookName,
                                    LocalDate issueDate, LocalDate dueDate,
                                    LocalDate returnDate, String status) {}
