package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllActiveBooks() {
        return bookRepository.findByIsActiveTrue();
    }

    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
    }

    public List<Book> getBooksByCategory(Integer categoryId) {
        return bookRepository.findByCategoryCategoryIdAndIsActiveTrue(categoryId);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }

    public List<Book> getNewBooks() {
        return bookRepository.findNewBooks();
    }

    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Integer bookId, Book bookDetails) {
        Book book = getBookById(bookId);
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setCategory(bookDetails.getCategory());
        book.setDescription(bookDetails.getDescription());
        book.setPrice(bookDetails.getPrice());
        book.setStockQuantity(bookDetails.getStockQuantity());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setPages(bookDetails.getPages());
        book.setLanguage(bookDetails.getLanguage());
        book.setCoverImageUrl(bookDetails.getCoverImageUrl());
        
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Integer bookId) {
        Book book = getBookById(bookId);
        book.setIsActive(false);
        bookRepository.save(book);
    }

    @Transactional
    public void updateStock(Integer bookId, Integer quantity) {
        Book book = getBookById(bookId);
        book.setStockQuantity(book.getStockQuantity() - quantity);
        if (book.getStockQuantity() < 0) {
            throw new RuntimeException("Insufficient stock for book: " + book.getTitle());
        }
        bookRepository.save(book);
    }
}
