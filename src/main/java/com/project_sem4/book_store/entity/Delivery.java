package com.project_sem4.book_store.entity;

import com.project_sem4.book_store.enum_type.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "shipping_method_id", nullable = false)
    UUID shippingMethodId;

    @Column(name = "customer_id", nullable = false)
    UUID customerId;

    @Column(name = "shipper_id", nullable = false)
    UUID shipperId;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Column(name = "delivery_address", nullable = false, length = 255)
    String deliveryAddress;

    @Column(name = "estimate_delivery_time", nullable = false)
    LocalDateTime estimateDeliveryTime;

    @Column(name = "actual_delivery_time")
    LocalDateTime actualDeliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 50)
    DeliveryStatus deliveryStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "is_active")
    Boolean isActive;
}
