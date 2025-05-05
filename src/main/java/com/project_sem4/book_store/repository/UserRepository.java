package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID>, CustomUserRepository  {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
