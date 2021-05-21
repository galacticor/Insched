package com.bot.insched.service;

import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.EventRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowCalendarServiceImplTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private GoogleApiManager manager;

    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    private Calendar calendar;

    private Event event;

    private StoredCredential storedCredential;

    private DiscordUser user;

    @Test
    public void testGetEventSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
    }

    @Test
    public void testGetCalSUmmary() throws Exception {
        lenient().when(event.getSummary()).thenReturn(any(String.class));
    }



}
