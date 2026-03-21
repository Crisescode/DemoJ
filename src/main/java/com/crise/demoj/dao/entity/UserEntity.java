package com.crise.demoj.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("demoj_user")
public class UserEntity {
    private Long id;
    private String name;
    private String email;
    private String nameCn;
    private String password;
    private String phone;
    private Integer sex;

//    Instance createTime;
}
