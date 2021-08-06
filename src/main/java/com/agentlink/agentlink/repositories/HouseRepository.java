package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
