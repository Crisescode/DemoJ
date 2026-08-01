package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PermissionCreateRequestDto {
    @NotEmpty(message = "权限名称不能为空")
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
}
