package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_order.OrderResponse;

import com.project_sem4.book_store.enum_type.OrderStatusFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOrderRepository {
    Page<OrderResponse> searchOrders(String keyword, OrderStatusFilter type, Pageable pageable);

}
