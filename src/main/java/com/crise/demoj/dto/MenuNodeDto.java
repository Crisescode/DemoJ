package com.crise.demoj.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuNodeDto {
    private Long id;
    private String name;
    private String path;
    private String icon;
    private Integer sort;
    private Integer type;
    private List<MenuNodeDto> children = new ArrayList<>();
}
