package com.crise.demoj.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleDeleteAllRequestDto {
    private List<Long> ids;
}
