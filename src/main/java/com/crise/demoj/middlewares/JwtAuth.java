package com.crise.demoj.middlewares;

import com.crise.demoj.dao.entity.UserEntity;
import com.crise.demoj.dto.PermissionInfoDto;
import com.crise.demoj.dto.UserInfoDto;
import com.crise.demoj.dto.api.ResultCode;
import com.crise.demoj.exception.UserException;
import com.crise.demoj.service.PermissionService;
import com.crise.demoj.service.UserRoleMapService;
import com.crise.demoj.service.UserService;
import com.crise.demoj.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Slf4j
public class JwtAuth implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleMapService userRoleMapService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        // 1. 获取 token
        String token = request.getHeader("token");
        if (token == null) {
            throw new RuntimeException("jwt token 不存在");
        }

        // 2. 解析 token
        String realToken = parseToken(token);
        if (realToken == null) {
            throw new UserException(ResultCode.UNAUTHORIZED, "jwt token 格式错误");
        }

        // 3. 验证 jwt token
        String userName = jwtTokenUtils.getUserNameFromToken(realToken);
        if (userName == null) {
            throw new UserException(ResultCode.UNAUTHORIZED, "jwt token 验证失败");
        }

        // 3.1 token 快过期时自动续期
        if (jwtTokenUtils.isTokenAboutToExpire(realToken)) {
            log.info("==== ");
            UserEntity userEntity = userService.getUserByNameEntity(userName);
            if (userEntity != null) {
                String newToken = jwtTokenUtils.generateToken(userEntity);
                response.setHeader("token", newToken);
                log.debug("token 即将过期，已自动续期: user={}", userName);
            }
        }

        // 4. 验证用户是否存在
        UserInfoDto user = userService.getUserByName(userName);
        if (user == null) {
            throw new UserException(ResultCode.USER_FAILED, "用户不存在");
        }

        // 5. 将用户信息放入 request 中，供后续使用
        request.setAttribute("userInfo", user);

        // 6. 权限校验：加载用户的所有权限，检查当前请求是否匹配
        List<PermissionInfoDto> permissions = permissionService.getByUserId(user.getId());

        boolean hasPermission = false;
        for (PermissionInfoDto perm : permissions) {
            if (perm.getUrl() == null || perm.getUrl().isEmpty()) {
                continue;
            }
            String pattern = perm.getUrl().replace("**", ".*").replace("*", "[^/]*");
            if (requestURI.matches(pattern)) {
                if (perm.getMethod() == null || perm.getMethod().isEmpty() ||
                        perm.getMethod().equalsIgnoreCase(requestMethod)) {
                    hasPermission = true;
                    break;
                }
            }
        }

        if (!hasPermission) {
            log.warn("权限不足: user={}, uri={}, method={}", userName, requestURI, requestMethod);
            throw new UserException(ResultCode.FORBIDDEN, "没有相关权限");
        }

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
