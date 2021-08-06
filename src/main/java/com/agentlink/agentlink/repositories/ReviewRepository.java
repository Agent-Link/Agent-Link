package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
