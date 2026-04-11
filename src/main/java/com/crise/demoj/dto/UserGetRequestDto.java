package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserGetRequestDto {
    @NotEmpty
    @JsonProperty("username")
    private String username;
}
