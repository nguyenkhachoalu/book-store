package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.BookCategory;
import com.project_sem4.book_store.entity.BookCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
    List<BookCategory> findByBookId(UUID userId);
    List<BookCategory> findByCategoryId(UUID roleId);
}
