package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends BaseRepository<Wallet, UUID>{
    Optional<Wallet> findByUserId(UUID userId);
}
