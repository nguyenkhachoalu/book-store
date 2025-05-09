package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.ShippingMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShippingMethodRepository extends BaseRepository<ShippingMethod, UUID>{
    Page<ShippingMethod> findByShippingMethodNameContainingIgnoreCase(String keyword, Pageable pageable);
}
