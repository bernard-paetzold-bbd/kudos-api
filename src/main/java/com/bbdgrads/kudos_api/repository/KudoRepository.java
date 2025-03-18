package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface KudoRepository extends JpaRepository<Kudo, Long> {
    Optional<Kudo> findByKudoId(Long kudoId);

    List<Kudo> findByTargetUser(User targetUser);

    List<Kudo> findBySendingUser(User sendingUser);
}
