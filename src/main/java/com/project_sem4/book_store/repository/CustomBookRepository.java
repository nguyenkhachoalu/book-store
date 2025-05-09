package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_book.GetPagedBookResponse;
import com.project_sem4.book_store.enum_type.BookSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CustomBookRepository {
    Page<GetPagedBookResponse> searchBooks(String keyword, BookSearchType type,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           Pageable pageable);
}
