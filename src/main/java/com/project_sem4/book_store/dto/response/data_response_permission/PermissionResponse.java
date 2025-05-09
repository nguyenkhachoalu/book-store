package com.project_sem4.book_store.dto.response.data_response_permission;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    UUID id;
    String permissionCode;
    String permissionName;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}