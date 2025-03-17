package com.bbdgrads.kudos_api.service;

import java.util.List;
import java.util.Optional;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

public interface KudoService {
    // Return Kudo, so the saver can see the ID provided to it.
    Kudo save(Kudo kudo);

    void delete(long id);

    // Changed here
    Optional<Kudo> findByKudoId(Long kudoId);

    List<Kudo> findByTargetUser(User targetId);

    List<Kudo> findBySendingUser(User targetId);
}