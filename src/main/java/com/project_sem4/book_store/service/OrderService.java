package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.handle.handle_email.EmailMessage;
import com.project_sem4.book_store.dto.mapper.OrderMapper;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderConfirmResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderDetailResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderResponse;
import com.project_sem4.book_store.dto.response.data_response_order.OrderWithDetailsResponse;
import com.project_sem4.book_store.entity.*;
import com.project_sem4.book_store.enum_type.OrderStatusFilter;
import com.project_sem4.book_store.enum_type.OrderStatus;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    private final UserRepository userRepository;
    OrderDetailRepository orderDetailRepository;
    BookRepository bookRepository;
    OrderRepository orderRepository;
    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    WalletService walletService;
    EmailService emailService;
    OrderMapper orderMapper;
    @Transactional
    public String CreateOrder(UUID cartId){
        try{
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));


            Order order = Order.builder()
                    .userId(cart.getUserId())
                    .totalAmount(cart.getTotalPrice())
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            orderRepository.save(order);
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
            List<String> bookName = new ArrayList<>();
            for (CartItem cartItem : cartItems){
                Book book = bookRepository.findById(cartItem.getBookId())
                        .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
                if(book.getQuantity()<= cartItem.getQuantity()){
                    throw  new AppException(ErrorCode.INSUFFICIENT_BOOK_QUANTITY);
                }

                OrderDetail orderDetail = OrderDetail.builder()
                        .orderId(order.getId())
                        .bookId(book.getId())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .build();
                orderDetailRepository.save(orderDetail);

                bookName.add(book.getTitle());

                book.setQuantity(book.getQuantity() - orderDetail.getQuantity());
                book.setUpdatedAt(LocalDateTime.now());
                bookRepository.save(book);
            }

            walletService.purchase(order.getUserId(),order.getTotalAmount(),"Mua sản phẩm: " + String.join(", ", bookName));

            cartItemRepository.deleteAll(cartItems);
            cart.setTotalPrice(BigDecimal.ZERO);
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
            return "Thanh toán thành công";
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Create Order failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SELLER')")
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(order.getOrderStatus() == OrderStatus.CANCELLED){
            throw new AppException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }
        if(order.getOrderStatus() == OrderStatus.COMPLETED){
            throw new AppException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }
        if(newStatus == OrderStatus.CONFIRMED){
            sendConfirmEmail(order);
        }
        if(newStatus == OrderStatus.COMPLETED){
            sendCompletedEmail(order);
        }
        if(newStatus == OrderStatus.CANCELLED){
            restoreBooksFromCancelledOrder(order.getId());
            sendCancelledEmail(order);
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<OrderConfirmResponse> getConfirmedOrders() {
        List<Order> confirmedOrders = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);

        return confirmedOrders.stream()
                .map(order -> {
                    User user = userRepository.findById(order.getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                    return OrderConfirmResponse.builder()
                            .id(order.getId())
                            .username(user.getUsername())
                            .build();
                })
                .toList();
    }

    public PagedResponse<OrderResponse> getPagedOrders(String keyword, OrderStatusFilter type, Pageable pageable) {
        Page<OrderResponse> page = orderRepository.searchOrders(keyword, type, pageable);

        return PagedResponse.<OrderResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        String username = userRepository.findById(order.getUserId())
                .map(User::getUsername)
                .orElse("UNKNOWN");

        return orderMapper.toOrderResponse(order, username);
    }

    public List<OrderResponse> getOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        String username = userRepository.findById(userId)
                .map(User::getUsername)
                .orElse("UNKNOWN");

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUserId(),
                        username,
                        order.getTotalAmount(),
                        order.getOrderStatus(),
                        order.getCreatedAt(),
                        order.getUpdatedAt(),
                        order.getIsActive()
                ))
                .collect(Collectors.toList());
    }


    public OrderWithDetailsResponse getOrderWithDetails(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String username = userRepository.findById(order.getUserId())
                .map(User::getUsername)
                .orElse("UNKNOWN");
        List<OrderDetailResponse> detailResponses = getOrderDetailsByOrderId(orderId);

        return OrderWithDetailsResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .username(username)
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderDetails(detailResponses)
                .build();
    }


    private List<OrderDetailResponse> getOrderDetailsByOrderId(UUID orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        return details.stream().map(detail -> {
            Book book = bookRepository.findById(detail.getBookId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
            return OrderDetailResponse.builder()
                    .bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .quantity(detail.getQuantity())
                    .price(detail.getPrice())
                    .build();
        }).toList();
    }
    private void restoreBooksFromCancelledOrder(UUID orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        for (OrderDetail detail : orderDetails) {
            Book book = bookRepository.findById(detail.getBookId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
            book.setQuantity(book.getQuantity() + detail.getQuantity());
            book.setUpdatedAt(LocalDateTime.now());
            bookRepository.save(book);
        }
    }

    private void sendConfirmEmail(Order order) {
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String emailContent = emailService.generateOrderConfirmEmail(user.getFullName());
        EmailMessage message = new EmailMessage(List.of(user.getEmail()),
                "Đơn hàng của " + user.getUsername(), emailContent);
        emailService.sendEmail(message);
    }
    private void sendCompletedEmail(Order order) {
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String emailContent = emailService.generateDeliveryCompletionEmail(user.getFullName());
        EmailMessage message = new EmailMessage(List.of(user.getEmail()),
                "Đơn hàng của " + user.getUsername(), emailContent);
        emailService.sendEmail(message);
    }

    private void sendCancelledEmail(Order order) {
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String emailContent = emailService.generateCancelledEmail(user.getFullName());
        EmailMessage message = new EmailMessage(List.of(user.getEmail()),
                "Đơn hàng của " + user.getUsername(), emailContent);
        emailService.sendEmail(message);
    }
}
