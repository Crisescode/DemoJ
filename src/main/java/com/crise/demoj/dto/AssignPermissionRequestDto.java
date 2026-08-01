package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AssignPermissionRequestDto {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @JsonProperty("permission_ids")
    private List<Long> permissionIds;
}
