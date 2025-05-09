package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.response.data_response_cart.CartItemResponse;
import com.project_sem4.book_store.dto.response.data_response_cart.GetCartResponse;
import com.project_sem4.book_store.entity.*;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.BookRepository;
import com.project_sem4.book_store.repository.CartItemRepository;
import com.project_sem4.book_store.repository.CartRepository;
import com.project_sem4.book_store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    UserRepository userRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    BookRepository bookRepository;

    public void addToCart(UUID userId, UUID bookId, int quantity) {
        if (quantity <= 0) throw new AppException(ErrorCode.INVALID_QUANTITY);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (book.getQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_BOOK_QUANTITY);
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .totalPrice(BigDecimal.ZERO)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            return cartRepository.save(newCart);
        });

        CartItemId cartItemId = new CartItemId(cart.getId(), bookId);
        Optional<CartItem> existingItemOpt = cartItemRepository.findById(cartItemId);

        BigDecimal newItemTotal = book.getPrice().multiply(BigDecimal.valueOf(quantity));

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            int updatedQty = item.getQuantity() + quantity;

            if (updatedQty > book.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_BOOK_QUANTITY);
            }

            item.setQuantity(updatedQty);
            item.setPrice(book.getPrice().multiply(BigDecimal.valueOf(updatedQty)));
            item.setUpdatedAt(LocalDateTime.now());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cartId(cart.getId())
                    .bookId(bookId)
                    .quantity(quantity)
                    .price(newItemTotal)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            cartItemRepository.save(newItem);
        }

        updateCartTotal(cart);
    }

    public GetCartResponse getCartByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = cartItems.stream().map(item -> {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

            return CartItemResponse.builder()
                    .cartId(item.getCartId())
                    .bookId(item.getBookId())
                    .bookTitle(book.getTitle())
                    .coverImage(book.getCoverImage())
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .createdAt(item.getCreatedAt())
                    .updatedAt(item.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());

        return GetCartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .customerName(user.getFullName())
                .totalPrice(cart.getTotalPrice())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .isActive(cart.getIsActive())
                .cartItems(itemResponses)
                .build();
    }

    public void deleteCartItem(UUID userId, UUID bookId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        CartItemId id = new CartItemId(cart.getId(), bookId);
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(item);

        BigDecimal updatedTotal = cartItemRepository.sumTotalPriceByCartId(cart.getId());
        cart.setTotalPrice(updatedTotal);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }
    public void updateCartItem(UUID userId, UUID bookId, int quantity) {
        if (quantity <= 0)
            throw new AppException(ErrorCode.INVALID_QUANTITY);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (book.getQuantity() < quantity) {
            throw new AppException(ErrorCode.INSUFFICIENT_BOOK_QUANTITY);
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        CartItemId cartItemId = new CartItemId(cart.getId(), bookId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        item.setQuantity(quantity);
        item.setPrice(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
        item.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(item);

        updateCartTotal(cart);
    }
    private void updateCartTotal(Cart cart) {
        BigDecimal updatedTotal = cartItemRepository.sumTotalPriceByCartId(cart.getId());
        cart.setTotalPrice(updatedTotal);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }
}
