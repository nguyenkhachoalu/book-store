package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.cart_request.AddToCartRequest;
import com.project_sem4.book_store.dto.request.cart_request.UpdateCartItemRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.data_response_cart.GetCartResponse;
import com.project_sem4.book_store.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/add")
    public ApiResponse<String> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
        return ApiResponse.<String>builder().result("Thêm vào giỏ hàng thành công").build();
    }

    @GetMapping
    public ApiResponse<GetCartResponse> getCart(@RequestParam UUID userId) {
        return ApiResponse.<GetCartResponse>builder()
                .result(cartService.getCartByUserId(userId))
                .build();
    }

    @DeleteMapping("/item")
    public ApiResponse<String> deleteCartItem(@RequestParam UUID userId,
                                              @RequestParam UUID bookId) {
        cartService.deleteCartItem(userId, bookId);
        return ApiResponse.<String>builder().result("Xoá sản phẩm thành công").build();
    }

    @PutMapping("/item")
    public ApiResponse<String> updateCartItem( @RequestBody  UpdateCartItemRequest request) {
        cartService.updateCartItem(request);
        return ApiResponse.<String>builder().result("Cập nhật giỏ hàng thành công").build();
    }
}
