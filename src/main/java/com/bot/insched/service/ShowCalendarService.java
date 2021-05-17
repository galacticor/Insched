package com.bot.insched.service;

import com.google.api.services.calendar.model.Event;

import java.util.List;

public interface ShowCalendarService {
//    public List<Event> getEvents(String userId);
    public String getCalSummary(Event event);
    public String getCalDescription(Event event);
}
