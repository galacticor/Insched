package com.bot.insched.repository;

import com.bot.insched.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findByIdEvent(String id);

}
