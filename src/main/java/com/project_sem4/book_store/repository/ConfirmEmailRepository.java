package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.ConfirmEmail;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmEmailRepository extends BaseRepository<ConfirmEmail, UUID>{
    Optional<ConfirmEmail> findByConfirmCode(String confirmCode);
}
