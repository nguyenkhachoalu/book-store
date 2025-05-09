package com.project_sem4.book_store.dto.response.data_response_order;

import com.project_sem4.book_store.enum_type.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    UUID id;
    UUID userId;
    String username;
    BigDecimal totalAmount;
    OrderStatus orderStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;

    public OrderResponse(UUID id, UUID userId, String username, BigDecimal totalAmount,
                         OrderStatus orderStatus, LocalDateTime createdAt,
                         LocalDateTime updatedAt, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }
}
