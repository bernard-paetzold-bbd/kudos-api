package com.bbdgrads.kudos_api.service;

import java.util.List;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.User;

public interface LogService {
    List<Log> findByActingUser(User actingUser);

    List<Log> findByEventId(int eventId);

    List<Log> findByLogId(Long logId);

    List<Log> findByTargetUser(User targetUser);

    void save(Log log);
}