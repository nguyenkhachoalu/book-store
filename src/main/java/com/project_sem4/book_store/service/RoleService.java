package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.RoleMapper;
import com.project_sem4.book_store.dto.request.role_request.RoleRequest;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.entity.Permission;
import com.project_sem4.book_store.entity.Role;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.PermissionRepository;
import com.project_sem4.book_store.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    private final PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    public List<RoleResponse> getAllRoles(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    public RoleResponse updateRolePermission(UUID roleId, RoleRequest request) {
       try{
           log.info("Role ID: {}", roleId);
           Role role = roleRepository.findById(roleId)
                   .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
           role.setUpdatedAt(LocalDateTime.now());

           if (request.getPermissionIds() != null) {
               log.info("Permission IDs: {}", request.getPermissionIds());
               List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
               log.info("Fetched permissions count: {}", permissions.size());
               role.setPermissions(new HashSet<>(permissions));
           }

           Role updated = roleRepository.save(role);
           log.info("Updated Role ID: {}, Permissions count: {}", updated.getId(), updated.getPermissions().size());  // ✅ Log sau khi lưu
           return roleMapper.toRoleResponse(updated);
       }catch (AppException e) {
           throw e;
       }catch(Exception ex){
           log.error("Update RolePermission failed ", ex);
           throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
       }
    }

}
