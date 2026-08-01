package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionInfoDto {
    private Long id;
    private String name;
    private String description;
    private String url;
    @JsonProperty("method")
    private String method;
    @JsonProperty("parent_id")
    private Long parentId;
    private Integer type;
    private String icon;
    private Integer sort;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    private Integer status;
}
