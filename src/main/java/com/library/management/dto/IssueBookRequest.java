package com.library.management.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

public record IssueBookRequest(@NotNull Integer studentId,
                               @NotNull Integer bookId,
                               @NotNull @FutureOrPresent LocalDate dueDate) {}
