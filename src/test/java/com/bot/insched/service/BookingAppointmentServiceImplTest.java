package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.ObjectAlreadyExistsException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingAppointmentServiceImplTest {

    @Mock
    private DiscordUserRepository discordUserRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    BookingAppointmentServiceImpl bookingAppointmentService;

    private DiscordUser attendee;
    private StoredCredential storedCredential;
    private Event event;

    private String accessToken = "dummy_access_token";
    private String refreshToken = "dummy_refresh_token";
    private String desc = "description";

    private String eventToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";

    @BeforeEach
    public void setUp(){
        storedCredential = new StoredCredential();
        storedCredential.setAccessToken(accessToken);
        storedCredential.setRefreshToken(refreshToken);
        attendee = new DiscordUser("0", storedCredential);
        event = new Event();
        event.setCapacity(1);
        event.setIdEvent(UUID.fromString(eventToken));
    }

    @Test
    public void testCreateBookingNotLoggedIn() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(null);
        assertThrows(Exception.class, () -> {
            bookingAppointmentService.createBooking("1", eventToken);
        });
    }

    @Test
    public void testCreateBookingSlotEventFull() {
        DiscordUser attendee1 = new DiscordUser("1", storedCredential);
        List<DiscordUser> list = new ArrayList<DiscordUser>();
        list.add(attendee1);
        event.setListAttendee(list);
        event.updateAvailability();
        when(event.getIsAvailable()).thenReturn(false).thenThrow(Exception.class);
    }

    @Test
    public void testCreateBookingSlotDuplicateUser() {
        List<DiscordUser> list = new ArrayList<DiscordUser>();
        list.add(attendee);
        event.setListAttendee(list);
        event.updateAvailability();
        list = event.getListAttendee();
        when(list.contains(attendee)).thenReturn(true).thenThrow(Exception.class);
    }

    @Test
    public void testCreateBookingSlotDuplicateEvent() {
        List<Event> list = new ArrayList<Event>();
        list.add(event);
        attendee.setListEvent(list);
        list = attendee.getListEvent();
        when(list.contains(attendee)).thenReturn(true).thenThrow(ObjectAlreadyExistsException.class);
    }

    @Test
    public void testCreateBookingSlotSuccess() throws Exception {
        lenient().when(discordUserRepository.findByIdDiscord(anyString())).thenReturn(attendee);
        when(eventRepository.findByIdEvent(any(UUID.class))).thenReturn(event);

        String result = bookingAppointmentService.createBooking("0", "e79e7cf1-0b8c-48db-a05b-baafcb5953d2");
        assertEquals(result, "Booking slot event telah dibuat!");
    }

    @Test
    public void testDeleteBookingNotLoggedIn() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(null);
        assertThrows(Exception.class, () -> {
            bookingAppointmentService.deleteBooking("1", eventToken);
        });
    }

    @Test
    public void testDeleteBookingSlotEventNotFound() throws Exception {
        when(eventRepository.findByIdEvent(UUID.randomUUID())).thenReturn(null);
        String result = bookingAppointmentService.deleteBooking("0", "dummy-token");
        assertEquals(result, "Event tidak ditemukan");
    }

    @Test
    public void testDeleteBookingSlotNotBooked() {
        List<DiscordUser> list = event.getListAttendee();
        assertFalse(list.contains(attendee));
    }

    @Test
    public void testDeleteBookingSlotNoEventBooked() {
        List<Event> list = attendee.getListEvent();
        assertFalse(list.contains(event));
    }

    @Test
    public void testDeleteBookingSlotSuccess() throws Exception {
        lenient().when(discordUserRepository.findByIdDiscord(anyString())).thenReturn(attendee);
        when(eventRepository.findByIdEvent(any(UUID.class))).thenReturn(event);
        when(event.getListAttendee().contains(attendee)).thenReturn(true);
        when(attendee.getListEvent().contains(event)).thenReturn(true);

        String result = bookingAppointmentService.deleteBooking("0", "e79e7cf1-0b8c-48db-a05b-baafcb5953d2");
        assertEquals(result, "Booking slot event telah dihapus!");
    }

}
