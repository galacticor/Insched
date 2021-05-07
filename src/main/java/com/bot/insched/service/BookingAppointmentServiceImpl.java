package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.Booking;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.BookingRepository;
import com.bot.insched.repository.DiscordUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public String createBooking(String title, String description, String appointmentId, String discordId) throws Exception {
        DiscordUser user = findUserById(discordId);
        if (user == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            return "Appointment tidak ditemukan";
        }

        Booking booking = new Booking();
        booking.setTitle(title);
        booking.setDescription(description);
        booking.setAppointment(appointment);
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

        bookingRepository.deleteByBookingId(bookingId);
        return "Booking telah dihapus!";
    }
}
