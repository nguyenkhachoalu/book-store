package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface BookRepository extends BaseRepository<Book, UUID> {
}
