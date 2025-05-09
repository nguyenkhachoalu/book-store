package com.project_sem4.book_store.dto.response.data_response_book;

import com.project_sem4.book_store.dto.response.data_response_category.CategoryNameAndIdResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class GetPagedBookResponse {


    UUID id;
    String title;
    String authorName;
    BigDecimal price;
    Integer quantity;
    String description;
    String coverImage;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<CategoryNameAndIdResponse> categories;
}
