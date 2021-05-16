package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.Calendar.Events.List;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ShowCalendarServiceImpl implements ShowCalendarService {
    @Autowired
    private GoogleAPIManager manager;
    Events events;

    public List<Event> getEvents(String userId) {
        Calendar service = manager.getCalendarService(userId);
        List<Event> items;

        String pageToken = null;
        do {
            Events events = null;
            try {
                events = service.events().list("primary").setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            items = events.getItems();
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return items;
    }

    public String getCalSummary(Event event) {
        return event.getSummary();
    }

    public String getCalDescription(Event event) {
        return event.getDescription();
    }
}
