package com.project_sem4.book_store.dto.request.shipping_method_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingMethodRequest {
    String shippingMethodName;
}
