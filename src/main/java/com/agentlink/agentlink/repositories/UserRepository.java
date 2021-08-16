package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String name);

    User findByEmail(String email);
}
