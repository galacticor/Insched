package com.bot.insched.service;

import com.bot.insched.model.Event;

public interface EventService {
    Event createEvent(Event event);
    void deleteEvent(String idEvent);
    Event updateEvent(String idEvent,Event event);
    Event getEvent(String idEvent);

}
