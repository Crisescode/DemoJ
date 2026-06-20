package com.crise.demoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crise.demoj.dao.entity.UserRoleEntity;
import com.crise.demoj.dao.mapper.UserRoleMapper;
import com.crise.demoj.dto.*;
import com.crise.demoj.dto.api.CommonPage;
import com.crise.demoj.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleMapService userRoleMapService;

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

    public List<UserRoleInfoDto> getAllByIds(List<Long> ids) {
        List<UserRoleInfoDto> resp = new ArrayList<>();
        for(Long id : ids) {
            UserRoleInfoDto userRoleInfo = getById(id);
            resp.add(userRoleInfo);
        }

        return resp;
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
        UserRoleEntity entity = userRoleMapper.selectById(req.getId());
        if (entity == null) {
            String errMsg = String.format("UserRole(id: %d) not found.", req.getId());
            throw new UserException(errMsg);
        }

        // 无论哪个属性，都直接复制进去
        BeanUtils.copyProperties(req, entity);

        // 更新数据
        userRoleMapper.updateById(entity);

        return getById(req.getId());
    }

    // 获取所有角色，不分页
    public List<UserRoleInfoDto> listAll(UserRoleListAllRequestDto req) {
        List<UserRoleEntity> userRoleEntityList = userRoleMapper.selectList(
                new QueryWrapper<UserRoleEntity>().eq("is_active", 1)
        );
        List<UserRoleInfoDto> result = new ArrayList<>();
        for (UserRoleEntity entity : userRoleEntityList) {
            UserRoleInfoDto dto = new UserRoleInfoDto();
            BeanUtils.copyProperties(entity, dto);
            result.add(dto);
        }
        return result;
    }

    public CommonPage<UserRoleInfoDto> list(UserRoleListRequestDto req) {
        Page<UserRoleEntity> page = new Page<>(req.getPageNum(), req.getPageSize());

        IPage<UserRoleEntity> userRolePage = userRoleMapper.selectPage(page,
                new QueryWrapper<UserRoleEntity>().eq("is_active", 1).like("name", req.getKeyword())
        );

        List<UserRoleEntity> userRoles = userRolePage.getRecords();
        List<UserRoleInfoDto> userRoleInfos = new ArrayList<>();
        for(UserRoleEntity userRole : userRoles) {
            UserRoleInfoDto dto = new UserRoleInfoDto();
            BeanUtils.copyProperties(userRole, dto);
            userRoleInfos.add(dto);
        }

        Page<UserRoleInfoDto> dtoPage = new Page<>(
                userRolePage.getCurrent(),
                userRolePage.getSize(),
                userRolePage.getTotal()
        );
        dtoPage.setRecords(userRoleInfos);

        return CommonPage.fromPage(dtoPage);
    }

    public void setNotActive(Long id) {
        // 查询是否存在
        UserRoleEntity entity = userRoleMapper.selectById(id);
        if (entity == null) {
            String errMsg = String.format("UserRole(id: %d) not found.", id);
            throw new UserException(errMsg);
        }

        int res = userRoleMapper.update(entity, new UpdateWrapper<UserRoleEntity>().eq("id", id)
                .set("is_active", 0)
                .set("delete_token", entity.getId()));

        log.info("res: {}", res);
    }

    // 批量删除角色
    public HashMap<String, String> deleteAll(UserRoleDeleteAllRequestDto req) {
        // 获取 ids
        List<Long> ids = req.getIds();

        // 批量软删除
        for (Long id: ids) {
            List<Long> userIds = userRoleMapService.getUserIdsByRoleId(id);
            if (userIds.size() == 0) {
                setNotActive(id);
            }
        }

        HashMap<String, String> res = new HashMap<>();
        res.put("msg", "删除成功");

        return res;
    }
}
