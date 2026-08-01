package com.crise.demoj.controller;

import com.crise.demoj.dto.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product")
@Tag(name = "商品管理", description = "商品管理模块相关接口")
public class ProductController {
    @Operation(summary = "创建商品")
    @PostMapping("/create")
    public CommonResult<String> create() {
        return CommonResult.success("TODO");
    }
}
