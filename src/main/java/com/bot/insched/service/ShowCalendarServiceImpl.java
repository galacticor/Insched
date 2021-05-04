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
        Calendar service = manager.getCalendarService(userId);

        // Retrieve an event
        Event event = null;
        try {
            event = service.events().get("primary", "eventId").execute();
        } catch (IOException e) {
            e.printStackTrace();


        }

        System.out.println(event.getSummary());

        return event.getSummary();



    }

}
