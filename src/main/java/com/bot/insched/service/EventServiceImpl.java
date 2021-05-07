
package com.bot.insched.service;

import com.bot.insched.model.Event;
import com.bot.insched.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
