package com.project_sem4.book_store.dto.request.delivery_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryCreateRequest {
    UUID shippingMethodId;
    UUID shipperId;
    UUID orderId;
    String deliveryAddress;
}
