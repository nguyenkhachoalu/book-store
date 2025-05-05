package com.project_sem4.book_store.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedResponse<T> {
    List<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean hasNext;
    boolean hasPrevious;
}
