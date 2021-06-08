package com.bot.insched.service;

import com.bot.insched.model.Event;
import java.util.List;

public interface BookingAppointmentService {
    String createBooking(String requesterId, String token, String email) throws Exception;

    String deleteBooking(String requesterId, String token) throws Exception;

    List<Event> viewHostBookingSlots(String requesterId, String token) throws Exception;
}