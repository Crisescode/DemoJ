package com.crise.demoj.dao.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("ums_admin")
public class UserEntity {
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("icon")
    private String icon;

    @TableField("email")
    private String email;

    @TableField("nick_name")
    private String nick_name;

    @TableField("note")
    private String note;

    @TableField("create_time")
    private DateTime create_time;

    @TableField("login_time")
    private DateTime login_time;

    @TableField("status")
    private Integer status;
}
