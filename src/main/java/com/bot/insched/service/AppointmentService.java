package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import java.util.List;

public interface AppointmentService {
    List<Event> getAllAppointment(String idDiscord) throws Exception;

    String createSlot(String deskripsi, String waktu, int durasi, int kapasitas, String idDiscord)
        throws Exception;

    Appointment save(Appointment appointment);

    String getUserToken(DiscordUser discordUser);

    String editSlot(String tanggal, String jamLama, String jamBaru, int durasiBaru, String judulBaru,
                           String idDiscord) throws Exception;

    String deleteSlot(String token, String idDiscord) throws Exception;
}
