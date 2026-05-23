package com.crise.demoj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crise.demoj.dao.entity.UserEntity;
import com.crise.demoj.dto.*;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.dto.api.ResultCode;
import com.crise.demoj.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crise.demoj.dao.mapper.UserMapper;
import com.crise.demoj.utils.JwtTokenUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
            throw new UserException(ResultCode.USER_FAILED, "用户已存在");
        }

        // 没有则注册
        UserEntity newUser = new UserEntity();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());
        newUser.setIcon(req.getIcon());
        newUser.setEmail(req.getEmail());
        newUser.setNickName(req.getNickName());
        newUser.setNote(req.getNote());
        newUser.setStatus(1);

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

    public UserInfoDto getUserByName(String userName) {
        UserEntity user = userMapper.selectByName(userName);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(user, userInfoDto);

        return userInfoDto;
    }

    public UserInfoDto updateUser(UserUpdateRequestDto req) {
        // 1. 获取用户是否存在
        // 2. 更新相关的字段
        // 3. 将更新后的信息返回

        UserEntity user = userMapper.selectByName(req.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (req.getNote() != null) {
            user.setNote(req.getNote());
        }
        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }
        if (req.getIcon() != null) {
            user.setIcon(req.getIcon());
        }
        if (req.getNickName() != null) {
            user.setNickName(req.getNickName());
        }

        userMapper.updateById(user);

        return getUserByName(user.getUsername());
    }

    public void deleteUser(String userName) {
        UserEntity user = userMapper.selectByName(userName);
        if (user == null) {
            throw new UserException("用户不存在");
        }

        // 硬删除：真实把数据库对应 id 行数的数据删除
//        userMapper.deleteById(user.getId());
//        log.info("硬删除用户：{} 成功", userName);

        // 软删除：把 status 字段置为0
        user.setStatus(0);
        userMapper.updateById(user);
        log.info("软删除用户：{} 成功", userName);
    }

    public CommonPage<UserInfoDto> listUsers(UserListRequestDto req) {
        Page<UserEntity> page = new Page<>(req.getPageNum(), req.getPageSize());

        IPage<UserEntity> userPage = userMapper.selectPage(page, null);

        List<UserEntity> users = userPage.getRecords();
        List<UserInfoDto> userInfos = new ArrayList<>();
        for(UserEntity user : users) {
            UserInfoDto dto = new UserInfoDto();
            BeanUtils.copyProperties(user, dto);
            userInfos.add(dto);
        }


//        List<UserInfoDto> userInfos = userPage.getRecords().stream().map(user -> {
//            UserInfoDto dto = new UserInfoDto();
//            BeanUtils.copyProperties(user, dto);
//            return dto;
//        }).collect(Collectors.toList());

        Page<UserInfoDto> dtoPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        dtoPage.setRecords(userInfos);

        return CommonPage.fromPage(dtoPage);
    }
}
