package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.CartItem;
import com.project_sem4.book_store.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    @Query("SELECT COALESCE(SUM(c.price), 0) FROM CartItem c WHERE c.cartId = :cartId")
    BigDecimal sumTotalPriceByCartId(@Param("cartId") UUID cartId);

    List<CartItem> findByCartId(UUID cartId);
}
