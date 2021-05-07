package com.bot.insched.service;

import com.google.api.services.calendar.model.Event;

public interface ShowCalendarService {
    Event getCalService(String userId);
}
