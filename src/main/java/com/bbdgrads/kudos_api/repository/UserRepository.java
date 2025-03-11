package com.bbdgrads.kudos_api.repository;


import com.bbdgrads.kudos_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
