package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.PermissionMapper;
import com.project_sem4.book_store.dto.request.permission_request.PermissionRequest;
import com.project_sem4.book_store.dto.response.data_response_permission.PermissionResponse;
import com.project_sem4.book_store.entity.Permission;
import com.project_sem4.book_store.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission.setCreatedAt(LocalDateTime.now());
        permission.setIsActive(true);
        permission.setUpdatedAt(LocalDateTime.now());
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();

    }
    public void delete(UUID permissionId){
        permissionRepository.deleteById(permissionId);
    }
}
