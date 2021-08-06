package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.OpenHouseEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenHouseEventRepository extends JpaRepository<OpenHouseEvent, Long> {
}
