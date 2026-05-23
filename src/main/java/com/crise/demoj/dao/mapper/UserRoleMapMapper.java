package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.UserEntity;
import com.crise.demoj.dao.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRoleMapMapper extends BaseMapper<UserRoleEntity> {
    @Select("select * from ums_role where username = #{name} and status = 1")
    UserEntity selectByName(@Param("name") String name);
}
