package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRegisterRequestDto {
    @JsonProperty("user_name")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("phone_number")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name_cn")
    private String nameCN;

    @JsonProperty("sex")
    private Integer sex;
}
