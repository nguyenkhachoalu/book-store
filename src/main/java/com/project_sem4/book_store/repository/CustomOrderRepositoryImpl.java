package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_order.OrderResponse;
import com.project_sem4.book_store.enum_type.OrderStatusFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<OrderResponse> searchOrders(String keyword, OrderStatusFilter statusFilter, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("""
            SELECT new com.project_sem4.book_store.dto.response.data_response_order.OrderResponse(
                o.id, o.userId, u.username, o.totalAmount, o.orderStatus,
                o.createdAt, o.updatedAt, o.isActive
            )
            FROM Order o
            JOIN User u ON o.userId = u.id
            WHERE 1 = 1
        """);

        if (keyword != null && !keyword.isBlank()) {
            jpql.append(" AND LOWER(u.username) LIKE :keyword");
        }

        if (statusFilter != OrderStatusFilter.ALL) {
            jpql.append(" AND o.orderStatus = :status");
        }

        TypedQuery<OrderResponse> query = entityManager.createQuery(jpql.toString(), OrderResponse.class);

        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        }

        if (statusFilter != OrderStatusFilter.ALL) {
            query.setParameter("status", statusFilter.name());
        }

        int total = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<OrderResponse> results = query.getResultList();
        return new PageImpl<>(results, pageable, total);
    }
}
