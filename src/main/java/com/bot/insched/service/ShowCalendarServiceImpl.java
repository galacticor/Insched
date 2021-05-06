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

    public String getCalService(String userId){
        Event event;
        String summary = null;

        Calendar service = manager.getCalendarService(userId);

        // Retrieve an event
        try {
            event = service.events().get("primary", "eventId").execute();
            summary = event.getSummary();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

}
