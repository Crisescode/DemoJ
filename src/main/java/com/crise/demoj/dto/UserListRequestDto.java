package com.crise.demoj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserListRequestDto {
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("page_num")
    private Integer pageNum;
}
