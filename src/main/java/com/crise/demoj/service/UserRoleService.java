package com.crise.demoj.service;

import com.crise.demoj.dao.entity.UserRoleEntity;
import com.crise.demoj.dao.mapper.UserRoleMapper;
import com.crise.demoj.dto.UserRoleCreateRequestDto;
import com.crise.demoj.dto.UserRoleInfoDto;
import com.crise.demoj.dto.UserRoleUpdateRequestDto;
import com.crise.demoj.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserRoleInfoDto create(UserRoleCreateRequestDto req) {
        UserRoleEntity entity = new UserRoleEntity();
        BeanUtils.copyProperties(req, entity);

        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        int rows = userRoleMapper.insert(entity);

        if (rows > 0) {
            log.info("User inserted successfully.");
        } else {
            log.info("Failed to insert user.");
        }

        return getById(entity.getId());
    }

    public UserRoleInfoDto getById(Long id) {
        UserRoleEntity entity = userRoleMapper.selectById(id);
        if (entity == null) {
            String errMsg = String.format("UserRole(id: %d) not found.", id);
            throw new UserException(errMsg);
        }

        UserRoleInfoDto result = new UserRoleInfoDto();
        BeanUtils.copyProperties(entity, result);

        return result;
    }

    public HashMap<String, String> deleteById(Long id) {
        UserRoleEntity entity = userRoleMapper.selectById(id);
        if (entity == null) {
            String errMsg = String.format("UserRole(id: %d) not found.", id);
            throw new UserException(errMsg);
        }
        HashMap<String, String> result = new HashMap<>();
        int rows = userRoleMapper.deleteById(id);
        if (rows > 0) {
            result.put("message", "用户删除成功.");
        } else {
            result.put("message", "用户删除失败.");
        }
        return result;
    }

    public UserRoleInfoDto update(UserRoleUpdateRequestDto req) {
        return null;
    }
}
