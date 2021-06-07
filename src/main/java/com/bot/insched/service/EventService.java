package com.bot.insched.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import java.util.UUID;

public interface EventService {

    String getEventIdService(String discordId, String eventId);

    Calendar getCalendarbyId(String eventId);

    String createEventService(String discordId, Event event);

    Event createSlotEventService(String discordId, String eventId,
                                 com.bot.insched.model.Event eventModel);

    String deleteEventService(String discordId, String eventId);

    String updateEventService(String discordId, String eventId, String jenis, String newData);

    Event updateSlotEventService(String discordId,
                                 String newData, com.bot.insched.model.Event event);

    com.bot.insched.model.Event save(com.bot.insched.model.Event event);

    com.bot.insched.model.Event findById(String id);

    void deleteEventFromRepo(UUID token);
}
