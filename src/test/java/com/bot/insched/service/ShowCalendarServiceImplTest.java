package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.google.GoogleApiManager;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowCalendarServiceImplTest {

    @InjectMocks
    ShowCalendarServiceImpl showCalendarService;

    @Mock
    private Event event;

    @Mock
    private Events events;

    @Mock
    private GoogleApiManager manager;

    @Mock
    Calendar calendar;


    private List<Event> listEvent = new ArrayList<>();

    private String start_date = "2000-04-22T15:30:00.000-07:00";
    private String end_date = "2000-04-23T15:30:00.000-07:00";

    @Mock
    private EventDateTime eventDateTimeMulai;

    @Mock
    private EventDateTime eventDateTimeSelesai;

    @Mock
    private DateTime dateTimeMulai;

    @Mock
    private DateTime dateTimeSelesai;

    @BeforeEach
    public void setUp() {
        event = new Event();
        event.setSummary("Tes 1").setDescription("description");
        listEvent.add(event);

        dateTimeMulai = new DateTime(start_date);
        eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai);
        dateTimeSelesai = new DateTime(end_date);
        eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai);
        event.setStart(eventDateTimeMulai).setEnd(eventDateTimeSelesai);
    }


    @Test
    public void testGetCalDescription() throws Exception {
        String res = showCalendarService.getCalDescription(event);
        assertEquals(res, "description");
    }

    @Test
    public void testGetCalSummary() throws Exception {
        String res = showCalendarService.getCalSummary(event);
        assertEquals(res, "Tes 1");
    }


    @Test
    public void testGetListEventSuccess() throws Exception {
        String userId = "userId";
        DateTime now = new DateTime(System.currentTimeMillis());
        Calendar.Events calendarEvents = mock(Calendar.Events.class);
        Calendar.Events.List calendarEventsList = mock(Calendar.Events.List.class);

        lenient().when(manager.getCalendarService(userId)).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(calendarEvents);
        lenient().when(calendarEvents.list("primary")).thenReturn(calendarEventsList);
        lenient().when(calendarEventsList.setMaxResults(any(Integer.class))).thenReturn(calendarEventsList);
        lenient().when(calendarEventsList.setTimeMin(now)).thenReturn(calendarEventsList);
        lenient().when(calendarEventsList.setOrderBy("startTime")).thenReturn(calendarEventsList);
        lenient().when(calendarEventsList.setSingleEvents(true)).thenReturn(calendarEventsList);
        lenient().when(calendarEventsList.execute()).thenReturn(events);
        lenient().when(events.getItems()).thenReturn(listEvent);

//        assertNotNull(showCalendarService.getListEvents(userId));
    }

    @Test
    public void testGetListEventNotSuccess() throws Exception {
        lenient().when(manager.getCalendarService("userId")).thenReturn(null);
        assertThrows(NotLoggedInException.class, () -> {
            showCalendarService.getListEvents("userId");
        });
    }


    @Test
    public void testGetCalStart(){
        String res = showCalendarService.getCalStart(event);
        assertEquals(res,start_date);
    }

    @Test
    public void testGetCalEnd(){
        String res = showCalendarService.getCalEnd(event);
        assertEquals(res,end_date);
    }

    @Test
    public void testGetCalDescriptionNull(){
        event.setDescription(null);
        String res = showCalendarService.getCalDescription(event);
        assertEquals(res, "no description");

    }


    @Test
    public void testGetCalDescriptionMoreThan1000char(){
        String regex = new String(new char[1000]).replace('\0', '*');
        event.setDescription(regex);
        String res = showCalendarService.getCalDescription(event);
        assertEquals(res, regex.substring(0,90) + "...");

    }
}
