package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
public interface CartRepository extends BaseRepository<Cart, UUID>{
    Optional<Cart> findByUserId(UUID userId);
}
