package com.crise.demoj.controller;

// 用户模块
// 设计库表，字段
// 库名：demodb
// 表名：demoj_user
// 字段：
// id: 序号（使用数据库自增）
// name: 用户名, 类型：字符串，长度：32
// email: 邮箱, 类型：字符串，长度：64
// name_cn: 中文名, 类型：字符串，长度：64
// password: 密码（加密）, 类型：字符串，长度：64
// phone: 手机号, 类型：字符串，长度：11
// sex: 性别, 类型：int, 默认值：0
// create_time: 创建时间，类型：datetime, 默认值：当前时间
// update_time: 更新时间，类型：datetime，默认值：当前时间
// is_delete: 是否删除，类型：布尔类型，默认值：0
// delete_uuid: 删除标识，类型：字符串，长度：64，默认值：NA

// 表索引：
// 唯一索引：uniq_name_is_delete_delete_uuid
// 业务索引：idx_name, idx_email, idx_phone

// 1. 用户注册


// 2. 用户登录
// 3. 用户创建
// 4. 用户更改
// 5. 用户删除
// 6. 用户查询
// 7. 用户列表查询

import com.crise.demoj.dto.*;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.dto.api.CommonResult;
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

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public CommonResult<Map<String, String>> register(@Valid @RequestBody UserRegisterRequestDto req) {
        userService.register(req);

        Map<String, String> response = new HashMap<>();
        response.put("message", "注册成功");
        return CommonResult.success(response);
    }

    @Operation(summary = "用户登录", description = "登录成功后返回 JWT token")
    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@Valid @RequestBody UserLoginRequestDto req) {
        String token = userService.login(req);
        if (token == null) {
            throw new RuntimeException("登录失败");
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenHead", tokenHead);
        response.put("message", "登录成功");

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

    @Operation(summary = "获取指定用户的角色")  // Describes the operation's purpose in Swagger documentation
    @RequestMapping(value = "/role/{userId}", method = RequestMethod.GET)  // Maps to HTTP GET requests at /role/{userId}
    @ResponseBody  // Indicates the method's return value should be serialized directly into the response body
    public CommonResult<List<UserRoleInfoDto>> getRoleList(@PathVariable Long userId) {  // Method that retrieves role list for a specific user
        List<UserRoleInfoDto> roleList = userService.getRoleList(userId);  // Call service method to get the role list
        return CommonResult.success(roleList);  // Return success response with the role list
    }

}
