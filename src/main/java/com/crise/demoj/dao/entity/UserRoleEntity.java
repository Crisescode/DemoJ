package com.crise.demoj.dao.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ums_role")
public class UserRoleEntity {
    private Long id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("admin_count")
    private Integer adminCount;

    @TableField("status")
    private String status;

    @TableField("sort")
    private Integer sort;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
