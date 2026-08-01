package com.crise.demoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crise.demoj.dao.entity.PermissionEntity;
import com.crise.demoj.dao.mapper.PermissionMapper;
import com.crise.demoj.dao.mapper.RolePermissionRelationMapper;
import com.crise.demoj.dto.PermissionInfoDto;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionRelationMapper rolePermissionRelationMapper;

    public PermissionInfoDto create(PermissionEntity entity) {
        entity.setCreateTime(LocalDateTime.now());
        if (entity.getParentId() == null) entity.setParentId(0L);
        if (entity.getSort() == null) entity.setSort(0);
        if (entity.getStatus() == null) entity.setStatus(1);
        permissionMapper.insert(entity);
        return toDto(entity);
    }

    public PermissionInfoDto update(PermissionEntity entity) {
        PermissionEntity existing = permissionMapper.selectById(entity.getId());
        if (existing == null) {
            throw new UserException("权限不存在");
        }
        BeanUtils.copyProperties(entity, existing);
        permissionMapper.updateById(existing);
        return toDto(permissionMapper.selectById(entity.getId()));
    }

    public void delete(Long id) {
        PermissionEntity existing = permissionMapper.selectById(id);
        if (existing == null) {
            throw new UserException("权限不存在");
        }
        existing.setStatus(0);
        permissionMapper.updateById(existing);
    }

    public PermissionInfoDto getById(Long id) {
        PermissionEntity entity = permissionMapper.selectById(id);
        if (entity == null) {
            throw new UserException("权限不存在");
        }
        return toDto(entity);
    }

    public CommonPage<PermissionInfoDto> list(Integer pageNum, Integer pageSize, String keyword) {
        Page<PermissionEntity> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PermissionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword);
        }
        wrapper.orderByAsc("sort");
        IPage<PermissionEntity> result = permissionMapper.selectPage(page, wrapper);
        List<PermissionInfoDto> dtos = result.getRecords().stream().map(this::toDto).collect(Collectors.toList());
        Page<PermissionInfoDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(dtos);
        return CommonPage.fromPage(dtoPage);
    }

    public List<PermissionInfoDto> listAll() {
        List<PermissionEntity> entities = permissionMapper.selectList(
                new QueryWrapper<PermissionEntity>().eq("status", 1).orderByAsc("sort")
        );
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionRelationMapper.delete(
                new QueryWrapper<com.crise.demoj.dao.entity.RolePermissionRelationEntity>()
                        .eq("role_id", roleId)
        );
        for (Long permissionId : permissionIds) {
            com.crise.demoj.dao.entity.RolePermissionRelationEntity relation =
                    new com.crise.demoj.dao.entity.RolePermissionRelationEntity();
            relation.setRoleId(roleId);
            relation.setPermissionId(permissionId);
            relation.setCreateTime(LocalDateTime.now());
            rolePermissionRelationMapper.insert(relation);
        }
    }

    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return rolePermissionRelationMapper.getPermissionIdsByRoleId(roleId);
    }

    public List<PermissionInfoDto> getByUserId(Long userId) {
        List<PermissionEntity> entities = permissionMapper.getByUserId(userId);
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    private PermissionInfoDto toDto(PermissionEntity entity) {
        PermissionInfoDto dto = new PermissionInfoDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
