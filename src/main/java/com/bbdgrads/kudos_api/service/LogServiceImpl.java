package com.bbdgrads.kudos_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.LogRepository;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogRepository logRepository;

    @Override
    public void save(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> findByActingUser(User actingUser) {
        return logRepository.findByActingUser(actingUser);
    }

    @Override
    public List<Log> findByEvent(LogEvent event) {
        return logRepository.findByEvent(event);
    }

    @Override
    public List<Log> findByLogId(Long logId) {
        return logRepository.findByLogId(logId);
    }

    @Override
    public List<Log> findByTargetUser(User targetUser) {
        return logRepository.findByTargetUser(targetUser);
    }

}
