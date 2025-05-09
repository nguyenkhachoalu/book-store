package com.project_sem4.book_store.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "book_categories")
@IdClass(BookCategoryId.class)
public class BookCategory {
    @Id
    @Column(name = "book_id")
    UUID bookId;

    @Id
    @Column(name = "category_id")
    UUID categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    Category category;

    public BookCategory(UUID bookId, UUID categoryId) {
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

}
