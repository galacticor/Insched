package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.google.GoogleApiManager;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowCalendarServiceImpl implements
        ShowCalendarService {

    @Autowired
    GoogleApiManager manager;

    List<Event> listEvent = null;

    public List<Event> getListEvents(String userId) throws Exception {
        List<Event> items = new ArrayList<>();

        Calendar calendar = manager.getCalendarService(userId);
        if (calendar == null) {
            throw new NotLoggedInException();
        }

        DateTime now = new DateTime(System.currentTimeMillis());
        Calendar.Events.List events = calendar.events()
                .list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime");
        if (events != null) {
            items = events.setSingleEvents(true).execute().getItems();
        }
        return items;
    }


    public List<Event> getListEvents(String userId, DateTime min, DateTime max) throws Exception {
        List<Event> items = new ArrayList<>();

        Calendar calendar = manager.getCalendarService(userId);
        if (calendar == null) {
            throw new NotLoggedInException();
        }

        Calendar.Events.List events = calendar.events()
                .list("primary")
                .setMaxResults(10)
                .setTimeMin(min)
                .setTimeMax(max)
                .setOrderBy("startTime");
        if (events != null) {
            items = events.setSingleEvents(true).execute().getItems();
        }

        System.out.println("MIN = " + min.toString());
        System.out.println("MAX = " + max.toString());

        return items;
    }


    public String getCalSummary(Event event) {
        return event.getSummary();
    }

    public String getCalDescription(Event event) {
        String description = event.getDescription();
        if (description == null) {
            description = "no description";
        } else if (description.length() >= 1000) {
            description = description.substring(0,90) + "...";
        }
        return description;
    }

    public String getCalStart(Event event) {
        return event.getStart().getDateTime().toString();
    }

    public String getCalEnd(Event event) {
        return event.getEnd().getDateTime().toString();
    }

}
