package com.crise.demoj.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ums_admin_role_relation")
public class UserRoleMapEntity {
    private Long id;

    @TableField("admin_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
