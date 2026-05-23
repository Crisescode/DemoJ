package com.crise.demoj.middlewares;

import com.crise.demoj.dto.UserInfoDto;
import com.crise.demoj.service.UserService;
import com.crise.demoj.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 1. URL, HEADER, PARAMETER, COOKIE
// bean

@Component
@Slf4j
public class JwtAuth implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("requestURI:{}", requestURI);
        log.info("request: {}", request.getHeader("Authorization"));

        // 1. 获取 token
        String token = request.getHeader("token");
        if (token == null) {
            throw new RuntimeException("jwt token 不存在");
        }

        // 获取真正的 token
        String realToken = parseToken(token);

        // 验证 jwt token
        String userName = jwtTokenUtils.getUserNameFromToken(realToken);
        if (userName == null) {
            throw new RuntimeException("jwt token 验证失败");
        }

        // 验证用户是否存在，防止造 token
        UserInfoDto user = userService.getUserByName(userName);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 将用户信息放入 request 中

        return true;
    }

    private String parseToken(String token) {
        String tokenPrefix = "Bearer ";
        if(token != null && token.startsWith(tokenPrefix)) {
            return token.substring(tokenPrefix.length()).trim();
        }
        return null;
    }
}
