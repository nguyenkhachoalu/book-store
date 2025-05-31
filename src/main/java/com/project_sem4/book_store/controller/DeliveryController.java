package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.delivery_request.DeliveryCreateRequest;
import com.project_sem4.book_store.dto.request.delivery_request.DeliveryUpdateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_delivery.GetPagedDeliveriesResponse;
import com.project_sem4.book_store.entity.Delivery;
import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.enum_type.DeliverySearchType;
import com.project_sem4.book_store.enum_type.DeliveryStatus;
import com.project_sem4.book_store.service.DeliveryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController {

    DeliveryService deliveryService;

    @PostMapping
    public ApiResponse<Delivery> createDelivery(@RequestBody DeliveryCreateRequest request) {
        return ApiResponse.<Delivery>builder()
                .result(deliveryService.CreateDelivery(request))
                .build();
    }
    @PutMapping("/{deliveryId}")
    public ApiResponse<Delivery> updateDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody DeliveryUpdateRequest request
    ) {
        return ApiResponse.<Delivery>builder()
                .result(deliveryService.updateDelivery(deliveryId, request))
                .build();
    }

    @PutMapping("/{deliveryId}/status")
    public ApiResponse<Delivery> updateDeliveryStatus(
            @PathVariable UUID deliveryId,
            @RequestParam DeliveryStatus status
    ) {
        return ApiResponse.<Delivery>builder()
                .result(deliveryService.updateDeliveryStatus(deliveryId, status))
                .build();
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<GetPagedDeliveriesResponse> getDeliveryById(@PathVariable UUID deliveryId) {
        return ApiResponse.<GetPagedDeliveriesResponse>builder()
                .result(deliveryService.getDeliveryById(deliveryId))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PagedResponse<GetPagedDeliveriesResponse>> searchDeliveries(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "ALL") DeliverySearchType type,
            @RequestParam(required = false) DeliveryStatus status,
            Pageable pageable
    ) {
        return ApiResponse.<PagedResponse<GetPagedDeliveriesResponse>>builder()
                .result(deliveryService.getPagedDeliveries(keyword, type, status, pageable))
                .build();
    }

    @GetMapping("/shippers")
    public ApiResponse<List<User>> findAvailableShippers(@RequestParam String address) {
        return ApiResponse.<List<User>>builder()
                .result(deliveryService.findAvailableShippersByAddress(address))
                .build();
    }
}
