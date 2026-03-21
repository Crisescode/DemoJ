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

import com.crise.demoj.dto.ResultDto;
import com.crise.demoj.dto.UserLoginRequestDto;
import com.crise.demoj.dto.UserRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import com.crise.demoj.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/app/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostMapping("/register")
    public ResultDto<Map<String, String>> register(@RequestBody UserRegisterRequestDto req) {
        try {
            if(req.getUsername().length() > 32) {
                throw new RuntimeException("用户名长度不能超过32个字符");
            }
            if(req.getPassword().length() > 64) {
                throw new RuntimeException("密码长度不能超过64个字符");
            }

            userService.register(req);
        } catch (Exception e) {
            log.error("注册失败", e);
            return ResultDto.error("注册失败：" + e.getMessage());
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "注册成功");
        return ResultDto.success(response);
    }

    // 1. 查用户是否存在
    // 2. 存在则验证密码是否正确
    // 3. 正确则生成 jwt token
    @PostMapping("/login")
    public ResultDto<Map<String, String>> login(@RequestBody UserLoginRequestDto req) {
        String token = userService.login(req);
        if (token == null) {
            throw new RuntimeException("登录失败");
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenHead", tokenHead);
        response.put("message", "登录成功");

        return ResultDto.success(response);
    }

//    @PostMapping("/get")
//    public ResultDto<UserDto> getUser(@RequestBody UserGetRequestDto req) {
//        return ResultDto.success(userService.getUser(req));
//    }
//
//    @PostMapping("/update")
//    public ResultDto<Map<String, String>> updateUser(@RequestBody UserUpdateRequestDto req) {
//
//    }
//
//    @PostMapping("/delete")
//    public ResultDto<Map<String, String>> deleteUser(@RequestBody UserDeleteRequestDto req) {
//
//    }
}
