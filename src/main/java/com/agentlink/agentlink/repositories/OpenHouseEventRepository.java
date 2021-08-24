package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface OpenHouseEventRepository extends JpaRepository<OpenHouseEvent, Long> {

    @Query("FROM OpenHouseEvent O WHERE O.house.address LIKE %:query%")
    List<OpenHouseEvent> findAllQuery(String query);

    // Finds all events by event creator (house.user.id) id where the event start date/time has not reached the input date/time
    @Query("FROM OpenHouseEvent O WHERE O.house.user.id = ?1 AND O.dateStart >= ?2")
    List<OpenHouseEvent> findAllByCreatorIdAndDateStartAfter(long id, Date date);

    // Finds all events by event creator (house.user.id) id where the event end date/time has passed the input date/time.
    @Query("FROM OpenHouseEvent O WHERE O.house.user.id = ?1 AND O.dateEnd <= ?2")
    List<OpenHouseEvent> findAllByCreatorIdAndDateEndBefore(long id, Date date);


    // Finds all events by user id where an user has been set to host and the event has not passed the input date/time.
    @Query("FROM OpenHouseEvent O WHERE O.user.id = ?1 AND O.house.user.id <> O.user.id AND O.dateStart >= ?2")
    List<OpenHouseEvent> findAllByHostedByUserIdAndDateStartAfter(long id, Date date);

}
