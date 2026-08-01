package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PermissionUpdateRequestDto {
    @NotNull(message = "ID不能为空")
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

    private Integer status;
}
