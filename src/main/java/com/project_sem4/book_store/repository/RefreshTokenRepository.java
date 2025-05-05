package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends BaseRepository<RefreshToken, UUID>{
    Optional<RefreshToken> findByToken(String token);
}
