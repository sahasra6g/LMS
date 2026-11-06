package com.library.management.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.library.management.dto.*;
import com.library.management.service.BorrowService;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {
    private final BorrowService service;
    public BorrowController(BorrowService service) { this.service = service; }

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public BorrowedBookResponse issue(@Valid @RequestBody IssueBookRequest request,
                                      Authentication authentication) {
        return service.issue(request, authentication.getName());
    }

    @PutMapping("/{borrowId}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public BorrowedBookResponse returnBook(@PathVariable Integer borrowId) {
        return service.returnBook(borrowId);
    }

    @GetMapping("/my-history")
    @PreAuthorize("hasRole('STUDENT')")
    public List<BorrowedBookResponse> myHistory(Authentication authentication) {
        return service.memberHistory(authentication.getName());
    }
}
