package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.RolePermissionRelationEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RolePermissionRelationMapper extends BaseMapper<RolePermissionRelationEntity> {

    @Select("select permission_id from ums_role_permission_relation where role_id = #{roleId}")
    List<Long> getPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
