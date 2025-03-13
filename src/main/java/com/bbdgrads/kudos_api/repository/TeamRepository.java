package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
