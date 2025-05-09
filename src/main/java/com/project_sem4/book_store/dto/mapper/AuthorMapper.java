package com.project_sem4.book_store.dto.mapper;


import com.project_sem4.book_store.dto.response.data_response_author.AuthorResponse;
import com.project_sem4.book_store.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorResponse toAuthorResponse(Author author);
}
