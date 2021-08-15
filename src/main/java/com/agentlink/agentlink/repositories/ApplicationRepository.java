package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findApplicationsByOpenHouseEventId(long id);
}
