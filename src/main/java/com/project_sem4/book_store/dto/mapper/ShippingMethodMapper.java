package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.request.role_request.RoleRequest;
import com.project_sem4.book_store.dto.request.shipping_method_request.ShippingMethodRequest;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.dto.response.data_response_shipping_method.ShippingMethodResponse;
import com.project_sem4.book_store.entity.Role;
import com.project_sem4.book_store.entity.ShippingMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMethodMapper {
    ShippingMethod toShippingMethod(ShippingMethodRequest request);
    ShippingMethodResponse toShippingMethodResponse(ShippingMethod shippingMethod);
}
