package com.project_sem4.book_store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project_sem4.book_store.dto.request.delivery_request.DeliveryCreateRequest;
import com.project_sem4.book_store.dto.request.delivery_request.DeliveryUpdateRequest;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_delivery.GetPagedDeliveriesResponse;
import com.project_sem4.book_store.entity.Delivery;
import com.project_sem4.book_store.entity.Order;
import com.project_sem4.book_store.entity.ShippingMethod;
import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.enum_type.DeliverySearchType;
import com.project_sem4.book_store.enum_type.DeliveryStatus;
import com.project_sem4.book_store.enum_type.OrderStatus;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.DeliveryRepository;
import com.project_sem4.book_store.repository.OrderRepository;
import com.project_sem4.book_store.repository.ShippingMethodRepository;
import com.project_sem4.book_store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryService {
    private final ShippingMethodRepository shippingMethodRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    DeliveryRepository deliveryRepository;
    OrderService orderService;
    public Delivery CreateDelivery(DeliveryCreateRequest request){

        try{
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
            if(order.getOrderStatus() != OrderStatus.CONFIRMED){
                throw  new AppException(ErrorCode.ORDER_NOT_ELIGIBLE_FOR_CONFIRMATION);
            }
            Delivery delivery = Delivery.builder()
                    .shippingMethodId(request.getShippingMethodId())
                    .shipperId(request.getShipperId())
                    .orderId(request.getOrderId())
                    .deliveryAddress(request.getDeliveryAddress())
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .estimateDeliveryTime(LocalDateTime.now().plusDays(3))
                    .actualDeliveryTime(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            deliveryRepository.save(delivery);
            orderService.updateOrderStatus(order.getId(), OrderStatus.SHIPPING);

            return delivery;
        }catch (AppException e) {
            throw e; // Giữ nguyên AppException, đừng ghi đè
        } catch (Exception e) {
            log.error("Create delivery failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Delivery updateDelivery(UUID deliveryId, DeliveryUpdateRequest request) {
       try{
           Delivery delivery = deliveryRepository.findById(deliveryId)
                   .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_FOUND));

           if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
               throw new AppException(ErrorCode.DELIVERY_CANNOT_BE_MODIFIED);
           }

           delivery.setShippingMethodId(request.getShippingMethodId());
           delivery.setShipperId(request.getShipperId());
           delivery.setDeliveryAddress(request.getDeliveryAddress());
           delivery.setUpdatedAt(LocalDateTime.now());

           return deliveryRepository.save(delivery);
       }catch (AppException e) {
           throw e; // Giữ nguyên AppException, đừng ghi đè
       } catch (Exception e) {
           log.error("Update delivery failed", e);
           throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
       }

    }

    public Delivery updateDeliveryStatus(UUID deliveryId, DeliveryStatus newStatus) {
      try{
          Delivery delivery = deliveryRepository.findById(deliveryId)
                  .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_FOUND));
          Order order = orderRepository.findById(delivery.getOrderId())
                  .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
          if (delivery.getDeliveryStatus() == DeliveryStatus.CANCELLED ||
                  delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
              throw new AppException(ErrorCode.DELIVERY_CANNOT_BE_MODIFIED);
          }
          if(newStatus == DeliveryStatus.DELIVERED){
              orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
          }
          if(newStatus == DeliveryStatus.CANCELLED){
              orderService.updateOrderStatus(order.getId(), OrderStatus.CANCELLED);
          }
          delivery.setDeliveryStatus(newStatus);
          delivery.setUpdatedAt(LocalDateTime.now());

          return deliveryRepository.save(delivery);
      }catch (AppException e) {
          throw e; // Giữ nguyên AppException, đừng ghi đè
      } catch (Exception e) {
          log.error("Update delivery failed", e);
          throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
      }
    }

    public GetPagedDeliveriesResponse getDeliveryById(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOT_FOUND));

        Order order = orderRepository.findById(delivery.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));; // assuming mapped relation exists
        User customer = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));   // assuming order.getUser() is available
        User shipper = userRepository.findById(delivery.getShipperId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)); // if mapped
        ShippingMethod shippingMethod = shippingMethodRepository.findById(delivery.getShippingMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIPPING_METHOD_NOT_FOUND));
        return GetPagedDeliveriesResponse.builder()
                .id(delivery.getId())
                .shippingMethodId(delivery.getShippingMethodId())
                .orderId(delivery.getOrderId())
                .shipperId(delivery.getShipperId())
                .shippingMethodName(shippingMethod.getShippingMethodName())
                .shipperFullName(shipper.getFullName())
                .customerFullName(customer.getFullName())
                .customerPhone(customer.getPhone())
                .deliveryAddress(delivery.getDeliveryAddress())
                .estimateDeliveryTime(delivery.getEstimateDeliveryTime())
                .actualDeliveryTime(delivery.getActualDeliveryTime())
                .deliveryStatus(delivery.getDeliveryStatus())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .isActive(delivery.getIsActive())
                .build();
    }
    public PagedResponse<GetPagedDeliveriesResponse> getPagedDeliveries(
            String keyword,
            DeliverySearchType type,
            DeliveryStatus status,
            Pageable pageable
    ) {
        Page<GetPagedDeliveriesResponse> page = deliveryRepository.searchDeliveries(keyword, type, status, pageable);

        return PagedResponse.<GetPagedDeliveriesResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }


    public List<User> findAvailableShippersByAddress(String address) {
        List<UUID> shipperIds = deliveryRepository.findShippersByAddressAndStatus(address, DeliveryStatus.IN_TRANSIT);

        // Lấy tất cả users có role SHIPPER
        List<User> allShippers = userRepository.findByRoleCode("SHIPPER");

        if (shipperIds.isEmpty()) {
            return allShippers;
        }

        UUID activeShipperId = shipperIds.get(0); // Ưu tiên shipper đang giao hàng
        return allShippers.stream()
                .sorted((u1, u2) -> {
                    if (u1.getId().equals(activeShipperId)) return -1;
                    if (u2.getId().equals(activeShipperId)) return 1;
                    return 0;
                })
                .toList();
    }
}
