package com.project_sem4.book_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 255)
    String fullName;

    @Column(nullable = false, unique = true, length = 255)
    String username;

    @Column(name = "password", nullable = false, length = 255)
    String password;

    @Column(nullable = false, unique = true, length = 255)
    String email;

    @Column(length = 20)
    String phone;

    @Column(nullable = true, length = 255)
    String avatar;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "is_active")
    Boolean isActive;

}
