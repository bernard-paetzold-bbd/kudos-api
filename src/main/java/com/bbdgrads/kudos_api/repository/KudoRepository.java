package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KudoRepository extends JpaRepository<Kudo, Long> {
    List<Kudo> findByTarget_user_id(User target_user_id);
}
