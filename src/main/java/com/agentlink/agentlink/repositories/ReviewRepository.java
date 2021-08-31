package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Review;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBuyingUserOrderByDateDesc(User user);
}
