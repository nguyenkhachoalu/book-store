package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.BookMapper;
import com.project_sem4.book_store.dto.request.book_request.BookCreateRequest;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_book.BookResponse;
import com.project_sem4.book_store.dto.response.data_response_book.GetPagedBookResponse;
import com.project_sem4.book_store.dto.response.data_response_category.CategoryNameAndIdResponse;
import com.project_sem4.book_store.entity.Book;
import com.project_sem4.book_store.entity.BookCategory;
import com.project_sem4.book_store.entity.Category;
import com.project_sem4.book_store.enum_type.BookSearchType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.AuthorRepository;
import com.project_sem4.book_store.repository.BookCategoryRepository;
import com.project_sem4.book_store.repository.BookRepository;
import com.project_sem4.book_store.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    BookRepository bookRepository;
    BookCategoryRepository bookCategoryRepository;
    CategoryRepository categoryRepository;
    AuthorRepository authorRepository;
    BookMapper bookMapper;

    FileStorageService fileStorageService;

    @Value("${app.base-url}")
    @NonFinal
    String baseUrl;


    public PagedResponse<GetPagedBookResponse> searchBooks(String keyword,
                                                           BookSearchType type,
                                                           BigDecimal minPrice,
                                                           BigDecimal maxPrice,
                                                           Pageable pageable) {
        try {
            Page<GetPagedBookResponse> bookPage = bookRepository
                    .searchBooks(keyword, type, minPrice, maxPrice, pageable);
            List<GetPagedBookResponse> updatedBooks = bookPage.getContent().stream()
                    .peek(book -> {
                        if (book.getCoverImage() != null) {
                            book.setCoverImage(baseUrl + "/uploads/books/" + book.getCoverImage());
                        }
                    })
                    .collect(Collectors.toList());
            return PagedResponse.<GetPagedBookResponse>builder()
                    .content(updatedBooks)
                    .pageNumber(bookPage.getNumber())
                    .pageSize(bookPage.getSize())
                    .totalElements(bookPage.getTotalElements())
                    .totalPages(bookPage.getTotalPages())
                    .hasNext(bookPage.hasNext())
                    .hasPrevious(bookPage.hasPrevious())
                    .build();
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Search paged books failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public GetPagedBookResponse getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        List<CategoryNameAndIdResponse> categories = book.getBookCategories().stream()
                .map(BookCategory::getCategory)
                .map(cat -> new CategoryNameAndIdResponse(cat.getId(), cat.getCategoryName()))
                .collect(Collectors.toList());

        return GetPagedBookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthor().getAuthorName())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .coverImage(book.getCoverImage() != null ? baseUrl + "/uploads/books/" + book.getCoverImage() : null)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .categories(categories)
                .build();
    }
    public BookResponse createBook(BookCreateRequest request, MultipartFile imageFile) {
        try {
            validateRequest(request);

            String imageFilename = storeBookImage(imageFile, null);

            Book book = Book.builder()
                    .title(request.getTitle())
                    .authorId(request.getAuthorId())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .description(request.getDescription())
                    .coverImage(imageFilename)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();

            bookRepository.save(book);

            List<BookCategory> bookCategories = request.getCategoryIds().stream()
                    .map(catId -> new BookCategory(book.getId(), catId))
                    .toList();
            bookCategoryRepository.saveAll(bookCategories);

            return bookMapper.toBookResponse(book);
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Create book failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public BookResponse updateBook(UUID bookId, BookCreateRequest request, MultipartFile imageFile) {
        try {
            validateRequest(request);

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

            String newImageFilename = storeBookImage(imageFile, book.getCoverImage());

            book.setTitle(request.getTitle());
            book.setAuthorId(request.getAuthorId());
            book.setPrice(request.getPrice());
            book.setQuantity(request.getQuantity());
            book.setDescription(request.getDescription());
            book.setCoverImage(newImageFilename);
            book.setUpdatedAt(LocalDateTime.now());

            bookRepository.save(book);

            // Xử lý category
            List<BookCategory> currentCategories = bookCategoryRepository.findByBookId(bookId);
            Set<UUID> currentCategoryIds = currentCategories.stream().map(BookCategory::getCategoryId).collect(Collectors.toSet());
            Set<UUID> newCategoryIds = new HashSet<>(request.getCategoryIds());

            // Xoá các category cũ không còn trong danh sách mới
            List<BookCategory> toDelete = currentCategories.stream()
                    .filter(bc -> !newCategoryIds.contains(bc.getCategoryId()))
                    .toList();
            bookCategoryRepository.deleteAll(toDelete);

            // Thêm các category mới chưa có
            List<BookCategory> toAdd = newCategoryIds.stream()
                    .filter(catId -> !currentCategoryIds.contains(catId))
                    .map(catId -> new BookCategory(bookId, catId))
                    .toList();
            bookCategoryRepository.saveAll(toAdd);

            return bookMapper.toBookResponse(book);
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Update book failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public String deleteBook(UUID bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

            // Xóa các liên kết BookCategory trước
            List<BookCategory> bookCategories = bookCategoryRepository.findByBookId(bookId);
            bookCategoryRepository.deleteAll(bookCategories);

            // Sau đó xóa Book
            bookRepository.delete(book);

            return "Xóa sách thành công";
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Delete book failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }





    private void validateRequest(BookCreateRequest request) {
        // Kiểm tra tác giả tồn tại
        authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        // Kiểm tra tất cả category đều tồn tại
        List<UUID> categoryIds = request.getCategoryIds();
        List<UUID> existingCategoryIds = categoryRepository.findAllById(categoryIds).stream()
                .map(Category::getId)
                .toList();

        if (existingCategoryIds.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }


    public String storeBookImage(MultipartFile imageFile, String oldFilename) {
        if (imageFile == null || imageFile.isEmpty()) return oldFilename;

        if (oldFilename != null) {
            try {
                Files.deleteIfExists(Paths.get("uploads/books", oldFilename));
            } catch (IOException e) {
                log.warn("Không thể xóa ảnh sách cũ: {}", oldFilename);
            }
        }

        return fileStorageService.storeFile(imageFile, "books");
    }
}
