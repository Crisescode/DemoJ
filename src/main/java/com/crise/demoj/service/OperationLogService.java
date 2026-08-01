package com.crise.demoj.service;

import com.crise.demoj.dao.entity.OperationLogEntity;
import com.crise.demoj.dao.mapper.OperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    public void save(OperationLogEntity entity) {
        entity.setCreateTime(LocalDateTime.now());
        operationLogMapper.insert(entity);
    }
}
