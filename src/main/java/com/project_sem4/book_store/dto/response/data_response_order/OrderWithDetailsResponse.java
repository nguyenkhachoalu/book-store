package com.project_sem4.book_store.dto.response.data_response_order;

import com.project_sem4.book_store.enum_type.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderWithDetailsResponse {
    UUID id;
    UUID userId;
    String username;
    BigDecimal totalAmount;
    OrderStatus orderStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<OrderDetailResponse> orderDetails;
}