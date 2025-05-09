package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.ShippingMethodMapper;
import com.project_sem4.book_store.dto.request.shipping_method_request.ShippingMethodRequest;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_shipping_method.ShippingMethodResponse;
import com.project_sem4.book_store.entity.ShippingMethod;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.ShippingMethodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipperMethodService {
    ShippingMethodRepository shippingMethodRepository;
    ShippingMethodMapper shippingMethodMapper;

    public List<ShippingMethodResponse> getAll() {
        try {
            return shippingMethodRepository.findAll()
                    .stream()
                    .map(shippingMethodMapper::toShippingMethodResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Get all shipping methods failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public PagedResponse<ShippingMethodResponse> search(String keyword, Pageable pageable) {
        try {
            Page<ShippingMethod> page = shippingMethodRepository.findByShippingMethodNameContainingIgnoreCase(keyword, pageable);
            List<ShippingMethodResponse> content = page.getContent().stream()
                    .map(shippingMethodMapper::toShippingMethodResponse)
                    .toList();
            return PagedResponse.<ShippingMethodResponse>builder()
                    .content(content)
                    .pageNumber(page.getNumber())
                    .pageSize(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .hasNext(page.hasNext())
                    .hasPrevious(page.hasPrevious())
                    .build();
        } catch (Exception e) {
            log.error("Search shipping methods failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ShippingMethodResponse create(ShippingMethodRequest request) {
        try {
            ShippingMethod shippingMethod = ShippingMethod.builder()
                    .shippingMethodName(request.getShippingMethodName())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            shippingMethodRepository.save(shippingMethod);
            return shippingMethodMapper.toShippingMethodResponse(shippingMethod);
        } catch (Exception e) {
            log.error("Create shipping method failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ShippingMethodResponse update(UUID id, ShippingMethodRequest request) {
        try {
            ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
            shippingMethod.setShippingMethodName(request.getShippingMethodName());
            shippingMethod.setUpdatedAt(LocalDateTime.now());
            shippingMethodRepository.save(shippingMethod);
            return shippingMethodMapper.toShippingMethodResponse(shippingMethod);
        } catch (Exception e) {
            log.error("Update shipping method failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String delete(UUID id) {
        try {
            ShippingMethod method = shippingMethodRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
            shippingMethodRepository.delete(method);
            return "Xóa phương thức giao hàng thành công";
        } catch (Exception e) {
            log.error("Delete shipping method failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
