package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.request.wallet_request.WalletTransactionRequest;
import com.project_sem4.book_store.entity.Wallet;
import com.project_sem4.book_store.enum_type.TransactionType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletService {
    WalletRepository walletRepository;
    TransactionService transactionService;

    public Wallet getWalletByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }

    public Wallet getWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }

    public Wallet createWallet(UUID userId) {
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .currency("VND")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return walletRepository.save(wallet);
    }

    public void disableWallet(UUID walletId) {
        Wallet wallet = getWalletById(walletId);
        wallet.setIsActive(false);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SELLER')")
    public void deposit(WalletTransactionRequest request) {
       try{
           Wallet wallet = walletRepository.findByUserId(request.getUserId())
                   .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

           validateWallet(wallet);
           validateAmount(request.getAmount());

           wallet.increaseBalance(request.getAmount());
           walletRepository.save(wallet);

           transactionService.log(wallet.getId(), request.getAmount(), TransactionType.DEPOSIT, request.getDescription());
       }catch (AppException e) {
           throw e;
       }catch (Exception e) {
           log.error("Change password failed", e);
           throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
       }
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void withdraw(WalletTransactionRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        validateWallet(wallet);
        validateAmount(request.getAmount());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        wallet.decreaseBalance(request.getAmount());
        walletRepository.save(wallet);

        transactionService.log(wallet.getId(), request.getAmount(), TransactionType.WITHDRAWAL, request.getDescription());
    }

    public void purchase(UUID userId, BigDecimal amount, String description) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        validateWallet(wallet);
        validateAmount(amount);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        wallet.decreaseBalance(amount);
        walletRepository.save(wallet);

        transactionService.log(wallet.getId(), amount, TransactionType.PURCHASE, description);
    }
    private void validateWallet(Wallet wallet) {
        if (!Boolean.TRUE.equals(wallet.getIsActive())) {
            throw new AppException(ErrorCode.WALLET_DISABLED);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_AMOUNT);
        }
    }
}
