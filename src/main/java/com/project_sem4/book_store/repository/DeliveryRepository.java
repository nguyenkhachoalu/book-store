package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Delivery;
import com.project_sem4.book_store.enum_type.DeliveryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends BaseRepository<Delivery, UUID>, CustomDeliveryRepository{
    @Query("SELECT DISTINCT d.shipperId FROM Delivery d WHERE LOWER(d.deliveryAddress) = LOWER(:address) AND d.deliveryStatus = :status")
    List<UUID> findShippersByAddressAndStatus(@Param("address") String address, @Param("status") DeliveryStatus status);

}
