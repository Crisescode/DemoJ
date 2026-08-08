package com.crise.demoj.service;

import com.crise.demoj.dao.mapper.RolePermissionRelationMapper;

import javax.annotation.Resource;
import java.util.List;

public class RolePermissionMapService {
    @Resource
    private RolePermissionRelationMapper rolePermissionRelationMapper;

    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return rolePermissionRelationMapper.getPermissionIdsByRoleId(roleId);
    }
}
