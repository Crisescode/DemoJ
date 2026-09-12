package com.crise.demoj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaFallbackController {

    @GetMapping({
            "/",
            "/dashboard",
            "/users",
            "/roles",
            "/permissions",
            "/cache",
            "/login"
    })
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
