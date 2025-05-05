package com.project_sem4.book_store.dto.response.data_response_user;


import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;
    String fullName;
    String username;
    String email;
    String phone;
    String avatar;
    Boolean isActive;

}