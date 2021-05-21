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

    @Mock
    private Event event;

    private StoredCredential storedCredential;

    private DiscordUser user;

    @BeforeEach
    public void setUp() {
        event = new Event().setSummary("Tes 1");
        event.setDescription("no description");
    }

//    @Test
//    public void testGetEventSuccess() throws Exception {
//        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
//    }

//    @Test
//    public void testGetCalSummary() throws Exception {
////        lenient().when(event.getSummary()).thenReturn(String.class);
////        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
////        lenient().when(calendar.events().update("primary", "qefewfwef", event)).thenReturn(mock(Calendar.Events.Update.class));
////        lenient().when(calendar.events().update("primary", "qefewfwef", event).execute()).thenReturn(any(Event.class));
//        String res = event.getSummary();
//        assertEquals(res, anyString());
//    }

    @Test
    public void testGetCalDescription() throws Exception {
        lenient().when(event.getDescription()).thenReturn("no description");
//        when(event.getDescription()).thenReturn("no description");
//        when(event.getDescription()).thenReturn(mock(String.class));
//        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
//        lenient().when(calendar.events().update("primary", "qefewfwef", event)).thenReturn(mock(Calendar.Events.Update.class));
//        lenient().when(calendar.events().update("primary", "qefewfwef", event).execute()).thenReturn(any(Event.class));
        String res = event.getDescription();
        assertEquals(res, "no description");
    }

//    @Test
//    public void testGetCalSummaryNull() throws Exception {
////        lenient().when(event.getSummary()).thenReturn(String.class);
////        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
////        lenient().when(calendar.events().update("primary", "qefewfwef", event)).thenReturn(mock(Calendar.Events.Update.class));
////        lenient().when(calendar.events().update("primary", "qefewfwef", event).execute()).thenReturn(any(Event.class));
//        String res = event.getSummary();
//        assertEquals(res, null);
//    }


}
