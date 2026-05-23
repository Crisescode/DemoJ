package com.crise.demoj.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleGetRequestDto {
    @NotNull
    private Long id;
}
