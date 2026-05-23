package com.crise.demoj.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleDeleteRequestDto {
    @NotNull
    private Long id;
}
