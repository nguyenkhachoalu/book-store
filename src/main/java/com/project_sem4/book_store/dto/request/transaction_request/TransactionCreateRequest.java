package com.project_sem4.book_store.dto.request.transaction_request;

import com.project_sem4.book_store.enum_type.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionCreateRequest {
    UUID walletId;
    BigDecimal amount;
    TransactionType transactionType;
    String description;
}
