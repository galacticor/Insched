package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ShowCalendarServiceImpl implements ShowCalendarService{
    @Autowired
    private GoogleAPIManager manager;
    Event event;

    public Event getCalService(String userId) {
        Calendar service = manager.getCalendarService(userId);
        // Retrieve an event
        try {
            event = service.events().get("primary","eventId").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCalSummary(Event event) {
        event.getSummary();
        return null ;
    }

    public String getCalDescription(Event event) {
        event.getDescription();
        return null;
    }


}
