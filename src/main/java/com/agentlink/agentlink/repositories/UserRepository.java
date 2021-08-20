package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String name);

    User findByEmail(String email);

//    @Query("FROM User U WHERE U.firstName LIKE %:query%")
//    List<User> findAllQueryByFirstName(String query);
    User findAllByFirstNameLike(String query);
}
