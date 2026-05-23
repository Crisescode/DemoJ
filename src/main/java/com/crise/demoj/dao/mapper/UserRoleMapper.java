package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {
    @Select("select * from ums_role where name = #{name} and is_active = 1")
    UserRoleEntity selectByName(@Param("name") String name);
}
