package com.library.management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.library.management.dto.BookRequest;
import com.library.management.dto.BookResponse;
import com.library.management.entity.Book;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowRecordRepository;

@Service
public class BookService {
    private final BookRepository books;
    private final BorrowRecordRepository borrows;

    public BookService(BookRepository books, BorrowRecordRepository borrows) {
        this.books = books;
        this.borrows = borrows;
    }

    @Transactional
    public BookResponse add(BookRequest request) {
        if (request.totalCopies() == null || request.totalCopies() < 1)
            throw new IllegalArgumentException("Total copies must be at least 1");
        Book book = new Book();
        book.setName(request.name());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies());
        return toResponse(books.save(book));
    }

    public List<BookResponse> findAll() {
        return books.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void delete(Integer id) {
        Book book = books.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        if (borrows.existsByBookIdAndStatus(id, "BORROWED"))
            throw new IllegalStateException("Cannot delete a book with active borrow records");
        books.delete(book);
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getName(),
            book.getTotalCopies(), book.getAvailableCopies());
    }
}
