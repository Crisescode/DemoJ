package com.crise.demoj.controller;

import com.crise.demoj.dto.CacheInfoDto;
import com.crise.demoj.dto.CacheKeyDto;
import com.crise.demoj.dto.api.CommonResult;
import com.crise.demoj.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cache")
@Tag(name = "缓存管理", description = "Redis 缓存监控与管理")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @Operation(summary = "获取 Redis 服务器信息")
    @GetMapping("/info")
    public CommonResult<CacheInfoDto> getCacheInfo() {
        return CommonResult.success(cacheService.getCacheInfo());
    }

    @Operation(summary = "获取缓存键列表")
    @GetMapping("/keys")
    public CommonResult<List<CacheKeyDto>> getCacheKeys(@RequestParam(required = false, defaultValue = "*") String pattern) {
        return CommonResult.success(cacheService.getCacheKeys(pattern));
    }

    @Operation(summary = "获取缓存值")
    @GetMapping("/value")
    public CommonResult<Map<String, String>> getCacheValue(@RequestParam String key) {
        String value = cacheService.getCacheValue(key);
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value != null ? value : "null");
        return CommonResult.success(map);
    }

    @Operation(summary = "删除单个缓存键")
    @DeleteMapping("/key")
    public CommonResult<Map<String, Object>> deleteCacheKey(@RequestParam String key) {
        boolean result = cacheService.deleteCacheKey(key);
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("deleted", result);
        return CommonResult.success(map);
    }

    @Operation(summary = "批量删除缓存键")
    @PostMapping("/keys/delete")
    public CommonResult<Map<String, Object>> deleteCacheKeys(@RequestBody Map<String, List<String>> body) {
        long count = cacheService.deleteCacheKeys(body.get("keys"));
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return CommonResult.success(map);
    }

    @Operation(summary = "清空所有缓存")
    @PostMapping("/clear")
    public CommonResult<Map<String, Object>> clearAllCache() {
        long count = cacheService.clearAllCache();
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return CommonResult.success(map);
    }

    @Operation(summary = "创建或更新缓存")
    @PostMapping("/set")
    public CommonResult<Map<String, Object>> setCacheValue(@RequestBody Map<String, Object> body) {
        String key = (String) body.get("key");
        String value = (String) body.get("value");
        Long ttl = body.get("ttl") != null ? Long.valueOf(body.get("ttl").toString()) : null;
        cacheService.setCacheValue(key, value, ttl);
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("created", true);
        return CommonResult.success(map);
    }
}
