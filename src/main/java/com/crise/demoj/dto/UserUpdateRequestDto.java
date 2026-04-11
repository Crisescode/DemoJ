package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserUpdateRequestDto {
    @NotEmpty
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("note")
    private String note;
}
