package com.bot.insched.service;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.List;

public interface ShowCalendarService {
    public List<Event> getListEvents(String userId);
    public String getCalSummary(Event event);
    public String getCalDescription(Event event);
    public String getCalStart(Event event);
    public String getCalEnd(Event event);

}
