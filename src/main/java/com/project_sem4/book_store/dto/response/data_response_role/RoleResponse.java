package com.project_sem4.book_store.dto.response.data_response_role;

import com.project_sem4.book_store.dto.response.data_response_permission.PermissionResponse;
import com.project_sem4.book_store.dto.response.data_response_permission.RolePermissionResponse;
import com.project_sem4.book_store.entity.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    UUID id;
    String roleCode;
    String roleName;
    Set<RolePermissionResponse> permissions;
}
