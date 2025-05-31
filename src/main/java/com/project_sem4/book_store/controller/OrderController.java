package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderConfirmResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderWithDetailsResponse;
import com.project_sem4.book_store.entity.Order;
import com.project_sem4.book_store.enum_type.OrderStatusFilter;
import com.project_sem4.book_store.enum_type.OrderStatus;
import com.project_sem4.book_store.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    @PostMapping("/create")
    public ApiResponse<String> createOrder(@RequestParam UUID cartId) {
        return ApiResponse.<String>builder()
                .result(orderService.CreateOrder(cartId))
                .build();
    }

    @GetMapping("/confirmed")
    public ApiResponse<List<OrderConfirmResponse>> getConfirmedOrders() {
        return ApiResponse.<List<OrderConfirmResponse>>builder()
                .result(orderService.getConfirmedOrders())
                .build();
    }

    @GetMapping
    public ApiResponse<PagedResponse<OrderResponse>> getPagedOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "ALL") OrderStatusFilter type,
            Pageable pageable) {
        return ApiResponse.<PagedResponse<OrderResponse>>builder()
                .result(orderService.getPagedOrders(keyword, type, pageable))
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable UUID orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    @GetMapping("/{orderId}/details")
    public ApiResponse<OrderWithDetailsResponse> getOrderWithDetails(@PathVariable UUID orderId) {
        return ApiResponse.<OrderWithDetailsResponse>builder()
                .result(orderService.getOrderWithDetails(orderId))
                .build();
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<Order> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status) {
        return ApiResponse.<Order>builder()
                .result(orderService.updateOrderStatus(orderId, status))
                .build();
    }
    @GetMapping("/history/{userId}")
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable UUID userId) {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersByUserId(userId))
                .build();
    }
}
