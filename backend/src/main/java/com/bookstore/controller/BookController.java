package com.bookstore.controller;

import com.bookstore.dto.ApiResponse;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookService.getAllActiveBooks();
        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Integer id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(ApiResponse.success("Book found", book));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Book>>> getBooksByCategory(@PathVariable Integer categoryId) {
        List<Book> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(@RequestParam String keyword) {
        List<Book> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", books));
    }

    @GetMapping("/new")
    public ResponseEntity<ApiResponse<List<Book>>> getNewBooks() {
        List<Book> books = bookService.getNewBooks();
        return ResponseEntity.ok(ApiResponse.success("New books retrieved", books));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.ok(ApiResponse.success("Book created successfully", createdBook));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(ApiResponse.success("Book updated successfully", updatedBook));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Integer id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok(ApiResponse.success("Book deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
