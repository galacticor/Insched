package com.bot.insched.service;

import com.bot.insched.model.Appointment;

import java.util.List;

public interface AppointmentService {
    String createAppointment(String desc, String start_date, String end_date, String discordId);
    void deleteAppointment();
    List<Appointment> getAllAppointment(String idDiscord);


}
