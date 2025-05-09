package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.book_request.BookCreateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_book.BookResponse;
import com.project_sem4.book_store.dto.response.data_response_book.GetPagedBookResponse;
import com.project_sem4.book_store.enum_type.BookSearchType;
import com.project_sem4.book_store.service.BookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @PostMapping
    public ApiResponse<BookResponse> createBook(@RequestBody @Valid BookCreateRequest request) {
        return ApiResponse.<BookResponse>builder()
                .result(bookService.createBook(request))
                .build();
    }

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
    @GetMapping("/{bookId}")
    public ApiResponse<GetPagedBookResponse> getBookById(@PathVariable UUID bookId) {
        return ApiResponse.<GetPagedBookResponse>builder()
                .result(bookService.getBookById(bookId))
                .build();
    }
    @DeleteMapping("/{bookId}")
    public ApiResponse<String> deleteBook(@PathVariable UUID bookId) {
        return ApiResponse.<String>builder()
                .result(bookService.deleteBook(bookId))
                .build();
    }

    @PutMapping("/{bookId}")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable UUID bookId,
            @RequestBody @Valid BookCreateRequest request
    ) {
        return ApiResponse.<BookResponse>builder()
                .result(bookService.updateBook(bookId, request))
                .build();
    }
}
