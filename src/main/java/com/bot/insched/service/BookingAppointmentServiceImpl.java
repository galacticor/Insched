package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.Booking;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.BookingRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingAppointmentServiceImpl implements BookingAppointmentService{

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DiscordUserRepository discordUserRepository;

    @Autowired
    EventRepository eventRepository;

    @Override
    public String createBooking(String requesterId, String token, String datetime) {

        // Flow:
        // Searches host for appointment, searches correct event from appointment list of events,
        // register self and create booking object
        LocalDateTime start = LocalDateTime.parse(datetime);

        DiscordUser user = findUserById(requesterId);
        if (user == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        Appointment appointment = findAppointmentById(token);
        if (appointment == null) {
            return "Token appointment tidak ditemukan";
        }

        List<Event> listEvent = appointment.getListEvent();
        List<DiscordUser> attendees;

        int i = 0;
        int limit = listEvent.size();
        boolean flag = true;
        Event event;
        do {
            event = listEvent.get(i);
            flag = event.getStartTime().isEqual(start);
            ++i;
        } while (!flag && i<limit);

        if (flag) {
            attendees = event.getListAttendee();

            if (attendees.contains(user)) {
                return "User sudah terdaftar";
            }

            attendees.add(user);
            event.setListAttendee(attendees);
        } else return "Slot tidak ditemukan";

        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setRequester(user);
        save(booking);
        return "Booking telah dibuat!";
    }

    @Override
    public List<Booking> getAllUserBooking(String discordId) {
        DiscordUser user = discordUserRepository.findByIdDiscord(discordId);
        List<Booking> bookingList = new ArrayList<>();

        for (Booking booking : bookingRepository.findAllByUser(user)) {
            bookingList.add(booking);
        }
        return bookingList;
    }

    @Override
    public DiscordUser findUserById(String discordId) {
        return discordUserRepository.findByIdDiscord(discordId);
    }

    @Override
    public Appointment findAppointmentById(String appointmentId) {
        UUID uuid = UUID.fromString(appointmentId);
        return appointmentRepository.findByIdAppointment(uuid);
    }

    @Override
    public Booking findBookingById(String bookingId) {
        UUID uuid = UUID.fromString(bookingId);
        return bookingRepository.findByBookingId(uuid);
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public String delete(String bookingId) {
        UUID uuid = UUID.fromString(bookingId);
        Booking booking = bookingRepository.findByBookingId(uuid);
        if (booking == null) {
            return "Booking tidak ditemukan";
        }

        DiscordUser user = booking.getRequester();
        List<DiscordUser> attendees = booking.getEvent().getListAttendee();
        DiscordUser attendee;
        int i = 0;
        int limit = attendees.size();
        boolean flag;
        do {
            attendee = attendees.get(i);
            flag = attendee.equals(user);
            ++i;
        } while (!flag && i<limit);

        if (flag) {
            attendees.remove(user);
        } else return "User tidak terdaftar";

        bookingRepository.deleteByBookingId(bookingId);

        return "Booking telah dihapus!";
    }
}
