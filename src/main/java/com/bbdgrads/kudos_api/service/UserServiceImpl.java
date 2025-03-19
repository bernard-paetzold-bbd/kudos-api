package com.bbdgrads.kudos_api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvents;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LogService logService;

    @Override
    public User save(User user) {
        user.setUsername(user.getUsername().replace(' ', '_'));

        Optional<User> existingUserUsername = userRepository.findByUsername(user.getUsername());
        int count = 1;
        String proposedUsername = user.getUsername();

        while (existingUserUsername.isPresent()) {
            proposedUsername = user.getUsername().concat(String.format("_%d", count));
            existingUserUsername = userRepository.findByUsername(proposedUsername);
            count += 1;
        }

        user.setUsername(proposedUsername);

        var resultUser = userRepository.save(user);

        var log = new Log();

        log.setActingUser(resultUser);
        log.setTargetUser(resultUser);
        log.setEventId(0);
        log.setVerboseLog(
                String.format("%s -- %s --> %s", resultUser.getUsername(), LogEvents.events.get(0),
                        resultUser.getUsername()));

        logService.save(log);

        return resultUser;
    }

    @Override
    public void delete(long userId) {
        User tempUser = userRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("User with id %s does not exist.", userId)));

        userRepository.delete(tempUser);

        var deleteUserLog = new Log();

        deleteUserLog.setActingUser(tempUser);
        deleteUserLog.setTargetUser(tempUser);
        deleteUserLog.setEventId(0);

        logService.save(deleteUserLog);
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleToken(googleId);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findByUserGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean updateUserTeam(Long userId, Long teamId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        Optional<Team> teamOptional = teamRepository.findById(teamId);

        if (userOptional.isEmpty() || teamOptional.isEmpty()) {
            return false;
        }

        int updatedRows = userRepository.updateUserTeam(userId, teamOptional.get());

        if (updatedRows > 0) {
            userOptional.ifPresent(user -> {
                var log = new Log();

                log.setActingUser(user);
                log.setTargetUser(user);
                log.setEventId(2);
                logService.save(log);
            });

            return true;
        } else
            return false;
    }
}
