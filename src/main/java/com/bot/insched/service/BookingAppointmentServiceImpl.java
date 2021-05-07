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
    public String createBooking(String title, String desc, String appointmentId, String discordId) throws Exception {
        DiscordUser u = findUserById(discordId);
        if (u == null) return "Silahkan login terlebih dahulu menggunakan !login";

        Appointment a = findAppointmentById(appointmentId);
        if (a == null) return "Appointment tidak ditemukan";

        Booking booking = new Booking();
        booking.setBtitle(title);
        booking.setBdesc(desc);
        booking.setAppointment(a);
        booking.setRequester(u);
        save(booking);
        return "Booking telah dibuat!";
    }

    @Override
    public List<Booking> getAllUserBooking(String discordId) {
        DiscordUser u = discordUserRepository.findByIdDiscord(discordId);
        List<Booking> bookingList = new ArrayList<>();

        for (Booking b: bookingRepository.findAllByUser(u)) {
            bookingList.add(b);
        }
        return bookingList;
    }

    @Override
    public DiscordUser findUserById(String discordId) {
        return discordUserRepository.findByIdDiscord(discordId);
    }

    @Override
    public Appointment findAppointmentById(String appointmentId) {
        return appointmentRepository.findByIdAppointment(UUID.fromString(appointmentId));
    }

    @Override
    public Booking findBookingById(String bookingId) {
        return bookingRepository.findByBookingId(UUID.fromString(bookingId));
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public String delete(String bookingId) {
        Booking b = bookingRepository.findByBookingId(UUID.fromString(bookingId));
        if (b == null) return "Booking tidak ditemukan";
        bookingRepository.deleteByBookingId(bookingId);
        return "Booking telah dihapus!";
    }
}
