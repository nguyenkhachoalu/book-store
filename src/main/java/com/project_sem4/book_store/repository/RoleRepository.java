package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends BaseRepository<Role, UUID>{
    Optional<Role> findByRoleCode(String roleCode);
    List<Role> findByRoleCodeIn(List<String> roleCodes);
    boolean existsByRoleCode(String roleCode);
}
