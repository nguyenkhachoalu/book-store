package com.project_sem4.book_store.entity;


import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionId implements Serializable {
    UUID roleId;
    UUID permissionId;
}