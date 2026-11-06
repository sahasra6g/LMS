package com.library.management.dto;

public record BookResponse(Integer id, String name, Integer totalCopies, Integer availableCopies) {}
