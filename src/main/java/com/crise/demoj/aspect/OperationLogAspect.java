package com.crise.demoj.aspect;

import com.crise.demoj.annotation.OperationLog;
import com.crise.demoj.dao.entity.OperationLogEntity;
import com.crise.demoj.dto.UserInfoDto;
import com.crise.demoj.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Around("@annotation(com.crise.demoj.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        String ip = request != null ? request.getRemoteAddr() : "";
        String url = request != null ? request.getRequestURI() : "";

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        String operation = annotation.value();

        UserInfoDto userInfo = request != null ? (UserInfoDto) request.getAttribute("userInfo") : null;
        String username = userInfo != null ? userInfo.getUsername() : "";
        Long userId = userInfo != null ? userInfo.getId() : null;

        String methodName = method.getDeclaringClass().getName() + "." + method.getName();

        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable t) {
            long duration = System.currentTimeMillis() - start;
            saveLog(userId, username, operation, methodName, pjp.getArgs(), url, ip, duration, t.getMessage());
            throw t;
        }

        long duration = System.currentTimeMillis() - start;
        saveLog(userId, username, operation, methodName, pjp.getArgs(), url, ip, duration, null);

        return result;
    }

    private void saveLog(Long userId, String username, String operation, String methodName,
                         Object[] args, String url, String ip, long duration, String errorMsg) {
        OperationLogEntity entity = new OperationLogEntity();
        entity.setUserId(userId);
        entity.setUsername(username);
        entity.setOperation(operation);
        entity.setMethod(methodName);
        entity.setParams(argsToString(args));
        entity.setUrl(url);
        entity.setIp(ip);
        entity.setDuration(duration);
        entity.setResult(errorMsg != null ? "ERROR: " + errorMsg : "SUCCESS");
        operationLogService.save(entity);
    }

    private String argsToString(Object[] args) {
        if (args == null || args.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            try {
                sb.append(args[i] != null ? args[i].toString() : "null");
            } catch (Exception e) {
                sb.append("[unable to serialize]");
            }
        }
        return sb.toString();
    }
}
