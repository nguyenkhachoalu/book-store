package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Order;
import com.project_sem4.book_store.enum_type.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends BaseRepository<Order, UUID>, CustomOrderRepository {
    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
