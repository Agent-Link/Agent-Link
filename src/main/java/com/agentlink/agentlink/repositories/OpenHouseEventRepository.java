package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface OpenHouseEventRepository extends JpaRepository<OpenHouseEvent, Long> {
    List<OpenHouseEvent> findAllByUser(User user);

    @Query("FROM OpenHouseEvent O WHERE O.house.address LIKE %:query%")
    List<OpenHouseEvent> findAllQuery(String query);
}
