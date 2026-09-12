package com.crise.demoj.service;

import cn.hutool.core.util.StrUtil;
import com.crise.demoj.dto.CacheInfoDto;
import com.crise.demoj.dto.CacheKeyDto;
import com.crise.demoj.dto.CacheStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public CacheInfoDto getCacheInfo() {
        CacheInfoDto dto = new CacheInfoDto();

        Properties info = redisTemplate.execute((RedisCallback<Properties>) connection -> {
            return connection.serverCommands().info();
        });

        Map<String, String> infoMap = new HashMap<>();
        if (info != null) {
            for (String key : info.stringPropertyNames()) {
                infoMap.put(key, info.getProperty(key));
            }
        }
        dto.setInfo(infoMap);

        Long dbSize = redisTemplate.execute((RedisCallback<Long>) connection -> connection.serverCommands().dbSize());
        dto.setDbSize(dbSize);

        Properties cmdStats = redisTemplate.execute((RedisCallback<Properties>) connection -> {
            return connection.serverCommands().info("commandstats");
        });
        List<CacheStatsDto> statsList = new ArrayList<>();
        if (cmdStats != null) {
            for (String key : cmdStats.stringPropertyNames()) {
                if (key.startsWith("cmdstat_")) {
                    statsList.add(new CacheStatsDto(
                            key.replace("cmdstat_", ""),
                            cmdStats.getProperty(key)
                    ));
                }
            }
        }
        dto.setCommandStats(statsList);

        return dto;
    }

    public List<CacheKeyDto> getCacheKeys(String pattern) {
        Set<String> keys;
        if (StrUtil.isBlank(pattern)) {
            keys = redisTemplate.keys("*");
        } else {
            keys = redisTemplate.keys(pattern);
        }

        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        return keys.stream().map(key -> {
            CacheKeyDto dto = new CacheKeyDto();
            dto.setKey(key);

            String type = String.valueOf(redisTemplate.type(key));
            dto.setType(type != null ? type : "unknown");

            Long ttl = redisTemplate.getExpire(key);
            dto.setTtl(ttl);

            Long size = redisTemplate.execute((RedisCallback<Long>) connection -> connection.stringCommands().strLen(key.getBytes()));
            dto.setSize(size != null ? size : 0L);

            return dto;
        }).sorted(Comparator.comparing(CacheKeyDto::getKey)).collect(Collectors.toList());
    }

    public String getCacheValue(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public boolean deleteCacheKey(String key) {
        Boolean deleted = redisTemplate.delete(key);
        return deleted != null && deleted;
    }

    public long deleteCacheKeys(List<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count != null ? count : 0;
    }

    public long clearAllCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        Long count = redisTemplate.delete(keys);
        return count != null ? count : 0;
    }

    public void setCacheValue(String key, String value, Long ttl) {
        redisTemplate.opsForValue().set(key, value);
        if (ttl != null && ttl > 0) {
            redisTemplate.expire(key, java.time.Duration.ofSeconds(ttl));
        }
    }
}
