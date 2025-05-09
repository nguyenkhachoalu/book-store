package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.enum_type.UserSearchType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> searchUsers(String keyword, UserSearchType type, Pageable pageable) {
        String baseQuery = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE ";
        String condition;

        switch (type) {
            case USERNAME -> condition = "LOWER(u.username) LIKE :keyword";
            case FULL_NAME -> condition = "LOWER(u.fullName) LIKE :keyword";
            case EMAIL -> condition = "LOWER(u.email) LIKE :keyword";
            case PHONE -> condition = "u.phone LIKE :keyword";
            case ALL -> condition = "LOWER(u.username) LIKE :keyword OR LOWER(u.fullName) LIKE :keyword " +
                    "OR LOWER(u.email) LIKE :keyword OR u.phone LIKE :keyword";
            default -> throw new IllegalArgumentException("Unsupported search type");
        }

        String queryStr = baseQuery + condition;
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");

        // Count query needs to remove FETCH JOIN to avoid error
        String countQueryStr = "SELECT COUNT(DISTINCT u) FROM User u WHERE " + condition;
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);
        countQuery.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
        Long total = countQuery.getSingleResult();

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<User> users = query.getResultList();
        return new PageImpl<>(users, pageable, total);
    }
}
