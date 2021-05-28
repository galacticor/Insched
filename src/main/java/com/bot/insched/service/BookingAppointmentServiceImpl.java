package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.ObjectAlreadyExistsException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingAppointmentServiceImpl implements BookingAppointmentService{

    // TODO: Implement integration with Google API
    @Autowired
    private GoogleApiManager manager;

    @Autowired
    DiscordUserRepository discordUserRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public String createBooking(String requesterId, String token) throws Exception{

        DiscordUser attendee = discordUserRepository.findByIdDiscord(requesterId);
        if (attendee == null) {
            throw new NotLoggedInException();
        }

        UUID uuid = UUID.fromString(token);
        Event event = eventRepository.findByIdEvent(uuid);
        if (event == null) {
            return "Event tidak ditemukan!";
        }

        boolean isFull = event.getIsAvailable();
        if (isFull) {
            throw new SlotUnavailableException("Slot event sudah penuh!");
        }

        List<DiscordUser> listAttendee = event.getListAttendee();
        if (listAttendee.contains(attendee)) {
            throw new ObjectAlreadyExistsException("Sudah melakukan booking slot event!");
        }
        listAttendee.add(attendee);
        event.setListAttendee(listAttendee);
        event.updateAvailability();

        List<Event> listEvent = attendee.getListEvent();
        if (listEvent.contains(event)) {
            throw new ObjectAlreadyExistsException("Sudah melakukan booking slot event!");
        }
        listEvent.add(event);
        attendee.setListEvent(listEvent);

        return "Booking slot event telah dibuat!";
    }

    @Override
    public String deleteBooking(String requesterId, String token) throws Exception {

        DiscordUser attendee = discordUserRepository.findByIdDiscord(requesterId);
        if (attendee == null) {
            throw new NotLoggedInException();
        }

        UUID uuid = UUID.fromString(token);
        Event event = eventRepository.findByIdEvent(uuid);
        if (event == null) {
            return "Event tidak ditemukan";
        }

        List<DiscordUser> listAttendee = event.getListAttendee();
        if (!listAttendee.contains(attendee)) {
            return "Tidak ada booking untuk slot event ini!";
        }
        do {
            listAttendee.remove(attendee);
        } while (listAttendee.contains(attendee));
        event.setListAttendee(listAttendee);
        event.updateAvailability();

        List<Event> listEvent = attendee.getListEvent();
        if (!listEvent.contains(event)) {
            return "Tidak ada booking untuk slot event ini!";
        }
        do {
            listEvent.remove(event);
        } while (listEvent.contains(event));
        attendee.setListEvent(listEvent);

        return "Booking slot event telah dihapus!";
    }

    @Override
    public List<Event> viewHostBookingSlots(String requesterId, String token) throws Exception {
        DiscordUser attendee = discordUserRepository.findByIdDiscord(requesterId);
        if (attendee == null) {
            throw new NotLoggedInException();
        }

        UUID uuid = UUID.fromString(token);
        Appointment appointment = appointmentRepository.findByIdAppointment(uuid);
        if (appointment == null) {
            throw new ObjectNotFoundException("Appointment tidak ditemukan");
        }

        List<Event> eventList = appointment.getListEvent();
        return eventList;
    }

}
