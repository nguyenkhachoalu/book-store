package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends BaseRepository<Transaction, UUID> {
    List<Transaction> findAllByWalletId(UUID walletId);
}
