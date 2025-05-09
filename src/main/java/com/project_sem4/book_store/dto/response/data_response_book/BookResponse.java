package com.project_sem4.book_store.dto.response.data_response_book;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {
    UUID id;
    String title;
    UUID authorId;
    BigDecimal price;
    Integer quantity;
    String description;
    String coverImage;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
}
