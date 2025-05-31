package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_delivery.GetPagedDeliveriesResponse;
import com.project_sem4.book_store.enum_type.DeliverySearchType;
import com.project_sem4.book_store.enum_type.DeliveryStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomDeliveryRepositoryImpl implements CustomDeliveryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<GetPagedDeliveriesResponse> searchDeliveries(String keyword, DeliverySearchType type, DeliveryStatus status, Pageable pageable) {
        String base = """
            SELECT new com.project_sem4.book_store.dto.response.data_response_delivery.GetPagedDeliveriesResponse(
                d.id, sm.id, o.id, shipper.id, sm.shippingMethodName, shipper.fullName, customer.fullName, customer.phone,
                d.deliveryAddress, d.estimateDeliveryTime, d.actualDeliveryTime,
                d.deliveryStatus, d.createdAt, d.updatedAt, d.isActive
            )
            FROM Delivery d
            JOIN ShippingMethod sm ON d.shippingMethodId = sm.id
            JOIN User shipper ON d.shipperId = shipper.id
            JOIN Order o ON d.orderId = o.id
            JOIN User customer ON o.userId = customer.id
            WHERE
        """;

        String whereClause = switch (type) {
            case CUSTOMER_NAME -> "LOWER(customer.fullName) LIKE :keyword";
            case CUSTOMER_PHONE -> "customer.phone LIKE :keyword";
            case SHIPPER_NAME -> "LOWER(shipper.fullName) LIKE :keyword";
            case SHIPPING_METHOD -> "LOWER(sm.shippingMethodName) LIKE :keyword";
            case ALL -> """
                LOWER(customer.fullName) LIKE :keyword OR
                customer.phone LIKE :keyword OR
                LOWER(shipper.fullName) LIKE :keyword OR
                LOWER(sm.shippingMethodName) LIKE :keyword
            """;
        };

        if (status != null) {
            whereClause = "(" + whereClause + ") AND d.deliveryStatus = :status";
        }

        TypedQuery<GetPagedDeliveriesResponse> query = em.createQuery(base + whereClause, GetPagedDeliveriesResponse.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        if (status != null) query.setParameter("status", status);

        int total = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}
