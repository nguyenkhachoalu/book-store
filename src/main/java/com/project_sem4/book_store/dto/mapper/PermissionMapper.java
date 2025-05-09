package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.request.permission_request.PermissionRequest;
import com.project_sem4.book_store.dto.response.data_response_permission.PermissionResponse;
import com.project_sem4.book_store.dto.response.data_response_permission.RolePermissionResponse;
import com.project_sem4.book_store.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
    RolePermissionResponse toRolePermissionResponse(Permission permission);
}
