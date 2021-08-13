package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpenHouseEventRepository extends JpaRepository<OpenHouseEvent, Long> {
    List<OpenHouseEvent> findAllByUser(User user);
}
