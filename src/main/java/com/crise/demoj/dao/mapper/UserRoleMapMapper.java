package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.UserRoleMapEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapMapper extends BaseMapper<UserRoleMapEntity> {
    @Select("select role_id from ums_admin_role_relation where admin_id = #{admin_id} and is_active = 1")
    List<Long> getRoleIdByUserId(@Param("admin_id") Long userId);

    @Select("select * from ums_admin_role_relation where admin_id = #{adminId} and is_active = 1")
    List<UserRoleMapEntity> getEntityByUserId(@Param("adminId") Long adminId);
}
