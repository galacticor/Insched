package com.bot.insched.service;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.Booking;
import com.bot.insched.model.DiscordUser;

import java.util.List;

public interface BookingAppointmentService {
    String createBooking(String requesterId, String token, String datetime) throws Exception;

    List<Booking> getAllUserBooking(String discordId);

    DiscordUser findUserById(String discordId);

    Appointment findAppointmentById(String appointmentId);

    Booking findBookingById(String bookingId);

    Booking save(Booking booking);

    String delete(String booking);
}