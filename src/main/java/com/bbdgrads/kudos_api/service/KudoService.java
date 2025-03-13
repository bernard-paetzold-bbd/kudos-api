package com.bbdgrads.kudos_api.service;

import java.util.List;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

public interface KudoService {
    void save(Kudo kudo);

    void delete(long id);

    Kudo findByKudoId(Long kudoId);

    List<Kudo> findByTargetUser(User targetId);

    List<Kudo> findBySendingUser(User targetId);

    void setFlagged(Kudo kudo, boolean flagged);

    void setRead(Kudo kudo, boolean read);
}