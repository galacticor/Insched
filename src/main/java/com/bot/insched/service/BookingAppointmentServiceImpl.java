package com.bot.insched.service;

import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
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

    @Override
    public String createBooking(String requesterId, String token) {

        DiscordUser attendee = discordUserRepository.findByIdDiscord(requesterId);
        if (attendee == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        Event event = findEventByToken(token);
        if (event == null) {
            return "Event tidak ditemukan!";
        }

        boolean isFull = event.getIsAvailable();
        if (isFull) {
            return "Slot event sudah penuh!";
        }

        List<DiscordUser> listAttendee = event.getListAttendee();
        if (listAttendee.contains(attendee)) {
            return "Sudah melakukan booking slot event!";
        }
        listAttendee.add(attendee);
        event.setListAttendee(listAttendee);
        event.updateAvailability();

        List<Event> listEvent = attendee.getListEvent();
        if (listEvent.contains(event)) {
            return "Sudah melakukan booking slot event!";
        }
        listEvent.add(event);
        attendee.setListEvent(listEvent);

        return "Booking slot event telah dibuat!";
    }

    @Override
    public String deleteBooking(String requesterId, String token) {

        DiscordUser attendee = discordUserRepository.findByIdDiscord(requesterId);
        if (attendee == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        Event event = findEventByToken(token);
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

    // FIXME: Probably should be in EventRepository.java instead
    public Event findEventByToken(String token) {
        UUID uuid = UUID.fromString(token);
        return eventRepository.findByIdEvent(uuid);
    }
}
