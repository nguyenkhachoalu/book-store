package com.project_sem4.book_store.dto.request.user_request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 3, message = "FULL_NAME_INVALID")
    String full_name;
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    @Size(min = 4, message = "PASSWORD_INVALID")
    String password;
    String email;
    String phone;
}
