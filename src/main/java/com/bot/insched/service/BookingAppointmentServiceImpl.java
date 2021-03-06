package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.ObjectAlreadyExistsException;
import com.bot.insched.discord.exception.ObjectNotFoundException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
import java.util.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookingAppointmentServiceImpl implements BookingAppointmentService {

    // TODO: Implement integration with Google API
    @Autowired
    private GoogleApiManager manager;

    @Autowired
    DiscordUserRepository discordUserRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    EventService eventService;

    @Override
    public String createBooking(String requesterId, String token, String email) throws Exception {
        DiscordUser attendee = checkUserLoggedIn(requesterId);

        Event event = checkEventValidUUID(token);

        boolean isAvailable = event.isAvailable();
        if (!isAvailable) {
            throw new SlotUnavailableException("Slot event sudah penuh!");
        }

        List<DiscordUser> listAttendee = event.getListAttendee();
        List<Event> listEvent = attendee.getListEvent();
        if (listAttendee.contains(attendee) || listEvent.contains(event)) {
            throw new ObjectAlreadyExistsException("Sudah melakukan booking slot event!");
        }
        listAttendee.add(attendee);
        event.setListAttendee(listAttendee);
        event.updateAvailability();

        String ownerId = event.getAppointment().getOwner().getIdDiscord();

        if (eventService.updateSlotEventService(ownerId, email, event) == null) {
            return "Booking slot event gagal";
        }

        eventRepository.save(event);
        listEvent.add(event);
        attendee.setListEvent(listEvent);
        discordUserRepository.save(attendee);

        return "Booking slot event telah dibuat!";
    }

    @Override
    public String deleteBooking(String requesterId, String token) throws Exception {

        DiscordUser attendee = checkUserLoggedIn(requesterId);

        Event event = checkEventValidUUID(token);

        List<DiscordUser> listAttendee = event.getListAttendee();
        List<Event> listEvent = attendee.getListEvent();
        if (!listAttendee.contains(attendee) || !listEvent.contains(event)) {
            throw new ObjectNotFoundException("Tidak ada booking untuk slot event ini!");
        }
        do {
            listAttendee.remove(attendee);
        } while (listAttendee.contains(attendee));
        event.setListAttendee(listAttendee);
        event.updateAvailability();

        eventRepository.save(event);

        do {
            listEvent.remove(event);
        } while (listEvent.contains(event));
        attendee.setListEvent(listEvent);

        discordUserRepository.save(attendee);

        return "Booking slot event telah dihapus!";
    }

    @Override
    public List<Event> viewHostBookingSlots(String requesterId, String token) throws Exception {

        checkUserLoggedIn(requesterId);

        UUID uuid = UUID.fromString(token);
        Appointment appointment = appointmentRepository.findByIdAppointment(uuid);
        if (appointment == null) {
            throw new ObjectNotFoundException("Appointment tidak ditemukan!");
        }

        Set<Event> eventSet = new HashSet<>(appointment.getListEvent());
        List<Event> eventList = eventSet.stream().collect(Collectors.toList());
        Collections.sort(eventList);

        return eventList;
    }

    public DiscordUser checkUserLoggedIn(String requesterId) throws Exception {
        DiscordUser user = discordUserRepository.findByIdDiscord(requesterId);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
    }

    public Event checkEventValidUUID(String token) throws Exception {
        UUID uuid = UUID.fromString(token);
        Event event = eventRepository.findByIdEvent(uuid);
        if (event == null) {
            throw new ObjectNotFoundException("Event tidak ditemukan!");
        }
        return event;
    }

}
