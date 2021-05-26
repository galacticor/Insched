package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.model.DiscordUser;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private Calendar calendar;

    private StoredCredential storedCredential;

    private DiscordUser user;

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

        events = new Events();
        events.setNextPageToken("1234").setItems(listEvent);
        dateTimeMulai = new DateTime(start_date);
        eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai);
        dateTimeSelesai = new DateTime(end_date);
        eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai);
        event.setStart(eventDateTimeMulai).setEnd(eventDateTimeSelesai);
    }

//    @Test
//    public void testGetEventSuccess(){
//        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(mock(Calendar.class));
//        Calendar res = manager.getCalendarService(any());
//        assertEquals(res, manager.getCalendarService(any()));
//    }
//
//    @Test
//    public void testGetEventNotSuccess() throws Exception {
//        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(null);
//    }

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
        lenient().when(manager.getCalendarService("userId")).thenReturn(calendar);
        Calendar res = manager.getCalendarService("userId");
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        Calendar.Events evt = calendar.events();
        lenient().when(calendar.events().list("primary")).thenReturn(mock(Calendar.Events.List.class));
        Calendar.Events.List a = calendar.events().list("primary");
        lenient().when(calendar.events().list("primary").setPageToken("1234")).thenReturn(mock(Calendar.Events.List.class));
        Calendar.Events.List b = calendar.events().list("primary").setPageToken("1234");
        lenient().when(calendar.events().list("primary").setPageToken("1234").execute()).thenReturn(mock(Events.class));
        Events c = calendar.events().list("primary").setPageToken("1234").execute();
        lenient().when(calendar.events().list("primary").setPageToken("1234").execute().getItems()).thenReturn(listEvent);
        List<Event> le = calendar.events().list("primary").setPageToken("1234").execute().getItems();



//        System.out.println("GET LIST EVENT NYA ADALAH : " + calendar.events().list("primary").setPageToken("1234").execute().getSummary());
        List<Event> resu = showCalendarService.getListEvents("userId");

        assertNull(le);
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

    @Test
    public void testGet10LatestEventSuccess(){
        for (int i = 0; i < 12; i++) {
            listEvent.add(event);
        }
        List<Event> res = showCalendarService.get10LatestEvent(listEvent);
        assertNotNull(res);
    }



}
