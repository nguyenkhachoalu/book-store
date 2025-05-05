package com.project_sem4.book_store.dto.request.user_request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    String oldPassword;
    @Size(min = 4, message = "PASSWORD_INVALID")
    String newPassword;
}
