package com.project_sem4.book_store.controller;


import com.project_sem4.book_store.dto.request.role_request.RoleRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @PutMapping("/{roleId}")
    public ApiResponse<RoleResponse> updateRolePermission(
            @PathVariable UUID roleId,
            @RequestBody RoleRequest request) {
        log.info("----> HIT PUT /roles/{}", roleId);
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRolePermission(roleId, request))
                .build();
    }
}
