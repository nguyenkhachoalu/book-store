package com.project_sem4.book_store.dto.response.data_response_delivery;

import com.project_sem4.book_store.enum_type.DeliveryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetPagedDeliveriesResponse {

        UUID id;
        UUID shippingMethodId;
        UUID orderId;
        UUID shipperId;

        String shippingMethodName;
        String shipperFullName;
        String customerFullName;
        String customerPhone;
        String deliveryAddress;
        LocalDateTime estimateDeliveryTime;
        LocalDateTime actualDeliveryTime;
        DeliveryStatus deliveryStatus;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        Boolean isActive;


}
