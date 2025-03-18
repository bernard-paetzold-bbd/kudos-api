package com.bbdgrads.kudos_api.service;

import java.util.List;
import java.util.Optional;


import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(long userId) {
        User tempUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s does not exist.", userId)));
        userRepository.delete(tempUser);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findByUserToken(String userTokenId) {
        return userRepository.findByGoogleToken(userTokenId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean updateUserTeam(Long userId, Long teamId){
        Optional<User> userOptional = userRepository.findByUserId(userId);
        Optional<Team> teamOptional = teamRepository.findById(teamId);

        if (userOptional.isEmpty() || teamOptional.isEmpty()){
            return false;
        }

        int updatedRows = userRepository.updateUserTeam(userId, teamOptional.get());
        return updatedRows > 0;
    }
}
