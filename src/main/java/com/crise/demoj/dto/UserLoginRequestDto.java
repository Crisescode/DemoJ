package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @JsonProperty("user_name")
    String Username;
    @JsonProperty("password")
    String Password;
}
