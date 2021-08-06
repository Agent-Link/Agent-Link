package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
