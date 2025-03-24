package com.bbdgrads.kudos_api.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.LogEvents;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.LogRepository;

@Service
public class LogServiceImpl implements LogService {

    // private final OpenAPI customOpenAPI;

    // private final AuthService authService;

    @Autowired
    private LogRepository logRepository;

    // LogServiceImpl(AuthService authService, OpenAPI customOpenAPI) {
    //     this.authService = authService;
    //     this.customOpenAPI = customOpenAPI;
    // }

    // public LogServiceImpl() {
    //     this.authService = null;
    //     this.customOpenAPI = null;
    // }

    

    @Override
    public void save(Log log) {
        log.setVerboseLog((String.format("%s: ", LocalDateTime.now().toString())).concat(log.getVerboseLog() == null ? "" :log.getVerboseLog()));
        logRepository.save(log);
    }

    @Override
    public List<Log> findByActingUser(User actingUser) {
        return logRepository.findByActingUser(actingUser);
    }

    @Override
    public List<Log> findByEventId(int eventId) {
        return logRepository.findByEventId(eventId);
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
