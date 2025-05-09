package com.project_sem4.book_store.dto.response.data_response_order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    UUID bookId;
    String bookTitle;
    Integer quantity;
    BigDecimal price;
}
