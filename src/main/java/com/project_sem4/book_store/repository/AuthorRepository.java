package com.project_sem4.book_store.repository;
import com.project_sem4.book_store.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface AuthorRepository extends BaseRepository<Author , UUID>{
}
