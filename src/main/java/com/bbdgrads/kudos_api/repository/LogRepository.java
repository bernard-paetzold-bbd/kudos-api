package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByActingUser(User actingUser);

    List<Log> findByEventId(int eventId);

    List<Log> findByLogId(Long logId);

    List<Log> findByTargetUser(User targetUser);
}
