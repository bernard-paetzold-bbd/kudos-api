package com.bbdgrads.kudos_api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.service.LogService;

@RestController
@RequestMapping(path = "/logs")
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/create")
    public ResponseEntity<String> createLog(@RequestParam Long user_id, @RequestParam String name) {
        var log = new Log();

        logService.save(log);

        return ResponseEntity.ok("Saved");
    }

    // Get all logs relating to a specific user
    @GetMapping("/my-kudos/{userId}")
    public List<Log> getUserLogs(@PathVariable Long userId) {
        var userLog = new ArrayList<Log>();

        return userLog;
    }
}
