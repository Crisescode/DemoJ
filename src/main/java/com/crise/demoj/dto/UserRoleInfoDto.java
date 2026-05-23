package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRoleInfoDto {
    private Long id;
    private String name;
    private String description;
    private String status;

    @JsonProperty("admin_count")
    private Integer adminCount;
    private Integer sort;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    @JsonProperty("update_time")
    private LocalDateTime updateTime;
}
