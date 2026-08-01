package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.PermissionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    @Select("select p.* from ums_permission p " +
            "join ums_role_permission_relation rp on p.id = rp.permission_id " +
            "where rp.role_id = #{roleId} and p.status = 1")
    List<PermissionEntity> getByRoleId(@Param("roleId") Long roleId);

    @Select("select distinct p.* from ums_permission p " +
            "join ums_role_permission_relation rp on p.id = rp.permission_id " +
            "join ums_admin_role_relation ar on ar.role_id = rp.role_id " +
            "where ar.admin_id = #{userId} and p.status = 1")
    List<PermissionEntity> getByUserId(@Param("userId") Long userId);
}
