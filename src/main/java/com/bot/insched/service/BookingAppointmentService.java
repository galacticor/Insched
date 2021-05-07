package com.bot.insched.service;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.Booking;
import com.bot.insched.model.DiscordUser;

import java.util.List;
import java.util.UUID;

public interface BookingAppointmentService {
    String createBooking(String title, String desc, String appointmentId, String discordId) throws Exception;

    List<Booking> getAllUserBooking(String discordId);

    DiscordUser findUserById(String discordId);

    Appointment findAppointmentById(String appointmentId);

    Booking findBookingById(String bookingId);

    Booking save(Booking booking);

    String delete(String booking);
}