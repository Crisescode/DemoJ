package com.crise.demoj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crise.demoj.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<UserEntity> {
    @Select("select * from demoj_user where name = #{name} and is_delete = 0")
    UserEntity selectByName(@Param("name") String name);
}
