package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.permission_request.PermissionRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.data_response_permission.PermissionResponse;
import com.project_sem4.book_store.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@PathVariable UUID permissionId){
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
