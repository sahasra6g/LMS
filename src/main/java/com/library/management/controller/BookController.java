package com.library.management.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.library.management.dto.*;
import com.library.management.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;
    public BookController(BookService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public BookResponse add(@Valid @RequestBody BookRequest request) {
        return service.add(request);
    }

    @GetMapping
    public List<BookResponse> findAll() { return service.findAll(); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Book deleted successfully";
    }
}
