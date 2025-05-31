package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_delivery.GetPagedDeliveriesResponse;
import com.project_sem4.book_store.enum_type.DeliverySearchType;
import com.project_sem4.book_store.enum_type.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDeliveryRepository {
    public Page<GetPagedDeliveriesResponse> searchDeliveries(
            String keyword, DeliverySearchType type,
            DeliveryStatus status, Pageable pageable
    );

}
