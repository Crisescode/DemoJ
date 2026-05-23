package com.crise.demoj.controller;

import com.crise.demoj.dto.*;
import com.crise.demoj.dto.api.CommonResult;
import com.crise.demoj.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "用户角色管理", description = "后台用户角色管理")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @Operation(summary = "添加角色")
    @PostMapping("/create")
    public CommonResult<UserRoleInfoDto> create(@Valid @RequestBody UserRoleCreateRequestDto req) {
        return CommonResult.success(userRoleService.create(req));
    }

    @Operation(summary = "查询角色")
    @PostMapping("/get")
    public CommonResult<UserRoleInfoDto> get(@Valid @RequestBody UserRoleGetRequestDto req) {
        return CommonResult.success(userRoleService.getById(req.getId()));
    }

    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public CommonResult<HashMap<String, String>> delete(@Valid @RequestBody UserRoleDeleteRequestDto req) {
        return CommonResult.success(userRoleService.deleteById(req.getId()));
    }

//    @Operation(summary = "修改角色")
//    @PostMapping("/update")
//    public ResultDto<UserRoleInfoDto> update(@Valid @RequestBody UserRoleUpdateRequestDto req) {
//        return ResultDto.success(userRoleService.update(req));
//    }
}
