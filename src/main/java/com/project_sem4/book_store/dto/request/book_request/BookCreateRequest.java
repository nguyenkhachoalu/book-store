package com.project_sem4.book_store.dto.request.book_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCreateRequest {
    @NotBlank(message = "BOOK_TITLE_REQUIRED")
    String title;

    @NotNull(message = "BOOK_AUTHOR_ID_REQUIRED")
    UUID authorId;

    @NotNull(message = "BOOK_PRICE_REQUIRED")
    BigDecimal price;

    @NotNull(message = "BOOK_QUANTITY_REQUIRED")
    Integer quantity;
    String description;
    String coverImage;
    @NotEmpty(message = "BOOK_CATEGORY_REQUIRED")
    List<UUID> categoryIds;
}