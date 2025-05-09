package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_category.CategoryResponse;
import com.project_sem4.book_store.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping
    public ApiResponse<PagedResponse<CategoryResponse>> searchCategories(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Pageable pageable) {
        return ApiResponse.<PagedResponse<CategoryResponse>>builder()
                .result(categoryService.searchCategories(keyword, pageable))
                .build();
    }
    @GetMapping("/all")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestParam String name) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(name))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @RequestParam String name) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, name))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable UUID id) {
        return ApiResponse.<String>builder()
                .result(categoryService.deleteCategory(id))
                .build();
    }
}
