package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserRegisterRequestDto {
    @NotEmpty(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能大于32")
    @JsonProperty("username")
    private String username;

    @NotEmpty
    @JsonProperty("password")
    private String password;

    @JsonProperty("icon")
    private String icon;

    @Email
    @JsonProperty("email")
    private String email;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("note")
    private String note;
}
