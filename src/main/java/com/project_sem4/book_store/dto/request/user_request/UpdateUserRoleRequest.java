package com.project_sem4.book_store.dto.request.user_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRoleRequest {
    Set<String> roleCodes;
}
