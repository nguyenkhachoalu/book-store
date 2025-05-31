package com.project_sem4.book_store.controller;


import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.entity.Transaction;
import com.project_sem4.book_store.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
     TransactionService transactionService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Transaction>> getByUserId(@PathVariable UUID userId) {
        return ApiResponse.<List<Transaction>>builder()
                .result(transactionService.getTransactionsByUserId(userId))
                .build();
    }
}
