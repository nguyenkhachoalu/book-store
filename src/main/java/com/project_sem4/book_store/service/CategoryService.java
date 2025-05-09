package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.CategoryMapper;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_category.CategoryResponse;
import com.project_sem4.book_store.entity.BookCategory;
import com.project_sem4.book_store.entity.Category;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.BookCategoryRepository;
import com.project_sem4.book_store.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    BookCategoryRepository bookCategoryRepository;
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return categories.stream()
                    .map(categoryMapper::toCategoryResponse)
                    .toList();
        } catch (AppException e) {
            throw e;
        }catch (Exception e) {
            log.error("Get all categories failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public PagedResponse<com.project_sem4.book_store.dto.response.data_response_category.CategoryResponse> searchCategories(String keyword, Pageable pageable) {
       try{
           Page<Category> categoryPage = categoryRepository.findByCategoryNameContainingIgnoreCase(keyword, pageable);
           List<CategoryResponse> content = categoryPage.getContent().stream()
                   .map(categoryMapper::toCategoryResponse)
                   .toList();

           return PagedResponse.<CategoryResponse>builder()
                   .content(content)
                   .pageNumber(categoryPage.getNumber())
                   .pageSize(categoryPage.getSize())
                   .totalElements(categoryPage.getTotalElements())
                   .totalPages(categoryPage.getTotalPages())
                   .hasNext(categoryPage.hasNext())
                   .hasPrevious(categoryPage.hasPrevious())
                   .build();
       }catch (AppException e) {
           throw e;
       }catch (Exception e){
           log.error("Get paged category failed", e);
           throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
       }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public CategoryResponse createCategory(String name) {
        try {
            Category category = Category.builder()
                    .categoryName(name)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            categoryRepository.save(category);
            return categoryMapper.toCategoryResponse(category);
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Create category failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public CategoryResponse updateCategory(UUID categoryId, String name) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            category.setCategoryName(name);
            category.setUpdatedAt(LocalDateTime.now());
            categoryRepository.save(category);
            return categoryMapper.toCategoryResponse(category);
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Update category failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String deleteCategory(UUID categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

            // Xóa các liên kết BookCategory trước
            List<BookCategory> bookCategories = bookCategoryRepository.findByCategoryId(categoryId);
            bookCategoryRepository.deleteAll(bookCategories);

            // Sau đó xóa Category
            categoryRepository.delete(category);

            return "Xóa danh mục thành công";
        } catch (AppException e) {
            throw e;
        }catch (Exception e) {
            log.error("Delete category failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
