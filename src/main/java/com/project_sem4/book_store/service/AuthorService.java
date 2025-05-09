package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.AuthorMapper;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_author.AuthorResponse;
import com.project_sem4.book_store.entity.Author;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.AuthorRepository;
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
public class AuthorService {
    AuthorRepository authorRepository;
    AuthorMapper authorMapper;
    public List<AuthorResponse> getAllAuthors() {
        try {
            List<Author> authors = authorRepository.findAll();
            return authors.stream()
                    .map(authorMapper::toAuthorResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Get all authors failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public PagedResponse<AuthorResponse> searchAuthors(String keyword, Pageable pageable) {
        try {
            Page<Author> authorPage = authorRepository.findByAuthorNameContainingIgnoreCase(keyword, pageable);
            List<AuthorResponse> content = authorPage.getContent().stream()
                    .map(authorMapper::toAuthorResponse)
                    .toList();

            return PagedResponse.<AuthorResponse>builder()
                    .content(content)
                    .pageNumber(authorPage.getNumber())
                    .pageSize(authorPage.getSize())
                    .totalElements(authorPage.getTotalElements())
                    .totalPages(authorPage.getTotalPages())
                    .hasNext(authorPage.hasNext())
                    .hasPrevious(authorPage.hasPrevious())
                    .build();
        } catch (Exception e) {
            log.error("Get paged author failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public AuthorResponse createAuthor(String name) {
        try {
            Author author = Author.builder()
                    .authorName(name)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            authorRepository.save(author);
            return authorMapper.toAuthorResponse(author);
        } catch (Exception e) {
            log.error("Create author failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public AuthorResponse updateAuthor(UUID authorId, String name) {
        try {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

            author.setAuthorName(name);
            author.setUpdatedAt(LocalDateTime.now());
            authorRepository.save(author);
            return authorMapper.toAuthorResponse(author);
        } catch (Exception e) {
            log.error("Update author failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String deleteAuthor(UUID authorId) {
        try {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

            authorRepository.delete(author);
            return "Xoá tác giả thành công";
        } catch (Exception e) {
            log.error("Delete author failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}