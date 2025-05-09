package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface CategoryRepository extends BaseRepository<Category, UUID> {
    Page<Category> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}
