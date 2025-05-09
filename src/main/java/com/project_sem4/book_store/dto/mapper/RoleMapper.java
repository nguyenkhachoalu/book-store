package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.request.role_request.RoleRequest;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
