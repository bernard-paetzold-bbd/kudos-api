package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional since we are looking for a single object to be returned;
    public Optional<User> findByUserId(Long userId);

}
