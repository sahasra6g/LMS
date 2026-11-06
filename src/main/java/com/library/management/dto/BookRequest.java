package com.library.management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(@NotBlank @Size(max = 150) String name,
                          @Min(1) Integer totalCopies) {}
