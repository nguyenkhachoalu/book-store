package com.project_sem4.book_store.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCategoryId implements java.io.Serializable {
    UUID bookId;
    UUID categoryId;
}