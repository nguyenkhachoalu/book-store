package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.response.data_response_order.OrderResponse;
import com.project_sem4.book_store.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default OrderResponse toOrderResponse(Order order, String username) {
        if (order == null) return null;
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .username(username)
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .isActive(order.getIsActive())
                .build();
    }
}
