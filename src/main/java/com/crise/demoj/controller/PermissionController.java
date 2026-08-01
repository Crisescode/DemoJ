package com.crise.demoj.controller;

import com.crise.demoj.dao.entity.PermissionEntity;
import com.crise.demoj.dto.AssignPermissionRequestDto;
import com.crise.demoj.dto.PermissionCreateRequestDto;
import com.crise.demoj.dto.PermissionInfoDto;
import com.crise.demoj.dto.PermissionUpdateRequestDto;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.dto.api.CommonResult;
import com.crise.demoj.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "后台权限资源管理接口")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "创建权限")
    @PostMapping("/create")
    public CommonResult<PermissionInfoDto> create(@Valid @RequestBody PermissionCreateRequestDto req) {
        PermissionEntity entity = new PermissionEntity();
        BeanUtils.copyProperties(req, entity);
        return CommonResult.success(permissionService.create(entity));
    }

    @Operation(summary = "修改权限")
    @PostMapping("/update")
    public CommonResult<PermissionInfoDto> update(@Valid @RequestBody PermissionUpdateRequestDto req) {
        PermissionEntity entity = new PermissionEntity();
        BeanUtils.copyProperties(req, entity);
        return CommonResult.success(permissionService.update(entity));
    }

    @Operation(summary = "删除权限")
    @PostMapping("/delete")
    public CommonResult<Map<String, String>> delete(@RequestParam Long id) {
        permissionService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "删除成功");
        return CommonResult.success(response);
    }

    @Operation(summary = "查询权限详情")
    @PostMapping("/get")
    public CommonResult<PermissionInfoDto> get(@RequestParam Long id) {
        return CommonResult.success(permissionService.getById(id));
    }

    @Operation(summary = "权限列表（分页）")
    @PostMapping("/list")
    public CommonResult<CommonPage<PermissionInfoDto>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return CommonResult.success(permissionService.list(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取所有权限（不分页，树形结构用）")
    @PostMapping("/listAll")
    public CommonResult<List<PermissionInfoDto>> listAll() {
        return CommonResult.success(permissionService.listAll());
    }

    @Operation(summary = "给角色分配权限")
    @PostMapping("/assign")
    public CommonResult<Map<String, String>> assignPermissions(@Valid @RequestBody AssignPermissionRequestDto req) {
        permissionService.assignPermissions(req.getRoleId(), req.getPermissionIds());
        Map<String, String> response = new HashMap<>();
        response.put("message", "分配成功");
        return CommonResult.success(response);
    }

    @Operation(summary = "获取指定角色的权限ID列表")
    @PostMapping("/rolePermissionIds")
    public CommonResult<List<Long>> getPermissionIdsByRoleId(@RequestParam Long roleId) {
        return CommonResult.success(permissionService.getPermissionIdsByRoleId(roleId));
    }
}
