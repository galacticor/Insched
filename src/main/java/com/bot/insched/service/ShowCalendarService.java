package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;
import java.util.List;

public interface ShowCalendarService {
    public List<Event> getListEvents(String userId) throws Exception;

    public List<Event> getListEvents(String userId, DateTime min, DateTime max) throws Exception;

    public String getCalSummary(Event event);

    public String getCalDescription(Event event);

    public String getCalStart(Event event);

    public String getCalEnd(Event event);

}
