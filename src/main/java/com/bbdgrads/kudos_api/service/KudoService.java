package com.bbdgrads.kudos_api.service;

import java.util.List;

import com.bbdgrads.kudos_api.model.Kudo;

public interface KudoService {
    List<Kudo> findByTarget_user_id(Long target_id);

    void save(Kudo kudo);

    void delete(long id);
}