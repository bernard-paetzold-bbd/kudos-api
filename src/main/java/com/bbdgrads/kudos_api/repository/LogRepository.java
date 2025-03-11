package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
