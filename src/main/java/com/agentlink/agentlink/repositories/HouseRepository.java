package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {

    List<House> findByUser(User user);

    List<House> findAllByUser(User user);

    List<House> findAllByUserAndListingActive(User user, boolean isListingActive);

    @Query("FROM House H WHERE H.address LIKE %:query%")
    List<House> findAllHouseQuery(String query);
}

