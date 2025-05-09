package com.project_sem4.book_store.dto.response.data_response_permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionResponse {
    String permissionCode;
    String permissionName;
}
