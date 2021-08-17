package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("FROM Review r WHERE r.openHouseEvent.id = :eventId")
    Review findReviewWhere(long eventId);
}
