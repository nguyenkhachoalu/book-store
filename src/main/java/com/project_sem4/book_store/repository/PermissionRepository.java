package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.Permission;
import com.project_sem4.book_store.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository  extends BaseRepository<Permission, UUID>{
    Optional<Permission> findByPermissionCode(String permissionCode);
}
