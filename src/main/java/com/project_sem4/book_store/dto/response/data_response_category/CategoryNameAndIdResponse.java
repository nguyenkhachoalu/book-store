package com.project_sem4.book_store.dto.response.data_response_category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryNameAndIdResponse {
    UUID id;
    String categoryName;
}
