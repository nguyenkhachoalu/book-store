package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_author.AuthorResponse;
import com.project_sem4.book_store.service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController {
    AuthorService authorService;

    @GetMapping
    public ApiResponse<PagedResponse<AuthorResponse>> searchAuthors(@RequestParam(defaultValue = "") String keyword, Pageable pageable) {
        return ApiResponse.<PagedResponse<AuthorResponse>>builder()
                .result(authorService.searchAuthors(keyword, pageable))
                .build();
    }
    @GetMapping("/all")
    public ApiResponse<List<AuthorResponse>> getAllAuthors() {
        return ApiResponse.<List<AuthorResponse>>builder()
                .result(authorService.getAllAuthors())
                .build();
    }
    @PostMapping
    public ApiResponse<AuthorResponse> createAuthor(@RequestParam String name) {
        return ApiResponse.<AuthorResponse>builder()
                .result(authorService.createAuthor(name))
                .build();
    }

    @PutMapping("/{authorId}")
    public ApiResponse<AuthorResponse> updateAuthor(@PathVariable UUID authorId, @RequestParam String name) {
        return ApiResponse.<AuthorResponse>builder()
                .result(authorService.updateAuthor(authorId, name))
                .build();
    }

    @DeleteMapping("/{authorId}")
    public ApiResponse<String> deleteAuthor(@PathVariable UUID authorId) {
        return ApiResponse.<String>builder()
                .result(authorService.deleteAuthor(authorId))
                .build();
    }
}
