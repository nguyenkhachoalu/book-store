package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.RolePermission;
import com.project_sem4.book_store.entity.RolePermissionId;
import com.project_sem4.book_store.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    List<RolePermission> findByPermissionId(UUID permissionId);
    List<RolePermission> findByRoleId(UUID roleId);
}
