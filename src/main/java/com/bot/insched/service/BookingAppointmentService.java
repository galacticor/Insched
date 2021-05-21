package com.bot.insched.service;

public interface BookingAppointmentService {
    String createBooking(String requesterId, String token) throws Exception;
    String deleteBooking(String requesterId, String token) throws Exception;
}