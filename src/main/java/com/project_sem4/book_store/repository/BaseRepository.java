package com.project_sem4.book_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    Optional<T> findByIdAndIsActiveTrue(ID id);
    List<T> findAllByIsActiveTrue();
}