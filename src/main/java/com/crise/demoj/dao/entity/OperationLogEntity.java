package com.crise.demoj.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ums_operation_log")
public class OperationLogEntity {
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String username;

    private String operation;

    private String method;

    private String params;

    private String url;

    private String ip;

    private Long duration;

    private String result;

    @TableField("create_time")
    private LocalDateTime createTime;
}
