package com.project_sem4.book_store.dto.request.wallet_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletTransactionRequest {
    UUID userId;
    BigDecimal amount;
    String description;
}