package com.project_sem4.book_store.dto.response.data_response_cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetCartResponse {
    UUID id;
    UUID userId;
    String customerName;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    List<CartItemResponse> cartItems;
}
