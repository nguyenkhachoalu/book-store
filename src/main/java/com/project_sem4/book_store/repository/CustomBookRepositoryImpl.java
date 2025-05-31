package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.dto.response.data_response_book.GetPagedBookResponse;
import com.project_sem4.book_store.dto.response.data_response_category.CategoryNameAndIdResponse;
import com.project_sem4.book_store.entity.Book;
import com.project_sem4.book_store.enum_type.BookSearchType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<GetPagedBookResponse> searchBooks(String keyword, BookSearchType type,
                                                  BigDecimal minPrice, BigDecimal maxPrice,
                                                  Pageable pageable) {

        String baseFrom = "FROM Book b JOIN b.bookCategories bc JOIN bc.category c ";

        String keywordCondition = switch (type) {
            case TITLE -> "LOWER(b.title) LIKE :keyword";
            case AUTHOR_NAME -> "LOWER(b.author.authorName) LIKE :keyword";
            case CATEGORY_NAME -> "LOWER(c.categoryName) LIKE :keyword";
            case ALL -> "(LOWER(b.title) LIKE :keyword OR LOWER(b.author.authorName) LIKE :keyword OR LOWER(c.categoryName) LIKE :keyword)";
        };

        String priceCondition = "";
        if (minPrice != null) priceCondition += " AND b.price >= :minPrice";
        if (maxPrice != null) priceCondition += " AND b.price <= :maxPrice";

        String whereClause = "WHERE " + keywordCondition + priceCondition;

        // Query chính
        String dataQueryStr = "SELECT DISTINCT b " + baseFrom + whereClause +" ORDER BY b.createdAt DESC";
        TypedQuery<Book> dataQuery = entityManager.createQuery(dataQueryStr, Book.class);
        dataQuery.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        if (minPrice != null) dataQuery.setParameter("minPrice", minPrice);
        if (maxPrice != null) dataQuery.setParameter("maxPrice", maxPrice);
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        var books = dataQuery.getResultList();

        List<GetPagedBookResponse> results = books.stream().map(book -> {
            var categories = book.getBookCategories().stream()
                    .map(bc -> bc.getCategory())
                    .map(cat -> new CategoryNameAndIdResponse(cat.getId(), cat.getCategoryName()))
                    .collect(Collectors.toList());

            return GetPagedBookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .authorName(book.getAuthor().getAuthorName())
                    .price(book.getPrice())
                    .quantity(book.getQuantity())
                    .description(book.getDescription())
                    .coverImage(book.getCoverImage())
                    .createdAt(book.getCreatedAt())
                    .updatedAt(book.getUpdatedAt())
                    .categories(categories)
                    .build();
        }).collect(Collectors.toList());

        // Count query
        String countQueryStr = "SELECT COUNT(DISTINCT b) " + baseFrom + whereClause;
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);
        countQuery.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        if (minPrice != null) countQuery.setParameter("minPrice", minPrice);
        if (maxPrice != null) countQuery.setParameter("maxPrice", maxPrice);
        long total = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
}
