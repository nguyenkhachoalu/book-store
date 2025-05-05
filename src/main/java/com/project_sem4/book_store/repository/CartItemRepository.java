package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.CartItem;
import com.project_sem4.book_store.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
