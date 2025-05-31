package com.project_sem4.book_store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_sem4.book_store.dto.request.book_request.BookCreateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_book.BookResponse;
import com.project_sem4.book_store.dto.response.data_response_book.GetPagedBookResponse;
import com.project_sem4.book_store.enum_type.BookSearchType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.service.BookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BookResponse> createBook(
            @RequestPart("data") String jsonData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            BookCreateRequest request = new ObjectMapper().readValue(jsonData, BookCreateRequest.class);
            return ApiResponse.<BookResponse>builder()
                    .result(bookService.createBook(request, imageFile))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INVALID_DATA_FORMAT);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ApiResponse<PagedResponse<GetPagedBookResponse>> getBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "TITLE") BookSearchType type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        return ApiResponse.<PagedResponse<GetPagedBookResponse>>builder()
                .result(bookService.searchBooks(keyword, type, minPrice, maxPrice, pageable))
                .build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{bookId}")
    public ApiResponse<GetPagedBookResponse> getBookById(@PathVariable UUID bookId) {
        return ApiResponse.<GetPagedBookResponse>builder()
                .result(bookService.getBookById(bookId))
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{bookId}")
    public ApiResponse<String> deleteBook(@PathVariable UUID bookId) {
        return ApiResponse.<String>builder()
                .result(bookService.deleteBook(bookId))
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping(value = "/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BookResponse> updateBook(
            @PathVariable UUID bookId,
            @RequestPart("data") String jsonData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            BookCreateRequest request = new ObjectMapper().readValue(jsonData, BookCreateRequest.class);
            return ApiResponse.<BookResponse>builder()
                    .result(bookService.updateBook(bookId, request, imageFile))
                    .build();
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INVALID_DATA_FORMAT);
        }
    }
}
