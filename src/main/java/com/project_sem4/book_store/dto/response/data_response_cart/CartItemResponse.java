package com.project_sem4.book_store.dto.response.data_response_cart;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    UUID cartId;
    UUID bookId;
    String bookTitle;
    String coverImage;
    Integer quantity;
    BigDecimal price;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
