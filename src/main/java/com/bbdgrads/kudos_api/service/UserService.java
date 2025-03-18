package com.bbdgrads.kudos_api.service;

import java.util.List;
import java.util.Optional;

import com.bbdgrads.kudos_api.model.User;

public interface UserService {
    // Changed to optional from List
    Optional<User> findByUserId(Long userId);

    User save(User user);

    void delete(long userId);
}