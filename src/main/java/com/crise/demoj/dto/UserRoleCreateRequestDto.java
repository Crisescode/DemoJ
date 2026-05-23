package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleCreateRequestDto {
    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("status")
    private Integer status;
}
