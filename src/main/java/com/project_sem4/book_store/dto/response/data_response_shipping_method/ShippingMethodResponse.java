package com.project_sem4.book_store.dto.response.data_response_shipping_method;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingMethodResponse {
    UUID id;
    String shippingMethodName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
}
