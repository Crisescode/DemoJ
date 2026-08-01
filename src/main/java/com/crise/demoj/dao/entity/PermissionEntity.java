package com.crise.demoj.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ums_permission")
public class PermissionEntity {
    private Long id;

    private String name;

    private String description;

    private String url;

    @TableField("method")
    private String method;

    @TableField("parent_id")
    private Long parentId;

    private Integer type;

    private String icon;

    private Integer sort;

    @TableField("create_time")
    private LocalDateTime createTime;

    private Integer status;
}
