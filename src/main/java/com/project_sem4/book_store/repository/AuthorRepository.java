package com.project_sem4.book_store.repository;
import com.project_sem4.book_store.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
public interface AuthorRepository extends BaseRepository<Author , UUID>{
    Page<Author> findByAuthorNameContainingIgnoreCase(String keyword, Pageable pageable);
}
