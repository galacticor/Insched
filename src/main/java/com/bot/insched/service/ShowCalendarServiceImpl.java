package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ShowCalendarServiceImpl implements ShowCalendarService{
    @Autowired
    private GoogleAPIManager manager;
    Event event;

    public Event getCalService(String userId){
        Calendar service = manager.getCalendarService(userId);
        // Retrieve an event
        try {
            event = service.events().get("primary","eventId").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    public String getCalSummary(Event event){
        return event.getSummary();
    }

    public String getCalDescription(Event event){
        return event.getDescription();
    }


}
