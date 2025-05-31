package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.wallet_request.WalletTransactionRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.entity.Wallet;
import com.project_sem4.book_store.service.WalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {
    WalletService walletService;

    @GetMapping("/user/{userId}")
    public ApiResponse<Wallet> getWalletByUserId(@PathVariable UUID userId) {
        return ApiResponse.<Wallet>builder()
                .result(walletService.getWalletByUserId(userId))
                .build();
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SELLER')")
    public ApiResponse<String> deposit(@RequestBody WalletTransactionRequest request) {
        walletService.deposit(request);
        return ApiResponse.<String>builder().result("Nạp tiền thành công").build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SELLER')")
    public ApiResponse<String> withdraw(@RequestBody WalletTransactionRequest request) {
        walletService.withdraw(request);
        return ApiResponse.<String>builder().result("Rút tiền thành công").build();
    }
}
