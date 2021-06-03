package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.ObjectAlreadyExistsException;
import com.bot.insched.discord.exception.SlotUnavailableException;
<<<<<<< HEAD
=======
import com.bot.insched.discord.exception.ObjectNotFoundException;
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
<<<<<<< HEAD
import javassist.tools.rmi.ObjectNotFoundException;
=======
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingAppointmentServiceImplTest {

    @Mock
    private DiscordUserRepository discordUserRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    BookingAppointmentServiceImpl service;

    private DiscordUser dummyUser;
    private Appointment dummyAppointment;
    private Event dummyEvent;

<<<<<<< HEAD
    private String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    private String dummyId = "0";
=======
    private final String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    private final String dummyId = "0";
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f

    @BeforeEach
    public void setUp() {
        dummyUser = new DiscordUser();
        dummyUser.setIdDiscord(dummyId);
        dummyAppointment = new Appointment();
        dummyAppointment.setIdAppointment(UUID.fromString(dummyToken));
        dummyEvent = new Event();
        dummyEvent.setCapacity(1);
        dummyEvent.setIdEvent(UUID.fromString(dummyToken));
    }

    @Test
    public void testNotLoggedIn() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(null);
<<<<<<< HEAD
        assertThrows(NotLoggedInException.class, () -> {
            service.checkUserLoggedIn(dummyId);
        });
=======
        assertThrows(NotLoggedInException.class, () -> service.checkUserLoggedIn(dummyId));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testAppointmentNotFound() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(appointmentRepository.findByIdAppointment(any())).thenReturn(null);
<<<<<<< HEAD
        assertThrows(ObjectNotFoundException.class, () -> {
            service.viewHostBookingSlots(dummyId, dummyToken);
        });
    }


    @Test
    public void testEventNotFound() {
        when(eventRepository.findByIdEvent(any())).thenReturn(null);
        assertThrows(ObjectNotFoundException.class, () -> {
            service.checkEventValidUUID(dummyToken);
        });
=======
        assertThrows(ObjectNotFoundException.class, () -> service.viewHostBookingSlots(dummyId, dummyToken));
    }

    @Test
    public void testEventNotFound() {
        when(eventRepository.findByIdEvent(any())).thenReturn(null);
        assertThrows(ObjectNotFoundException.class, () -> service.checkEventValidUUID(dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }
    @Test
    public void testCreateBookingWhenFull() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> list = new ArrayList<DiscordUser>();
        list.add(new DiscordUser());
        list.add(new DiscordUser());
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        assertThrows(SlotUnavailableException.class, () -> {
            service.createBooking(dummyId, dummyToken);
        });
=======
        List<DiscordUser> list = new ArrayList<>();
        list.add(new DiscordUser());
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        assertThrows(SlotUnavailableException.class, () -> service.createBooking(dummyId, dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testCreateBookingUserDuplicate() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> list = new ArrayList<DiscordUser>();
        list.add(dummyUser);
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        assertThrows(ObjectAlreadyExistsException.class, () -> {
            service.createBooking(dummyId, dummyToken);
        });
=======
        List<DiscordUser> list = new ArrayList<>();
        list.add(dummyUser);
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        dummyEvent.setAvailable(true);
        assertThrows(ObjectAlreadyExistsException.class, () -> service.createBooking(dummyId, dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testCreateBookingEventDuplicate() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> listUser = new ArrayList<DiscordUser>();
        listUser.add(dummyUser);
        dummyEvent.setListAttendee(listUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<Event>();
        listEvent.add(dummyEvent);
        dummyUser.setListEvent(listEvent);
        assertThrows(ObjectAlreadyExistsException.class, () -> {
            service.createBooking(dummyId, dummyToken);
        });
=======
        List<DiscordUser> listUser = new ArrayList<>();
        listUser.add(dummyUser);
        dummyEvent.setListAttendee(listUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<>();
        listEvent.add(dummyEvent);
        dummyUser.setListEvent(listEvent);
        dummyEvent.setAvailable(true);
        assertThrows(ObjectAlreadyExistsException.class, () -> service.createBooking(dummyId, dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testCreateBookingSuccess() throws Exception {
<<<<<<< HEAD
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
        List<DiscordUser> listUser = new ArrayList<DiscordUser>();
        dummyEvent.setListAttendee(listUser);
        List<Event> listEvent = new ArrayList<Event>();
        dummyUser.setListEvent(listEvent);
=======
        List<Event> listEvent = new ArrayList<>();
        dummyUser.setListEvent(listEvent);
        List<DiscordUser> listUser = new ArrayList<>();
        dummyEvent.setListAttendee(listUser);
        dummyEvent.updateAvailability();
        dummyEvent.setAvailable(true);
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
        String res = service.createBooking(dummyId, dummyToken);
        assertEquals(res, "Booking slot event telah dibuat!");
    }

    @Test
    public void testDeleteBookingUserNotFound() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> list = new ArrayList<DiscordUser>();
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        assertThrows(ObjectNotFoundException.class, () -> {
            service.deleteBooking(dummyId, dummyToken);
        });
=======
        List<DiscordUser> list = new ArrayList<>();
        dummyEvent.setListAttendee(list);
        dummyEvent.updateAvailability();
        assertThrows(ObjectNotFoundException.class, () -> service.deleteBooking(dummyId, dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testDeleteBookingEventNotFound() {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> listUser = new ArrayList<DiscordUser>();
        dummyEvent.setListAttendee(listUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<Event>();
        dummyUser.setListEvent(listEvent);
        assertThrows(ObjectNotFoundException.class, () -> {
            service.deleteBooking(dummyId, dummyToken);
        });
=======
        List<DiscordUser> listUser = new ArrayList<>();
        dummyEvent.setListAttendee(listUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<>();
        dummyUser.setListEvent(listEvent);
        assertThrows(ObjectNotFoundException.class, () -> service.deleteBooking(dummyId, dummyToken));
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
    }

    @Test
    public void testDeleteBookingSuccess() throws Exception {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(eventRepository.findByIdEvent(any())).thenReturn(dummyEvent);
<<<<<<< HEAD
        List<DiscordUser> listUser = new ArrayList<DiscordUser>();
        dummyEvent.setListAttendee(listUser);
        listUser.add(dummyUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<Event>();
=======
        List<DiscordUser> listUser = new ArrayList<>();
        dummyEvent.setListAttendee(listUser);
        listUser.add(dummyUser);
        dummyEvent.updateAvailability();
        List<Event> listEvent = new ArrayList<>();
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
        listEvent.add(dummyEvent);
        dummyUser.setListEvent(listEvent);
        String res = service.deleteBooking(dummyId, dummyToken);
        assertEquals(res, "Booking slot event telah dihapus!");
    }

    @Test
    public void testViewHostBookingSlotsSuccess() throws Exception {
        when(discordUserRepository.findByIdDiscord(any())).thenReturn(dummyUser);
        when(appointmentRepository.findByIdAppointment(any())).thenReturn(dummyAppointment);
<<<<<<< HEAD
        List<Event> list = new ArrayList<Event>();
=======
        List<Event> list = new ArrayList<>();
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
        dummyAppointment.setListEvent(list);
        assertEquals(service.viewHostBookingSlots(dummyId, dummyToken), list);
    }
}