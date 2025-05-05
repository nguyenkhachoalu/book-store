package com.project_sem4.book_store.dto.request.user_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String full_name;
    String username;
    String password;
    String email;
    String phone;
}
