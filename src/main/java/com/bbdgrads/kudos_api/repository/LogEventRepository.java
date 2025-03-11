package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventRepository extends JpaRepository<LogEvent, Long> {
}
