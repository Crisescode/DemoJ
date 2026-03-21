package com.crise.demoj.service;

import com.crise.demoj.dao.entity.UserEntity;
import com.crise.demoj.dto.UserLoginRequestDto;
import com.crise.demoj.dto.UserRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crise.demoj.dao.mapper.UserMapper;
import com.crise.demoj.utils.JwtTokenUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    public void register(UserRegisterRequestDto req) {
        // 查询是否有该用户
        UserEntity user = userMapper.selectByName(req.getUsername());
        if (user != null) {
            throw new RuntimeException("用户已存在");
        }

        // 没有则注册
        UserEntity newUser = new UserEntity();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());
        newUser.setEmail(req.getEmail());

        int rows = userMapper.insert(newUser);

        if (rows > 0) {
            System.out.println("User inserted successfully.");
        } else {
            System.out.println("Failed to insert user.");
        }
    }

    public String login(UserLoginRequestDto req) {
        String token = null;

        // 1. 查询是否有该用户
        UserEntity user = userMapper.selectByName(req.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 比较密码
        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        try {
            // 3. 生成token
            token = jwtTokenUtils.generateToken(user);
        } catch (Exception e) {
            log.warn("登录异常:{}", e.getMessage());
        }

        return token;
    }
}
