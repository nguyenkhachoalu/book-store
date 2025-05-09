package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.response.data_response_category.CategoryResponse;
import com.project_sem4.book_store.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
