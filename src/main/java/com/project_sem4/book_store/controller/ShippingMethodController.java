package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.shipping_method_request.ShippingMethodRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_shipping_method.ShippingMethodResponse;
import com.project_sem4.book_store.service.ShipperMethodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/shipping_method")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingMethodController {
    ShipperMethodService shipperMethodService;

    @GetMapping
    public ApiResponse<List<ShippingMethodResponse>> getAll() {
        return ApiResponse.<List<ShippingMethodResponse>>builder()
                .result(shipperMethodService.getAll())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PagedResponse<ShippingMethodResponse>> search(@RequestParam String keyword, Pageable pageable) {
        return ApiResponse.<PagedResponse<ShippingMethodResponse>>builder()
                .result(shipperMethodService.search(keyword, pageable))
                .build();
    }

    @PostMapping
    public ApiResponse<ShippingMethodResponse> create(@RequestBody ShippingMethodRequest request) {
        return ApiResponse.<ShippingMethodResponse>builder()
                .result(shipperMethodService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ShippingMethodResponse> update(@PathVariable UUID id, @RequestBody ShippingMethodRequest request) {
        return ApiResponse.<ShippingMethodResponse>builder()
                .result(shipperMethodService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable UUID id) {
        return ApiResponse.<String>builder()
                .result(shipperMethodService.delete(id))
                .build();
    }
}
