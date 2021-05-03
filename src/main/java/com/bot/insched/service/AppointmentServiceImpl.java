package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DiscordUserRepository discordUserRepository;


    @Override
    public String createAppointment(String desc, String start_date, String end_date, String discordId) {
        DiscordUser user = discordUserRepository.findByIdDiscord(discordId);

        if (user == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        LocalDate startDate = LocalDate.parse(start_date);
        LocalDate endDate = LocalDate.parse(end_date);
        LocalDate now = LocalDate.now();

        if (startDate.isBefore(now) || endDate.isBefore(now) || endDate.isBefore(startDate)) {
            return "Appointment yang dibuat hanya dapat dimulai dari hari ini dan selesai minimal hari ini!";
        }

        Appointment appointment = new Appointment(desc, start_date, end_date);
        appointment.setOwner(user);
        appointmentRepository.save(appointment);

        return "Appointment berhasil dibuat!";
    }

    @Override
    public List<Appointment> getAllAppointment(String idDiscord) {
        DiscordUser user = discordUserRepository.findByIdDiscord(idDiscord);

        List<Appointment> listAppointment = new ArrayList<>();

        for (Appointment app: appointmentRepository.findAll()) {
            if (app.getOwner() == user)
                listAppointment.add(app);
        };

        return listAppointment;
    }

    @Override
    public void deleteAppointment() {

    }
}