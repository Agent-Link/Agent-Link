package com.agentlink.agentlink.repositories;

import com.agentlink.agentlink.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findApplicationsByOpenHouseEventId(long id);

    List<Application> findAllByUserId(long id);

    @Query("FROM Application A WHERE A.user.id = ?1 AND A.openHouseEvent.user.id <> A.user.id AND A.openHouseEvent.dateStart >= ?2 ORDER BY A.openHouseEvent.dateStart")
    List<Application> findAllByUserIdWhereNotHostAndEventHasNotStarted(long id, Date date);

}
