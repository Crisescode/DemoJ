package com.crise.demoj.dto;

import java.util.List;
import java.util.Map;

public class CacheInfoDto {

    private Map<String, String> info;

    private Long dbSize;

    private List<CacheStatsDto> commandStats;

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public Long getDbSize() {
        return dbSize;
    }

    public void setDbSize(Long dbSize) {
        this.dbSize = dbSize;
    }

    public List<CacheStatsDto> getCommandStats() {
        return commandStats;
    }

    public void setCommandStats(List<CacheStatsDto> commandStats) {
        this.commandStats = commandStats;
    }
}
