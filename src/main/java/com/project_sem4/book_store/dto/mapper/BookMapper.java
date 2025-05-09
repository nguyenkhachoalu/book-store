package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.response.data_response_book.BookResponse;
import com.project_sem4.book_store.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookResponse toBookResponse(Book book);
}
