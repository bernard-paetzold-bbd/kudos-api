package com.bbdgrads.kudos_api.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvents;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.service.LogService;
import com.bbdgrads.kudos_api.service.UserServiceImpl;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/logs")
public class LogController {
    @Autowired
    private LogService logService;

    @Autowired
    private UserServiceImpl userService;

    // Get all logs relating to a specific acting user
    @GetMapping("/acting-user/{userId}")
    public ResponseEntity<List<LogDto>> getLogsByActingUser(@PathVariable Long userId) {
        List<Log> userLogs = Collections.emptyList();

        var user = userService.findByUserId(userId);

        if (!user.isPresent()) {
            userLogs = logService.findByActingUser(user.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertLogsToDto(userLogs));
    }

    // Get all logs relating to a specific target user
    @GetMapping("/target-user/{userId}")
    public ResponseEntity<List<LogDto>> getLogsByTargetUser(@PathVariable Long userId) {
        List<Log> userLogs = Collections.emptyList();

        var user = userService.findByUserId(userId);

        if (!user.isPresent()) {
            userLogs = logService.findByTargetUser(user.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertLogsToDto(userLogs));
    }

    // Get all logs by event
    @GetMapping("/type/{event}")
    public ResponseEntity<List<LogDto>> getLogsByEvent(@PathVariable String event) {
        List<Log> logs = Collections.emptyList();

        // Map the event name to it's id
        var eventId = LogEvents.events.inverse().get(event);

        // If no match is found, try and parse it as an id
        if (eventId == null) {
            eventId = Integer.parseInt(event);
        }

        logs = logService.findByEventId(eventId);

        return ResponseEntity.status(HttpStatus.OK).body(convertLogsToDto(logs));
    }

    private List<LogDto> convertLogsToDto(List<Log> logs) {
        return logs.stream()
                .map(log -> new LogDto(
                        log.getLogId(),
                        log.getActingUser() != null ? log.getActingUser().getUsername() : null,
                        log.getTargetUser() != null ? log.getTargetUser().getUsername() : null,
                        log.getKudo() != null ? log.getKudo().getKudoId() : null,
                        log.getTeam() != null ? log.getTeam().getName() : null,
                        log.getEventId(),
                        log.getVerboseLog(),
                        log.getLogTime()))
                .toList();
    }

    private LogDto convertLogToDto(Log log) {
        return new LogDto(
                log.getLogId(),
                log.getActingUser() != null ? log.getActingUser().getUsername() : null,
                log.getTargetUser() != null ? log.getTargetUser().getUsername() : null,
                log.getKudo() != null ? log.getKudo().getKudoId() : null,
                log.getTeam() != null ? log.getTeam().getName() : null,
                log.getEventId(),
                log.getVerboseLog(),
                log.getLogTime());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class LogDto {
        private Long logId;
        private String actingUser;
        private String targetUser;
        private Long kudoId;
        private String team;
        private int eventId;
        private String verboseLog;
        private LocalDateTime logTime;
    }
}
