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
public class EventServiceImplTest {
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

    private String accessToken = "dummy_access_token";
    private String refreshToken = "dummy_refresh_token";
    private String desc = "ini_deskripsi";
    private String eventId = "0123456789abcdefghijklmnopqrstuv";
    private String start_date = "2000-04-22T15:30:00-07:00";
    private String end_date = "2000-04-23T15:30:00-07:00";

    @BeforeEach
    public void setUp() {
        storedCredential = new StoredCredential();
        storedCredential.setAccessToken(accessToken);
        storedCredential.setRefreshToken(refreshToken);
        user = new DiscordUser("123456", storedCredential);
        DateTime dateTimeMulai = new DateTime(start_date);
        EventDateTime eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai)
                .setTimeZone("America/Los_Angeles");
        DateTime dateTimeSelesai = new DateTime(end_date);
        EventDateTime eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai)
                .setTimeZone("America/Los_Angeles");
        event = new Event().setDescription(desc);
        event.setStart(eventDateTimeMulai).setEnd(eventDateTimeSelesai);

    }
    // Create Event Test
    @Test
    public void testCreateEventSuccess() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().insert("primary",event)).thenReturn(mock(Calendar.Events.Insert.class));
        lenient().when(calendar.events().insert("primary",event).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().insert("primary",event).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().insert("primary",event).execute().getId()).thenReturn("Id");
        String res = eventService.createEventService("123456",event);
        assertEquals(res, "Event Berhasil dibuat \n" +
                "HTML\n" +
                "Event id anda adalah Id");


    }

    // Update Event Test
    @Test
    public void testUpdateEventSuccess() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().update("primary","qefewfwef",event)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary","qefewfwef",event).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary","qefewfwef",event).execute().getHtmlLink()).thenReturn("HTML");
        String res = eventService.updateEventService("123456","qefewfwef",event);
        assertEquals(res, "Event berhasil di-Update \n" +
                "link event anda: \n" +
                "HTML");

    }
    // save event Test
    @Test
    public void testSaveEventSuccess() throws Exception{
        com.bot.insched.model.Event event =  eventService.save(mock(com.bot.insched.model.Event.class));
        assertEquals(eventRepository.save(mock(com.bot.insched.model.Event.class))
                , event);

    }
    // Get Event Test
    @Test
    public void testGetEventSuccess() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary","qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary","qefewfwef").execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().get("primary","qefewfwef").execute().getHtmlLink()).thenReturn("HTML");
        String res = eventService.getEventService("123456","qefewfwef");
        assertEquals(res, "Event link anda adalah HTML");

    }

    // Delete Event Test
    @Test
    public void testDeleteEventException() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().delete("primary","tes")).thenReturn(mock(Calendar.Events.Delete.class));
//        lenient().doNothing().when(calendar.events().delete("primary","tes").execute());
        lenient().when(calendar.events().delete("primary","tes").execute()).thenThrow(NullPointerException.class);
        String res = eventService.deleteEventService("123456",event.getId());
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");

    }
    @Test
    public void testGetEventException() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary","qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary","qefewfwef").execute()).thenReturn(any(Event.class));
        String res = eventService.getEventService("123456","qefewfwef");
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");

    }
    @Test
    public void testCreateEventException() throws Exception{
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().insert("primary",event)).thenReturn(mock(Calendar.Events.Insert.class));
        lenient().when(calendar.events().insert("primary",event).execute()).thenReturn(any(Event.class));
        String res = eventService.createEventService("123456",event);
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }

    @Test
    public void testUpdateEventException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event).execute()).thenReturn(any(Event.class));
        String res = eventService.updateEventService("123456", "qefewfwef", event);
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }
    // user not logged in
    @Test
    public void testCreateEventNotLoggedIn() throws Exception{
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.createEventService("123456",event);
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }
    @Test
    public void testUpdateEventNotLoggedIn() throws Exception{
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.updateEventService("123456","safefweg",event);
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }
    @Test
    public void testDeleteEventNotLoggedIn() throws Exception{
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.deleteEventService("123456","wwegfwegwegf");
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }
    @Test
    public void testGetEventNotLoggedIn() throws Exception{
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.getEventService("123456","dfwefwvf");
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }

    // Get calendar by Id test
    @Test
    public void testGetCalendarbyId() throws Exception{
        System.out.println(calendar);
        when(manager.getCalendarService(any())).thenReturn(calendar);
        assertEquals(eventService.getCalendarbyId(any()), calendar);
    }


}
