package com.project_sem4.book_store.dto.request.user_request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 3, message = "FULL_NAME_INVALID")
    String full_name;
    String email;
    String phone;
    String avata_path;
}
