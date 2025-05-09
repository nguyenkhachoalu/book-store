package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.request.transaction_request.TransactionCreateRequest;
import com.project_sem4.book_store.entity.Transaction;
import com.project_sem4.book_store.enum_type.TransactionType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.TransactionRepository;
import com.project_sem4.book_store.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {

    TransactionRepository transactionRepository;
    WalletRepository walletRepository;


    public Transaction log(UUID walletId, BigDecimal amount, TransactionType type, String description) {
        walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .walletId(walletId)
                .amount(amount)
                .transactionType(type)
                .description(description)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        return transactionRepository.save(transaction);
    }



    public List<Transaction> getTransactionsByWalletId(UUID walletId) {
        walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        return transactionRepository.findAllByWalletId(walletId);
    }
}