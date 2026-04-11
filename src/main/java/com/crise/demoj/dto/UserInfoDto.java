package com.crise.demoj.dto;

import cn.hutool.core.date.DateTime;
import lombok.Data;

@Data
public class UserInfoDto {
    private Long id;
    private String username;
    private String email;
    private String icon;
    private String nickName;
    private String note;
    private DateTime loginTime;
    private Integer status;
}
