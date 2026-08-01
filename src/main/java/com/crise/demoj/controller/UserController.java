package com.crise.demoj.controller;

import com.crise.demoj.annotation.RateLimit;
import com.crise.demoj.dto.*;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.dto.api.CommonResult;
import com.crise.demoj.dto.api.ResultCode;
import com.crise.demoj.exception.UserException;
import com.crise.demoj.utils.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import com.crise.demoj.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "用户管理", description = "用户注册、登录、增删改查接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @RateLimit(count = 3, timeWindow = 60)
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public CommonResult<Map<String, String>> register(@Valid @RequestBody UserRegisterRequestDto req) {
        userService.register(req);

        Map<String, String> response = new HashMap<>();
        response.put("message", "注册成功");
        return CommonResult.success(response);
    }

    @RateLimit(count = 5, timeWindow = 60)
    @Operation(summary = "用户登录", description = "登录成功后返回 JWT token 和 refresh token")
    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@Valid @RequestBody UserLoginRequestDto req) {
        Map<String, String> tokens = userService.login(req);
        if (tokens == null) {
            throw new RuntimeException("登录失败");
        }

        String token = tokens.get("token");
        Map<String, String> response = new HashMap<>();
        response.put("token", tokenHead + " " + token);
        response.put("tokenHead", tokenHead);
        response.put("message", tokens.get("message"));

        return CommonResult.success(response);
    }

    @Operation(summary = "查询用户信息")
    @PostMapping("/info")
    public CommonResult<UserInfoDto> getUserInfo(@Valid @RequestBody UserGetRequestDto req) {
        return CommonResult.success(userService.getUserByName(req.getUsername()));
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/update")
    public CommonResult<UserInfoDto> updateUser(@Valid @RequestBody UserUpdateRequestDto req) {
        return CommonResult.success(userService.updateUser(req));
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public CommonResult<Map<String, String>> deleteUser(@Valid @RequestBody UserDeleteRequestDto req) {
        userService.deleteUser(req.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("message", "删除成功");
        return CommonResult.success(response);
    }

    @Operation(summary = "用户列表（分页）")
    @PostMapping("/list")
    public CommonResult<CommonPage<UserInfoDto>> listUser(@Valid @RequestBody UserListRequestDto req) {
        return CommonResult.success(userService.listUsers(req));
    }

    @Operation(summary = "给用户分配角色")
    @PostMapping(value = "/role/update")
    public CommonResult updateRole(@Valid @RequestBody UserRoleUpdateByUserRequestDto req) {
        int count = userService.updateRole(req.getUserId(), req.getRoleIds());
        return CommonResult.success(count);
    }

    @Operation(summary = "获取指定用户的角色")
    @GetMapping(value = "/role/{userId}")
    public CommonResult<List<UserRoleInfoDto>> getRoleList(@PathVariable Long userId) {
        List<UserRoleInfoDto> roleList = userService.getRoleList(userId);
        return CommonResult.success(roleList);
    }
}
