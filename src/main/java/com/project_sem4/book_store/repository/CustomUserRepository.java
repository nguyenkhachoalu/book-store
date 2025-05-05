package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.enum_type.UserSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {
    Page<User> searchUsers(String keyword, UserSearchType type, Pageable pageable);
}
