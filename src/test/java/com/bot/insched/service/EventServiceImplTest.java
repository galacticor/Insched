package com.bot.insched.service;

import com.bot.insched.discord.command.DeleteEventCommand;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    public void testCreateEventSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().insert("primary", event)).thenReturn(mock(Calendar.Events.Insert.class));
        lenient().when(calendar.events().insert("primary", event).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().insert("primary", event).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().insert("primary", event).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().insert("primary", event).execute().getStart().getDateTime().toString()).thenReturn("1356648092");
        lenient().when(calendar.events().insert("primary", event).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().insert("primary", event).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().insert("primary", event).execute().getEnd().getDateTime().toString()).thenReturn("1393195067");
        lenient().when(calendar.events().insert("primary", event).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().insert("primary", event).execute().getId()).thenReturn("Id");
        String res = eventService.createEventService("123456", event);
        assertEquals(res, "Event yang anda cari berhasil ditemukan \n"
                + "Berikut link event yang anda cari: [LINK](HTML) \n"
                + ":id: Id \n"
                + "\n"
                + ":clock: Mulai pada  : 1356648092 \n"
                + ":clock: Selesai pada: 1393195067 \n"
                + "\n"
                + "Deskripsi Event anda adalah null");
    }

    @Test
    public void testGetEventIdSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", event.getId())).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute().getStart().getDateTime().toString()).thenReturn("1356648092");
        lenient().when(calendar.events().get("primary", event.getId()).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute().getEnd().getDateTime().toString()).thenReturn("1393195067");
        lenient().when(calendar.events().get("primary", event.getId()).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().get("primary", event.getId()).execute().getId()).thenReturn("Id");
        String res = eventService.getEventIdService("123456", event.getId());
        assertEquals(res, "Event yang anda cari berhasil ditemukan \n"
                + "Berikut link event yang anda cari: [LINK](HTML) \n"
                + ":id: Id \n"
                + "\n"
                + ":clock: Mulai pada  : 1356648092 \n"
                + ":clock: Selesai pada: 1393195067 \n"
                + "\n"
                + "Deskripsi Event anda adalah null");
    }

    // Update Event Test
    @Test
    public void testUpdateEventDescriptionSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        Event event1 = mock(Event.class);
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(event1);
        lenient().when(calendar.events().update("primary", "qefewfwef", event1)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime().toString()).thenReturn("8844488");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime().toString()).thenReturn("629775688");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getDescription()).thenReturn("tes");
        String res = eventService.updateEventService("123456", "qefewfwef", "deskripsi", "tes");
        assertEquals(res, "Event Berhasil di-update \n"
                + "Berikut link event baru anda: [LINK](HTML) \n"
                + ":id: null \n"
                + "\n"
                + ":clock: Mulai pada  : 8844488 \n"
                + ":clock: Selesai pada: 629775688 \n"
                + "\n"
                + "Deskripsi Event anda adalah tes");

    }

    @Test
    public void testUpdateEventSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        Event event1 = mock(Event.class);
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(event1);
        lenient().when(calendar.events().update("primary", "qefewfwef", event1)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime().toString()).thenReturn("8844488");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime().toString()).thenReturn("629775688");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getDescription()).thenReturn("tes");
        String res = eventService.updateEventService("123456", "qefewfwef", "deskripsi", "tes");
        assertEquals(res, "Event Berhasil di-update \n"
                + "Berikut link event baru anda: [LINK](HTML) \n"
                + ":id: null \n"
                + "\n"
                + ":clock: Mulai pada  : 8844488 \n"
                + ":clock: Selesai pada: 629775688 \n"
                + "\n"
                + "Deskripsi Event anda adalah tes");
    }

    @Test
    public void testUpdateEventStartSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        Event event1 = mock(Event.class);
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(event1);
        lenient().when(calendar.events().update("primary", "qefewfwef", event1)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime().toString()).thenReturn("8844488");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime().toString()).thenReturn("629775688");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getDescription()).thenReturn("tes");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()
                .getDateTime().toString()).thenReturn("1999-05-21T05:30:00.000+07:00");
        String ret = eventService.updateEventService("123456", "qefewfwef",
                "mulai", "1999-05-21T05:30:00.000+07:00");
        assertEquals(ret, "Event Berhasil di-update \n"
                + "Berikut link event baru anda: [LINK](HTML) \n"
                + ":id: null \n"
                + "\n"
                + ":clock: Mulai pada  : 1999-05-21T05:30:00.000+07:00 \n"
                + ":clock: Selesai pada: 629775688 \n"
                + "\n"
                + "Deskripsi Event anda adalah tes");
    }

    @Test
    public void testUpdateEventEndSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        Event event1 = mock(Event.class);
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(event1);
        lenient().when(calendar.events().update("primary", "qefewfwef", event1)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime().toString()).thenReturn("8844488");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime().toString()).thenReturn("629775688");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getDescription()).thenReturn("tes");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()
                .getDateTime().toString()).thenReturn("1999-05-21T05:31:00.000+07:00");
        String ret = eventService.updateEventService("123456", "qefewfwef",
                "selesai", "1999-05-21T05:31:00.000+07:00");
        assertEquals(ret, "Event Berhasil di-update \n"
                + "Berikut link event baru anda: [LINK](HTML) \n"
                + ":id: null \n"
                + "\n"
                + ":clock: Mulai pada  : 8844488 \n"
                + ":clock: Selesai pada: 1999-05-21T05:31:00.000+07:00 \n"
                + "\n"
                + "Deskripsi Event anda adalah tes");
    }

    @Test
    public void testUpdateEventSummarySuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        Event event1 = mock(Event.class);
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(event1);
        lenient().when(calendar.events().update("primary", "qefewfwef", event1)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getStart().getDateTime().toString()).thenReturn("8844488");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd()).thenReturn(mock(EventDateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime()).thenReturn(mock(DateTime.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getEnd().getDateTime().toString()).thenReturn("629775688");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getHtmlLink()).thenReturn("HTML");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute().getDescription()).thenReturn("tes");
        lenient().when(calendar.events().update("primary", "qefewfwef", event1).execute()
                .getSummary()).thenReturn("tes");
        String ret = eventService.updateEventService("123456", "qefewfwef",
                "summary", "tes");
        assertEquals(ret, "Event Berhasil di-update \n"
                + "Berikut link event baru anda: [LINK](HTML) \n"
                + ":id: null \n"
                + "\n"
                + ":clock: Mulai pada  : 8844488 \n"
                + ":clock: Selesai pada: 629775688 \n"
                + "\n"
                + "Deskripsi Event anda adalah tes");
    }

    // save event Test
    @Test
    public void testSaveEventSuccess() throws Exception {
        com.bot.insched.model.Event event = eventService.save(mock(com.bot.insched.model.Event.class));
        assertEquals(eventRepository.save(mock(com.bot.insched.model.Event.class))
                , event);

    }

    // Get Event Test
    @Test
    public void testGetEventSuccess() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(mock(Event.class));
        lenient().when(calendar.events().get("primary", "qefewfwef").execute().getHtmlLink()).thenReturn("HTML");
        Event res = eventService.getEventService("123456", "qefewfwef");
        assertNotNull(res);
    }


    // Delete Event Test
    @Test
    public void testDeleteEventException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().delete("primary", "tes")).thenReturn(mock(Calendar.Events.Delete.class));
        lenient().when(calendar.events().delete("primary", "tes").execute()).thenThrow(NullPointerException.class);
        String res = eventService.deleteEventService("123456", event.getId());
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }

    @Test
    public void testGetEventException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", "qefewfwef")).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary", "qefewfwef").execute()).thenReturn(any(Event.class));
        Event res = eventService.getEventService("123456", "qefewfwef");
        assertEquals(res, null);

    }
    @Test
    public void testGetEventIdException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().get("primary", event.getId())).thenReturn(mock(Calendar.Events.Get.class));
        lenient().when(calendar.events().get("primary", event.getId()).execute()).thenReturn(null);
        String res = eventService.getEventIdService("123456", event.getId());
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }

    @Test
    public void testCreateEventException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().insert("primary", event)).thenReturn(mock(Calendar.Events.Insert.class));
        lenient().when(calendar.events().insert("primary", event).execute()).thenReturn(any(Event.class));
        String res = eventService.createEventService("123456", event);
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }

    @Test
    public void testUpdateEventException() throws Exception {
        lenient().when(manager.getCalendarService(any(String.class))).thenReturn(calendar);
        lenient().when(calendar.events()).thenReturn(mock(Calendar.Events.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event)).thenReturn(mock(Calendar.Events.Update.class));
        lenient().when(calendar.events().update("primary", "qefewfwef", event).execute()).thenReturn(any(Event.class));
        String res = eventService.updateEventService("123456", "qefewfwef", "mulai", "tes");
        assertEquals(res, "Terjadi kesalahan pastikan anda memasukkan input dengan benar");
    }

    // user not logged in
    @Test
    public void testCreateEventNotLoggedIn() throws Exception {
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.createEventService("123456", event);
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }

    @Test
    public void testUpdateEventNotLoggedIn() throws Exception {
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.updateEventService("123456", "safefweg", "mulai", "12412415");
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }

    @Test
    public void testDeleteEventNotLoggedIn() throws Exception {
        when(manager.getCalendarService(any(String.class))).thenReturn(null);
        String res = eventService.deleteEventService("123456", "wwegfwegwegf");
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }

    // Get calendar by Id test
    @Test
    public void testGetCalendarbyId() throws Exception {
        when(manager.getCalendarService(any())).thenReturn(calendar);
        assertEquals(eventService.getCalendarbyId(any()), calendar);
    }

    @Test
    public void testFindEventModelById() {
        com.bot.insched.model.Event e = new com.bot.insched.model.Event();
        UUID random = UUID.randomUUID();
        e.setIdEvent(random);
        when(eventRepository.findByIdEvent(any(UUID.class)))
            .thenReturn(e);
        assertEquals(eventService.findById(random.toString()), e);
    }

    @Test
    public void testDeleteEventFromRepo() {
        eventService.deleteEventFromRepo(UUID.randomUUID());
    }


}
