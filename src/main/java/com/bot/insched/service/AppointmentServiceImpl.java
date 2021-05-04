package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DiscordUserRepository discordUserRepository;

    @Autowired
    EventRepository eventRepository;

    @Override
    public DiscordUser findUserById(String discordId) {
        return discordUserRepository.findByIdDiscord(discordId);
    }


    @Override
    public String createAppointment(String desc, String start_date, String end_date, String discordId) throws Exception{
        DiscordUser user = findUserById(discordId);
        if (user == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }

        LocalDate startDate = LocalDate.parse(start_date);
        LocalDate endDate = LocalDate.parse(end_date);
        LocalDate now = LocalDate.now();

        if (startDate.isBefore(now) || endDate.isBefore(now) || endDate.isBefore(startDate)) {
            return "Appointment yang dibuat hanya dapat dimulai dari hari ini dan selesai minimal hari ini!";
        }

        Appointment appointment = new Appointment(desc, startDate, endDate);
        appointment.setOwner(user);
        appointmentRepository.save(appointment);

        return "Appointment berhasil dibuat!";
    }

    @Override
    public List<Appointment> getAllUserAppointment(String idDiscord) {
        DiscordUser user = discordUserRepository.findByIdDiscord(idDiscord);

        List<Appointment> listAppointment = new ArrayList<>();

        for (Appointment app: appointmentRepository.findAllByOwner(user)) {
            listAppointment.add(app);
        };

        return listAppointment;
    }

    @Override
    public Appointment findAppointmentById(String idAppointment) {
        UUID uuid = UUID.fromString(idAppointment);
        return appointmentRepository.findByIdAppointment(uuid);
    }

    @Override
    public String createSlot(String jamMulai, int durasi, int kapasitas, Appointment appointment) {
        Event event = new Event(jamMulai, durasi, kapasitas);
        appointment.getListEvent().add(event);
        event.setAppointment(appointment);
        eventRepository.save(event);
        appointmentRepository.save(appointment);

        return "Slot berhasil dibuat!";
    }

    @Override
    public void deleteAppointment() {

    }
}