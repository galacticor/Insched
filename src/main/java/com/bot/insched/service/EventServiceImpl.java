package com.bot.insched.service;

import com.bot.insched.model.Event;
import com.bot.insched.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EventServiceImpl implements EventService{
    @Autowired
    private EventRepository eventRepository;
    @Override
    public Event createEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event getEvent(String idEvent) {
        return eventRepository.findByIdEvent(idEvent);
    }

    @Override
    public Event updateEvent(String idEvent,Event event) {
        event.setIdEvent(idEvent);
        eventRepository.save(event);
        return event;
    }

    @Override
    public void deleteEvent(String idEvent) {
        Event event = eventRepository.findByIdEvent(idEvent);
        eventRepository.delete(event);
    }
}
