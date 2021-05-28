package com.bot.insched.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

public interface EventService {
    Event getEventService(String discordId, String eventId);

    Calendar getCalendarbyId(String eventId);

    String createEventService(String discordId, Event event);

    String deleteEventService(String discordId, String eventId);

    String updateEventService(String discordId, String eventId, String jenis, String newData);

    com.bot.insched.model.Event save(com.bot.insched.model.Event event);

    com.bot.insched.model.Event findById(String id);
}
