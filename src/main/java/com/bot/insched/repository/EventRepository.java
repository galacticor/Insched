package com.bot.insched.repository;

import com.bot.insched.model.Event;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findByIdEvent(UUID id);

    void deleteByIdEvent(UUID id);

}
