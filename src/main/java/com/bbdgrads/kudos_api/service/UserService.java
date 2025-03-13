package com.bbdgrads.kudos_api.service;

import java.util.List;

import com.bbdgrads.kudos_api.model.User;

public interface UserService {
    List<User> findByUserId(Long userId);

    void save(User user);

    void delete(long id);
}