package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;

import java.util.List;

public interface AppointmentService {
    String createAppointment(String desc, String start_date, String end_date, String discordId) throws Exception;
    void deleteAppointment();
    List<Appointment> getAllUserAppointment(String idDiscord);
    DiscordUser findUserById(String discordId);
    Appointment findAppointmentById(String idAppointment);
    String createSlot(String jamMulai, int durasi, int kapasitas, Appointment appointment);
    Appointment save(Appointment appointment);
}
