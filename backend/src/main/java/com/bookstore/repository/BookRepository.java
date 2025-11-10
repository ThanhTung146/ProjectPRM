package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    List<Book> findByIsActiveTrue();
    
    List<Book> findByCategoryCategoryIdAndIsActiveTrue(Integer categoryId);
    
    List<Book> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title);
    
    List<Book> findByAuthorContainingIgnoreCaseAndIsActiveTrue(String author);
    
    Optional<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE b.isActive = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> searchBooks(@Param("keyword") String keyword);
    
    @Query("SELECT b FROM Book b WHERE b.isActive = true ORDER BY b.createdAt DESC")
    List<Book> findNewBooks();
}
