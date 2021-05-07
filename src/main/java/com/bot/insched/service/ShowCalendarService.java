package com.bot.insched.service;

import com.google.api.services.calendar.model.Event;

public interface ShowCalendarService {
    Event getCalService(String userId);
    public String getCalSummary(Event event);
    public String getCalDescription(Event event);
}
