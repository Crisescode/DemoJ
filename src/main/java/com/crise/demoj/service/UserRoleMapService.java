package com.crise.demoj.service;

import com.crise.demoj.dao.entity.UserEntity;
import com.crise.demoj.dao.entity.UserRoleMapEntity;
import com.crise.demoj.dao.mapper.UserRoleMapMapper;
import com.crise.demoj.dto.UserRoleInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserRoleMapService {
    @Resource
    private UserRoleMapMapper userRoleMapMapper;

    @Autowired
    private UserRoleService userRoleService;

    public List<UserRoleInfoDto> getRoleListByUserId(Long userId){
        // 1. 从关联表中查询到该用户关联了哪些角色
        List<Long> roleIds = userRoleMapMapper.getRoleIdByUserId(userId);

        // 2. 根据角色id查询角色信息
        List<UserRoleInfoDto> resp = new ArrayList<>();
        for(Long roleId : roleIds) {
            log.info("roleId: {}", roleId);
            UserRoleInfoDto usrRoleInfo = userRoleService.getById(roleId);
            resp.add(usrRoleInfo);
        }

        // 3. 返回
        return resp;
    }

    public int create(Long userId, List<Long> roleIds) {
        // 直接创建
        int count = 0;
        LocalDateTime now = LocalDateTime.now();
        for (Long roleId : roleIds) {
            UserRoleMapEntity newEntity = new UserRoleMapEntity();
            newEntity.setUserId(userId);
            newEntity.setRoleId(roleId);
            newEntity.setCreateTime(now);
            newEntity.setUpdateTime(now);
            int row = userRoleMapMapper.insert(newEntity);
            count += row;
        }

        return count;
    }

    public void deleteByUserId(Long userId) {
        // 1. 查询关联关系
        List<UserRoleMapEntity> entities = userRoleMapMapper.getEntityByUserId(userId);

        // 2. 删除关联关系
        for(UserRoleMapEntity entity : entities) {
            userRoleMapMapper.deleteById(entity.getId());
        }
    }
}
