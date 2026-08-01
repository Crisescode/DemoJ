package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenRequestDto {
    @NotEmpty(message = "token 不能为空")
    @JsonProperty("token")
    private String token;
}
