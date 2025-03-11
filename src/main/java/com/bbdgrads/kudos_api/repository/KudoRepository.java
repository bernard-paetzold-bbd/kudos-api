package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Kudo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KudoRepository extends JpaRepository<Kudo, Long> {
}
