package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserRoleUpdateByUserRequestDto {
    @NotNull
    @JsonProperty("user_id")
    Long userId;

    @NotNull
    @JsonProperty("role_ids")
    List<Long> roleIds;
}
